package com.pochixcx.discord;

import static com.pochixcx.Sentrix.ADMIN_CHANNEL;
import static com.pochixcx.Sentrix.LOGGER;

import com.pochixcx.player.PlayerManager;
import com.pochixcx.util.Utils;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionType;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        try {
            if (event.getType() != InteractionType.COMMAND)
                return;

            if (((event.getUser().isBot())))
                return;

            if (!Utils.isAdmin(event.getUser().getId())) {
                event.reply("**You don't have permission to use this command**").setEphemeral(true).queue();
                return;
            }

            if (!event.getChannelId().equals(ADMIN_CHANNEL.getId())) {

                event.reply("**You can't use this command here**").setEphemeral(true).queue();
                return;
            }

            if (event.getName().equals("whitelist")) {

                String username = event.getOption("username").getAsString();
                String ip = event.getOption("ip").getAsString();

                try {

                    if (!Utils.isValidIp(ip)) {
                        event.reply("**Invalid IP format**").setEphemeral(true).queue();
                        return;
                    }

                    PlayerManager.whitelistPlayer(username, ip);
                    event.reply("Player " + username + " added").queue();
                    return;
                } catch (Exception e) {
                    event.reply("Error: " + e.getMessage()).setEphemeral(true).queue();
                    return;
                }
            }

            if (event.getName().equals("remove_player")) {

                try {
                    String name = event.getOption("name").getAsString();

                    PlayerManager.removePlayer(name);
                    event.reply("Player " + name + " removed").queue();

                    return;
                } catch (Exception e) {
                    event.reply("Error: " + e.getMessage()).setEphemeral(true).queue();
                }
            }

            if (event.getName().equals("add_ip")) {

                String username = event.getOption("username").getAsString();
                String ip = event.getOption("ip").getAsString();

                if (!Utils.isValidIp(ip)) {
                    event.reply("**Invalid IP format**").setEphemeral(true).queue();
                    return;
                }
                try {
                    PlayerManager.addPlayerIp(username, ip);
                    event.reply("**IP added, " + username + " can now access the server**").setEphemeral(true)
                            .queue();
                    return;

                } catch (Exception e) {
                    event.reply("Error: " + e.getMessage()).setEphemeral(true).queue();
                    return;
                }
            }

            if (event.getName().equals("player_count")) {

                event.reply(String.valueOf(PlayerManager.getPlayerCount())).queue();
                return;
            }

            // if (event.getName().equals("info")) {

            // Modal modal = Utils.verifyIpModal();

            // event.replyModal(modal).queue();
            // return;

            // }

        } catch (

        Exception e) {
            LOGGER.warn(e.getStackTrace().toString());
            event.reply("Error: " + e.getMessage()).setEphemeral(true).queue();
        }

    }

}
