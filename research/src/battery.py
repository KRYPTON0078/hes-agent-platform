"""Simple residential BESS plant model (Coulomb counting)."""

from __future__ import annotations

from dataclasses import dataclass

import numpy as np


@dataclass
class BatteryConfig:
    capacity_kwh: float = 10.0
    eta_charge: float = 0.95
    eta_discharge: float = 0.95
    p_max_kw: float = 5.0
    soc_min: float = 0.05
    soc_max: float = 0.95
    soc0: float = 0.50
    dt_s: float = 60.0
    power_noise_std_kw: float = 0.02
    soc_noise_std: float = 0.001


class BatteryPlant:
    def __init__(self, cfg: BatteryConfig, rng: np.random.Generator):
        self.cfg = cfg
        self.rng = rng
        self.soc_true = float(np.clip(cfg.soc0, cfg.soc_min, cfg.soc_max))
        self.p_cmd_kw = 0.0  # positive = discharge to load/grid, negative = charge

    def set_power_command(self, p_kw: float) -> None:
        self.p_cmd_kw = float(np.clip(p_kw, -self.cfg.p_max_kw, self.cfg.p_max_kw))

    def step(self) -> tuple[float, float]:
        """Advance one sample. Returns (true_soc, true_power_kw)."""
        cfg = self.cfg
        p = self.p_cmd_kw

        # Enforce SoC limits on commanded power.
        if self.soc_true <= cfg.soc_min and p > 0:
            p = 0.0
        if self.soc_true >= cfg.soc_max and p < 0:
            p = 0.0

        dt_h = cfg.dt_s / 3600.0
        if p >= 0:
            # Discharge: remove energy / eta from stored charge equivalent.
            d_soc = -(p * dt_h) / (cfg.capacity_kwh * cfg.eta_discharge)
        else:
            # Charge: store |p|*eta into capacity.
            d_soc = -(p * dt_h) * cfg.eta_charge / cfg.capacity_kwh

        self.soc_true = float(np.clip(self.soc_true + d_soc, cfg.soc_min, cfg.soc_max))
        return self.soc_true, p

    def measure(self, soc_true: float, p_true: float) -> tuple[float, float]:
        soc_m = soc_true + float(self.rng.normal(0.0, self.cfg.soc_noise_std))
        p_m = p_true + float(self.rng.normal(0.0, self.cfg.power_noise_std_kw))
        soc_m = float(np.clip(soc_m, 0.0, 1.0))
        return soc_m, p_m
