package com.pochixcx.minecraft;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.pochixcx.player.PlayerManager;
import com.pochixcx.player.PlayerManagerException;
import com.pochixcx.util.Utils;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ConsoleCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("sentrix_whitelist")
                .requires(source -> !source.isExecutedByPlayer())
                .then(CommandManager.argument("username", StringArgumentType.string()).then(CommandManager
                        .argument("ip", StringArgumentType.string())
                        .executes(context -> {
                            if (context.getInput().trim().split("\\s+").length != 3) {
                                context.getSource().sendError(Text.literal("Usage: /add-player <username> <ip>"));
                                return 0;
                            }

                            String username = StringArgumentType.getString(context, "username");
                            String ip = StringArgumentType.getString(context, "ip");

                            if (!Utils.isValidIp(ip)) {
                                context.getSource().sendError(Text.literal("Invalid IP format"));
                                return 0;
                            }

                            try {
                                PlayerManager.whitelistPlayer(username, ip);
                            } catch (PlayerManagerException e) {
                                context.getSource().sendError(Text.literal(e.getMessage()));
                                return 0;
                            }
                            context.getSource().sendFeedback(() -> Text.literal("New player whitelisted: " + username),
                                    false);

                            return Command.SINGLE_SUCCESS;
                        }))));

        dispatcher.register(CommandManager.literal("sentrix_remove_player")
                .requires(source -> !source.isExecutedByPlayer())
                .then(CommandManager.argument("username", StringArgumentType.string())
                        .executes(context -> {
                            if (context.getInput().trim().split("\\s+").length != 2) {
                                context.getSource().sendError(Text.literal("Usage: /sentrix_remove_player <username>"));
                                return 0;
                            }

                            String username = StringArgumentType.getString(context, "username");

                            try {
                                PlayerManager.removePlayer(username);
                                context.getSource().sendFeedback(() -> Text.literal("Player removed "), false);

                                return Command.SINGLE_SUCCESS;
                            } catch (PlayerManagerException e) {
                                context.getSource().sendError(Text.literal(e.getMessage()));
                                return 0;
                            }
                        })));

        dispatcher.register(CommandManager.literal("sentrix_add_ip")
                .requires(source -> !source.isExecutedByPlayer())
                .then(CommandManager.argument("username", StringArgumentType.string())
                        .then(CommandManager.argument("ip", StringArgumentType.string())
                                .executes(context -> {
                                    if (context.getInput().trim()
                                            .split("\\s+").length != 3) {
                                        context.getSource().sendError(
                                                Text.literal("Usage: /sentrix_add_ip <username> <ip>"));
                                        return 0;
                                    }

                                    String username = StringArgumentType.getString(context, "username");
                                    String ip = StringArgumentType.getString(context, "ip");

                                    if (!Utils.isValidIp(ip)) {
                                        context.getSource().sendError(Text.literal("Invalid IP format"));
                                        return 0;
                                    }

                                    try {
                                        PlayerManager.addPlayerIp(username, ip);

                                        context.getSource().sendFeedback(
                                                () -> Text.literal("New player IP added: "), false);

                                        return Command.SINGLE_SUCCESS;
                                    } catch (PlayerManagerException e) {
                                        context.getSource().sendError(Text.literal(e.getMessage()));
                                        return 0;
                                    }
                                }))));

        dispatcher.register(CommandManager.literal("sentrix_remove_ip")
                .requires(source -> !source.isExecutedByPlayer())
                .then(CommandManager.argument("username", StringArgumentType.string())
                        .then(CommandManager.argument("ip", StringArgumentType.string())
                                .executes(context -> {
                                    if (context.getInput().trim()
                                            .split("\\s+").length != 3) {
                                        context.getSource().sendError(
                                                Text.literal("Usage: /sentrix_remove_ip <username> <ip>"));
                                        return 0;
                                    }

                                    String username = StringArgumentType.getString(context, "username");
                                    String ip = StringArgumentType.getString(context, "ip");

                                    if (!Utils.isValidIp(ip)) {
                                        context.getSource().sendError(Text.literal("Invalid IP format"));
                                        return 0;
                                    }

                                    try {
                                        PlayerManager.removePlayerIp(username, ip);

                                        context.getSource().sendFeedback(
                                                () -> Text.literal("IP remove for player: " + username), false);

                                        return Command.SINGLE_SUCCESS;
                                    } catch (PlayerManagerException e) {
                                        context.getSource().sendError(Text.literal(e.getMessage()));
                                        return 0;
                                    }
                                }))));

        dispatcher.register(CommandManager.literal("sentrix_reload").requires(source -> !source.isExecutedByPlayer())
                .executes(context -> {
                    PlayerManager.reload();
                    context.getSource().sendFeedback(() -> Text.literal("Reloaded players"), false);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(CommandManager.literal("sentrix_count").requires(source -> !source.isExecutedByPlayer())
                .executes(context -> {
                    String size = String.valueOf(PlayerManager.getPlayerCount());
                    context.getSource().sendFeedback(() -> Text.literal("Player count: " + size), false);
                    return Command.SINGLE_SUCCESS;
                }));
    }
}
