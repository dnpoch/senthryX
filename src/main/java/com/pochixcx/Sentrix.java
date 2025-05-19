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
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pochixcx.discord.ModalListener;
import com.pochixcx.discord.SlashCommandListener;
import com.pochixcx.minecraft.ConsoleCommands;
import com.pochixcx.minecraft.LoginEventListener;
import com.pochixcx.player.PlayerManager;
import com.pochixcx.util.Config;
import com.pochixcx.util.ConfigManager;
import com.pochixcx.util.Utils;

public class Sentrix implements ModInitializer {
	public static final String MOD_ID = "sentrix-fabric";
	public static final File DC_CONFIG = new File(FabricLoader.getInstance().getConfigDir().toFile(),
			"sentrix-discord.json");
	public static Config CONFIG;
	public static TextChannel ADMIN_CHANNEL;
	public static ArrayList<TextChannel> BROADCAST_CHANNELS = new ArrayList<TextChannel>();
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
				LOGGER.info("-----------------------------------------");
				LOGGER.info("SentriX is disabled, please enable it in the config file");
				LOGGER.info("-----------------------------------------");
				return;
			}

			PlayerManager.load();

			jda = JDABuilder.createDefault(CONFIG.bot_token)
					.enableIntents(
							GatewayIntent.GUILD_MEMBERS,
							GatewayIntent.MESSAGE_CONTENT)
					.setActivity(Activity.playing(CONFIG.presence))
					.addEventListeners(new SlashCommandListener(), new ModalListener())
					.build();

			jda.awaitReady();

			Utils.updateDiscordCommands();;

			ADMIN_CHANNEL = jda.getTextChannelById(CONFIG.admin_channel_id);

			if (ADMIN_CHANNEL == null) {
				throw new NullPointerException("Invalid admin channel id: " + CONFIG.admin_channel_id);
			}

			CONFIG.logging_channels.forEach((channel_id) -> {
				TextChannel channel = jda.getTextChannelById(channel_id);
				if (channel == null) {
					throw new NullPointerException("Invalid broadcast channel id: " + channel_id);
				}
				BROADCAST_CHANNELS.add(channel);
			});

			CommandRegistrationCallback.EVENT
					.register((dispatcher, registryAccess, environment) -> ConsoleCommands.register(dispatcher));

			LoginEventListener.init();

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