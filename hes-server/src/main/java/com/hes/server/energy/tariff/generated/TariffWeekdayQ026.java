package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ026 implements TariffSlot {
    @Override public String id() { return "TAR-WD-026"; }
    @Override public int startMinuteInclusive() { return 390; }
    @Override public int endMinuteExclusive() { return 405; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.12"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.055"); }
    @Override public boolean preferCharge() { return true; }
}