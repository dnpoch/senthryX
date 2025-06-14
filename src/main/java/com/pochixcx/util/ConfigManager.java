package com.pochixcx.util;

import static com.pochixcx.Senthryx.CONFIG;
import static com.pochixcx.Senthryx.DC_CONFIG;
import static com.pochixcx.Senthryx.LOGGER;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigManager {

    public static void init() throws IOException {
        if (!DC_CONFIG.exists()) {
            LOGGER.info("No config file found, creating one");
            try {
                create();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        LOGGER.info("Loading config file");
        try {
            load();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static void create() throws IOException {
        Config config = new Config();
        DC_CONFIG.getParentFile().mkdirs();
        DC_CONFIG.createNewFile();

        Writer writer = new FileWriter(DC_CONFIG, false);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(config, writer);

        writer.flush();
        writer.close();

    }

    private static void load() throws FileNotFoundException {
        FileReader reader = new FileReader(DC_CONFIG);

        CONFIG = new Gson().fromJson(reader, Config.class);
    }

}
