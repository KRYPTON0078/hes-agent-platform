package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ061 implements TariffSlot {
    @Override public String id() { return "TAR-WE-061"; }
    @Override public int startMinuteInclusive() { return 915; }
    @Override public int endMinuteExclusive() { return 930; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.128"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.0505"); }
    @Override public boolean preferCharge() { return true; }
}