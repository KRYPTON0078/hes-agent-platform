package com.hes.server.energy.tariff;

import java.math.BigDecimal;

public interface TariffSlot {
    String id();
    int startMinuteInclusive();
    int endMinuteExclusive();
    boolean weekendOnly();
    BigDecimal importRatePerKwh();
    BigDecimal exportRatePerKwh();
    boolean preferCharge();
}