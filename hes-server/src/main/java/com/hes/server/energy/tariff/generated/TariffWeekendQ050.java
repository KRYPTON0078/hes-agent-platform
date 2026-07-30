package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekendQ050 implements TariffSlot {
    @Override public String id() { return "TAR-WE-050"; }
    @Override public int startMinuteInclusive() { return 750; }
    @Override public int endMinuteExclusive() { return 765; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.116"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.0475"); }
    @Override public boolean preferCharge() { return true; }
}