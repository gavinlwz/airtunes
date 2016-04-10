package com.mycompany.airtunes;

import java.util.HashMap;

public class SearchController {

//    HashMap<String, Group> groups;
//    HashMap<String, User> users;
    //FirebaseCalls fb;

    public SearchController() { }

    public boolean searchGroup(String search, HashMap<String, Group> groups) {
        boolean contains = false;
        for (String key : groups.keySet()) {
            if (key.contains(search)) {
                Group group = groups.get(key);
                //add to the table View
                if (!group.isPrivate) {
                    SearchGroupActivity.groupNames.add(group.groupName);
                    SearchGroupActivity.queueAdapter.notifyDataSetChanged();
                    contains = true;
                }
            }
        }
        return contains;
    }

//    public Group searchGroup(String search, HashMap<String, Group> groups) {
//        for (String key : groups.keySet()) {
//            if (key.equals(search)) {
//                return groups.get(key);
//            }
//        }
//
//        return null;
//    }

    public User searchUser(String search, HashMap<String, User> users) {
        if (users.containsKey(search)) {
            User user = users.get(search);
            return user;
        } else {
            return null;
        }
    }
}
