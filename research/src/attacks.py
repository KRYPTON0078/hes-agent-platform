"""Attack scenarios for detectability study.

Threat model:
- Compromised agent credential/gateway: rewrite agent telemetry and inject
  charge/discharge commands.
- Net meter integrity: attacker cannot forge P_G^meas (authenticated/utility path).
- Net meter confidentiality (A6 only): attacker may have read-only access to the
  local meter data stream (e.g., Modbus/Zigbee tap on the home LAN). That is
  enough to blend P_L^est toward P_G^meas + P_B^rep, but not to rewrite the
  meter reading seen by the ESMS.
- Home load estimate P_L^est is soft and may be partially or fully compromised.
"""

from __future__ import annotations

from dataclasses import dataclass
from enum import Enum

import numpy as np


class AttackKind(str, Enum):
    NONE = "benign"
    A1_SOC_BIAS = "A1_soc_bias"
    A2_SOC_RAMP = "A2_soc_ramp"
    A3_CMD_INJECT = "A3_cmd_inject"
    A4_COORDINATED = "A4_coordinated"
    A5_STEALTH_CONSISTENT = "A5_stealth_consistent"
    A6_LOAD_EST_SPOOF = "A6_load_est_spoof"


@dataclass
class AttackConfig:
    kind: AttackKind = AttackKind.NONE
    start_step: int = 180
    soc_bias: float = -0.12
    soc_ramp_per_step: float = -0.0008
    inject_charge_kw: float = -3.0
    report_power_frac_a3: float = 0.20
    report_power_frac_a4: float = 0.05
    load_est_spoof_alpha: float = 0.0


class AttackEngine:
    def __init__(
        self,
        cfg: AttackConfig,
        capacity_kwh: float = 10.0,
        eta_c: float = 0.95,
        eta_d: float = 0.95,
        dt_s: float = 60.0,
    ):
        self.cfg = cfg
        self.capacity_kwh = capacity_kwh
        self.eta_c = eta_c
        self.eta_d = eta_d
        self.dt_s = dt_s
        self._ramp_accum = 0.0
        self._stealth_soc: float | None = None
        self.active = False

    def maybe_activate(self, step: int) -> None:
        if self.cfg.kind == AttackKind.NONE:
            self.active = False
            return
        self.active = step >= self.cfg.start_step

    def _expected_dsoc(self, p_kw: float) -> float:
        dt_h = self.dt_s / 3600.0
        if p_kw >= 0:
            return -(p_kw * dt_h) / (self.capacity_kwh * self.eta_d)
        return -(p_kw * dt_h) * self.eta_c / self.capacity_kwh

    def override_power_command(self, intended_p_kw: float, step: int) -> float:
        self.maybe_activate(step)
        if not self.active:
            return intended_p_kw
        if self.cfg.kind in (
            AttackKind.A3_CMD_INJECT,
            AttackKind.A4_COORDINATED,
            AttackKind.A5_STEALTH_CONSISTENT,
            AttackKind.A6_LOAD_EST_SPOOF,
        ):
            return self.cfg.inject_charge_kw
        return intended_p_kw

    def spoof_reports(self, soc_measured: float, p_measured: float, step: int) -> tuple[float, float]:
        self.maybe_activate(step)
        if not self.active:
            return soc_measured, p_measured

        kind = self.cfg.kind
        if kind == AttackKind.A1_SOC_BIAS:
            return float(max(0.0, min(1.0, soc_measured + self.cfg.soc_bias))), p_measured
        if kind == AttackKind.A2_SOC_RAMP:
            self._ramp_accum += self.cfg.soc_ramp_per_step
            return float(max(0.0, min(1.0, soc_measured + self._ramp_accum))), p_measured
        if kind == AttackKind.A3_CMD_INJECT:
            return soc_measured, self.cfg.report_power_frac_a3 * p_measured
        if kind == AttackKind.A4_COORDINATED:
            self._ramp_accum += self.cfg.soc_ramp_per_step
            soc = float(max(0.0, min(1.0, soc_measured + self._ramp_accum)))
            return soc, self.cfg.report_power_frac_a4 * p_measured
        if kind in (AttackKind.A5_STEALTH_CONSISTENT, AttackKind.A6_LOAD_EST_SPOOF):
            p_rep = 0.0
            if self._stealth_soc is None:
                self._stealth_soc = soc_measured
            self._stealth_soc = float(
                max(0.0, min(1.0, self._stealth_soc + self._expected_dsoc(p_rep)))
            )
            return self._stealth_soc, p_rep
        return soc_measured, p_measured

    def spoof_load_estimate(self, p_load_est: float, p_grid_meas: float, p_batt_rep: float, step: int) -> float:
        """A6: bias load estimate using read-only meter + forged P_B^rep.

        Requires real-time read of P_G^meas (local tap), not write access.
        At alpha=1, r^(2) is nulled in the noise-free model without forging the meter.
        """
        self.maybe_activate(step)
        if not self.active or self.cfg.kind != AttackKind.A6_LOAD_EST_SPOOF:
            return p_load_est
        alpha = float(np.clip(self.cfg.load_est_spoof_alpha, 0.0, 1.0))
        # Hide injection: choose P_L^est so hat{P}_B ≈ P_B^rep when alpha→1.
        p_hide = p_grid_meas + p_batt_rep
        return float((1.0 - alpha) * p_load_est + alpha * p_hide)
