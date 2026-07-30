package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ002 implements TariffSlot {
    @Override public String id() { return "TAR-WD-002"; }
    @Override public int startMinuteInclusive() { return 30; }
    @Override public int endMinuteExclusive() { return 45; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.12"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.052"); }
    @Override public boolean preferCharge() { return true; }
}