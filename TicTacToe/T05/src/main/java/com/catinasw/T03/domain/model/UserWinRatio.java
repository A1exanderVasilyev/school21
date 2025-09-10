package com.catinasw.T03.domain.model;

import java.util.UUID;

public class UserWinRatio {
    private final UUID uuid;
    private final double winRatio;

    public UserWinRatio(UUID uuid, double winRatio) {
        this.uuid = uuid;
        this.winRatio = winRatio;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getWinRatio() {
        return winRatio;
    }
}
