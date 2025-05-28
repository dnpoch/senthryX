package com.pochixcx.minecraft.event;

import java.util.UUID;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Deprecated
public interface LoginEvent {

    Event<OnPlayerLogin> ON_PLAYER_LOGIN = EventFactory.createArrayBacked(OnPlayerLogin.class,
            callbacks -> (uuid, username, ip) -> {
                for (OnPlayerLogin callback : callbacks) {
                    LoginResult result = callback.onLogin(uuid, username, ip);
                    if (!result.isAllowed())
                        return LoginResult.deny(result.getKickMessage());
                }
                return LoginResult.allow();
            });

    interface OnPlayerLogin {
        LoginResult onLogin(UUID uuid, String username, String ip);
    }

}
