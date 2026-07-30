package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ092 implements TariffSlot {
    @Override public String id() { return "TAR-WE-092"; }
    @Override public int startMinuteInclusive() { return 1380; }
    @Override public int endMinuteExclusive() { return 1395; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.128"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.043"); }
    @Override public boolean preferCharge() { return false; }
}