package com.mycompany.airtunes;

import java.util.HashMap;

public class SearchController {

    public SearchController() { }

    // Searches for group based on search keyword and adds them to results array
    public boolean searchGroup(String search, HashMap<String, Group> groups) {
        boolean contains = false;
        for (String key : groups.keySet()) {
            if (key.contains(search)) {
                Group group = groups.get(key);

                //Add group to the table of results, if group is not private
                if (!group.isPrivate) {
                    SearchGroupActivity.groupNames.add(group.groupName);
                    SearchGroupActivity.queueAdapter.notifyDataSetChanged();
                    contains = true;
                }
            }
        }
        return contains;
    }

    // Searches for users based on search keyword and returns User object
    public User searchUser(String search, HashMap<String, User> users) {
        if (users.containsKey(search)) {
            User user = users.get(search);
            return user;
        } else {
            return null;
        }
    }
}
