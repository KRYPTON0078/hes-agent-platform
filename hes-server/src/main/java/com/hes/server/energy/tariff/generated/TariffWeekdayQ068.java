package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ068 implements TariffSlot {
    @Override public String id() { return "TAR-WD-068"; }
    @Override public int startMinuteInclusive() { return 1020; }
    @Override public int endMinuteExclusive() { return 1035; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.28"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.083"); }
    @Override public boolean preferCharge() { return false; }
}