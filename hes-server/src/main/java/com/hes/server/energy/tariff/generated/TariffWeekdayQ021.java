package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ021 implements TariffSlot {
    @Override public String id() { return "TAR-WD-021"; }
    @Override public int startMinuteInclusive() { return 315; }
    @Override public int endMinuteExclusive() { return 330; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.145"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.05"); }
    @Override public boolean preferCharge() { return true; }
}