package com.pochixcx.player;

import static com.pochixcx.Sentrix.LOGGER;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

public class PlayerManager {
    private static HashMap<String, Player> whitelist = new HashMap<String, Player>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File config = FabricLoader.getInstance().getConfigDir().resolve("sentrix-players.json")
            .toFile();

    public static void load() {
        LOGGER.info("Loading players...");

        try {
            if (!config.exists()) {
                LOGGER.info("No player file found, creating one....");
                config.getParentFile().mkdirs();
                config.createNewFile();

                Writer writer = new FileWriter(config, false);
                gson.toJson(whitelist, writer);
                writer.flush();
                writer.close();

                refresh();
            }

            refresh();
        } catch (Exception e) {
            LOGGER.error("Error loading players: " + e.getMessage());
        }

    }

    private static void refresh() {
        try {
            FileReader reader = new FileReader(config);
            Type type = new TypeToken<HashMap<String, Player>>() {
            }.getType();
            HashMap<String, Player> map = gson.fromJson(reader, type);

            whitelist = map;
            LOGGER.info("Players loaded: " + whitelist.size());
        } catch (Exception e) {
            LOGGER.error("Error refreshing players: " + e.getMessage());
        }
    }

    public static Player findPlayer(String usernname) {
        Player player = whitelist.get(usernname);

        if (player == null) {
            LOGGER.info("Player not found: " + usernname);
            return null;
        }

        return player;
    }

    public static void whitelistPlayer(String username, String ip) throws PlayerManagerException {
        try {

            if (whitelist.containsKey(username)) {
                LOGGER.info("Player already exists: " + username);

                throw new PlayerManagerException("Player already exists" + username);
            }

            Player player = new Player(username, ip);

            whitelist.put(username, player);
            Writer writer = new FileWriter(config, false);
            gson.toJson(whitelist, writer);
            writer.flush();
            writer.close();

            refresh();
        } catch (Exception e) {
            LOGGER.error("Error adding player: " + e.getMessage());
        }
    }

    public static void removePlayer(String username) throws PlayerManagerException {

        try {
            Player player = findPlayer(username);

            if (player == null)
                throw new PlayerManagerException("Player not found" + username);

            whitelist.remove(username);
            Writer writer = new FileWriter(config, false);
            gson.toJson(whitelist, writer);
            writer.flush();
            writer.close();

            refresh();
        } catch (Exception e) {
            LOGGER.error("Error removing player: " + e.getMessage());
        }
    }

    public static void addPlayerIp(String username, String ip) throws PlayerManagerException {
        Player player = findPlayer(username);

        try {
            if (player == null)
                throw new PlayerManagerException("Player not found" + username);

            if (player.getIps().contains(ip)) {
                LOGGER.info("IP already exists for player: " + username);
                throw new PlayerManagerException("IP already exists for player: " + username);
            }

            player.getIps().add(ip);
            player.setDateUpdated();
            Writer writer = new FileWriter(config, false);
            gson.toJson(whitelist, writer);
            writer.flush();
            writer.close();

            refresh();
        } catch (Exception e) {
            LOGGER.error("Error adding player ip: " + e.getMessage());
        }
    }

    public static void removePlayerIp(String username, String ip) throws PlayerManagerException {

        Player player = findPlayer(username);

        try {
            if (player == null)
                throw new PlayerManagerException("Player not found" + username);

            if (player.getIps().contains(ip)) {
                player.getIps().remove(ip);
                player.setDateUpdated();
                Writer writer = new FileWriter(config, false);
                gson.toJson(whitelist, writer);
                writer.flush();
                writer.close();

                refresh();
            }

            throw new PlayerManagerException("IP does not exist");

        } catch (Exception e) {
            LOGGER.error("Error removing player ip: " + e.getMessage());
        }
    }

    public static int getPlayerCount() {
        return whitelist.size();
    }

    public static void reload() {
        try {
            refresh();
        } catch (Exception e) {
            LOGGER.error("Error reloading players: " + e.getMessage());
        }
    }

    public static boolean isPlayerAllowed(String username, String ip) {
        Player player = findPlayer(username);

        if (player != null) {
            if (!player.getIps().contains(ip) || player.getIps().isEmpty()) {
                LOGGER.info("Player not allowed: " + username + " IP: " + ip);
                return false;
            }

            return true;
        }
        return false;
    }

}
