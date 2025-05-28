package com.pochixcx.util;

import java.util.ArrayList;

public class Config {
    public boolean activate = true;
    public boolean enable_discord = true;
    public String bot_token = ""; // supply your discord bot token here if enable_discord is true
    public String admin_channel_id = ""; // supply a discord channel ID for admin commands if enable_discord is true
    public boolean enable_public_logging = true;
    public String public_log_channel = ""; // supply a discord channel ID if enable_public_logging is true
    public ArrayList<String> admin_ids = new ArrayList<String>();
    public String presence = "Minecraft";
    public String kick_message = "You are not whitelisted";
    public String unknown_ip_message = "You are logging in from an unknown IP address, please verify your new IP address to the admins";

}
