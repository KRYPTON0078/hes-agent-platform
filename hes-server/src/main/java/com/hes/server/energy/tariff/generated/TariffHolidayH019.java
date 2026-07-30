package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/** Holiday half-hour tariff slot (treated as weekend-only calendar overlay). */
@Component
public class TariffHolidayH019 implements TariffSlot {
    @Override public String id() { return "TAR-HOL-019"; }
    @Override public int startMinuteInclusive() { return 570; }
    @Override public int endMinuteExclusive() { return 600; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.09"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.032"); }
    @Override public boolean preferCharge() { return false; }
}