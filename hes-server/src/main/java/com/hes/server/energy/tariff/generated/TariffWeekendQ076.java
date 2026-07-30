package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ076 implements TariffSlot {
    @Override public String id() { return "TAR-WE-076"; }
    @Override public int startMinuteInclusive() { return 1140; }
    @Override public int endMinuteExclusive() { return 1155; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.112"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.046"); }
    @Override public boolean preferCharge() { return false; }
}