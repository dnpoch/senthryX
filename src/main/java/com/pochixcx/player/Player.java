package com.pochixcx.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
public class Player {
    private final String username;
    private ArrayList<String> linked_ips;
    private final Date dateCreated;
    private Date dateUpdated;

    public Player(String username, String ip) {
        this.username = username;
        this.linked_ips = new ArrayList<String>(List.of(ip));
        this.dateCreated = new Date();
        this.dateUpdated = new Date();
    }

    public ArrayList<String> getIps() {
        return this.linked_ips;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public Date getDateUpdated() {
        return this.dateUpdated;
    }

    public void setDateUpdated() {
        this.dateUpdated = new Date();
    }

}
