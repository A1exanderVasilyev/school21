package com.catinasw.T03.domain.util;

import java.util.UUID;

public class RegistrationResponse {
    private final boolean isRegistrationSuccess;
    private final String registrationMessage;
    private final UUID uuid;

    private RegistrationResponse(boolean isRegistrationSuccess, String registrationMessage, UUID uuid) {
        this.isRegistrationSuccess = isRegistrationSuccess;
        this.registrationMessage = registrationMessage;
        this.uuid = uuid;
    }

    public static RegistrationResponse success(String message, UUID uuid) {
        return new RegistrationResponse(true, message, uuid);
    }

    public static RegistrationResponse failed (String message) {
        return new RegistrationResponse(false, message, null);
    }

    public boolean isRegistrationSuccess() {
        return isRegistrationSuccess;
    }

    public String getRegistrationMessage() {
        return registrationMessage;
    }

    public UUID getUuid() {
        return uuid;
    }
}
