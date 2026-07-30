package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ017 implements TariffSlot {
    @Override public String id() { return "TAR-WD-017"; }
    @Override public int startMinuteInclusive() { return 255; }
    @Override public int endMinuteExclusive() { return 270; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.14"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.053"); }
    @Override public boolean preferCharge() { return true; }
}