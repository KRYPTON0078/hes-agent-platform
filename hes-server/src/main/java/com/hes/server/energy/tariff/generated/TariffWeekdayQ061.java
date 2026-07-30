package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ061 implements TariffSlot {
    @Override public String id() { return "TAR-WD-061"; }
    @Override public int startMinuteInclusive() { return 915; }
    @Override public int endMinuteExclusive() { return 930; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.135"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.055"); }
    @Override public boolean preferCharge() { return true; }
}