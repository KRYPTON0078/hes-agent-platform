package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/** Holiday half-hour tariff slot (treated as weekend-only calendar overlay). */
@Component
public class TariffHolidayH024 implements TariffSlot {
    @Override public String id() { return "TAR-HOL-024"; }
    @Override public int startMinuteInclusive() { return 720; }
    @Override public int endMinuteExclusive() { return 750; }
    @Override public boolean weekendOnly() { return true; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.096"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.03"); }
    @Override public boolean preferCharge() { return true; }
}