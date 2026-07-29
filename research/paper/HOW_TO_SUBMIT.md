# How to get this paper submitted

## Status
Manuscript is **ready to submit** after the final polish pass. Science claims match held-out results. Remaining blocker is logistics (your email + Overleaf compile).

## Overleaf checklist (do this once before PDF upload)

1. **Author email (manual):** In `main.tex`, replace  
   `Email: (add institutional email before submission)`  
   with your University of Macau address. Do not invent a coauthor or ORCID if you do not have one yet.
2. **Figures:** Upload these PNGs so names match `\includegraphics` exactly (keep filenames; they are fine):
   - `baseline_roc.png`
   - `mdip_curves.png`
   - `a6_alpha_sweep.png`
   - `A5_stealth_consistent.png`  
   Either put them in a `figures/` folder next to `main.tex`, or upload `research/figures/` as a sibling and keep `\graphicspath{{figures/}{../figures/}{./}}`.
3. **Compile twice:** `pdflatex main.tex` (or Overleaf Recompile) until no undefined references.
4. **DOI spot-check:** Click each `doi:` link in the PDF bibliography; all seven should resolve on IEEE Xplore / MDPI.
5. **Table numbers:** Confirm Table I rates match `../results/summary.json` detection rates.

## Compile locally

```bash
cd research/paper
pdflatex main.tex
pdflatex main.tex
```

## Venue options (conference first)

- IEEE SmartGridComm (short paper / poster; stronger if you later add one real load trace)
- ACM e-Energy (methodology-friendly simulation studies)
- IEEE ISGT student poster competition
- CPS-IoT Week workshops (e.g., CyPhy, SafeThings)
- IEEE student / workshop tracks with Xplore (e.g., PESS-class)

Prefer one venue at a time. Do not start with a top Transactions journal.

## Authenticity rules

- Do not invent coauthors, advisors, funding, or field deployments.
- Every number in the results tables must match `../results/summary.json` / MDIP CSVs.
- If a venue asks about generative AI assistance, disclose drafting help honestly per their policy.

## Submission package

- PDF from `main.tex`
- Optional zip of `research/src`, `research/results`, `research/figures`
- Short cover note: detectability margins for meter-aware charge-injection monitoring (not a new detector)
