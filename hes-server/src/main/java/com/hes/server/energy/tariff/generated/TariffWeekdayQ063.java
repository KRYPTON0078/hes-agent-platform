package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ063 implements TariffSlot {
    @Override public String id() { return "TAR-WD-063"; }
    @Override public int startMinuteInclusive() { return 945; }
    @Override public int endMinuteExclusive() { return 960; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.14"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.05"); }
    @Override public boolean preferCharge() { return true; }
}