"""Residential Energy IoT simulation with agent path + independent meter."""

from __future__ import annotations

from dataclasses import dataclass, field

import numpy as np
import pandas as pd

from attacks import AttackConfig, AttackEngine, AttackKind
from battery import BatteryConfig, BatteryPlant
from detector import DetectorParams, DualChannelDetector
from house import GridMeter, HouseConfig, house_load_kw


@dataclass
class DetectorMismatch:
    capacity_scale: float = 1.0
    eta_scale: float = 1.0


@dataclass
class SimConfig:
    n_steps: int = 720
    seed: int = 42
    attack: AttackConfig = field(default_factory=AttackConfig)
    mismatch: DetectorMismatch = field(default_factory=DetectorMismatch)
    schedule: str = "diurnal"
    params: DetectorParams | None = None
    meter_noise_std_kw: float | None = None
    load_estimate_noise_std_kw: float | None = None
    load_estimate_scale: float | None = None


def battery_schedule(step: int, dt_s: float, kind: str) -> float:
    hour = (step * dt_s / 3600.0) % 24.0
    if kind == "evening_peak":
        if 11.0 <= hour < 14.0:
            return -1.5
        if 17.0 <= hour < 21.0:
            return 3.0
        if 22.0 <= hour or hour < 5.0:
            return 0.4
        return 0.0
    if 10.0 <= hour < 15.0:
        return -2.0
    if 18.0 <= hour < 22.0:
        return 2.5
    if 0.0 <= hour < 6.0:
        return 0.3
    return 0.0


def run_simulation(sim: SimConfig) -> pd.DataFrame:
    rng = np.random.default_rng(sim.seed)
    batt_cfg = BatteryConfig()
    house_cfg = HouseConfig()
    if sim.meter_noise_std_kw is not None:
        house_cfg.meter_noise_std_kw = sim.meter_noise_std_kw
    if sim.load_estimate_noise_std_kw is not None:
        house_cfg.load_estimate_noise_std_kw = sim.load_estimate_noise_std_kw
    if sim.load_estimate_scale is not None:
        house_cfg.load_estimate_scale = sim.load_estimate_scale

    plant = BatteryPlant(batt_cfg, rng)
    meter = GridMeter(house_cfg, rng)
    attack = AttackEngine(
        sim.attack,
        capacity_kwh=batt_cfg.capacity_kwh,
        eta_c=batt_cfg.eta_charge,
        eta_d=batt_cfg.eta_discharge,
        dt_s=batt_cfg.dt_s,
    )

    if sim.params is None:
        params = DetectorParams(
            capacity_kwh=batt_cfg.capacity_kwh * sim.mismatch.capacity_scale,
            eta_charge=float(np.clip(batt_cfg.eta_charge * sim.mismatch.eta_scale, 0.8, 0.99)),
            eta_discharge=float(np.clip(batt_cfg.eta_discharge * sim.mismatch.eta_scale, 0.8, 0.99)),
            dt_s=batt_cfg.dt_s,
        )
    else:
        params = DetectorParams(**{**sim.params.__dict__})
        params.capacity_kwh = batt_cfg.capacity_kwh * sim.mismatch.capacity_scale
        params.eta_charge = float(np.clip(batt_cfg.eta_charge * sim.mismatch.eta_scale, 0.8, 0.99))
        params.eta_discharge = float(
            np.clip(batt_cfg.eta_discharge * sim.mismatch.eta_scale, 0.8, 0.99)
        )

    det = DualChannelDetector(params)
    rows = []
    for k in range(sim.n_steps):
        p_load = house_load_kw(k, batt_cfg.dt_s, house_cfg, sim.schedule)
        intended = battery_schedule(k, batt_cfg.dt_s, sim.schedule)
        p_cmd = attack.override_power_command(intended, k)
        plant.set_power_command(p_cmd)
        soc_true, p_batt_true = plant.step()
        soc_m, p_m = plant.measure(soc_true, p_batt_true)
        soc_rep, p_rep = attack.spoof_reports(soc_m, p_m, k)
        m = meter.read(p_load, p_batt_true, k)
        p_load_est = attack.spoof_load_estimate(m.p_load_est_kw, m.p_grid_meas_kw, p_rep, k)
        out = det.update(soc_rep, p_rep, m.p_grid_meas_kw, p_load_est)

        rows.append(
            {
                "step": k,
                "t_min": k * batt_cfg.dt_s / 60.0,
                "soc_true": soc_true,
                "p_batt_true": p_batt_true,
                "soc_reported": soc_rep,
                "p_batt_reported": p_rep,
                "p_load_true": m.p_load_true_kw,
                "p_load_est": p_load_est,
                "p_grid_true": m.p_grid_true_kw,
                "p_grid_meas": m.p_grid_meas_kw,
                "meter_dropout": m.dropped,
                "r1": out["r1"],
                "r2": out["r2"],
                "p_batt_hat": out["p_batt_hat"],
                "alarm_s1_threshold": out["alarm_s1_threshold"],
                "alarm_s1_cusum": out["alarm_s1_cusum"],
                "alarm_s1_ewma": out["alarm_s1_ewma"],
                "alarm_s2_threshold": out["alarm_s2_threshold"],
                "alarm_s2_cusum": out["alarm_s2_cusum"],
                "alarm_s2_ewma": out["alarm_s2_ewma"],
                "alarm_fuse": out["alarm_fuse"],
                "attack_active": attack.active,
                "attack_kind": sim.attack.kind.value,
            }
        )
    return pd.DataFrame(rows)


def detection_delay(df: pd.DataFrame, alarm_col: str, start_step: int) -> float | None:
    hit = df.loc[(df["step"] >= start_step) & (df[alarm_col]), "step"]
    if hit.empty:
        return None
    return float(hit.iloc[0] - start_step)


def false_positive_rate(df: pd.DataFrame, alarm_col: str, start_step: int) -> float:
    pre = df.loc[df["step"] < start_step, alarm_col]
    if len(pre) == 0:
        return 0.0
    return float(pre.mean())


def alarms_per_day(df: pd.DataFrame, alarm_col: str, dt_s: float = 60.0) -> float:
    """Count rising edges of alarm, normalized to per-day rate."""
    a = df[alarm_col].astype(bool).to_numpy()
    if len(a) < 2:
        return 0.0
    rises = int(np.sum((~a[:-1]) & a[1:]))
    days = (len(a) * dt_s) / 86400.0
    return float(rises / max(days, 1e-9))


def summarize(df: pd.DataFrame, start_step: int) -> dict:
    out = {}
    for name, col in [
        ("s1_th", "alarm_s1_threshold"),
        ("s1_cu", "alarm_s1_cusum"),
        ("s1_ew", "alarm_s1_ewma"),
        ("s2_th", "alarm_s2_threshold"),
        ("s2_cu", "alarm_s2_cusum"),
        ("s2_ew", "alarm_s2_ewma"),
        ("fuse", "alarm_fuse"),
    ]:
        delay = detection_delay(df, col, start_step)
        out[f"fpr_{name}"] = false_positive_rate(df, col, start_step)
        out[f"detected_{name}"] = delay is not None
        out[f"delay_{name}"] = delay
    return out
