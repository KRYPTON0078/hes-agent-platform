# Brief for Prof. Keng-Weng Lao

Do **not** list Prof. Lao as coauthor until he agrees.

## Status: ready to submit

A final critical evaluation judged the draft **ready for submission** to a workshop / student / empirical CPS-security venue. Optional polish (abstract energy impact, seed clarification) is in `main.tex`. Manual remaining step: Magne fills in the institutional email in the author block before PDF upload.

> For a stealth consistent charge-injection adversary, we quantify MDIP under meter/load noise, compare S2 threshold vs EWMA vs CUSUM at matched FPR, and show when S2 collapses under load-estimate spoofing **given read-only meter access but no forge capability**.

## Headline numbers (held-out)
- A5: S1 0.15 / S2 1.0 (delay 0)
- 14-day fuse APD ≈ 0.086/day
- MDIP (DR≥0.9): **0.05 kW** on 8/9 noise pairs; **0.1 kW** on one pair
- Nominal 0.05 kW delay ≈ **127 min** ≈ **0.1 kWh** unauthorized energy before alarm
- A6: S2 DR=1 through α=0.9; DR=0 at α=1 (with meter read)

## Venue options
- IEEE SmartGridComm (short / poster; add real load trace later if possible)
- ACM e-Energy
- IEEE ISGT student poster
- CPS-IoT Week workshops (CyPhy, SafeThings)
- IEEE student workshop tracks (PESS-class)

Conference first; IEEE Access only if conference deadlines miss. Not yet top Transactions without real-load / HIL.

## Email draft

Subject: ESMS detectability margins draft ready to submit (workshop)

Dear Prof. Lao,

I am Magne Dina Neves (Year-4 ECE). I prepared a short empirical paper on residential ESMS integrity monitoring. After several review iterations, the draft is ready for a workshop-style submission: a detectability-margins study (MDIP, matched FPR baselines, load-estimate adversary with read-only meter access), not a claim of a new detector.

I would value your advice on venue choice, and whether you would consider supervising a Phase-2 extension on public residential load data or HIL after submission.

Best regards,  
Magne Dina Neves  
University of Macau

Contact: johnnylao@um.edu.mo  
Homepage: https://fst.um.edu.mo/people/johnnylao/

## Attach
- Overleaf PDF (`main.tex`) after Magne inserts email
- `mdip_curves.png`, `a6_alpha_sweep.png`, `baseline_roc.png`
- `research/results/summary.json`

## Phase 2 (with his guidance)
1. Replay attacks on Pecan Street (or local) load traces with the same held-out split  
2. Optional adaptive $P_L$ estimator vs textbook residual  
3. HIL / inverter non-ideality only if lab access exists
