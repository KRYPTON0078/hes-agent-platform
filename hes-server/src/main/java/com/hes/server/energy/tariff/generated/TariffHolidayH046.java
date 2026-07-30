package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/** Holiday half-hour tariff slot (treated as weekend-only calendar overlay). */
@Component
public class TariffHolidayH046 implements TariffSlot {
    @Override public String id() { return "TAR-HOL-046"; }
    @Override public int startMinuteInclusive() { return 1380; }
    @Override public int endMinuteExclusive() { return 1410; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.099"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.038"); }
    @Override public boolean preferCharge() { return false; }
}