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


   // String identity;
    String name;

    private int id;
    private boolean privacy;

    private String password;
    private String username;
    ArrayList<Song> favSongs;


//    public User(String firstName, String lastName, String username) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.username = username;
//        this.favSongs = new ArrayList<>();
//    }

    public User(String firstName, String lastName, String username, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.favSongs = new ArrayList<>();
        this.id = id;
        this.privacy = false;
    }

    public User(String name, String username) {
        this.name = name;
        this.username = username;
        this.favSongs = new ArrayList<>();
        //this.identity = id;
    }



    public User() {};



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() { return name; }

    public int getId() {
        return id;
    }

    public boolean getPrivacy() {
        return privacy;
    }

    public void togglePrivacy() {
        privacy = !privacy;
    }


    public String getUsername() { return username; }


    public String getPassword() { return password; }


    public void addSongs(Song song) {
        favSongs.add(song);
    }

    public void removeSongs(Song song) {
        favSongs.remove(song);
    }

    public ArrayList<Song> getSongs() { return favSongs; }

    public ArrayList<Song> getFavSongs() {
        if (this.favSongs == null) {
            favSongs = new ArrayList<Song>();
        }
        return this.favSongs;
    }

    public void setFavSongs(ArrayList<Song> favSongs) {
        this.favSongs = favSongs;
    }

}
