package com.mycompany.airtunes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mycompany.airtunes.R;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Track;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PlaylistActivity extends ActionBarActivity {
    public static ArrayAdapter<String> queueAdapter;
    public static ArrayList<Track> queue;
    public static ArrayList<String> queueSongs;
    ListView playlist;
    Exception mException = null;

    // Properties for non-song attributes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        //Wai's Code on Receiving Groups
        Group model = (Group) getIntent().getSerializableExtra("Group");
        System.out.println("Group name received is: " + model.groupName);
        // Update Room information
        ((TextView)findViewById(R.id.ownerView)).setText(model.owner);
        ((TextView) findViewById(R.id.roomNameView)).setText(model.groupName);



        playlist = (ListView) findViewById(R.id.listView);
        queue = new ArrayList<Track>();
        queueSongs = new ArrayList<String>();


//        queue.add("Hello");
//        queue.add("Daft Punk");
        queueAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, queueSongs);
        playlist.setAdapter(queueAdapter);

        for (String song : model.songNames) {
            new RetrieveStuff().execute(song);
        }

        for (String s : queueSongs) {
            System.out.println("Song in queue: " + s);
        }




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


    }

//    void makeApiRequest(String urlString) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection urlConnection = null;
//                URL url = null;
//                JSONObject object = null;
//                InputStream inStream = null;
//                try {
//                    url = new URL(urlString.toString());
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.setDoOutput(true);
//                    urlConnection.setDoInput(true);
//                    urlConnection.connect();
//                    inStream = urlConnection.getInputStream();
//                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
//                    String temp, response = "";
//                    while ((temp = bReader.readLine()) != null) {
//                        response += temp;
//                    }
//                    Log.d("yoyoyoyooyoy", ""+response);
//                    object = (JSONObject) new JSONTokener(response).nextValue();
//                } catch (Exception e) {
//                    this.mException = e;
//                    Log.d(getClass().getName(), "exception = "+mException);
//                } finally {
//                    if (inStream != null) {
//                        try {
//                            // this will close the bReader as well
//                            inStream.close();
//                        } catch (IOException ignored) {
//                        }
//                    }
//                    if (urlConnection != null) {
//                        urlConnection.disconnect();
//                    }
//                }
//            }
//
//        }).start();
//
//    }

}
