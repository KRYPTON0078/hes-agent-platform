# ADR-011: Deterministic analytics and forecast stub

## Decision
Use SQL rollups for hourly SOC/throughput and a linear TOU-aware forecast model (linear-tou-v1) instead of opaque ML.

## Rationale
Recruiters and operators can verify formulas; FleetKpi beans encode explicit KPI math.