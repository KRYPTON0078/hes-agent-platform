package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffShoulderH004 implements TariffSlot {
    @Override public String id() { return "TAR-SH-004"; }
    @Override public int startMinuteInclusive() { return 120; }
    @Override public int endMinuteExclusive() { return 150; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.118"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.049"); }
    @Override public boolean preferCharge() { return true; }
}