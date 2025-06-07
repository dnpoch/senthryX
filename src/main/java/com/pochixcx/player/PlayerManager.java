package com.pochixcx.player;

import static com.pochixcx.Senthryx.CONFIG_PATH;
import static com.pochixcx.Senthryx.LOGGER;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlayerManager {
    private static HashMap<String, Player> whitelist = new HashMap<String, Player>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final File players = new File(CONFIG_PATH, "players.json");

    public static void load() {
        LOGGER.info("Loading players...");

        try {
            if (!players.exists()) {
                LOGGER.info("No player file found, creating one....");
                players.getParentFile().mkdirs();
                players.createNewFile();

                Writer writer = new FileWriter(players, false);
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
            FileReader reader = new FileReader(players);
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

        if (whitelist.containsKey(username)) {
            LOGGER.info("Player already exists: " + username);

            throw new PlayerManagerException("Player already exists: " + username);
        }

        try {

            Player player = new Player(username, ip);

            whitelist.put(username, player);
            Writer writer = new FileWriter(players, false);
            gson.toJson(whitelist, writer);
            writer.flush();
            writer.close();

            refresh();
        } catch (Exception e) {
            LOGGER.error("Error adding player: " + e.getMessage());
        }
    }

    public static void removePlayer(String username) throws PlayerManagerException {

        Player player = findPlayer(username);

        if (player == null)
            throw new PlayerManagerException("Player not found: " + username);

        try {
            whitelist.remove(username);
            Writer writer = new FileWriter(players, false);
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

        if (player == null)
            throw new PlayerManagerException("Player not found: " + username);

        ArrayList<String> ips = player.getIps();

        if (ips.contains(ip)) {
            LOGGER.info("IP already exists for player: " + username);
            throw new PlayerManagerException("IP already exists for player: " + username);
        }

        try {
            if (ips.size() > 3) {
                ips.remove(0);
            }
            ips.add(ip);
            player.setDateUpdated();
            Writer writer = new FileWriter(players, false);
            gson.toJson(whitelist, writer);
            writer.flush();
            writer.close();

            refresh();
        } catch (Exception e) {
            LOGGER.error("Error adding player ip: " + e.getMessage());
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

}
