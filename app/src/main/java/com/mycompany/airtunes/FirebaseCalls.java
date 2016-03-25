package com.mycompany.airtunes;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.wrapper.spotify.models.Track;

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

        // Members

        User testUser = new User("Wai", "Wu", "ihugacownow");
        this.createUser(testUser);

        User one = new User("Wai", "Wu", "Wai 2!");
        this.createUser(one);

        User two = new User("Wai", "Wu", "Another Wai!");
        this.createUser(two);

        //one.addSongs("Hello Dohee Song");
        //one.addSongs(new Track());
        one.addSongs(new Song("lajksdhakjshd", "Hello Dohee Song", "dohee", null));
        this.updateUserSongs(one);

        // Rooms
        Group newRoom = new Group("groupName1", "groupOwner1");
        this.createRoom(newRoom);


        newRoom.addSong("song 1");
        this.updateRoomSongs(newRoom);

        newRoom.addSong("song 2");
        this.updateRoomSongs(newRoom);

        newRoom.removeSong("song 1");
        this.updateRoomSongs(newRoom);

        newRoom.addMember("Another Wai!");
        this.updateRoomMembers(newRoom);

        newRoom.addMember("Wai 2!");
        this.updateRoomMembers(newRoom);

        newRoom.removeMember("Another Wai!");
        this.updateRoomMembers(newRoom);
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
                    System.out.println("New room saved successfully with ID: " + newRoomRef.getKey());
                }
            }
        });

    }

    public void updateRoomMembers(Group group) {
       Firebase updateRoomRef = this.roomRef.child(group.getGroupName());
        Map<String, Object> info = new HashMap<String, Object> ();
        info.put("memberNames", group.getMemberNames());
        updateRoomRef.updateChildren(info);
    }

    public void updateRoomSongs(Group group) {
        Firebase updateRoomRef = roomRef.child(group.getGroupName()).child("songs");
        updateRoomRef.setValue(group.getSongs());

//        Map<String, Object> songInfo = new HashMap<String, Object> ();
//        songInfo.put("songs", group.getSongs());
//        updateRoomRef.updateChildren(songInfo);
    }

// Users

    public void createUser(User user) {
        Firebase newUserRef = userRef.child(user.getUsername());
        newUserRef.setValue(user, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    System.out.println("User could not be saved. " + firebaseError.getMessage());
                } else {
                    System.out.println("User saved successfully  with ID: ");
                }
            }
        });
    }

    public void updateUserSongs(User user) {
        Firebase updateRef = this.userRef.child(user.getUsername());
        Map<String, Object> info = new HashMap<String, Object> ();
        info.put("favSongs", user.getSongs());
        updateRef.updateChildren(info);
    }




    // Favorite lists

    public void createFavoriteList(ArrayList<String> songs) {

    }
    public void updateSongList(ArrayList<String> songs) {

    }



}
