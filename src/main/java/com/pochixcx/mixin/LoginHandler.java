package com.pochixcx.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.pochixcx.minecraft.event.LoginEvent;
import com.pochixcx.minecraft.event.LoginResult;

@Mixin(ServerLoginNetworkHandler.class)
public class LoginHandler {

    private String username;
    private String ip;
    private UUID uuid;

    @Shadow
    private ClientConnection connection;
    @Shadow
    private MinecraftServer server;

    @Inject(at = @At("HEAD"), method = "onHello", cancellable = true)
    private void onLoginHello(LoginHelloC2SPacket packet, CallbackInfo info) {
        this.ip = extractIp(connection.getAddress().toString());
        this.username = packet.name();
        this.uuid = packet.profileId();
        ServerLoginNetworkHandler handler = (ServerLoginNetworkHandler) (Object) this;

        LoginResult result = LoginEvent.ON_PLAYER_LOGIN.invoker().onLogin(uuid, username, ip);

        if (!result.isAllowed()) {
            handler.disconnect(Text.of(result.getKickMessage() + "\nIP: " + ip));
            info.cancel();
            return;
        }

    }

    private static String extractIp(String input) {
        if (input.startsWith("/")) {
            input = input.substring(1);
        }
        return input.split(":")[0];
    }

}