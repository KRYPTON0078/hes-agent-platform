# Energy Ops Runbook

## Charge schedules
1. Create a schedule via POST /api/v1/ops/schedules.
2. Add windows (TOU_PEAK_DISCHARGE, TOU_OFFPEAK_CHARGE, SOC floor/ceiling, weekend eco, export limit, demand response).
3. Evaluate with device SOC / export / DR flag; executions are audited in schedule_execution.

## TOU tariff slots
Weekday and weekend quarter-hour slots resolve import/export rates via TariffLookupService.
Peak weekday hours (17:00-21:00) prefer discharge; overnight prefers charge.

## Operator checklist
- Confirm timezone on schedule matches site locale.
- Keep SOC floor >= 15% for battery health.
- Review recent executions before enabling demand-response windows.