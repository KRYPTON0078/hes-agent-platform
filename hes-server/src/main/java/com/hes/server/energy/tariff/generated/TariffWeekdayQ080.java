package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ080 implements TariffSlot {
    @Override public String id() { return "TAR-WD-080"; }
    @Override public int startMinuteInclusive() { return 1200; }
    @Override public int endMinuteExclusive() { return 1215; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.34"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.08"); }
    @Override public boolean preferCharge() { return false; }
}