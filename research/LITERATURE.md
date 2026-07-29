# Literature pack and differentiation

Author paper framing: Magne Dina Neves (solo), University of Macau.
Topic: ESMS/cloud-layer coordinated SoC spoofing + charge-command injection; energy-balance residual + CUSUM.

## Core IEEE / verified references (must cite)

### Surveys and system context
1. R. D. Trevizan et al., "Cyberphysical Security of Grid Battery Energy Storage Systems," IEEE Access, 2022. DOI/Xplore: 10.1109/ACCESS.2022.3178987 (doc 9787060).
   - Why: names ESMS as outward-facing critical layer; consumer-scale BESS need turnkey security; research gaps on threat models and anomaly detection.

2. J. Ye et al., "A Review of Cyber-Physical Security for Photovoltaic Systems," IEEE JESTPE, 2021/2022. DOI: 10.1109/JESTPE.2021.3111728.
   - Why: adjacent DER CPS security; cite to show PV sensor/MPPT work is a different surface.

### Crowded BMS-sensor FDIA line (differentiate explicitly)
3. P. Zhuang and H. Liang, "False Data Injection Attacks Against State-of-Charge Estimation of Battery Energy Storage Systems in Smart Distribution Networks," IEEE Trans. Smart Grid, 2021. DOI: 10.1109/TSG.2020.3042926 (doc 9284586).

4. V. O'Brien, R. D. Trevizan, V. S. Rao, "Detection of False Data Injection Attacks in Battery Stacks Using Input Noise-Aware Nonlinear State Estimation and Cumulative Sum Algorithms," IEEE Access / related Sandia line, 2023. Xplore: doc 10230848.

5. Online and Offline Identification of False Data Injection Attacks in Battery Sensors Using a Single Particle Model, IEEE, 2024. Xplore: doc 10746526.
   - Why: UKF + CUSUM on voltage sensors. Our paper is NOT this. We use reported SoC and power at ESMS, no cell voltages, no UKF.

### Closest adjacent (must differentiate in Related Work)
6. Self-protective inverters using steady-state and dynamic reference models (malicious setpoint inspection at device), IEEE JESTIE, 2022. Xplore: doc 9860056.
   - Diff: device-side setpoint check vs cloud ESMS SoC-power consistency under dual-channel attack.

7. "Large Language Model for Smart Inverter Cyber-Attack Detection via Textual Analysis of Volt/VAR Commands," IEEE Trans. Smart Grid, 2024. Xplore: doc 10663471.
   - Diff: Volt/VAR textual commands, not residential battery SoC telemetry integrity.

8. False Data Injection Attacks on RL-Based Charging Coordination (home batteries / lying SoC), Appl. Sci. 2024. DOI: 10.3390/app142310874.
   - Diff: economic gaming of charging priority via under-reported SoC + ML detector; we study coordinated SoC spoof + unauthorized charge commands with physics residual.

9. Distributed secondary control for ESS under FDI, IEEE Trans. Industrial Electronics, 2024. Xplore: doc 10538177.
   - Diff: resilient control for SoC balancing under FDI on consensus links; not residential ESMS residual monitoring.

### Real-world motivation (cite carefully as incidents / advisories, not as your experiments)
10. CISA ICS advisory on Outback Power Mojave Inverter (command injection / sensitive data). ICSA-25-044-17.
11. Public analyses of residential inverter cloud/MQTT firmware and remote control risks (APsystems-related reporting, 2025-2026). Use as motivation for cloud/ESMS attack surface, not as reproduced exploits.

## Differentiation table (use in paper Section II)

| Work | Layer | Attack | Detector | Our difference |
|---|---|---|---|---|
| 10746526 / Sandia CUSUM | BMS sensors | Voltage FDIA | UKF residual + CUSUM | We have no cell V/I; ESMS reported SoC and P |
| Zhuang TSG SoC FDIA | Distribution SE | Stealth SoC FDIA | Grid state estimation context | Residential ESMS + command channel |
| Self-protective inverter | Device | Malicious setpoints | Analytical plant model at inverter | Cloud consistency of SoC vs energy throughput |
| Lying SoC charging | Aggregator | Under-report SoC | DNN on SoC sequences | Dual-channel + energy-balance residual |
| This work | Residential ESMS/cloud | Coordinated SoC spoof + charge injection | Energy-balance residual + CUSUM | Explicit dual-channel evaluation |

## Contribution claim (keep narrow)

We evaluate whether a lightweight energy-balance residual computed from ESMS-visible fields (reported SoC, reported power, timestamps) can detect coordinated integrity attacks that combine telemetry spoofing and unauthorized charge commands on a residential BESS management path, without requiring BMS-internal voltage sensors or Kalman filtering.

## Venue note
First target: IEEE student / workshop conference (e.g. PESS). Do not claim Transactions-level novelty. Simulation testbed only.
