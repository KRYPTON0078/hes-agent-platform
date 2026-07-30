package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ056 implements TariffSlot {
    @Override public String id() { return "TAR-WE-056"; }
    @Override public int startMinuteInclusive() { return 840; }
    @Override public int endMinuteExclusive() { return 855; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.124"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.043"); }
    @Override public boolean preferCharge() { return true; }
}