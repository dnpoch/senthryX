package com.pochixcx.discord;

import static com.pochixcx.Sentrix.ADMIN_CHANNEL;
import static com.pochixcx.util.Utils.isValidIp;

import com.pochixcx.player.Player;
import com.pochixcx.player.PlayerManager;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionType;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

        if (event.getType() != InteractionType.MODAL_SUBMIT)
            return;

        if (event.getModalId().equals("modal_whitelist")) {

            if (event.getChannel().getIdLong() != ADMIN_CHANNEL.getIdLong()) {

                event.reply("You can't use this command here").setEphemeral(true).queue();
                return;

            }

            String username = event.getValue("username").getAsString();
            String ip = event.getValue("ip").getAsString();

            try {
                Player player = PlayerManager.findPlayer(username);

                if (player != null) {

                    event.reply("Player already exist").setEphemeral(true).queue();
                    return;

                }

                if (!isValidIp(ip)) {

                    event.reply("**Invalid IP format**").setEphemeral(true).queue();
                    return;

                }

                PlayerManager.whitelistPlayer(username, ip);
                event.reply("Player " + username + " added").queue();
                return;
            } catch (Exception e) {
                e.getStackTrace();
                event.reply("Error: " + e.getMessage()).setEphemeral(true).queue();
            }
        }

        if (event.getModalId().equals("modal_add_ip")) {
            try {
                String username = event.getValue("username").getAsString();
                String ip = event.getValue("ip").getAsString();

                if (isValidIp(ip)) {
                    PlayerManager.addPlayerIp(username, ip);

                    event.reply("**IP added, you can now access the server**").setEphemeral(true).queue();
                    return;
                }

                event.reply("**Invalid IP format**").setEphemeral(true).queue();
                return;

            } catch (Exception e) {
                e.getStackTrace();
                event.reply("Error: " + e.getMessage()).setEphemeral(true).queue();
                return;
            }
        }

        if (event.getModalId().equals("modal_remove_ip")) {
            try {
                String username = event.getValue("username").getAsString();
                String ip = event.getValue("ip").getAsString();

                if (isValidIp(ip)) {
                    PlayerManager.removePlayerIp(username, ip);
                    event.reply("**Player IP removed**").setEphemeral(true).queue();
                    return;
                }

                event.reply("**Invalid IP format**").setEphemeral(true).queue();
                return;

            } catch (Exception e) {
                e.getStackTrace();
                event.reply("Error: " + e.getMessage()).setEphemeral(true).queue();
                return;
            }
        }

    }

}
