package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ080 implements TariffSlot {
    @Override public String id() { return "TAR-WE-080"; }
    @Override public int startMinuteInclusive() { return 1200; }
    @Override public int endMinuteExclusive() { return 1215; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.116"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.052"); }
    @Override public boolean preferCharge() { return false; }
}