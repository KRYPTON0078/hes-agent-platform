package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffShoulderH022 implements TariffSlot {
    @Override public String id() { return "TAR-SH-022"; }
    @Override public int startMinuteInclusive() { return 660; }
    @Override public int endMinuteExclusive() { return 690; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.114"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.051"); }
    @Override public boolean preferCharge() { return false; }
}