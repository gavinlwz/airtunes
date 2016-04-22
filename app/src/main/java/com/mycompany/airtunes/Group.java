package com.mycompany.airtunes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Group class that represents a group including its properties (group name, its members, privacy,
 * owner, etc)
 * */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Group implements Serializable {

    boolean isPG13;
    String owner;
    String groupName;
    ArrayList<String> memberNames; //members of group
    ArrayList<String> songNames; //songs in the group playlist
    ArrayList<Song> songs; //songs as Song objects
    boolean isPrivate = false; //group initially set to private


    // TODO: Akash Change song to actual Song objects
    // TODO: Arvind Pass the user object from login to constructor
    /**
     * ????????
     * */
    public Group() {
    }

    /**
     * constructor setting instance variables to initial values
     * @param groupName String name of group
     * @param owner String name of owner of group (initial creator)
     * @param isPrivate boolean visibility of group
     * */
    public Group(String groupName, String owner, boolean isPrivate) {
        this.owner = owner;
        this.groupName = groupName;
        this.isPG13 = true;
        this.memberNames = new ArrayList<String>();
//        this.memberNames.add(owner);
////        //TODO: remove
////        this.memberNames.add("tahmid");
        this.songNames = new ArrayList<String>();
        this.songs = new ArrayList<Song>();
        this.isPrivate = isPrivate;
    }


    public boolean getPG13() {
        return isPG13;
    }

    public void setPG13(boolean kids) {
        isPG13 = kids;
    }


    /**
     * adds new member to group
     * @param name String
     * */

    public void addMember(String name) {
        if (!memberNames.contains(name)) {
            this.memberNames.add(name);
        }
    }

    /**
     * removes member from group
     * @param name String
     * */
    public void removeMember(String name) {
        this.memberNames.remove(name);
    }

    /**
     * adds new song to playlist at certain index in queue
     * @param song Song
     * @param index int
     * */
    public void addSong(Song song, int index) {
        if (this.songs == null) {
            this.songs = new ArrayList<>();
        }
        if (this.songNames == null) {
            this.songNames = new ArrayList<>();
        }
        this.songs.add(index, song);
        this.songNames.add(song.getName());
    }

    /**
     * adds new song list
     * @param song String
     * */
    public void addSong(String song) {
        this.songNames.add(song);
    }

    /**
     * adds new song to list
     * @param song Song
     * */
    public void addSong(Song song) {
        if (this.songs == null) {
            this.songs = new ArrayList<>();
        }
        this.songs.add(song);
        if (this.songNames == null) this.songNames = new ArrayList<String>();
        this.songNames.add(song.getName());
    }

    /**
     * removes song from list
     * @param song Song
     * */
    public void removeSong(Song song) {
        if (this.songs == null) this.songs = new ArrayList<Song>();
        if (this.songNames == null) this.songNames = new ArrayList<String>();
        if (songs.size() > 0 && songNames.size() > 0) {
            System.out.println("Removing song from model: " + song);
            this.songs.remove(song);
            this.songNames.remove(song.getName());
        }

    }

    /**
     * @return list of songs
     * */
    public List<Song> getSongs() {
        if (songs == null) {
            this.songs = new ArrayList<Song>();
        }
        return songs;
    }

    /*
    * ==========================================
    * ==========================================
    * GETTER AND SETTER FUNCTIONS FOR THIS CLASS
    * ==========================================
    * ==========================================
    * */
    public String getOwner() {
        return owner;
    }

    public void changeDj(String newDj) {
        this.owner = newDj;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<String> getMemberNames() {
        if (memberNames == null) {
            memberNames = new ArrayList<>();
        }
        return memberNames;
    }

    public List<String> getSongNames() {
        if (this.songNames == null) {
            this.songNames = new ArrayList<String>();
        }

        return songNames;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean p) {
        isPrivate = p;
    }

    public void changeToPrivate() {
        this.isPrivate = true;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMemberNames(ArrayList<String> memberNames) {
        this.memberNames = memberNames;
    }

    public void setSongNames(ArrayList<String> songNames) {
        this.songNames = songNames;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }




}
