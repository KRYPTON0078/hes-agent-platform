package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ070 implements TariffSlot {
    @Override public String id() { return "TAR-WD-070"; }
    @Override public int startMinuteInclusive() { return 1050; }
    @Override public int endMinuteExclusive() { return 1065; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.3"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.08"); }
    @Override public boolean preferCharge() { return false; }
}