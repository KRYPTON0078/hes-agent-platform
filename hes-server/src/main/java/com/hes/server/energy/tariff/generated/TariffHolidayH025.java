package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/** Holiday half-hour tariff slot (treated as weekend-only calendar overlay). */
@Component
public class TariffHolidayH025 implements TariffSlot {
    @Override public String id() { return "TAR-HOL-025"; }
    @Override public int startMinuteInclusive() { return 750; }
    @Override public int endMinuteExclusive() { return 780; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.096"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.032"); }
    @Override public boolean preferCharge() { return true; }
}