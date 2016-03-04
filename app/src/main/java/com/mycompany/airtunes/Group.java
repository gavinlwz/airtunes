package com.mycompany.airtunes;

/**
 * Created by ihugacownow on 3/4/16.
 */

import java.util.List;
import java.util.HashSet;
import java.util.Deque;
import java.util.Set;
import java.util.ArrayList;
import java.io.Serializable;



public class Group implements Serializable {
    List<String> memberNames;
    String groupName;
    Set<String> songNames;
    String owner;

    // TODO: Akash Change song to actual Song objects
    // TODO: Arvind Pass the user object from login to constructor
    public Group(String groupName, String owner) {
        this.groupName = groupName;
        this.memberNames = new ArrayList<String>();
        this.songNames = new HashSet<String>();
        this.owner = owner;
    }

    public void addMember(String name) {
        this.memberNames.add(name);
    }

    public void addSong(String song) {
        this.songNames.add(song);
    }





}
