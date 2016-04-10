package com.mycompany.airtunes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * User class that represents user object with unique ID
 * */
@JsonIgnoreProperties(ignoreUnknown=true)
public class User {
    private String firstName;
    private String lastName;
    String name;
    private String id;
    private boolean privacy;
    private String password;
    private String username;
    ArrayList<Song> favSongs;
    private Group currentRoom;

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
        this.favSongs = new ArrayList<>();
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
     * @return list of user's favorite songs
     * */
    public ArrayList<Song> getFavSongs() {
        if (this.favSongs == null) {
            favSongs = new ArrayList<>();
        }
        return this.favSongs;
    }

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
    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public Group getCurrentRoom() { return this.currentRoom; }

    public String getName() { return name; }

    public String getId() { return id; }

    public boolean getPrivacy() { return privacy; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public ArrayList<Song> getSongs() { return favSongs; }
}
