package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffShoulderH008 implements TariffSlot {
    @Override public String id() { return "TAR-SH-008"; }
    @Override public int startMinuteInclusive() { return 240; }
    @Override public int endMinuteExclusive() { return 270; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.126"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.045"); }
    @Override public boolean preferCharge() { return true; }
}