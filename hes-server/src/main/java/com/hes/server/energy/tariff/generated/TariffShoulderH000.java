package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffShoulderH000 implements TariffSlot {
    @Override public String id() { return "TAR-SH-000"; }
    @Override public int startMinuteInclusive() { return 0; }
    @Override public int endMinuteExclusive() { return 30; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.11"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.045"); }
    @Override public boolean preferCharge() { return true; }
}