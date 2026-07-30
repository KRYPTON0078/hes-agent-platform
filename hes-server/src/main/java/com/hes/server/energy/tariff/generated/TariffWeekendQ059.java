package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ059 implements TariffSlot {
    @Override public String id() { return "TAR-WE-059"; }
    @Override public int startMinuteInclusive() { return 885; }
    @Override public int endMinuteExclusive() { return 900; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.128"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.0475"); }
    @Override public boolean preferCharge() { return true; }
}