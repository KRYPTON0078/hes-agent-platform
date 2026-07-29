# Detection of Coordinated SoC Telemetry Spoofing and Charge-Command Injection in Residential Energy Storage Management Systems

**Magne Dina Neves**  
Faculty of Science and Technology  
University of Macau  
Macao SAR, China  
Email: (add institutional email before submission)

---

## Abstract

Residential battery energy storage systems (BESS) are increasingly managed through cloud-connected energy storage management systems (ESMS). An attacker who can alter reported state of charge (SoC) or inject charge and discharge commands can push the battery into unsafe or unfair operating regions without touching battery-management-system (BMS) cell sensors. This paper studies that ESMS-layer threat using a software cyber-physical testbed of a 10 kWh residential battery. We evaluate four attack cases: constant SoC bias, stealthy SoC ramp, unauthorized charge-command injection with under-reported power, and a coordinated combination of ramp spoofing and command injection. As a detector that uses only ESMS-visible fields (reported SoC, reported power, and time), we compute an energy-balance residual and apply both a fixed threshold and a cumulative sum (CUSUM) test. In our simulations, CUSUM detected all four attacks with zero false alarms on the benign profile, including a slow SoC ramp that the plain threshold missed. Detection delay for the coordinated case was 3 minutes under CUSUM versus 73 minutes under the fixed threshold. The study is limited to a Coulomb-counting plant model and synthetic schedules. It is not a field trial and it is not a BMS voltage-sensor false-data study.

**Index Terms:** Battery energy storage, cyber-physical security, false data injection, energy storage management system, state of charge, CUSUM.

---

## I. Introduction

Home batteries are no longer only local backup devices. Many products report SoC and power to a cloud or local ESMS and accept remote charge or discharge setpoints. That architecture is convenient for monitoring and for demand response, but it enlarges the attack surface. Public security analyses of residential inverters and home storage interfaces have repeatedly shown weak authentication, exposed remote interfaces, and risky firmware update paths. Those findings motivate integrity checks at the management layer, not only encryption of links.

A large body of IEEE work already studies false data injection attacks (FDIAs) on BMS voltage and current sensors and on distribution-level SoC estimation. Typical defenses use nonlinear filters such as the extended or unscented Kalman filter and then run residual tests such as chi-squared or CUSUM. That line is important, but it assumes access to cell or stack electrical measurements inside the BMS. A residential aggregator or cloud ESMS often sees only what the device agent reports: SoC, power, status flags, and timestamps.

This paper focuses on that ESMS-visible view. The question is simple: if reported SoC and reported power must stay consistent with battery energy balance, can a lightweight residual detector catch coordinated spoofing and command injection?

The contributions are:

1) A clear ESMS-layer threat model for residential BESS that separates cloud-visible integrity attacks from BMS cell-sensor FDIAs.
2) Four reproducible attack scenarios on a software testbed, including a coordinated dual-channel case.
3) An energy-balance residual detector with threshold and CUSUM variants, evaluated by detection delay and false-alarm behavior.
4) An honest report of what the detector catches quickly and what remains hard (very slow spoofing when the residual is smaller than the CUSUM drift).

---

## II. Related Work

Trevizan et al. survey cyber-physical security of grid battery storage and highlight the ESMS as an outward-facing subsystem that exchanges operating data upward and power commands downward [1]. They also note that consumer-scale BESS often lack specialized operators and need practical system-level defenses.

At the BMS layer, Zhuang and Liang analyze FDIAs against SoC estimation in smart distribution networks [2]. Follow-on detection work uses physics-based battery models, Kalman-type estimators, and CUSUM on sensor residuals [3], [4]. Online and offline identification of voltage-sensor FDIAs with a single-particle model and unscented Kalman filtering is a recent example of that crowded line [4]. Our detector does not use cell voltages or a UKF. It uses reported SoC and power only.

Device-side defenses also exist. Self-protective inverter designs inspect incoming setpoints with analytical reference models before local control engages them [5]. Smart-inverter work has also studied textual analysis of Volt/VAR commands [6]. Those methods protect the inverter command path. They do not evaluate ESMS consistency between spoofed SoC telemetry and hidden charge throughput.

Another adjacent line studies "lying" SoC reports that game charging coordination for unfair priority [7]. Detectors there are often machine-learning classifiers on SoC sequences. We instead use a physics residual and we include unauthorized command injection together with telemetry spoofing.

Distributed secondary control under FDI for ESS SoC balancing is another active topic [8]. That work aims at resilient multi-agent control. Our scope is smaller and more operational: detect integrity problems on a single residential ESMS path.

---

## III. System and Threat Model

### A. Residential ESMS View

We model a single behind-the-meter battery with capacity \(E_{\mathrm{nom}} = 10\,\mathrm{kWh}\). The plant uses Coulomb counting with charge and discharge efficiencies \(\eta_c = \eta_d = 0.95\). Sampling time is \(\Delta t = 60\,\mathrm{s}\). The ESMS receives reported SoC \(s_k\) and reported power \(p_k\) (kW). Sign convention: positive power means discharge, negative power means charge.

A benign residential schedule charges near midday (PV surplus), discharges in the evening, and draws a small overnight load. Measurement noise is added to SoC and power before reporting.

### B. Attacker Capabilities

The attacker can compromise the ESMS or agent credential path enough to:

1) alter reported SoC and/or reported power, and/or
2) replace the intended power command with an unauthorized charge command.

The attacker does not need physical access to BMS cell voltage or current sensors. Goals include masking a low SoC, forcing unwanted charge, or hiding charge activity from operators.

### C. Attack Cases

- **A1 (SoC bias):** after step 180, reported SoC is shifted by \(-0.12\).
- **A2 (SoC ramp):** after step 180, a slow spoofed ramp of \(-0.0008\) SoC per step is added.
- **A3 (command injection):** after step 180, the battery is forced to charge at \(-3\,\mathrm{kW}\), while reported power shows only 20% of the true power.
- **A4 (coordinated):** A2 ramp plus A3 injection, with reported power reduced to 5% of true power.

Attack start at step 180 equals 3 hours into the 12-hour run.

---

## IV. Detection Method

### A. Energy-Balance Residual

Let \(s_k\) be reported SoC and \(p_k\) reported power. The expected SoC change from energy balance is

\[
\hat{\Delta}s_k =
\begin{cases}
-\dfrac{p_k \Delta t_h}{E_{\mathrm{nom}}\eta_d}, & p_k \ge 0, \\[6pt]
-\dfrac{p_k \Delta t_h\,\eta_c}{E_{\mathrm{nom}}}, & p_k < 0,
\end{cases}
\]

where \(\Delta t_h = \Delta t / 3600\). The residual is

\[
r_k = (s_k - s_{k-1}) - \hat{\Delta}s_k.
\]

If telemetry and commands are consistent with the plant, \(r_k\) should stay near noise. Spoofed SoC or hidden charge throughput pushes \(r_k\) away from zero.

### B. Threshold Test

Raise an alarm when \(|r_k| > \tau\). We use \(\tau = 0.008\) as the default operating point after a small sensitivity sweep.

### C. CUSUM Test

To catch small persistent shifts, we run a two-sided CUSUM on \(r_k\):

\[
S_k^{+} = \max(0, S_{k-1}^{+} + r_k - \nu), \quad
S_k^{-} = \max(0, S_{k-1}^{-} - r_k - \nu),
\]

and alarm when \(S_k^{+} > H\) or \(S_k^{-} > H\). Tuned values used in the reported runs are \(\nu = 0.00025\) and \(H = 0.012\). Drift must be smaller than the residual of the slowest attack of interest. In an earlier trial with \(\nu = 0.002\), A2 escaped detection. That failure is recorded in the lab notebook and motivated the retune.

---

## V. Experimental Setup

All experiments were run in Python with a fixed seed (42). Each scenario lasts 720 steps (12 hours). Metrics are:

- false-positive rate before attack start (or over the full benign run),
- whether an alarm occurs after attack start,
- detection delay in steps (minutes, because \(\Delta t = 60\,\mathrm{s}\)).

Code, CSV logs, and figures are in the companion `research/` package so the runs can be repeated.

---

## VI. Results

Table I summarizes detection outcomes.

**Table I. Detection summary (seed 42, 720 steps).**

| Scenario | FPR (thr) | FPR (CUSUM) | Detected (thr) | Detected (CUSUM) | Delay thr (min) | Delay CUSUM (min) |
|---|---:|---:|---|---|---:|---:|
| Benign | 0.00 | 0.00 | no | no | - | - |
| A1 SoC bias | 0.00 | 0.00 | yes | yes | 0 | 0 |
| A2 SoC ramp | 0.00 | 0.00 | no | yes | - | 20 |
| A3 cmd inject | 0.00 | 0.00 | yes | yes | 73 | 3 |
| A4 coordinated | 0.00 | 0.00 | yes | yes | 73 | 3 |

Observations:

1) A sudden SoC bias (A1) creates a large one-step residual and is caught immediately by both detectors.
2) A slow SoC ramp (A2) stays below the fixed threshold but accumulates in CUSUM and is caught after 20 minutes.
3) Hidden charge injection (A3, A4) creates a sustained mismatch between reported power and true SoC motion. CUSUM raises an alarm in 3 minutes. The fixed threshold needs 73 minutes at \(\tau = 0.008\).
4) On the benign schedule, both detectors stayed quiet (FPR = 0 in these runs).

A threshold sweep on A4 showed the usual tradeoff. \(\tau = 0.003\) alarmed earlier but produced a pre-attack false-positive rate of about 5.6%. \(\tau = 0.005\) kept FPR at 0 in this seed and delayed detection to 9 minutes for the threshold rule. Larger thresholds became too conservative.

Figures for each scenario (true versus reported SoC, true versus reported power, and residual) are generated by `run_campaign.py` and stored under `research/figures/`.

---

## VII. Discussion and Limitations

The main practical point is that ESMS operators already store SoC and power. An energy-balance residual is cheap to compute and does not require cell telemetry. CUSUM helps against slow spoofing that a raw threshold misses.

Limitations are real and should be stated in any review reply:

1) The plant is Coulomb counting with constant efficiencies. Real batteries have temperature effects, aging, and SoC estimation bias inside the BMS.
2) Schedules are synthetic. They are physically consistent but not taken from a metered home.
3) The attacker model assumes the ability to rewrite reports and commands. We do not claim a new remote exploit against a commercial product.
4) Zero FPR on one benign seed does not prove zero FPR in the field. More profiles and hardware-in-the-loop tests are needed.
5) This paper does not replace BMS-layer sensor FDIA detectors. It complements them at the management layer.

---

## VIII. Conclusion

This work examined coordinated integrity attacks on the residential ESMS path and tested a simple energy-balance residual with CUSUM. In simulation, CUSUM detected constant bias, slow SoC ramp, command injection, and the coordinated case, with no false alarms on the benign run. The method is intentionally narrow: ESMS-visible fields only, software testbed only, and a student-conference scope rather than a claim of full BESS cybersecurity.

Future work includes multi-home fleets, parameter uncertainty in \(\eta\) and \(E_{\mathrm{nom}}\), comparison against learning-based detectors, and validation on a hardware battery emulator.

---

## References

[1] R. D. Trevizan, J. Obert, V. De Angelis, T. A. Nguyen, V. S. Rao, and B. R. Chalamala, "Cyberphysical Security of Grid Battery Energy Storage Systems," *IEEE Access*, vol. 10, pp. 59675-59721, 2022, doi: 10.1109/ACCESS.2022.3178987.

[2] P. Zhuang and H. Liang, "False Data Injection Attacks Against State-of-Charge Estimation of Battery Energy Storage Systems in Smart Distribution Networks," *IEEE Trans. Smart Grid*, vol. 12, no. 3, pp. 2566-2577, May 2021, doi: 10.1109/TSG.2020.3042926.

[3] V. O'Brien, V. S. Rao, and R. D. Trevizan, "Detection of False Data Injection Attacks in Battery Stacks Using Input Noise-Aware Nonlinear State Estimation and Cumulative Sum Algorithms," *IEEE Trans. Ind. Appl.*, vol. 59, no. 6, Nov./Dec. 2023, doi: 10.1109/TIA.2023.3308548. (Confirm page numbers in IEEE Xplore before camera-ready.)

[4] V. A. O'Brien, V. S. Rao, and R. D. Trevizan, "Online and Offline Identification of False Data Injection Attacks in Battery Sensors Using a Single Particle Model," *IEEE Open Access J. Power Energy*, 2024, doi: 10.1109/OAJPE.2024.3493757.

[5] T. Hossen, M. Gursoy, and B. Mirafzal, "Self-Protective Inverters Against Malicious Setpoints Using Analytical Reference Models," *IEEE J. Emerg. Sel. Topics Ind. Electron.*, vol. 3, no. 4, pp. 871-877, Oct. 2022, doi: 10.1109/JESTIE.2022.3199672.

[6] A. Selim, J. Zhao, and B. Yang, "Large Language Model for Smart Inverter Cyber-Attack Detection via Textual Analysis of Volt/VAR Commands," *IEEE Trans. Smart Grid*, vol. 15, no. 6, pp. 6179-6182, Nov. 2024, doi: 10.1109/TSG.2024.3453648.

[7] A. A. El-Shazly et al., "False Data Injection Attacks on Reinforcement Learning-Based Charging Coordination in Smart Grids and a Countermeasure," *Appl. Sci.*, vol. 14, no. 23, Art. no. 10874, 2024, doi: 10.3390/app142310874.

[8] S. Fan, D. Yue, H. Yan, and C. Deng, "Distributed Round-Robin Protocol-Based Secondary Control for ESSs Under FDI Attacks," *IEEE Trans. Ind. Electron.*, vol. 71, no. 12, pp. 16493-16502, Dec. 2024, doi: 10.1109/TIE.2024.3390733.

---

## Author checklist before submission

1) Open each DOI in IEEE Xplore / the publisher site and confirm page numbers for [3] and [5].
2) Add your institutional email and ORCID.
3) Paste this text into the IEEE conference template (IEEEtran).
4) Insert figures from `research/figures/` as Fig. 1-5.
5) Rewrite any sentence that does not sound like you. Prefer short sentences.
6) Avoid long em dashes in the final PDF. Use commas or separate sentences.
7) Run your university plagiarism checker.
8) Submit to a student or workshop IEEE venue first (for example IEEE PESS). Do not start with a top Transactions journal.
