package com.pochixcx.minecraft.event;

public class LoginResult {

    private final boolean allowed;
    private final String kickMessage;

    private LoginResult(boolean allowed, String kickMessagee) {

        this.allowed = allowed;
        this.kickMessage = kickMessagee;

    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getKickMessage() {
        return kickMessage;
    }

    public static LoginResult allow() {
        return new LoginResult(true, null);
    }

    public static LoginResult deny(String reason) {
        return new LoginResult(false, reason);

    }
}
