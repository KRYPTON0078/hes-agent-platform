package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ093 implements TariffSlot {
    @Override public String id() { return "TAR-WE-093"; }
    @Override public int startMinuteInclusive() { return 1395; }
    @Override public int endMinuteExclusive() { return 1410; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.128"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.0445"); }
    @Override public boolean preferCharge() { return false; }
}