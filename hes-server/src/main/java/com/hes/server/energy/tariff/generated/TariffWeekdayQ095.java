package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ095 implements TariffSlot {
    @Override public String id() { return "TAR-WD-095"; }
    @Override public int startMinuteInclusive() { return 1425; }
    @Override public int endMinuteExclusive() { return 1440; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.12"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.054"); }
    @Override public boolean preferCharge() { return true; }
}