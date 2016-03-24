package com.mycompany.airtunes;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ihugacownow on 3/24/16.
 */
public class FirebaseCalls {

    Firebase myFirebaseRef;
    Firebase userRef;
    Firebase userIDOne;
    Firebase roomRef;


    public FirebaseCalls() {
        myFirebaseRef = new Firebase("https://crackling-fire-3903.firebaseio.com/");
        userRef = myFirebaseRef.child("users");
        userIDOne = userRef.child("1");
        roomRef = myFirebaseRef.child("rooms");
    }


    public void test() {
        // Testing code
        User testUser = new User("Wai", "Wu", "ihugacownow", 1);

        System.out.println("Just before saving data");

    }


// Update Remote

    public void createRoom(Group group) {
        final Firebase newRoomRef = roomRef.push();
        newRoomRef.setValue(group, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Room could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("Room saved successfully with ID: " + newRoomRef.getKey());
                    group.fbID = newRoomRef;
                }
            }
        });

    }

    public void createUser(User user) {
        final Firebase newUserRef = userRef.push();
        newUserRef.setValue(user, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("User could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("User saved successfully  with ID: " + newUserRef.getKey());
                    user.id = newUserRef.getKey() + "";
                }
            }
        });
    }

    public static void createFavoriteList(ArrayList<String> songs) {

    }

    public void updateRoom(Group group) {
       //Firebase updateRoomRef = this.roomRef.child(group.fbID);
        Map<String, Object> groupInfo = new HashMap<String, Object>();
        groupInfo.put(group.fbID, group);
        this.roomRef.updateChildren(groupInfo);

    }

    public static void updateUser(User user) {

    }

    public static void updateSongList(ArrayList<String> songs) {

    }



}
