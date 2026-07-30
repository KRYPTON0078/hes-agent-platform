package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ067 implements TariffSlot {
    @Override public String id() { return "TAR-WD-067"; }
    @Override public int startMinuteInclusive() { return 1005; }
    @Override public int endMinuteExclusive() { return 1020; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.28"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.082"); }
    @Override public boolean preferCharge() { return false; }
}