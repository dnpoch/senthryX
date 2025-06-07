package com.pochixcx.minecraft.listener;

import static com.pochixcx.Senthryx.LOGGER;
import static com.pochixcx.Senthryx.jda;
import static com.pochixcx.Senthryx.ADMIN_CHANNEL;
import static com.pochixcx.Senthryx.LOG_CHANNEL;
import static com.pochixcx.Senthryx.CONFIG;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;

import com.mojang.authlib.GameProfile;
import com.pochixcx.minecraft.event.LoginResult;
import com.pochixcx.mixin.ServerLoginNetworkHandlerAccessor;
import com.pochixcx.player.Player;
import com.pochixcx.player.PlayerManager;
import com.pochixcx.util.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.MissingAccessException;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking.LoginSynchronizer;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
public class LoginEventHandler {

    /**
     * Registers the login event handler.
     */
    public static void register() {
        ServerLoginConnectionEvents.QUERY_START.register(LoginEventHandler::onLogin);

    }

    /**
     * This method is called when a player attempts to connect to the server.
     * It checks if the player is whitelisted and if their IP address used is binded
     * to the user.
     */
    private static void onLogin(ServerLoginNetworkHandler handler, MinecraftServer server, PacketSender packetSender,
            LoginSynchronizer sync) {
        ClientConnection connection = ((ServerLoginNetworkHandlerAccessor) handler).getClientConnection();
        GameProfile profile = ((ServerLoginNetworkHandlerAccessor) handler).getGameProfile();

        InetSocketAddress inetAddress = (InetSocketAddress) connection.getAddress();

        String username = profile.getName();
        String uuid = profile.getId().toString(); // Not used, but can be logged if needed
        String ip_address = inetAddress.getAddress().getHostAddress();

        if (username.length() == 0 || !(Utils.isValidIp(ip_address))) {
            LOGGER.warn("Username or IP address is empty, disconnecting player.");
            server.execute(() -> handler.disconnect(Text.of("Invalid")));
            return;
        }

        LOGGER.info("connection attempt info: {} " + "username: " + username + " ip_address: "
                + ip_address);

        sync.waitFor(CompletableFuture.runAsync(() -> verifyer(handler, username, ip_address, server), server));

    }

    /****
     * The method used to verify if player is whitelisted or not
     * 
     * @param handler  network handler
     * @param username username of the player
     * @param ip       IP address of the player
     * @param server   Minecraft server instance
     */
    private static void verifyer(ServerLoginNetworkHandler handler, String username, String ip,
            MinecraftServer server) {
        Player player = PlayerManager.findPlayer(username);

        if (player == null) {
            if (jda != null) {
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Login attempt from unknown player")
                        .addField("", "**🧑‍💻 Username:** " + username, false)
                        .addField("", "**🌐 IP address: **" + ip, false)
                        .setColor(0xFF7043)
                        .build();

                ADMIN_CHANNEL.sendMessageEmbeds(embed).queue();
            }

            LOGGER.info("disconnected not whitelisted: " + username);
            server.execute(() -> handler.disconnect(Text.of(CONFIG.kick_message)));
            return;
        }

        if (!player.getIps().contains(ip)) {
            if (jda != null && CONFIG.enable_public_logging) {
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle(username + " attempted to login from unknown ip")
                        .addField("", "**🌐 IP address:** " + Utils.obfuscateIp(ip), false)
                        .setColor(0xFF0000)
                        .build();

                LOG_CHANNEL.sendMessageEmbeds(embed).queue();

            }

            LOGGER.info("disconnected unknow ip attempt: " + username);
            server.execute(() -> handler.disconnect(Text.of(CONFIG.unknown_ip_message)));
            return;
        }

        LOGGER.info("attempt successful: " + username);
        return;
    }

}
