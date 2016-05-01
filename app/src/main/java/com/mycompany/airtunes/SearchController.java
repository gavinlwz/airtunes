package com.mycompany.airtunes;

import java.util.HashMap;

/**
 * SearchController class for search function (primarily groups and users)
 * */
public class SearchController {
    /**
     * Searches for group based on search keyword and adds them to results array
     * @param search String
     * @param groups HashMap<String, Group></String,>
     * */
    public boolean searchGroup(String search, HashMap<String, Group> groups) {
        boolean contains = false;
        for (String key : groups.keySet()) {
            if (key.contains(search)) {
                Group group = groups.get(key);

                // Add group to the table of results, if group is not private
                if (!group.isPrivate) {
                    SearchGroupActivity.groupNames.add(group.groupName);
                    SearchGroupActivity.queueAdapter.notifyDataSetChanged();
                    contains = true;
                }
            }
        }
        return contains;
    }

    /**
     * Searches for users based on search keyword and returns User object
     * @param search String
     * @param users HashMap<String, User></String,>
     * */
    public boolean searchUser(String search, HashMap<String, User> users) {
        boolean contains = false;
        for (String key : users.keySet()) {
            if (key.contains(search)) {
                User user = users.get(key);
                SearchUserActivity.userNames.add(user.getUsername());
                SearchUserActivity.queueAdapter.notifyDataSetChanged();
                contains = true;

            }
        }
        return contains;
    }
}
