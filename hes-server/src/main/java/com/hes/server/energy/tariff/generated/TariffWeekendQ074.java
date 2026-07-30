package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ074 implements TariffSlot {
    @Override public String id() { return "TAR-WE-074"; }
    @Override public int startMinuteInclusive() { return 1110; }
    @Override public int endMinuteExclusive() { return 1125; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.108"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.043"); }
    @Override public boolean preferCharge() { return false; }
}