# Lab notebook

Date: 2026-07-24
Author: Magne Dina Neves
Project: Residential ESMS coordinated integrity attack detection

## Setup
- Simulation language: Python 3.12
- Sample time: 60 s
- Battery: 10 kWh usable model, eta_c = eta_d = 0.95, |P|_max = 5 kW
- Horizon: 720 steps (12 h)
- Seed: 42
- Attack start: step 180 (3 h) unless benign

## Sign convention
- Positive power: discharge
- Negative power: charge

## Detector
- Energy-balance residual: reported dSoC minus expected dSoC from reported power
- Detector: energy-balance residual + threshold / EWMA / CUSUM
- Evaluation: 20 seeds, two schedules, mismatch sweep, stealth A5
- Note: first campaign with drift 0.002 missed A2. Retuned. Then upgraded after critical review.

## Scenarios
- benign: schedule only
- A1: constant SoC under-report of 0.12 after start
- A2: SoC ramp -0.0008 per step after start
- A3: inject -3 kW charge command; report only 20% of true power
- A4: A2 ramp + A3 inject; report 5% of true power

## Notes for paper honesty
- Software CPS testbed only. No field devices.
- Not a BMS cell-voltage FDIA study.
- Cite IEEE crowded line and state the ESMS-layer difference clearly.
