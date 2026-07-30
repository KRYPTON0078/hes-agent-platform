package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ032 implements TariffSlot {
    @Override public String id() { return "TAR-WD-032"; }
    @Override public int startMinuteInclusive() { return 480; }
    @Override public int endMinuteExclusive() { return 495; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.13"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.054"); }
    @Override public boolean preferCharge() { return true; }
}