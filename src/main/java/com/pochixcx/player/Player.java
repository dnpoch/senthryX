package com.pochixcx.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Player {
    private String username;
    private ArrayList<String> linked_ips;
    private Date lastLogin;
    private Date dateCreated;
    private Date dateUpdated;

    public Player(String username, String ip) {
        this.username = username;
        this.linked_ips = new ArrayList<String>(List.of(ip));
        this.lastLogin = new Date();
        this.dateCreated = new Date();
        this.dateUpdated = new Date();
    }

    public String getUsername() {
        return this.username;
    }

    public ArrayList<String> getIps() {
        return this.linked_ips;
    }

    public Date getLastLogin() {
        return this.lastLogin;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public Date getDateUpdated() {
        return this.dateUpdated;
    }

    public void setLastLogin(String username) {
        this.lastLogin = new Date();
    }

    public void setDateUpdated() {
        this.dateUpdated = new Date();
    }

}
