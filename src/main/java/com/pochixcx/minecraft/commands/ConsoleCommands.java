package com.pochixcx.minecraft.commands;

import static com.pochixcx.Sentrix.MOD_ID;

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
        dispatcher.register(CommandManager.literal(MOD_ID)
                .requires(source -> !source.isExecutedByPlayer())
                .then(CommandManager.literal("whitelist")
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("username", StringArgumentType.string())
                                        .then(CommandManager.argument("ip", StringArgumentType.string())
                                                .executes(ctx -> {

                                                    String username = StringArgumentType.getString(ctx, "username");
                                                    String ip = StringArgumentType.getString(ctx, "ip");

                                                    if (!Utils.isValidIp(ip)) {
                                                        ctx.getSource()
                                                                .sendError(Text.literal("Invalid IP format"));
                                                        return 0;
                                                    }

                                                    try {
                                                        PlayerManager.whitelistPlayer(username, ip);
                                                        ctx.getSource().sendFeedback(
                                                                () -> Text
                                                                        .literal("New player whitelisted: " + username),
                                                                false);

                                                        return Command.SINGLE_SUCCESS;
                                                    } catch (PlayerManagerException e) {
                                                        ctx.getSource().sendError(Text.literal(e.getMessage()));
                                                        return 0;
                                                    }

                                                }))))
                        .then(CommandManager.literal("remove")
                                .then(CommandManager.argument("username", StringArgumentType.string())
                                        .executes(ctx -> {
                                            String username = StringArgumentType.getString(ctx, "username");
                                            try {
                                                PlayerManager.removePlayer(username);
                                                ctx.getSource().sendFeedback(() -> Text.literal("Player removed "),
                                                        false);

                                                return Command.SINGLE_SUCCESS;
                                            } catch (PlayerManagerException e) {
                                                ctx.getSource().sendError(Text.literal(e.getMessage()));
                                                return 0;
                                            }
                                        })))

                )
                .then(CommandManager.literal("ip")
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("username", StringArgumentType.string())
                                        .then(CommandManager.argument("ip", StringArgumentType.string())
                                                .executes(ctx -> {
                                                    String username = StringArgumentType.getString(ctx,
                                                            "username");
                                                    String ip = StringArgumentType.getString(ctx, "ip");

                                                    if (!Utils.isValidIp(ip)) {
                                                        ctx.getSource()
                                                                .sendError(Text.literal("Invalid IP format"));
                                                        return 0;
                                                    }

                                                    try {
                                                        PlayerManager.addPlayerIp(username, ip);

                                                        ctx.getSource().sendFeedback(
                                                                () -> Text.literal("New player IP added"),
                                                                false);

                                                        return Command.SINGLE_SUCCESS;
                                                    } catch (PlayerManagerException e) {
                                                        ctx.getSource()
                                                                .sendError(Text.literal(e.getMessage()));
                                                        return 0;
                                                    }
                                                }))))

                )
                .then(CommandManager.literal("reload")
                        .executes(ctx -> {
                            PlayerManager.reload();
                            ctx.getSource().sendFeedback(() -> Text.literal("Reloaded players"), false);
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(CommandManager.literal("count")
                        .executes(ctx -> {
                            String size = String.valueOf(PlayerManager.getPlayerCount());
                            ctx.getSource().sendFeedback(() -> Text.literal("Player count: " + size), false);
                            return Command.SINGLE_SUCCESS;
                        }))

        );
    }
}
