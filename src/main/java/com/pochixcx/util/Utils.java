package com.pochixcx.util;

import static com.pochixcx.Senthryx.CONFIG;
import static com.pochixcx.Senthryx.jda;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class Utils {

        public static boolean isValidIp(String ip) {

                Pattern pattern = Pattern.compile(
                                "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

                Matcher matcher = pattern.matcher(ip);

                return matcher.matches();
        }

        public static boolean isAdmin(String id) {
                if (CONFIG.admin_ids.contains(id)) {
                        return true;
                }
                return false;
        }

        public static String obfuscateIp(String ip) {
                String[] octates = ip.split("\\.");

                if (octates.length != 4)
                        throw new IllegalArgumentException("Invalid IP address size");

                for (int i = octates.length - 2; i < 4; i++) {
                        octates[i] = octates[i].replaceAll(".", "X");
                }

                return String.join(".", octates);
        }

        public static void updateDiscordCommands() {
                CommandListUpdateAction commands = jda.updateCommands();

                commands.addCommands(Commands.slash("whitelist_add", "Whitelist a new player")
                                .setContexts(InteractionContextType.GUILD)
                                .addOptions(new OptionData(OptionType.STRING, "username", "Username of the player",
                                                true),
                                                new OptionData(OptionType.STRING, "ip", "IP address of the player",
                                                                true)));

                commands.addCommands(Commands.slash("whitelist_remove", "Removes a player from the whitelist")
                                .setContexts(InteractionContextType.GUILD)
                                .addOption(OptionType.STRING, "name", "Username of thhe player to remove", true));

                commands.addCommands(Commands.slash("add_ip", "To add a new player IP address")
                                .setContexts(InteractionContextType.GUILD)
                                .addOptions(new OptionData(OptionType.STRING, "username", "Username of the player",
                                                true),
                                                new OptionData(OptionType.STRING, "ip", "New IP address of the player",
                                                                true))
                                .setDefaultPermissions(DefaultMemberPermissions.DISABLED));

                commands.addCommands(Commands.slash("player_count", "To view the total count of whitelisted players")
                                .setContexts(InteractionContextType.GUILD)
                                .setDefaultPermissions(DefaultMemberPermissions.DISABLED));

                // commands.addCommands(Commands.slash("info", "Information about the bot")
                // .setContexts(InteractionContextType.GUILD)
                // .setDefaultPermissions(DefaultMemberPermissions.DISABLED));

                commands.queue();
        }

}
