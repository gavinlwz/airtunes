package com.mycompany.airtunes;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

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

    private static final FirebaseCalls dataHolder = new FirebaseCalls();
    public static FirebaseCalls getInstance() { return dataHolder; }

    private FirebaseCalls() {
        myFirebaseRef = new Firebase("https://crackling-fire-3903.firebaseio.com/");
        userRef = myFirebaseRef.child("users");
        userIDOne = userRef.child("1");
        roomRef = myFirebaseRef.child("rooms");


        // Add event listeners
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Data in firebase has changed, updated data is");
                System.out.println(dataSnapshot.getValue());
                Object updatedObj = dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


    }

    public void test() {
        // Testing code
        User testUser = new User("Wai", "Wu", "ihugacownow", 1);
        System.out.println("Just before saving data");

    }


// Update Remote

    public void createRoom(Group group) {
        // TODO: Check if there is existing room name
        final Firebase newRoomRef = roomRef.child(group.getGroupName());
        newRoomRef.setValue(group, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("Room could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("Room saved successfully with ID: " + newRoomRef.getKey());
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
                }
            }
        });
    }

    public static void createFavoriteList(ArrayList<String> songs) {

    }

    public void updateRoom(Group group) {
       Firebase updateRoomRef = this.roomRef.child(group.getGroupName());
        updateRoomRef.setValue(group);
//        Map<String, Object> groupInfo = new HashMap<String, Object>();
//
//        groupInfo.put(group.fbID, group);
//        this.roomRef.updateChildren(groupInfo);

    }

    public static void updateUser(User user) {

    }

    public static void updateSongList(ArrayList<String> songs) {

    }



}
