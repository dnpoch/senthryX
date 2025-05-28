package com.pochixcx;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pochixcx.discord.SlashCommandListener;
// import com.pochixcx.minecraft.LoginEventListener;
import com.pochixcx.minecraft.commands.ConsoleCommands;
import com.pochixcx.minecraft.listener.LoginEventHandler;
import com.pochixcx.player.PlayerManager;
import com.pochixcx.util.Config;
import com.pochixcx.util.ConfigManager;
import com.pochixcx.util.Utils;

public class Sentrix implements ModInitializer {
	public static final String MOD_ID = "sentrix";
	public static final File CONFIG_PATH = new File(FabricLoader.getInstance().getConfigDir().toFile(), MOD_ID);
	public static final File DC_CONFIG = new File(CONFIG_PATH, "config.json");
	public static Config CONFIG;
	public static TextChannel ADMIN_CHANNEL;
	public static TextChannel LOG_CHANNEL;
	public static JDA jda;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("-----------------------------------------");
		LOGGER.info("SentriX-fabric");
		LOGGER.info("By pochixcx");
		LOGGER.info("-----------------------------------------");

		try {
			ConfigManager.init();

			if (!CONFIG.activate) {
				LOGGER.info("SentriX is disabled, please enable it in the config file");
				return;
			}

			PlayerManager.load();

			if (CONFIG.enable_discord) {
				jda = JDABuilder.createDefault(CONFIG.bot_token)
						.enableIntents(
								GatewayIntent.GUILD_MEMBERS,
								GatewayIntent.MESSAGE_CONTENT)
						.setActivity(Activity.playing(CONFIG.presence))
						.addEventListeners(new SlashCommandListener())
						.build();

				jda.awaitReady();

				Utils.updateDiscordCommands();

				ADMIN_CHANNEL = jda.getTextChannelById(CONFIG.admin_channel_id);

				if (ADMIN_CHANNEL == null) {
					throw new NullPointerException("Invalid admin channel id: " + CONFIG.admin_channel_id);
				}

				LOG_CHANNEL = jda.getTextChannelById(CONFIG.public_log_channel);
				if (CONFIG.enable_public_logging && LOG_CHANNEL == null) {
					throw new NullPointerException("Invalid public log channel id: " + CONFIG.public_log_channel);
				}

				// CONFIG.logging_channels.forEach((channel_id) -> {
				// 	TextChannel channel = jda.getTextChannelById(channel_id);
				// 	if (channel == null) {
				// 		throw new NullPointerException("Invalid broadcast channel id: " + channel_id);
				// 	}
				// 	BROADCAST_CHANNELS.add(channel);
				// });

			}

			CommandRegistrationCallback.EVENT
					.register((dispatcher, registryAccess, environment) -> ConsoleCommands.register(dispatcher));

			LoginEventHandler.register();

			// LoginEventListener.init();

			// Gracefule discord bot shutdown on server stop
			ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
				if (jda != null) {
					LOGGER.info("Shutting down SentriX discord bot...");
					shutdown();
				}

			});

		} catch (Exception e) {
			e.getStackTrace();
			LOGGER.error(e.getMessage());
			System.exit(1);
		}

		LOGGER.info("-----------------------------------------");
		LOGGER.info("SentriX-fabric");
		LOGGER.info("Loaded");
		LOGGER.info("-----------------------------------------");

	}

	private static void shutdown() {
		try {
			jda.shutdown();
			if (!jda.awaitShutdown(Duration.ofSeconds(10))) {

				jda.shutdownNow();

			}

		} catch (Exception e) {
			LOGGER.error(e.getStackTrace().toString());
		}
	}

}