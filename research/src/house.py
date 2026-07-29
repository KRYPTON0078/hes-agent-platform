"""Household load and independent grid net meter."""

from __future__ import annotations

from dataclasses import dataclass

import numpy as np


@dataclass
class HouseConfig:
    # Sign: positive house load consumes from AC bus.
    base_load_kw: float = 0.8
    evening_boost_kw: float = 1.5
    meter_noise_std_kw: float = 0.05
    # Known/estimated house load used by ESMS for meter cross-check.
    # 1.0 = perfect knowledge; <1 or >1 models estimation error.
    load_estimate_scale: float = 1.0
    load_estimate_noise_std_kw: float = 0.08
    dropout_prob: float = 0.002


def house_load_kw(step: int, dt_s: float, cfg: HouseConfig, schedule: str = "diurnal") -> float:
    hour = (step * dt_s / 3600.0) % 24.0
    p = cfg.base_load_kw
    if schedule == "evening_peak":
        if 17.0 <= hour < 22.0:
            p += cfg.evening_boost_kw + 0.4
        elif 7.0 <= hour < 9.0:
            p += 0.5
    else:
        if 18.0 <= hour < 22.0:
            p += cfg.evening_boost_kw
        elif 7.0 <= hour < 9.0:
            p += 0.4
    # Mild daily ripple
    p += 0.15 * np.sin(2.0 * np.pi * hour / 24.0)
    return float(max(0.1, p))


@dataclass
class MeterReading:
    p_grid_true_kw: float
    p_grid_meas_kw: float
    p_load_true_kw: float
    p_load_est_kw: float
    dropped: bool


class GridMeter:
    """Independent utility/home net meter.

    Convention matching battery plant:
    - battery positive power = discharge into AC bus (reduces grid import)
    - house load positive = consumption
    - grid import positive = power drawn from grid
    So: P_grid = P_load - P_batt
    """

    def __init__(self, cfg: HouseConfig, rng: np.random.Generator):
        self.cfg = cfg
        self.rng = rng
        self._last_meas = 0.0

    def read(self, p_load_true: float, p_batt_true: float, step: int) -> MeterReading:
        p_grid_true = p_load_true - p_batt_true
        dropped = bool(self.rng.random() < self.cfg.dropout_prob)
        if dropped:
            p_meas = self._last_meas
        else:
            p_meas = p_grid_true + float(self.rng.normal(0.0, self.cfg.meter_noise_std_kw))
            self._last_meas = p_meas
        p_load_est = (
            p_load_true * self.cfg.load_estimate_scale
            + float(self.rng.normal(0.0, self.cfg.load_estimate_noise_std_kw))
        )
        p_load_est = float(max(0.0, p_load_est))
        return MeterReading(
            p_grid_true_kw=float(p_grid_true),
            p_grid_meas_kw=float(p_meas),
            p_load_true_kw=float(p_load_true),
            p_load_est_kw=p_load_est,
            dropped=dropped,
        )
