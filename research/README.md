# Residential ESMS integrity-attack study

Solo research package for Magne Dina Neves.

Paper draft (Markdown): [paper/DRAFT.md](paper/DRAFT.md)
LaTeX manuscript: [paper/main.tex](paper/main.tex)
Literature notes: [LITERATURE.md](LITERATURE.md)
Lab notes: [LAB_NOTEBOOK.md](LAB_NOTEBOOK.md)
How to submit: [paper/HOW_TO_SUBMIT.md](paper/HOW_TO_SUBMIT.md)

## What this is

A software cyber-physical testbed that:

1. Simulates a 10 kWh residential battery with Coulomb counting
2. Applies ESMS-layer attacks (SoC spoofing and/or charge-command injection)
3. Detects inconsistency with an energy-balance residual + CUSUM
4. Writes CSV logs and PNG figures used in the draft

This is intentionally not a BMS cell-voltage FDIA remake.

## Run

```bash
python -m pip install -r requirements.txt
python src/run_campaign.py
```

Outputs:

- `results/*.csv` and `results/summary.json`
- `figures/*.png`

## Writing style note for the draft

The draft avoids long em dashes and generic "AI essay" filler. Still rewrite the prose in your own words before submission.
