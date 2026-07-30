package com.hes.server.energy.tariff;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TariffLookupService {
    private final List<TariffSlot> slots;

    public TariffLookupService(List<TariffSlot> slots) {
        this.slots = slots;
    }

    public Optional<TariffSlot> activeSlot(int minuteOfDay, boolean weekend) {
        return slots.stream()
                .filter(s -> s.weekendOnly() == weekend)
                .filter(s -> minuteOfDay >= s.startMinuteInclusive() && minuteOfDay < s.endMinuteExclusive())
                .min(Comparator.comparingInt(TariffSlot::startMinuteInclusive));
    }

    public boolean shouldPreferCharge(int minuteOfDay, boolean weekend) {
        return activeSlot(minuteOfDay, weekend).map(TariffSlot::preferCharge).orElse(false);
    }

    public BigDecimal importRate(int minuteOfDay, boolean weekend) {
        return activeSlot(minuteOfDay, weekend).map(TariffSlot::importRatePerKwh).orElse(BigDecimal.ZERO);
    }
}