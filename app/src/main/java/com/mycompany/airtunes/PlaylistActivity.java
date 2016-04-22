package com.mycompany.airtunes;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;

import android.view.DragEvent;

import android.view.GestureDetector;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.AdapterView;
import android.view.MotionEvent;

import com.firebase.client.Firebase;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.wrapper.spotify.models.Playlist;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * Activity class that handles all logic for adding / deleting songs from playlist, adding
 * and leaving groups, and DJ rights.
 * */
public class PlaylistActivity extends ActionBarActivity {
    public static ArrayAdapter<String> queueAdapter;
    boolean play = false;
    boolean isPaused = false;

    // WC added stuff
    public HashSet<String> currentUserNames = new HashSet<String>();

    public String groupName;

    boolean isShuffling = false;
   // boolean isPaused = true;
    ListView playlist;
    public static Group model;
    public static FirebaseCalls fb;
    boolean firstTimePlayButtonPressed = true;
    static Song currentSong;
    static User me;
    Timer timer;

    ToggleButton toggleButton;
    Handler mHandler;
    static int counter = 0;
    int pos = 0;
    int tapCounter = 0;
    long time=0;
    //public List<String> songNames;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID search for a group was selected
            case R.id.searchForAGroup:
                Toast.makeText(this, "Search for group selected", Toast.LENGTH_SHORT)
                        .show();
                Intent i = new Intent(getApplicationContext(), SearchGroupActivity.class);
                startActivity(i);
                break;
            // action with ID search for a user was selected
            case R.id.searchForAUser:
                Toast.makeText(this, "Search for user selected", Toast.LENGTH_SHORT)
                        .show();
                Intent ii = new Intent(getApplicationContext(), SearchUserActivity.class);
                startActivity(ii);
                break;
            // action with ID go to my own profile was selected
            case R.id.goToMyProfile:
                Toast.makeText(this, "Go to profile selected", Toast.LENGTH_SHORT)
                        .show();
                Intent iii = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(iii);
                break;
            // action with ID logout completely was selected
            case R.id.logout:
                Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT)
                        .show();
                Intent iiii = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(iiii);
                break;
            default:
                break;
        }
        return true;
    }

    //Handles logout functionality
    public void logout(View v) {
        MainActivity.logout(v);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();
        //songNames = new ArrayList<String>();

        // Update user information
        me = fb.currentUser;
        fb.users.put(fb.currentUser.getUsername(), fb.currentUser);

        // Toggle group privacy
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    model.setIsPrivate(true);
                } else {
                    model.setIsPrivate(false);
                }
                fb.toggleGroupPrivacy(model);
            }
        });

        //Update Room information
        model = (Group) getIntent().getSerializableExtra("Group");
        if (model.getSongs().isEmpty()) {
            System.out.println("the model is new so it has on songs obviously!!!!!");
        } else {
            System.out.println("The model is new so how the hell can it have songs????!!!!!!!!!");
        }
        ((TextView) findViewById(R.id.ownerView)).setText(model.owner);
        ((TextView) findViewById(R.id.roomNameView)).setText(model.groupName);
        model.addMember(me.getUsername());
        fb.updateRoomMembers(model);
        groupName = model.groupName;

        //Update view with list of current songs in room
        playlist = (ListView) findViewById(R.id.listView);
        System.out.println("Model is : " + model);
//        for (Song s : model.getSongs()) {
//            songNames.add(s.getName());
//        }
        queueAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, model.getSongNames());
        playlist.setAdapter(queueAdapter);
        ArrayList<String> songNames = new ArrayList<String>();

        for (Song s : model.getSongs()) {
            songNames.add(s.getName());
        }
        queueAdapter.clear();

        queueAdapter.addAll(songNames);
        queueAdapter.notifyDataSetChanged();
        System.out.println("PLEASE UPDATE");

        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                pos = position;
                tapCounter++;
                System.out.println("tap " + tapCounter);
                time = System.currentTimeMillis();
            }
        });

        playlist.setOnTouchListener(new OnTouchListenerSwipeTap(this) {
//            @Override
//            public void onDoubleTap(MotionEvent e) {
//                Toast.makeText(PlaylistActivity.this, "Added to favorites!", Toast.LENGTH_SHORT).show();
//                 String songName = (String) playlist.getItemAtPosition(e.getSource());
//                if (currentSong != null && me != null) {
//                    me.addSongs(currentSong);
//                    fb.updateUserSongs(me);
//                    //System.out.println(me.favSongs);
//                }
//            }

            // swipe down: search for groups
            @Override
            public void onSwipeDown() {
                Toast.makeText(PlaylistActivity.this, "Search for groups", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), SearchGroupActivity.class);
                startActivity(i);
            }

            // swipe up: search for users
            @Override
            public void onSwipeUp() {
                Toast.makeText(PlaylistActivity.this, "Search for users", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), SearchUserActivity.class);
                startActivity(i);
            }

            // swipe right: view my own favorite list
            @Override
            public void onSwipeRight() {
                Toast.makeText(PlaylistActivity.this, "View favorites list", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), FavoriteSongsDisplayActivity.class);
                i.putExtra("Group", model);
                startActivity(i);
            }

            // nothing to do for this one
            @Override
            public void onSwipeLeft() {
                Toast.makeText(PlaylistActivity.this, "Swiped left!", Toast.LENGTH_SHORT).show();
            }
        });

        //handle dynamically adding / deleting songs
        deleteSongs();
        songController();
        //refreshView();
        mHandler = new Handler();
        //m_Runnable.run();

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (tapCounter > 0 && time != 0) {
                    long ctime = System.currentTimeMillis();
                    if (ctime - time >= (long)1000) {
                        System.out.println("time diff is " + (ctime - time));
                        if (tapCounter == 1) {
                            singleClick();
                        } else {
                            doubleClick();
                        }
                        tapCounter = 0;
                        time = 0;
                    }
                }
                if (counter == 0) {
                    if (fb.groups.get(model.groupName) == null) {
                        System.out.println("akatsuka");
                        model.setSongs(new ArrayList<Song>());
                        //MainActivity.mPlayer.pause();
                       // MainActivity.mPlayer.clearQueue();

                       isPaused = true;

                        timer.cancel();
                        timer.purge();
                        finish();
                        counter++;
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }

                }


            }
        }, 1000, 1000);

        // Add users in firebase to current users
        for (String name : model.getMemberNames()) {
            System.out.println("Adding user to currentUserNames");
            currentUserNames.add(name);
        }

        // Handlers
        refreshMembers();
        //mStatusChecker.run();
    }


    public void singleClick() {
        String songName = (String) playlist.getItemAtPosition(pos);
        System.out.println("Clicked on: " + songName);
        for (Song s : model.getSongs()) {
            if (s.getName().equals(songName)) {
                currentSong = s;
            }
        }
        new RetrieveSong().execute();
    }

    public void doubleClick() {
//        Toast.makeText(PlaylistActivity.this, "Added to favorites!", Toast.LENGTH_SHORT).show();
        String songName = (String) playlist.getItemAtPosition(pos);
        Song songObject = null;
        for (Song s : model.getSongs()) {
            if (s.getName().equals(songName)) {
                System.out.println("got the song");
                songObject = s;
            }
        }
        if (songObject != null && me != null) {
            System.out.println("whoo valid song!!!");
            me.addSongs(songObject);
            fb.updateUserSongs(me);
            //System.out.println(me.favSongs);
        }
    }

    public void songController() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MainActivity.mPlayer.getPlayerState(new PlayerStateCallback() {
                    @Override
                    public void onPlayerState(PlayerState playerState) {
                        if (me.getUsername().equals(model.getOwner())) {
                            if (!playerState.playing) {
                                if (play && isPaused && !firstTimePlayButtonPressed) {
                                   MainActivity.mPlayer.resume();
                                    isPaused = false;
                                   return;
                                    }
                                if (model.getSongs().size() > 0) {
                                    if (!isPaused) {
                                        if (firstTimePlayButtonPressed) {
                                            firstTimePlayButtonPressed = false;
                                        }
                                        MainActivity.mPlayer.play(model.getSongs().get(0).getUri());
                                        model.removeSong(model.getSongs().get(0));
                                        System.out.println("how often?");
//                                        queueAdapter.clear();
//                                        queueAdapter.addAll(model.getSongNames());
//                                        queueAdapter.notifyDataSetChanged();
                                        ArrayList<String> songNames = new ArrayList<String>();

                                        for (Song s : model.getSongs()) {
                                            songNames.add(s.getName());
                                        }
                                        queueAdapter.clear();

                                        queueAdapter.addAll(songNames);
                                        queueAdapter.notifyDataSetChanged();
                                        play = true;
                                        return;
                                    }
                                }
                            } else {
                                if (play && isPaused) {
                                    MainActivity.mPlayer.pause();
                                    play = false;
                                    return;
                                }
                            }
                        }
                    }
                });
            }
        }, 1000, 1000);

    }

    //Auto-refreshes view to dynamically add/delete songs
    public void refreshView() {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (fb.groups.get(model.groupName) == null) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                //Update user information
                me = fb.currentUser;
                fb.users.put(fb.currentUser.getUsername(), fb.currentUser);
                toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

                //Update Room information
                model = (Group) getIntent().getSerializableExtra("Group");
                ((TextView) findViewById(R.id.ownerView)).setText(model.owner);
                ((TextView) findViewById(R.id.roomNameView)).setText(model.groupName);
//                    model.addMember(me.getUsername());
//                    fb.updateRoomMembers(model);

                //Update view with list of current songs in room
//                playlist = (ListView) findViewById(R.id.listView);
//
//                queueAdapter = new ArrayAdapter<String>(PlaylistActivity.this, android.R.layout.simple_list_item_1, songNames);
//                playlist.setAdapter(queueAdapter);

            }
        }, 1000, 1000);
    }

//    //Random Adding user to group
//    Runnable adder =
//            new Runnable() {
//                @Override
//                public void run() {
//
//                    mHandler.postDelayed(adder,10000);
//
//                }
//            };

    //To send user leave room notification


    // ----- SECTION to do leave room notifications -------
    String toastMsg;

    public void refreshMembers() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            //    Runnable mMemberChecker =
//            new Runnable() {
            @Override
            public void run() {
//                    System.out.println("Member checker is running. ");
                Group remoteGroup = fb.groups.get(groupName);
                if (remoteGroup != null) {
                    List<String> serverNames = remoteGroup.getMemberNames();
//                    List<String> serverNames = model.getMemberNames();
//                    System.out.println("Number of members in model: " + serverNames);
                    int serverSize = serverNames.size();
                    int localSize = currentUserNames.size();

//                    System.out.println("fb group size is: " + fb.groups.get(groupName).getMemberNames().size());
//                    System.out.println("User size: " + serverSize + " currentUser size: " + localSize);

//                    HashSet<String> testUserNames = new HashSet<String>();
//                    testUserNames.add("ihugacownow");
//                    testUserNames.add("tahmid");
                    if (serverSize < currentUserNames.size()) {
                        for (String name : currentUserNames) {
                            if (!serverNames.contains(name)) {
                                currentUserNames.remove(name);

                                // Show Toast
                                toastMsg = name + " has left the group";
                                mMemberHandler.obtainMessage(1).sendToTarget();

                                System.out.println("--------------- fucker " + name + " has left the group -------");
                                break;

                            }

                        }
                    } else if (serverSize > currentUserNames.size()) {
                        System.out.println(currentUserNames.size() + ": is the currentUserName Size");
                        for (String name : serverNames) {
                            if (!currentUserNames.contains(name)) {
                                currentUserNames.add(name);
                                toastMsg = name + " has joined the group";
                                mMemberHandler.obtainMessage(1).sendToTarget();


                                System.out.println("--------------- fucker " + name + " has joined the group -------");
                                break;

                            }

                        }
                    }
                }

//                    mHandler.postDelayed(mMemberChecker,3000);
            }
        }, 2000, 4000);

    }

    public Handler mMemberHandler = new Handler() {
        public void handleMessage(Message msg) {
            // Show Toast
            Context context = getApplicationContext();
//            CharSequence text = msg + " has joined the group";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, toastMsg, duration);
            toast.show();
        }
    };


    //To update the playlist
    Runnable mStatusChecker =
            new Runnable() {
                @Override
                public void run() {
                    //Update user information
                    me = fb.currentUser;
                    fb.users.put(fb.currentUser.getUsername(), fb.currentUser);
                    toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

                    //Update Room information
                    model = (Group) getIntent().getSerializableExtra("Group");
                    ((TextView) findViewById(R.id.ownerView)).setText(model.owner);
                    ((TextView) findViewById(R.id.roomNameView)).setText(model.groupName);
//                    model.addMember(me.getUsername());
//                    fb.updateRoomMembers(model);

                    //Update view with list of current songs in room
//                    playlist = (ListView) findViewById(R.id.listView);
//
//                    queueAdapter = new ArrayAdapter<String>(PlaylistActivity.this, android.R.layout.simple_list_item_1, songNames);
//                    playlist.setAdapter(queueAdapter);

                    mHandler.postDelayed(mStatusChecker,1000);

                }
            };


    void startRepeatingTask() {
        mStatusChecker.run();
//        mMemberChecker.run();
    }

    //Logic for deleting songs from playlist on long click
    public void deleteSongs() {

        playlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if (!me.getUsername().equals(model.getOwner())) {
                    //System.out.println("Cannot delete song because not owner");
                    Context context = getApplicationContext();
                    CharSequence text = "You need to be owner to delete song";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    return true;
                }
                String songName = (String) playlist.getItemAtPosition(position);
                //System.out.println("Long Clicked on: " + songName);
                for (Song s : model.getSongs()) {
                    System.out.println("Song in model is: " + s.getName());
                    if (s.getName().equals(songName)) {
                        model.removeSong(s);
                        fb.updateRoomSongs(model);
//                        queueAdapter.clear();
//                        queueAdapter.addAll(model.getSongNames());
//                        queueAdapter.notifyDataSetChanged();
                        //songNames.remove(s.getName());
                        return true;
                    }
                }
                return true;
            }
        });


        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (fb.groups.get(model.groupName) == null) {

                }


            }
        }, 1000, 1000);


        mHandler = new Handler();
        startRepeatingTask();



    }

    private final Runnable m_Runnable = new Runnable()

    {
        public void run()

        {
                Toast.makeText(PlaylistActivity.this, "fuck the po po", Toast.LENGTH_SHORT).show();

                System.out.println("groupies " + fb.groups.get(model.groupName));
                if (fb.groups.get(model.groupName) == null) {
                    System.out.println("akatsuka");
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    mHandler.removeCallbacks(m_Runnable);
                    startActivity(i);
                    Thread.currentThread().interrupt();

                }

            mHandler.postDelayed(m_Runnable,20000);

        }

    };

    //Invite user from search to join group
    public void onInviteButtonClick(View view) {
        SearchView search = (SearchView) findViewById(R.id.searchForUser);
        String query = search.getQuery() + "";
        //System.out.println("The username of account holder is: " + me.getUsername());
        //System.out.println("The owner is " + model.getOwner());

        if (me.getUsername().equals(model.getOwner())) {
            model.addMember(query);
            //fb.testGroup = model;
            fb.updateRoomMembers(model);
            //System.out.println(model.getMemberNames());


        }
    }

    //Change DJ to user specified in the search
    public void onChangeDjButtonClick(View view) {
        SearchView search = (SearchView) findViewById(R.id.searchForUser);
        String query = search.getQuery() + "";
        //System.out.println("The username of account holder is: " + me.getUsername());
        //System.out.println("The owner is " + model.getOwner());

        if (me.getUsername().equals(model.getOwner())) {
            if (model.getMemberNames().contains(query)) {
                model.changeDj(query);
                fb.updateDj(model);
                //System.out.println(model.getMemberNames());
                //System.out.println("New Dj is: " + model.getOwner());
            }

        }


    }

    //Play / continue current song
    public void onPlayButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            play = true;
        }
    }

    //Pause current song
    public void onPauseButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            isPaused = true;
        }

    }

    //Skip to next song
    public void onNextButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            play = false;
            isPaused = false;
            MainActivity.mPlayer.pause();
            //MainActivity.mPlayer.skipToNext();
//            if (model.getSongs().size() > 0) {
//                //System.out.println("NEXT");
//                MainActivity.mPlayer.play(model.getSongs().get(0).getUri());
//                model.removeSong(model.getSongs().get(0));
//                if (isPaused) {
//                    isPaused = false;
//                }
//                if (!play) {
//                    play = true;
//                }
//            }
//
        }


    }


    public void onPg13ButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            model.setPG13(!model.isPG13);
            fb.updateRoomPg13(model);
        }
        
    }


    //Randomize selection of next song to create shuffling
    public void onSetShuffleButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            if (model.getSongs().size() > 1) {
                Random r = new Random();
                int n = r.nextInt(model.getSongs().size());
                Song s = model.getSongs().get(n);
                model.removeSong(s);
                model.addSong(s, 0);
                fb.updateRoomSongs(model);
//                queueAdapter.clear();
//                queueAdapter.addAll(model.getSongNames());
//                queueAdapter.notifyDataSetChanged();
                ArrayList<String> songNames = new ArrayList<String>();

                for (Song so : model.getSongs()) {
                    songNames.add(so.getName());
                }
                queueAdapter.clear();

                queueAdapter.addAll(songNames);
                queueAdapter.notifyDataSetChanged();
            }


        }
    }


    //Query song from Spotify and add it to the view
    public void onAddSongButtonClick(View view) throws ExecutionException, InterruptedException {

        SearchView search = (SearchView) findViewById(R.id.songSearchView);
        String query1 = search.getQuery() + "";
        String query2 = "track";
        String[] query = new String[2];
        query[0] = query1;
        query[1] = query2;

        AsyncTask<String, Void, String> rs = new RetrieveSongs();
        rs.execute(query);
        rs.get();

        List<Song> songs = model.getSongs();
        System.out.println("# of songs = " + songs.size());
        if (!songs.isEmpty()) {
            if (model.isPG13 && songs.get(songs.size() - 1).isExplicit) {
                model.removeSong(songs.get(songs.size() - 1));
                System.out.println("entering here?");
               // fb.updateRoomSongs(model);
                if (model.getSongs().size() == 0) {
                    MainActivity.mPlayer.pause();
                    MainActivity.mPlayer.clearQueue();
                    play = false;
                    isPaused=false;
                    currentSong = null;
                }

                makeToast("This song is explicit and cannot be added to playlist");
            }
        }
        fb.updateRoomSongs(model);
        System.out.println("#songs = " + songs.size());
        ArrayList<String> songNames = new ArrayList<String>();
        for (Song s : songs) {
            songNames.add(s.getName());
        }
        queueAdapter.clear();

        queueAdapter.addAll(songNames);
        queueAdapter.notifyDataSetChanged();
//        songNames = new ArrayList<String>();
//        for (Song s : model.getSongs()) {
//            songNames.add(s.getName());
//        }
//        queueAdapter = new ArrayAdapter<String>(
//                this, android.R.layout.simple_list_item_1, songNames);
//        playlist.setAdapter(queueAdapter);

//        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                String songName = (String) playlist.getItemAtPosition(pos);
//                System.out.println("Clicked on: " + songName);
//                for (Song s : model.getSongs()) {
//                    if (s.getName().equals(songName)) {
//                        currentSong = s;
//                    }
//                }
//                new RetrieveSong().execute();
//            }
//        });




    }



    public void makeToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    //Favorite current song and add to user's "favorites" list

    public void onFavoriteButtonClick(View view) {
        if (currentSong != null && me != null) {
            me.addSongs(currentSong);
            fb.updateUserSongs(me);
            //System.out.println(me.favSongs);
        }
    }

    //Make the current group private
    public void onPrivacyButtonClick(View view) {
        model.setIsPrivate(!model.isPrivate);
    }

    //check if user is a DJ, if so transfer DJ rights
    // check if user is last user in group, if so we need to disable the room
    // disabling the room involves: 1. delete the room from list of rooms 2. deleting songs from the room.
    public void onLeaveRoomButtonClick(View view) {

        //System.out.println(me.getUsername());
        MainActivity.mPlayer.pause();

        if (model.getMemberNames().contains(me.getUsername())) {
            model.removeMember(me.getUsername());
            fb.updateRoomMembers(model);
            //System.out.println("ROOM SIZE = " + model.getMemberNames().size());
            if (model.getMemberNames().size() == 0) {
                fb.groups.remove(model.getGroupName());
                fb.removeRoom(model.getGroupName());
//                fb.updateRoomAsRemoved(model);
                //System.out.println("removing room");

                finish();
                return;
            }
            if (model.getOwner().equals(fb.currentUser.getUsername())) {
                reassignDj();


            }
        }

        finish();
    }

    //Reassigns DJ rights when DJ leaves room
    public void reassignDj() {
        //System.out.println("model.getMemberNames().get(0)" + model.getMemberNames().get(0));
        model.changeDj(model.getMemberNames().get(0));
        fb.updateRoomMembers(model);

    }

    public void onDisband(View view) {
        if (me.getUsername().equals(model.owner)) {
            //MainActivity.mPlayer.shutdown();
//            MainActivity.mPlayer.pause();
//            isPaused = true;
            fb.removeRoom(model.groupName);
        }

    }

    //Queries a random song from Spotify
    public void onRandomButtonClick(View view) {
        String[] query = new String[1];
        query[0] = "random";
        new RetrieveSongs().execute(query);
    }

    //Allows user to view their favorite songs from the View
    public void viewFavSongs(View v) {
        Intent i = new Intent(getApplicationContext(), FavoriteSongsDisplayActivity.class);
        i.putExtra("Group", model);
        startActivity(i);
    }

    //Async method to retrieve song information from Spotify
    //Prevents locking of main UI thread
    class RetrieveSong extends AsyncTask<Void, Void, String> {
        private Exception exception;


        protected void onPreExecute() {
        }


        protected String doInBackground(Void... urls) {
            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;

            //Main API Get request
            try {
                String s = "";
                String finalUri = "";
                for (int i = currentSong.getUri().length() - 1; i > 0; i--) {
                    if (currentSong.getUri().charAt(i) == ':') {
                        break;
                    } else {
                        s += currentSong.getUri().charAt(i);
                    }
                }
                for (int i = s.length() - 1; i >= 0; i--) {
                    finalUri += s.charAt(i);

                }
                String searchUrl = "https://api.spotify.com/v1/tracks/" + finalUri;
                url = new URL(searchUrl);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.setRequestProperty("Authorization", "Bearer " + MainActivity.access_token);
                urlConnection.setDoOutput(false);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                while ((temp = bReader.readLine()) != null) {
                    response += temp;
                }
                object = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject albums = (JSONObject) object.get("album");
                JSONArray images = (JSONArray) albums.get("images");
                JSONObject image = (JSONObject) images.get(0);
                currentSong.setPictureUrl((String) image.get("url"));


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //Start new intent to view song
                Intent i = new Intent(getApplicationContext(), SongDisplayActivity.class);
                i.putExtra("songTitle", currentSong.getName());
                i.putExtra("albumCover", currentSong.getPictureUrl());
                i.putExtra("artistName", currentSong.getArtist());
                i.putExtra("song", currentSong);
                startActivity(i);
                //System.out.println("I am done with async");
                if (inStream != null) {
                    try {
                        // this will close the bReader as well
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }

}
