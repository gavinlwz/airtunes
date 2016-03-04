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
    HashMap<String, User> users;

    public SearchController() {
        groups = new HashMap<String, Group>();

        //TODO: remove hard coded stuff
        Group groupA = new Group("a", "Akash");
        groupA.addSong("jesus take the wheel");
        groupA.addSong("take me to church");
        groupA.addSong("hymn for the weekend");

        groups.put("a", groupA);

        Group groupB = new Group("aa", "Wai");
        Group groupC = new Group("aaa", "Jessica");
        Group groupD = new Group("aaaa", "Arvind");

        groups.put("aa", groupB);
        groups.put("aaa", groupC);
        groups.put("aaaa", groupD);



    }

    public void populateUsers() {
        User user1 = new User("Arvind", "Raju", "rajua", 1);
        users.put("Arvind", user1);

        User user2 = new User("Wai", "Wu", "ihugacownow", 2);
        users.put("Wai", user2);

        User user3 = new User("Akash", "Subramanian", "asub", 3);
        users.put("Akash", user3);

        User user4 = new User("Jessica", "Kim", "jkim", 4);
        users.put("Jessica", user4);
    }

    public void addGroup(String name, String owner) {
        Group group = new Group(name, owner);
        groups.put(name, group);
        System.out.println("Added group with name: " + name);
        //TODO: Update server
    }

    public boolean searchGroup(String search) {
        boolean contains = false;
        for (String key : groups.keySet()) {
            if (key.contains(search)) {
                Group group = groups.get(key);
                // Add to the table View
                SearchGroupActivity.queueSongs.add(group.groupName);
                SearchGroupActivity.queueAdapter.notifyDataSetChanged();
                contains = true;
            }
        }
        return contains;
    }

    public User searchUser(String search) {
        if (users.containsKey(search)) {
            User user = users.get(search);
            return user;
        } else {
            return null;
        }

    }





}
