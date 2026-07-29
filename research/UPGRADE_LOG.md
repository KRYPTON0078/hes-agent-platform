# Phase-1 upgrade log (response to harsh review)

Date: 2026-07-24

## New core claim
ESMS-only energy-balance residuals are bypassable by consistent dual spoofing.
Independent household net meter restores detection of unauthorized charge injection.

## Code
- `house.py`: load + meter
- `detector.py`: S1 / S2 / fuse + calibration
- `simulate.py`: full CPS loop with noise/dropouts
- `run_campaign.py`: Monte Carlo, multi-day, ROC sweep

## Headline results (20 seeds)
- A1--A4: S1 CUSUM detects inconsistent spoofs
- A5: S1 weak (0.25); S2 CUSUM 1.00 at delay 0; fuse 1.00
- S2 scale sweep: A5 TPR 1.0 with benign FPR driven to 0 at higher scales

## Overleaf
Re-upload `main.tex` and figures, especially:
- `A5_stealth_consistent.png`
- `A4_coordinated.png`
- `roc_s2_a5.png`
