package com.pochixcx.util;

import java.util.ArrayList;

public class Config {
    public boolean activate = true;
    public String bot_token = "";
    public String admin_channel_id = "";
    public ArrayList<String> logging_channels = new ArrayList<String>();
    public ArrayList<String> admin_ids = new ArrayList<String>();
    public String presence = "Minecraft";
    public String kick_message = "You are not allowed in this server";
    public String unknown_ip_message = "You are logging in from an unknown IP address, please verify your new IP address via discord";

}
