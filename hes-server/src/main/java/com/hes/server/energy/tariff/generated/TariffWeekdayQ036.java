package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ036 implements TariffSlot {
    @Override public String id() { return "TAR-WD-036"; }
    @Override public int startMinuteInclusive() { return 540; }
    @Override public int endMinuteExclusive() { return 555; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.135"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.051"); }
    @Override public boolean preferCharge() { return true; }
}