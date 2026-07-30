package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffShoulderH020 implements TariffSlot {
    @Override public String id() { return "TAR-SH-020"; }
    @Override public int startMinuteInclusive() { return 600; }
    @Override public int endMinuteExclusive() { return 630; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.11"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.049"); }
    @Override public boolean preferCharge() { return false; }
}