package com.mycompany.airtunes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.airtunes.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SongDisplay extends ActionBarActivity {
    String songTitle = "";
    String albumCover = "";
    String artistName = "";
    public static FirebaseCalls fb = FirebaseCalls.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_display);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            songTitle = extras.getString("songTitle");
            System.out.println("songTitle is hallo there : " + songTitle);
            albumCover = extras.getString("albumCover");
            System.out.println("albumCover is hallo there : " + albumCover);
            artistName = extras.getString("artistName");
            System.out.println("artistName is hallo there : " + artistName);

        }

        new RetrieveImageBitmap().execute();


        TextView tv = (TextView) findViewById(R.id.songTitle);
        tv.setText(songTitle);

        tv = (TextView) findViewById(R.id.artistName);
        tv.setText(artistName);

    }

    public void onFavoriteButtonClick(View view) {
        System.out.println("FAVORITE SONG WAS CLICKED BITCHHHH");

        //fb.users.get(me.getUsername()).addSongs(currentSong);
        Song currentSong = null;
        for (Song song : PlaylistActivity.model.getSongs()) {
            System.out.println(song.getName());
            System.out.println(songTitle);
            if (song.getName().equals(songTitle)) {
                currentSong = song;
                System.out.println(currentSong);
            }
        }
        PlaylistActivity.me.addSongs(currentSong);
        fb.updateUserSongs(PlaylistActivity.me);
        System.out.println("Favorite songs are now: " + PlaylistActivity.me.favSongs);

    }

    class RetrieveImageBitmap extends AsyncTask<Void, Void, Void> {
        Bitmap bitMap;

        private Exception exception;

        protected void onPreExecute() { }

        protected Void doInBackground(Void... urls) {
            try {
                URL url = new URL(albumCover);
                //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
                HttpGet httpRequest = null;

                httpRequest = new HttpGet(url.toURI());

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient
                        .execute(httpRequest);

                HttpEntity entity = response.getEntity();
                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                InputStream input = b_entity.getContent();

                Bitmap bitmap = BitmapFactory.decodeStream(input);

                bitMap = bitmap;

            } catch (Exception ex) {

            }
            return null;

        }

        protected void onPostExecute(Void hallo) {
            System.out.println("fuckers");

            ImageView imageView = (ImageView) findViewById(R.id.albumCover);
            System.out.println(bitMap);
            imageView.setImageBitmap(bitMap);


        }
    }

}
