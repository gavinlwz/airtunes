package com.mycompany.airtunes;

/**
 * Created by ihugacownow on 3/3/16.
 *
 */

import java.util.List;
import java.util.HashSet;
import java.util.Deque;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class SearchController {

    HashMap<String, Group> groups;

    public SearchController() {
        groups = new HashMap<String, Group>();

    }

    public void addGroup(String name, String owner) {
        Group group = new Group(name, owner);
        groups.put(name, group);
        System.out.println("Added group with name: " + name);
        //TODO: Update server
    }

    public Group searchGroup(String search) {
        if (groups.containsKey(search)) {
            Group group = groups.get(search);
            return group;
        } else {
            return null;
        }

    }





}
