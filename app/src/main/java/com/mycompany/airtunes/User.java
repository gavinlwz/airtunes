package com.mycompany.airtunes;

import java.util.ArrayList;

/**
 * Created by arvindraju on 3/4/16.
 */
public class User {
    private String firstName;
    private String lastName;
    int id;
    private String username;
    private String password;
    private ArrayList<String> favSongs;


    public User(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.favSongs = new ArrayList<>();
    }

    public User(String firstName, String lastName, String username, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.favSongs = new ArrayList<>();
        this.id = id;
    }



//    public User() {};



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void addSongs(String song) {
        favSongs.add(song);
    }

    public void removeSongs(String song) {
        favSongs.remove(song);
    }

    public ArrayList<String> getSongs() { return favSongs; }


}
