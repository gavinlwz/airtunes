package com.mycompany.airtunes;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Firebase class for the backend -- database
 * */
public class FirebaseCalls {
    Firebase myFirebaseRef;
    Firebase userRef;
    Firebase userIDOne;
    Firebase roomRef;
    HashMap<String, User> users;
    HashMap<String, Group> groups;
    User currentUser;
    Group testGroup;

    private static final FirebaseCalls dataHolder = new FirebaseCalls();
    public static FirebaseCalls getInstance() { return dataHolder; }

    private FirebaseCalls() {
        myFirebaseRef = new Firebase("https://crackling-fire-3903.firebaseio.com/");
        userRef = myFirebaseRef.child("users");
        userIDOne = userRef.child("1");
        roomRef = myFirebaseRef.child("rooms");

        users = new HashMap<>();
        groups = new HashMap<>();

        // Add event listeners
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                users = null;
                users = new HashMap<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    System.out.println(snapshot);
                    String username = snapshot.getKey();

                    try {
                        User user = snapshot.getValue(User.class);
                        System.out.println(user);
                        users.put(username, user);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("Adding user to users");

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        // Add event listeners
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("* Room information is changing");
                System.out.println(dataSnapshot.getValue());
                groups = null;
                groups = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //System.out.println(snapshot);
                    String groupName = snapshot.getKey();
                    try {
                        Group group = snapshot.getValue(Group.class);
                        groups.put(groupName, group);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }

    /*
    * ===================================
    * ===================================
    * INFORMATION ABOUT GROUP IN DATABASE
    * ===================================
    * ===================================
    * */
    /**
     * create a new group in database
     * @param group Group
     * */
    public void createRoom(Group group) {
        final Firebase newRoomRef = roomRef.child(group.getGroupName());

        // Create new room in firebase
        newRoomRef.setValue(group, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    //System.out.println("Room could not be saved. " + firebaseError.getMessage());
                } else {
                    //System.out.println("New room saved successfully w ID: " + newRoomRef.getKey());
                }
            }
        });

    }

    /**
     * Remove the room from firebase
     * @param group Group
     * */
    public void updateRoomAsRemoved(Group group) {
        Firebase updateRoomRef = this.roomRef.child(group.getGroupName());
        updateRoomRef = null;
    }

    /**
     * Update the members in the room
     * @param group Group
     * */
    public void updateRoomMembers(Group group) {
       Firebase updateRoomRef = this.roomRef.child(group.getGroupName());
        Map<String, Object> info = new HashMap<String, Object> ();
        info.put("memberNames", group.getMemberNames());
        info.put("owner", group.getOwner());
        updateRoomRef.updateChildren(info);
    }

    /**
     * Delete a group from database
     * @param groupName String
     * */
    public void removeRoom(String groupName) {
        final Firebase newRoomRef = roomRef.child(groupName);
        newRoomRef.removeValue();
        groups.remove(groupName);
        System.out.println("calling Removeroom in FB ");
    }

    /**
     * Update new owner of group
     * @param group Group
     * */
    public void updateDj(Group group) {
        Firebase updateRoomRef = this.roomRef.child(group.getGroupName());
        Map<String, Object> info = new HashMap<>();
        info.put("owner", group.getOwner());
        updateRoomRef.updateChildren(info);
    }

    public void updateRoomPg13(Group group) {
        Firebase updateRoomRef = this.roomRef.child(group.getGroupName()).child("pg13");
        updateRoomRef.setValue(group.isPG13);
    }

    /**
     * Update group's songs in playlist
     * @param group Group
     * */
    public void updateRoomSongs(Group group) {
        Firebase updateRoomRef = roomRef.child(group.getGroupName()).child("songs");
        updateRoomRef.setValue(group.getSongs());
    }

    /**
     * Change privacy of group
     * @param group User
     * */
    public void toggleGroupPrivacy(Group group) {
        Firebase updateRoomRef = roomRef.child(group.getGroupName()).child("isPrivate");
        updateRoomRef.setValue(group.getIsPrivate());
    }

    public void pullUserSongs(User user) {
        Firebase updateRef = userRef.child(user.getUsername()).child("favSongs");
        //updateRef.value
    }

    /*
    * ===================================
    * ===================================
    * INFORMATION ABOUT USERS IN DATABASE
    * ===================================
    * ===================================
    * */
    public void createUser(User user) {
        Firebase newUserRef = userRef.child(user.getUsername());
        newUserRef.setValue(user, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    //System.out.println("User could not be saved. " + firebaseError.getMessage());
                } else {
                    //System.out.println("User saved successfully  with ID: ");
                }
            }
        });
    }

    /**
     * update user's favorite songs in database
     * @param user User
     * */
    public void updateUserSongs(User user) {
        Firebase updateRef = userRef.child(user.getUsername()).child("favSongs");
        updateRef.setValue(user.getFavSongs());
        Map<String, Object> info = new HashMap<> ();
        info.put("favSongs", user.getFavSongs());
    }


    /**
     * change privacy of user
     * @param user User
     * */
    public void toggleUserPrivacy(User user) {
        Firebase updateRef = userRef.child(user.getUsername()).child("privacy");
        updateRef.setValue(user.privacy);
    }
}
