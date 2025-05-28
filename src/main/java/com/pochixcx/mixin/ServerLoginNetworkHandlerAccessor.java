package com.pochixcx.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.authlib.GameProfile;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerLoginNetworkHandler;

/****
 * mixin accessor for ServerLoginNetworkHandler
 */
@Mixin(ServerLoginNetworkHandler.class)
public interface ServerLoginNetworkHandlerAccessor {

    @Accessor("profile")
    GameProfile getGameProfile();

    @Accessor("connection")
    ClientConnection getClientConnection();

}
