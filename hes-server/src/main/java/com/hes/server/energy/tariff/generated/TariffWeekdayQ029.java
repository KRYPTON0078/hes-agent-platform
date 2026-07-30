package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ029 implements TariffSlot {
    @Override public String id() { return "TAR-WD-029"; }
    @Override public int startMinuteInclusive() { return 435; }
    @Override public int endMinuteExclusive() { return 450; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.125"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.051"); }
    @Override public boolean preferCharge() { return true; }
}