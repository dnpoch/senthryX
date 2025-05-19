package com.pochixcx.discord;

import static com.pochixcx.Sentrix.ADMIN_CHANNEL;
import static com.pochixcx.Sentrix.LOGGER;

import java.util.List;

import com.pochixcx.discord.util.ModalBuilder;
import com.pochixcx.discord.util.TextInputSpec;
import com.pochixcx.player.PlayerManager;
import com.pochixcx.util.Utils;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionType;
import net.dv8tion.jda.api.interactions.modals.Modal;

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

                if (event.getName().equals("whitelist")) {

                    List<TextInputSpec> specs = List.of(
                            new TextInputSpec("username", "Username", "Minecraft IGN", 1, 20),
                            new TextInputSpec("ip", "IP address", "IP address", 7, 15));

                    Modal modal = ModalBuilder.build(
                            "modal_whitelist",
                            "Whitelist a new player",
                            specs);

                    event.replyModal(modal).queue();
                    return;
                }

                if (event.getName().equals("remove_player")) {

                    try {
                        String name = event.getOption("name").getAsString();

                        PlayerManager.removePlayer(name);
                        event.reply("Player " + name + " removed").queue();
                    } catch (Exception e) {
                        event.reply("Error: " + e.getMessage()).setEphemeral(true).queue();
                    }
                }

                if (event.getName().equals("add_ip")) {

                    List<TextInputSpec> specs = List.of(
                            new TextInputSpec("username", "Username", "Minecraft IGN", 1, 20),
                            new TextInputSpec("ip", "IP address", "IP address", 7, 15));

                    Modal modal = ModalBuilder.build(
                            "modal_add_ip",
                            "Add a new player IP address",
                            specs);

                    event.replyModal(modal).queue();
                    return;
                }

                if (event.getName().equals("remove_ip")) {

                    List<TextInputSpec> specs = List.of(
                            new TextInputSpec("username", "Username", "Minecraft IGN", 1, 20),
                            new TextInputSpec("ip", "IP address", "IP address", 7, 15));

                    Modal modal = ModalBuilder.build(
                            "modal_remove_ip",
                            "Remove a player's IP address",
                            specs);
                    event.replyModal(modal).queue();
                    return;
                }

                if (event.getName().equals("count")) {

                    event.reply(String.valueOf(PlayerManager.getPlayerCount())).queue();
                    return;
                }

                // if (event.getName().equals("info")) {

                // Modal modal = Utils.verifyIpModal();

                // event.replyModal(modal).queue();
                // return;

                // }

            }

            event.reply("**You can't use this command here**").setEphemeral(true).queue();

        } catch (

        Exception e) {
            LOGGER.warn(e.getStackTrace().toString());
            event.reply("Error: " + e.getMessage()).setEphemeral(true).queue();
        }

    }

}
