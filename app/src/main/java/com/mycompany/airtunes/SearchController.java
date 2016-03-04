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

    public Group searchGroup(String search) {
        if (groups.containsKey(search)) {
            Group group = groups.get(search);
            return group;
        } else {
            return null;
        }

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
