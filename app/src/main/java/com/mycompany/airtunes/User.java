package com.mycompany.airtunes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User class that represents user object with unique ID
 * */
@JsonIgnoreProperties(ignoreUnknown=true)
public class User implements Serializable{
    private String firstName;
    private String lastName;
    String name;
    String id;
    boolean privacy;
    private String username;
    List<Song> favSongs;

    public User() {}

    /**
     * constructor setting initial values of the user
     * @param firstName String
     * @param lastName String
     * @param username String
     * @param id String
     * */
    public User(String firstName, String lastName, String username, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.privacy = false;
        this.favSongs = new ArrayList<Song>();
    }

    /**
     * constructor setting only name and username
     * @param name String
     * @param username String
     * */
    public User(String name, String username) {
        this.name = name;
        this.username = username;
        this.favSongs = new ArrayList<>();
        this.firstName = "first";
        this.lastName = "last";
    }

    /**
     * change privacy of user
     * */
    public void togglePrivacy() { privacy = !privacy; }

    /**
     * add new song to user's favorites list
     * @param song Song
     * */
    public void addSongs(Song song) {
        if (this.favSongs == null) {
            favSongs = new ArrayList<>();
        }
        favSongs.add(song);
    }

    /**
     * remove song from favorites list
     * @param song Song
     * */
    public void removeSongs(Song song) { favSongs.remove(song); }


    /**
     * @param favSongs set of favorite songs
     * */
    public void setFavSongs(ArrayList<Song> favSongs) {
        if (this.favSongs == null) {
            favSongs = new ArrayList<>();
        }
        this.favSongs = favSongs;
    }

    /*
    * ==========================================
    * ==========================================
    * GETTER AND SETTER FUNCTIONS FOR THIS CLASS
    * ==========================================
    * ==========================================
    * */

    public String getName() { return name; }

    public boolean getPrivacy() { return privacy; }

    public String getUsername() { return username; }

    public List<Song> getFavSongs() {
        return this.favSongs;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }



}
