package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ019 implements TariffSlot {
    @Override public String id() { return "TAR-WE-019"; }
    @Override public int startMinuteInclusive() { return 285; }
    @Override public int endMinuteExclusive() { return 300; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.12"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.0415"); }
    @Override public boolean preferCharge() { return false; }
}