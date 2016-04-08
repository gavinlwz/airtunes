package com.mycompany.airtunes;

import com.firebase.client.ChildEventListener;
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
                System.out.println("Data in firebase has changed, updated data is");
                System.out.println(dataSnapshot.getValue());

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    System.out.println(snapshot);
                    String username = snapshot.getKey();
                    User user = snapshot.getValue(User.class);
                    System.out.println(user.getName());
                    System.out.println(user.favSongs);
                    System.out.println(user.getId());

                    users.put(username, user);
                    System.out.println("Adding user to users");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        // Add event listeners
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Data in firebase has changed, updated data is");
                System.out.println(dataSnapshot.getValue());

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println(snapshot);
                    String groupName = snapshot.getKey();

                    try {
                        Group group = snapshot.getValue(Group.class);
                        System.out.println(group);
                        System.out.println("HURRAY GROUP ADDED");
                        groups.put(groupName, group);
                    } catch(Exception e) {
                        e.printStackTrace();

                    }
                    System.out.println("Adding group to gruops: " + groupName);
                }


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });






//        // Add event listener for rooms
//        // So when room is added
//        roomRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });






    }


//    public void test() {
//        // Testing code
//
//        // Members
//
//        User testUser = new User("Wai", "Wu", "ihugacownow");
//        this.createUser(testUser);
//
//        User one = new User("Wai", "Wu", "Wai 2!");
//        this.createUser(one);
//
//        User two = new User("Wai", "Wu", "Another Wai!");
//        this.createUser(two);
//
//
//        //one.addSongs(new Track());
//        one.addSongs(new Song("lajksdhakjshd", "child
//
// ", "dohee", null));
//        this.updateUserSongs(one);
//
//        // Rooms
//        Group newRoom = new Group("groupName1", "groupOwner1");
//        this.createRoom(newRoom);
//
//
//        newRoom.addSong("song 1");
//        this.updateRoomSongs(newRoom);
//
//        newRoom.addSong("song 2");
//        this.updateRoomSongs(newRoom);
//
//        newRoom.removeSong("song 1");
//        this.updateRoomSongs(newRoom);
//
//        newRoom.addMember("Another Wai!");
//        this.adbers(newRoom);
//
//        newRoom.addMember("Wai 2!");
//        this.updateRoomMembers(newRoom);
//
//        newRoom.removeMember("Another Wai!");
//        this.updateRoomMembers(newRoom);
//
//        System.out.println("dfasfsaad sfds fasdf dsfas user size is: " + users.size());
//    }

//    public void test() {
//        // Testing code
//
//        // Members
//
//
//        User one = new User("Wai", "Wu");
//        this.createUser(one);
//
//
//        //one.addSongs(new Track());
//        one.addSongs(new Song("lajksdhakjshd", "Hello Dohee Song", "dohee", null));
//        this.updateUserSongs(one);
//
//        // Rooms
//        Group newRoom = new Group("groupName1", "groupOwner1", true);
//        this.createRoom(newRoom);
//
//
//        newRoom.addSong("song 1");
//        this.updateRoomSongs(newRoom);
//
//        newRoom.addSong("song 2");
//        this.updateRoomSongs(newRoom);
//
//        newRoom.removeSong("song 1");
//        this.updateRoomSongs(newRoom);
//
//        newRoom.addMember("Another Wai!");
//        this.updateRoomMembers(newRoom);
//
//        newRoom.addMember("Wai 2!");
//        this.updateRoomMembers(newRoom);
//
//        newRoom.removeMember("Another Wai!");
//        this.updateRoomMembers(newRoom);
//
//        System.out.println("dfasfsaad sfds fasdf dsfas user size is: " + users.size());
//    }


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

        newRoomRef.addChildEventListener(new ChildEventListener() {

            // Event
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("-----------------------------Child has been changed -----------------------");
                System.out.println(dataSnapshot);
                System.out.println(s);
                //TODO: Hard coded
                testGroup = groups.get("testing");
                Object listOfMembers = dataSnapshot.child("memberNames").getValue();
                String roomName = dataSnapshot.getKey();
//                inviteRoom = roomName;
                System.out.println("Members in invited room:  " + roomName + " are : " + listOfMembers);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }

    public void updateRoomMembers(Group group) {
       Firebase updateRoomRef = this.roomRef.child(group.getGroupName());
        Map<String, Object> info = new HashMap<String, Object> ();
        info.put("memberNames", group.getMemberNames());
        info.put("owner", group.getOwner());
        updateRoomRef.updateChildren(info);
    }

    public void removeRoom(String groupName) {
        final Firebase newRoomRef = roomRef.child(groupName);
        newRoomRef.setValue(null);
     //  Map<String, Object> info = new HashMap<String, Object> ();
//        info.remove(groupName);
//        info.put("rooms", groups);
//        updateRoomRef.updateChildren(info);
    }

    public void updateDj(Group group) {
        Firebase updateRoomRef = this.roomRef.child(group.getGroupName());
        Map<String, Object> info = new HashMap<String, Object> ();
        info.put("owner", group.getOwner());
        updateRoomRef.updateChildren(info);
    }

    public void updateRoomSongs(Group group) {
        Firebase updateRoomRef = roomRef.child(group.getGroupName()).child("songs");
        updateRoomRef.setValue(group.getSongs());
//        if (group.getSongs() == null) {
//
//        }

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
        Firebase updateRef = this.userRef.child(user.getUsername()).child("favSongs");
        updateRef.setValue(user.getSongs());
        Map<String, Object> info = new HashMap<String, Object> ();
        info.put("favSongs", user.getSongs());
    }




    // Favorite lists

    public void createFavoriteList(ArrayList<String> songs) {

    }
    public void updateSongList(ArrayList<String> songs) {

    }



}
