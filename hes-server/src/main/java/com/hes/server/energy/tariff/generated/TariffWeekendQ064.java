package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ064 implements TariffSlot {
    @Override public String id() { return "TAR-WE-064"; }
    @Override public int startMinuteInclusive() { return 960; }
    @Override public int endMinuteExclusive() { return 975; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.1"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.0415"); }
    @Override public boolean preferCharge() { return false; }
}