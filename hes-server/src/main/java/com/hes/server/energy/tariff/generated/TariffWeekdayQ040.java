package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ040 implements TariffSlot {
    @Override public String id() { return "TAR-WD-040"; }
    @Override public int startMinuteInclusive() { return 600; }
    @Override public int endMinuteExclusive() { return 615; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.14"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.055"); }
    @Override public boolean preferCharge() { return true; }
}