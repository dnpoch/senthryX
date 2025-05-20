package com.pochixcx.minecraft;

import static com.pochixcx.Sentrix.ADMIN_CHANNEL;
import static com.pochixcx.Sentrix.BROADCAST_CHANNELS;
import static com.pochixcx.Sentrix.CONFIG;
import static com.pochixcx.Sentrix.jda;
import static com.pochixcx.Sentrix.LOGGER;

import java.util.UUID;

import com.pochixcx.minecraft.event.LoginEvent;
import com.pochixcx.minecraft.event.LoginResult;
import com.pochixcx.player.Player;
import com.pochixcx.player.PlayerManager;
import com.pochixcx.util.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.MissingAccessException;

public class LoginEventListener {

    public static void init() {

        LoginEvent.ON_PLAYER_LOGIN.register((uuid, username, ip) -> onLogin(uuid, username, ip));

    }

    private static LoginResult onLogin(UUID uuid, String username, String ip) {
        LOGGER.info("USERNAME: " + username + " UUID: " + uuid + " IP: " + ip);

        Player player = PlayerManager.findPlayer(username);

        if (player == null) {

            if (jda != null) {
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Login attempt from unknown player")
                        .addField("", "**🧑‍💻 Username:** " + username, false)
                        .addField("", "**🆔 UUID: **" + uuid.toString(), false)
                        .addField("", "**🌐 IP address: **" + ip, false)
                        .setColor(0xFF7043)
                        .build();

                ADMIN_CHANNEL.sendMessageEmbeds(embed).queue();
            }
            return LoginResult.deny(CONFIG.kick_message);
        }

        if (player.getIps().contains(ip)) {
            return LoginResult.allow();
        }

        if (jda != null) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Login attempt from unknown ip")
                    .addField("", "**🧑‍💻 Username:** " + username, false)
                    .addField("", "**🌐 IP address:** " + Utils.obfuscateIp(ip), false)
                    .setColor(0xFF0000)
                    .build();

            BROADCAST_CHANNELS.forEach(channel -> {
                try {
                    channel.sendMessageEmbeds(embed).queue();
                } catch (Exception e) {
                    if (e instanceof MissingAccessException) {
                        LOGGER.error("Error sending message to channel: " + channel.getName() + " " + e.getMessage());
                        return;
                    }

                }
            });
        }

        return LoginResult.deny(CONFIG.unknown_ip_message);

    }

}
