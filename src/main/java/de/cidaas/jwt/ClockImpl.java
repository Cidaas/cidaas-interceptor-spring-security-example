package de.cidaas.jwt;

import de.cidaas.jwt.interfaces.Clock;

import java.util.Date;

final class ClockImpl implements Clock {

    ClockImpl() {
    }

    @Override
    public Date getToday() {
        return new Date();
    }
}
