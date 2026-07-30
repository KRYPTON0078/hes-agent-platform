# Performance notes: analytics indexes

- 	elemetry_hourly_rollup.uk_device_hour supports device timeline scans.
- ault_rate_daily.idx_fault_rate supports fleet worst-offender queries.
- energy_forecast.idx_forecast_hour supports horizon materialization.
Prefer range queries on indexed hour/day buckets; avoid full telemetry table scans for dashboards.