package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffWeekdayQ082 implements TariffSlot {
    @Override public String id() { return "TAR-WD-082"; }
    @Override public int startMinuteInclusive() { return 1230; }
    @Override public int endMinuteExclusive() { return 1245; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.34"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.082"); }
    @Override public boolean preferCharge() { return false; }
}