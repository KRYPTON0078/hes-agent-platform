package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ075 implements TariffSlot {
    @Override public String id() { return "TAR-WD-075"; }
    @Override public int startMinuteInclusive() { return 1125; }
    @Override public int endMinuteExclusive() { return 1140; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.32"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.08"); }
    @Override public boolean preferCharge() { return false; }
}