package com.mycompany.airtunes;

/**
 * Created by ihugacownow on 3/4/16.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wrapper.spotify.models.Track;

import java.util.List;
import java.util.HashSet;
import java.util.Deque;
import java.util.Set;
import java.util.ArrayList;
import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown=true)
public class Group implements Serializable {
     List<String> memberNames;
     String groupName;
     List<String> songNames;
    List<Song> songs;
     String owner;
    boolean isPrivate = false;



    // TODO: Akash Change song to actual Song objects
    // TODO: Arvind Pass the user object from login to constructor
    public Group() {
        songNames = new ArrayList<String>();
    };

    public Group(String groupName, String owner, boolean isPrivate) {
        this.groupName = groupName;

        this.memberNames = new ArrayList<String>();
        this.songNames = new ArrayList<String>();
        this.songs = new ArrayList<Song>();

        this.owner = owner;
        this.isPrivate = isPrivate;
    }

    public void changeToPrivate() {
        this.isPrivate = true;
    }

    public void addMember(String name) {
        this.memberNames.add(name);
    }

    public void removeMember(String name) {
        this.memberNames.remove(name);

    }

    public void addSong(String song) {
        this.songNames.add(song);
    }
    public void addSong(Song song) {
        if (this.songs == null) {
            this.songs = new ArrayList<Song>();
        }
        this.songs.add(song);
        this.songNames.add(song.getName());
    }

    public void removeSong(Song song) {
        System.out.println("Removing song from model: " + song);
        this.songs.remove(song);
        this.songNames.remove(song.getName());

    }

    public List<Song> getSongs() {
        if (songs == null) {
            this.songs = new ArrayList<Song>();
        }
        return songs;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }

    public List<String> getSongNames() {
        return songNames;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getOwner() {
        return owner;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean p) {
        isPrivate = p;
    }

    public void changeDj(String newDj) {
        this.owner = newDj;
    }










}
