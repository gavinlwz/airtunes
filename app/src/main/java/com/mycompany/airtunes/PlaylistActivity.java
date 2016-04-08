package com.mycompany.airtunes;

import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mycompany.airtunes.R;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Track;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlaylistActivity extends ActionBarActivity {
    public static ArrayAdapter<String> queueAdapter;
    //public static ArrayList<Track> queue;
    //public static ArrayList<String> queueSongs;
    boolean play = false;
    boolean isPaused = false;

    boolean isShuffling = false;
   // boolean isPaused = true;
    ListView playlist;
    Exception mException = null;
    public static Group model;
    public static FirebaseCalls fb;
    boolean firstTimePlayButtonPressed = true;
    static Song currentSong;
    static User me;
    Timer timer;

    ToggleButton toggleButton;
    Handler mHandler;

    public static List<String> songNames;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();
        songNames = new ArrayList<String>();
        //fb.test();

        me = fb.currentUser;
        fb.users.put(fb.currentUser.getUsername(), fb.currentUser);
        //me = fb.users.get(fb.currentUser.getUsername());
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        //Wai code for the auto update of playlist


        //Wai's Code on Receiving Groups

        model = (Group) getIntent().getSerializableExtra("Group");
        //model = new Group("Why", "Wai");
        System.out.println("Group name received is: " + model.groupName);
        // Update Room information
        ((TextView) findViewById(R.id.ownerView)).setText(model.owner);
        ((TextView) findViewById(R.id.roomNameView)).setText(model.groupName);
        model.addMember(me.getUsername());
        fb.updateRoomMembers(model);

        playlist = (ListView) findViewById(R.id.listView);
        //queue = new ArrayList<Track>();
        // queueSongs = new ArrayList<String>();
        System.out.println("Model is : " + model);
//        songNames.clear();
        for (Song s : model.getSongs()) {
            songNames.add(s.getName());
        }

        queueAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, songNames);
//        System.out.println("Getting song names yo " + model.getSongNames());
        playlist.setAdapter(queueAdapter);
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String songName = (String) playlist.getItemAtPosition(position);
                System.out.println("Clicked on: " + songName);
                for (Song s : model.getSongs()) {
                    if (s.getName().equals(songName)) {
                        currentSong = s;
                    }
                }
                new RetrieveSong().execute();
            }
        });

        playlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                String songName = (String) playlist.getItemAtPosition(position);
                System.out.println("Long Clicked on: " + songName);
                for (Song s : model.getSongs()) {
                    System.out.println("Song in model is: " + s.getName());
                    if (s.getName().equals(songName)) {
                        model.removeSong(s);
                        fb.updateRoomSongs(model);
                        songNames.remove(s.getName());
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
                MainActivity.mPlayer.getPlayerState(new PlayerStateCallback() {
                   @Override
                   public void onPlayerState(PlayerState playerState) {
                       System.out.println("IS THE PLAYER PLAYING????" + playerState.playing);
                       if (!playerState.playing) {
                            if (play && isPaused && !firstTimePlayButtonPressed) {
                                MainActivity.mPlayer.resume();
                                isPaused = false;
                                return;
                            }

                           if (model.getSongs().size() > 0) {
                               if (firstTimePlayButtonPressed) {
                                   firstTimePlayButtonPressed = false;
                               }
                               if (me.getUsername().equals(model.getOwner())) {
                                   MainActivity.mPlayer.play(model.getSongs().get(0).getUri());
                                   model.removeSong(model.getSongs().get(0));
                                   play = true;
                               }

                               return;
                           }
                       } else {
                           if (play && isPaused) {
                               MainActivity.mPlayer.pause();
                               play = false;
                               return;
                           }
                       }
                   }
               });
            }
        }, 1000, 1000);
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//               MainActivity.mPlayer.getPlayerState(new PlayerStateCallback() {
//                   @Override
//                   public void onPlayerState(PlayerState playerState) {
//                      // System.out.println("IS THE PLAYER PLAYING????")
//                       if (!playerState.playing && !play) {
//
//                           if (model.getSongs().size() > 0) {
//                               MainActivity.mPlayer.play(model.getSongs().get(0).getUri());
//                               play = !play;
//                           }
//                       }
//                   }
//               });
////                if (firstTimePlayButtonPressed) {
////                    firstTimePlayButtonPressed = !firstTimePlayButtonPressed;
////                    if (model.getSongs().size() > 0) {
////                        MainActivity.mPlayer.play(model.getSongs().get(0).getUri());
////                    }
////
////                }
//            }
//        }, 1000, 1000);

        mHandler = new Handler();
        startRepeatingTask();


//        for (String song : model.songNames) {
//            new RetrieveStuff().execute(song);
//        }
//
//        for (String s : queueSongs) {
//            System.out.println("Song in queue: " + s);
//        }


//        new RetrieveStuff().execute("jesus take the wheel");
//        new RetrieveStuff().execute("happy pharrel");


        //MainActivity.mPlayer.play(queue.get(0).getUri());
        //makeApiRequest("https://api.spotify.com/v1/search?q=hello%20adele&limit=1&market=US&type=track");
//        final TrackSearchRequest request = MainActivity.api.searchTracks("Mr. Brightside").market("US").build();
//        Log.d(getClass().getName(), "" + request);
//
//        try {
//            final Page<Track> trackSearchResult = request.get();
//            System.out.println("I got " + trackSearchResult.getTotal() + " results!");
//        } catch (Exception e) {
//            System.out.println("Something went wrong!" + e.getMessage());
//        }

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//                //buttons[inew][jnew].setBackgroundColor(Color.BLACK);
//                if (queue.size() != 0) {
//                    System.out.println(queue.get(0).getUri());
//                    MainActivity.mPlayer.play(queue.get(0).getUri());
//                }
//            }
//        }, 10000);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // To update the playlist
    Runnable mStatusChecker =
            new Runnable() {
                @Override
                public void run() {
                    try {
                        queueAdapter.notifyDataSetChanged(); //this function can change value of mInterval.
                    } finally {
                        // 100% guarantee that this always happens, even if
                        // your update method throws an exception
                        mHandler.postDelayed(mStatusChecker, 1000);
                    }
                }
            };


    //    void updatePlaylist() {
//        runOnUiThread();
//    }
    void startRepeatingTask() {
        mStatusChecker.run();
    }

    public void onInviteButtonClick(View view) {
        SearchView search = (SearchView) findViewById(R.id.searchForUser);
        String query = search.getQuery() + "";
        System.out.println("The username of account holder is: " + me.getUsername());
        System.out.println("The owner is " + model.getOwner());

        if (me.getUsername().equals(model.getOwner())) {
            model.addMember(query);
            //fb.testGroup = model;
            fb.updateRoomMembers(model);
            System.out.println(model.getMemberNames());


        }
    }

    public void onChangeDjButtonClick(View view) {
        SearchView search = (SearchView) findViewById(R.id.searchForUser);
        String query = search.getQuery() + "";
        System.out.println("The username of account holder is: " + me.getUsername());
        System.out.println("The owner is " + model.getOwner());

        if (me.getUsername().equals(model.getOwner())) {
            if (model.getMemberNames().contains(query)) {
                model.changeDj(query);
                fb.updateDj(model);
                System.out.println(model.getMemberNames());
                System.out.println("New Dj is: " + model.getOwner());
            }

        }


    }

    public void onPlayButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            play = true;
        }

        //isPaused = false;
//        System.out.println("play button clicked");
//        if (me.getUsername().equals(model.getOwner())) {
//            System.out.println("KAJSDHKALSDHJASD");
//            play = !play;
//            if (play) {
//                System.out.println("Plyaing track: ");
//                MainActivity.mPlayer.resume();
//                if (firstTimePlayButtonPressed) {
//                    firstTimePlayButtonPressed = !firstTimePlayButtonPressed;
//                    MainActivity.mPlayer.play(model.getSongs().get(0).getUri());
//                }
//
//            } else {
//                MainActivity.mPlayer.pause();
//            }
//        }
    }

    public void onPauseButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            isPaused = true;
        }

        //play = false;
    }

    public void onToggleStar(View view) {

    }

    public void onNextButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            //MainActivity.mPlayer.skipToNext();
            if (model.getSongs().size() > 0) {
                MainActivity.mPlayer.play(model.getSongs().get(0).getUri());
                model.removeSong(model.getSongs().get(0));
            }

        }


    }

    public void onSetShuffleButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
//            isShuffling = !isShuffling;
//            System.out.println("Setting the player to shuffling mode");
//            MainActivity.mPlayer.setShuffle(isShuffling);
            if (model.getSongs().size() > 1) {
                Random r = new Random();
                int n = r.nextInt(model.getSongs().size());
                Song s = model.getSongs().get(n);
                model.removeSong(s);
                model.addSong(s, 0);
            }


        }
    }

    public void onAddSongButtonClick(View view) {
        SearchView search = (SearchView) findViewById(R.id.songSearchView);
        String query1 = search.getQuery() + "";
        String query2 = "track";
        String[] query = new String[2];
        query[0] = query1;
        query[1] = query2;
        new RetrieveStuff().execute(query);


        //ListView lv = (ListView) findViewById(R.id.listView);
//        lv.requestLayout();


    }

    public void onFavoriteButtonClick(View view) {
        System.out.println("FAVORITE SONG WAS CLICKED BITCHHHH");

        if (currentSong != null && me != null) {
            System.out.println("CURRENT SONG = " + currentSong);
            System.out.println("CURRENT USER = " + me);
            //fb.users.get(me.getUsername()).addSongs(currentSong);
            me.addSongs(currentSong);
            fb.updateUserSongs(me);
            System.out.println(me.favSongs);
        }
    }

    public void onPrivacyButtonClick(View view) {
        model.setIsPrivate(!model.isPrivate);
    }

    public void onLeaveRoomButtonClick(View view) {
        //check if user is a DJ, if so transfer DJ rights
        // check if user is last user in group, if so we need to disable the room
        // disabling the room involves: 1. delete the room from list of rooms 2. deleting songs from the room.
        System.out.println(me.getUsername());
        if (model.getMemberNames().contains(me.getUsername())) {
            model.removeMember(me.getUsername());
            fb.updateRoomMembers(model);
            System.out.println("ROOM SIZE = " + model.getMemberNames().size());
            if (model.getMemberNames().size() == 0) {
                fb.groups.remove(model.getGroupName());
                fb.removeRoom(model.getGroupName());
                System.out.println("removing room");
                finish();
                return;
            }
            if (model.getOwner().equals(fb.currentUser.getUsername())) {
                reassignDj();


            }
        }

        finish();
    }

    public void reassignDj() {
        model.changeDj(model.getMemberNames().get(0));

    }

    public void onRandomButtonClick(View view) {
        String[] query = new String[1];
        query[0] = "random";
        new RetrieveStuff().execute(query);
    }


    class RetrieveSong extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
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
                    System.out.println("hallo " + temp);
                }
                object = (JSONObject) new JSONTokener(response).nextValue();
                JSONObject albums = (JSONObject) object.get("album");
                JSONArray images = (JSONArray) albums.get("images");
                JSONObject image = (JSONObject) images.get(0);
                System.out.println("this is bullshit " + image.get("url"));
                currentSong.setPictureUrl((String) image.get("url"));


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Intent i = new Intent(getApplicationContext(), SongDisplay.class);
                i.putExtra("songTitle", currentSong.getName());
                i.putExtra("albumCover", currentSong.getPictureUrl());
                i.putExtra("artistName", currentSong.getArtist());
                startActivity(i);
                System.out.println("I am done with async");
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
