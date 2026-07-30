package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffShoulderH017 implements TariffSlot {
    @Override public String id() { return "TAR-SH-017"; }
    @Override public int startMinuteInclusive() { return 510; }
    @Override public int endMinuteExclusive() { return 540; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.19"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.046"); }
    @Override public boolean preferCharge() { return false; }
}