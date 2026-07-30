package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffShoulderH011 implements TariffSlot {
    @Override public String id() { return "TAR-SH-011"; }
    @Override public int startMinuteInclusive() { return 330; }
    @Override public int endMinuteExclusive() { return 360; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.114"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.048"); }
    @Override public boolean preferCharge() { return true; }
}