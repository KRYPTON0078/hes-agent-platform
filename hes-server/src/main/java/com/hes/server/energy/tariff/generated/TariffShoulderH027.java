package com.hes.server.energy.tariff.generated;

import com.hes.server.energy.tariff.TariffSlot;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TariffShoulderH027 implements TariffSlot {
    @Override public String id() { return "TAR-SH-027"; }
    @Override public int startMinuteInclusive() { return 810; }
    @Override public int endMinuteExclusive() { return 840; }
    @Override public boolean weekendOnly() { return false; }
    @Override public BigDecimal importRatePerKwh() { return new BigDecimal("0.126"); }
    @Override public BigDecimal exportRatePerKwh() { return new BigDecimal("0.048"); }
    @Override public boolean preferCharge() { return false; }
}