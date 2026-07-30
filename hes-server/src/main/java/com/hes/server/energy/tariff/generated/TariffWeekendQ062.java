package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ062 implements TariffSlot {
    @Override public String id() { return "TAR-WE-062"; }
    @Override public int startMinuteInclusive() { return 930; }
    @Override public int endMinuteExclusive() { return 945; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.1"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.052"); }
    @Override public boolean preferCharge() { return false; }
}