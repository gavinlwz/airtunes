package com.mycompany.airtunes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wrapper.spotify.models.Track;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by arvindraju on 3/4/16.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class User {
    private String firstName;
    private String lastName;
    int id;
    private String username;
    private ArrayList<Song> favSongs;


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



    public User() {};



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

    public void addSongs(Song song) {
        favSongs.add(song);
    }

    public void removeSongs(Song song) {
        favSongs.remove(song);
    }

    public ArrayList<Song> getSongs() { return favSongs; }


}
