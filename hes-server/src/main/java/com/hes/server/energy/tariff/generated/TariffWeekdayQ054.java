package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ054 implements TariffSlot {
    @Override public String id() { return "TAR-WD-054"; }
    @Override public int startMinuteInclusive() { return 810; }
    @Override public int endMinuteExclusive() { return 825; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.13"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.055"); }
    @Override public boolean preferCharge() { return true; }
}