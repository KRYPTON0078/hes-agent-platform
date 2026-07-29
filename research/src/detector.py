"""S1 energy-balance, S2 meter cross-check, fused detectors, S2 baselines."""

from __future__ import annotations

from dataclasses import dataclass

import numpy as np

# Calibration coefficients (documented for the paper; do not change silently).
S1_TH_K = 5.0
S1_CUSUM_DRIFT_K = 0.5
S1_CUSUM_H_K = 4.0
S1_EWMA_TH_K = 3.0
S2_TH_K = 4.0
S2_CUSUM_DRIFT_K = 0.5
# Initial H coefficient; APD refinement on calib seeds may raise H further.
S2_CUSUM_H_K = 8.0
S2_EWMA_TH_K = 3.0
S2_EWMA_ALPHA = 0.2
TARGET_ALARMS_PER_DAY = 0.1


@dataclass
class DetectorParams:
    capacity_kwh: float = 10.0
    eta_charge: float = 0.95
    eta_discharge: float = 0.95
    dt_s: float = 60.0
    s1_threshold: float = 0.008
    s1_cusum_drift: float = 0.00025
    s1_cusum_h: float = 0.012
    s1_ewma_threshold: float = 0.004
    s2_threshold: float = 0.35
    s2_cusum_drift: float = 0.05
    s2_cusum_h: float = 0.8
    s2_ewma_threshold: float = 0.25
    ewma_alpha: float = S2_EWMA_ALPHA
    # Empirical stds from calibration (for paper reporting).
    s1_std: float = 0.0
    s2_std: float = 0.0


class DualChannelDetector:
    def __init__(self, params: DetectorParams):
        self.p = params
        self.prev_soc: float | None = None
        self.s1_cusum_pos = 0.0
        self.s1_cusum_neg = 0.0
        self.s2_cusum_pos = 0.0
        self.s2_cusum_neg = 0.0
        self.s1_ewma = 0.0
        self.s2_ewma = 0.0
        self.s1_ewma_init = False
        self.s2_ewma_init = False

    def expected_dsoc(self, p_kw: float) -> float:
        dt_h = self.p.dt_s / 3600.0
        if p_kw >= 0:
            return -(p_kw * dt_h) / (self.p.capacity_kwh * self.p.eta_discharge)
        return -(p_kw * dt_h) * self.p.eta_charge / self.p.capacity_kwh

    def update(
        self,
        soc_reported: float,
        p_batt_reported: float,
        p_grid_meas: float,
        p_load_est: float,
    ) -> dict:
        if self.prev_soc is None:
            self.prev_soc = soc_reported
            r1 = 0.0
        else:
            r1 = (soc_reported - self.prev_soc) - self.expected_dsoc(p_batt_reported)
            self.prev_soc = soc_reported

        alarm_s1_th = abs(r1) > self.p.s1_threshold
        self.s1_cusum_pos = max(0.0, self.s1_cusum_pos + r1 - self.p.s1_cusum_drift)
        self.s1_cusum_neg = max(0.0, self.s1_cusum_neg - r1 - self.p.s1_cusum_drift)
        alarm_s1_cu = (self.s1_cusum_pos > self.p.s1_cusum_h) or (self.s1_cusum_neg > self.p.s1_cusum_h)

        if not self.s1_ewma_init:
            self.s1_ewma = r1
            self.s1_ewma_init = True
        else:
            a = self.p.ewma_alpha
            self.s1_ewma = a * r1 + (1.0 - a) * self.s1_ewma
        alarm_s1_ew = abs(self.s1_ewma) > self.p.s1_ewma_threshold

        p_batt_hat = p_load_est - p_grid_meas
        r2 = p_batt_hat - p_batt_reported
        alarm_s2_th = abs(r2) > self.p.s2_threshold
        self.s2_cusum_pos = max(0.0, self.s2_cusum_pos + r2 - self.p.s2_cusum_drift)
        self.s2_cusum_neg = max(0.0, self.s2_cusum_neg - r2 - self.p.s2_cusum_drift)
        alarm_s2_cu = (self.s2_cusum_pos > self.p.s2_cusum_h) or (self.s2_cusum_neg > self.p.s2_cusum_h)

        if not self.s2_ewma_init:
            self.s2_ewma = r2
            self.s2_ewma_init = True
        else:
            a = self.p.ewma_alpha
            self.s2_ewma = a * r2 + (1.0 - a) * self.s2_ewma
        alarm_s2_ew = abs(self.s2_ewma) > self.p.s2_ewma_threshold

        alarm_fuse = alarm_s1_cu or alarm_s2_cu

        return {
            "r1": r1,
            "r2": r2,
            "p_batt_hat": p_batt_hat,
            "alarm_s1_threshold": alarm_s1_th,
            "alarm_s1_cusum": alarm_s1_cu,
            "alarm_s1_ewma": alarm_s1_ew,
            "alarm_s2_threshold": alarm_s2_th,
            "alarm_s2_cusum": alarm_s2_cu,
            "alarm_s2_ewma": alarm_s2_ew,
            "alarm_fuse": alarm_fuse,
        }


def calibrate_from_benign(residuals_s1: np.ndarray, residuals_s2: np.ndarray) -> DetectorParams:
    """
    Held-out calibration rule (self-contained):
      sigma1 = std(r1), sigma2 = std(r2) on benign calibration seeds
      s1_threshold = max(0.004, S1_TH_K * sigma1)
      s1_cusum_drift = max(1e-4, S1_CUSUM_DRIFT_K * sigma1)
      s1_cusum_h = max(0.006, S1_CUSUM_H_K * sigma1)
      s1_ewma_threshold = max(0.002, S1_EWMA_TH_K * sigma1)
      s2_threshold = max(0.25, S2_TH_K * sigma2)
      s2_cusum_drift = max(0.03, S2_CUSUM_DRIFT_K * sigma2)
      s2_cusum_h = max(1.0, S2_CUSUM_H_K * sigma2)
      s2_ewma_threshold = max(0.15, S2_EWMA_TH_K * sigma2)
      Then refine s2_cusum_h upward on calib seeds until APD <= TARGET_ALARMS_PER_DAY.
    """
    params = DetectorParams()
    s1 = np.asarray(residuals_s1, dtype=float)
    s2 = np.asarray(residuals_s2, dtype=float)
    s1 = s1[np.isfinite(s1)]
    s2 = s2[np.isfinite(s2)]
    if len(s1) < 10 or len(s2) < 10:
        return params

    s1_std = float(np.std(s1)) + 1e-9
    s2_std = float(np.std(s2)) + 1e-9
    params.s1_std = s1_std
    params.s2_std = s2_std
    params.s1_threshold = max(0.004, S1_TH_K * s1_std)
    params.s1_cusum_drift = max(0.0001, S1_CUSUM_DRIFT_K * s1_std)
    params.s1_cusum_h = max(0.006, S1_CUSUM_H_K * s1_std)
    params.s1_ewma_threshold = max(0.002, S1_EWMA_TH_K * s1_std)
    params.s2_threshold = max(0.25, S2_TH_K * s2_std)
    params.s2_cusum_drift = max(0.03, S2_CUSUM_DRIFT_K * s2_std)
    params.s2_cusum_h = max(1.0, S2_CUSUM_H_K * s2_std)
    params.s2_ewma_threshold = max(0.15, S2_EWMA_TH_K * s2_std)
    params.ewma_alpha = S2_EWMA_ALPHA
    return params


def refine_s2_cusum_h_for_apd(
    params: DetectorParams,
    measure_apd_fn,
    target_apd: float = TARGET_ALARMS_PER_DAY,
    h_mults: tuple[float, ...] = (1.0, 1.5, 2.0, 3.0, 4.0, 6.0, 8.0, 12.0),
) -> DetectorParams:
    """Raise S2 CUSUM H on calibration data until mean alarms/day <= target.

    measure_apd_fn(params) -> float must use CALIBRATION seeds only.
    """
    base_h = float(params.s2_cusum_h)
    best = DetectorParams(**params.__dict__)
    for m in h_mults:
        trial = DetectorParams(**params.__dict__)
        trial.s2_cusum_h = base_h * m
        apd = float(measure_apd_fn(trial))
        trial_meta_h = trial.s2_cusum_h
        if apd <= target_apd:
            best = trial
            best.s2_cusum_h = trial_meta_h
            return best
        best = trial
    return best
