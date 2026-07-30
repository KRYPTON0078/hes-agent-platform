package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ073 implements TariffSlot {
    @Override public String id() { return "TAR-WE-073"; }
    @Override public int startMinuteInclusive() { return 1095; }
    @Override public int endMinuteExclusive() { return 1110; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.108"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.0415"); }
    @Override public boolean preferCharge() { return false; }
}