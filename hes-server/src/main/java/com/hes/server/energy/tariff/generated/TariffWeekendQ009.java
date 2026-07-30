package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ009 implements TariffSlot {
    @Override public String id() { return "TAR-WE-009"; }
    @Override public int startMinuteInclusive() { return 135; }
    @Override public int endMinuteExclusive() { return 150; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.108"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.04"); }
    @Override public boolean preferCharge() { return false; }
}