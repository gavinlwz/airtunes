package com.mycompany.airtunes;

/**
 * Created by ihugacownow on 3/4/16.
 */

import com.wrapper.spotify.models.Track;

import java.util.List;
import java.util.HashSet;
import java.util.Deque;
import java.util.Set;
import java.util.ArrayList;
import java.io.Serializable;



public class Group implements Serializable {
     List<String> memberUsernames;
     String groupName;
     List<String> songNames;
    List<Song> songs;
     String owner;
     String fbID;

    // TODO: Akash Change song to actual Song objects
    // TODO: Arvind Pass the user object from login to constructor
    public Group() {};
    public Group(String groupName, String owner) {
        this.groupName = groupName;

        this.memberUsernames = new ArrayList<String>();
        this.songNames = new ArrayList<String>();
        this.songs = new ArrayList<Song>();

        this.owner = owner;
    }

    public void addMember(String name) {
        this.memberUsernames.add(name);
    }

    public void removeMember(String name) { this.memberUsernames.remove(name); }

    public void addSong(String song) {
        this.songNames.add(song);
    }
    public void addSong(Song song) {
        this.songs.add(song);
        addSong(song.getName());
    }

    public void removeSong(String song) {this.songNames.remove(song); }
    public void removeSong(Song song) {
        this.songs.remove(song);
        removeSong(song.getName());
    }

    public List<Song> getSongs() {
        return songs;
    }

    public List<String> getMemberNames() {
        return memberUsernames;
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






}
