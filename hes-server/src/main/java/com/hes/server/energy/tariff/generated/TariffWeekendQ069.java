package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ069 implements TariffSlot {
    @Override public String id() { return "TAR-WE-069"; }
    @Override public int startMinuteInclusive() { return 1035; }
    @Override public int endMinuteExclusive() { return 1050; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.104"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.049"); }
    @Override public boolean preferCharge() { return false; }
}