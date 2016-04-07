package com.mycompany.airtunes;

import android.content.Intent;
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
import com.mycompany.airtunes.R;
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

public class PlaylistActivity extends ActionBarActivity {
    public static ArrayAdapter<String> queueAdapter;
    //public static ArrayList<Track> queue;
    //public static ArrayList<String> queueSongs;
    boolean play = false;
    boolean isShuffling = false;
    ListView playlist;
    Exception mException = null;
    public static Group model;
    public static FirebaseCalls fb;
    boolean firstTimePlayButtonPressed = true;
    Song currentSong;
    User me;

    ToggleButton toggleButton;

    // Properties for non-song attributes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();
        //fb.test();
        me = fb.currentUser;
        fb.users.put(fb.currentUser.getUsername(), fb.currentUser);
        //me = fb.users.get(fb.currentUser.getUsername());
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        //Wai's Code on Receiving Groups

        model = (Group) getIntent().getSerializableExtra("Group");
        //model = new Group("Why", "Wai");
        System.out.println("Group name received is: " + model.groupName);
        // Update Room information
        ((TextView)findViewById(R.id.ownerView)).setText(model.owner);
        ((TextView) findViewById(R.id.roomNameView)).setText(model.groupName);
        model.addMember(me.getUsername());
        fb.updateRoomMembers(model);

        playlist = (ListView) findViewById(R.id.listView);
        //queue = new ArrayList<Track>();
       // queueSongs = new ArrayList<String>();
        List<String> songNames = new ArrayList<String>();
        for (Song s : model.getSongs()) {
            songNames.add(s.getName());
        }
        queueAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, songNames);
        System.out.println("Getting song names yo " + model.getSongNames());
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
    }

    public void onInviteButtonClick(View view) {
        SearchView search = (SearchView) findViewById(R.id.searchForUser);
        String query = search.getQuery() + "";
        System.out.println("The username of account holder is: " + me.getUsername());
        System.out.println("The owner is " + model.getOwner());

//        if (me.getUsername().equals(model.getOwner())) {
            model.addMember(query);
            fb.testGroup = model;
            fb.updateRoomMembers(model);
       // }
    }
    public void onPlayButtonClick(View view) {
        System.out.println("play button clicked");
        if (me.getUsername().equals(model.getOwner())) {
            System.out.println("KAJSDHKALSDHJASD");
            play = !play;
            if (play) {
                MainActivity.mPlayer.resume();
                if (firstTimePlayButtonPressed) {
                    firstTimePlayButtonPressed = !firstTimePlayButtonPressed;
                    MainActivity.mPlayer.play(model.getSongs().get(0).getUri());
                }

            } else {
                MainActivity.mPlayer.pause();
            }
        }
    }

    public void onToggleStar(View view) {
        
    }

    public void onNextButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            MainActivity.mPlayer.skipToNext();
        }

    }

    public void onShuffleButtonClick(View view) {
        if (me.getUsername().equals(model.getOwner())) {
            isShuffling = !isShuffling;
            MainActivity.mPlayer.setShuffle(isShuffling);
        }
    }

    public void onAddSongButtonClick(View view) {
        SearchView search = (SearchView) findViewById(R.id.songSearchView);
        String query = search.getQuery() + "";
        new RetrieveStuff().execute(query);

        //ListView lv = (ListView) findViewById(R.id.listView);
//        lv.requestLayout();


    }

    public void onFavoriteButtonClick(View view) {
        if (currentSong != null && me != null) {
            System.out.println("CURRENT SONG = " + currentSong);
            System.out.println("CURRENT USER = " + me);
            //fb.users.get(me.getUsername()).addSongs(currentSong);
           me.addSongs(currentSong);
            fb.updateUserSongs(me);
        }
    }

    public void onPrivacyButtonClick(View view) {
        model.setIsPrivate(!model.isPrivate);
    }



    class RetrieveSong extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() { }

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
                JSONObject albums = (JSONObject)object.get("album");
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
