package com.mycompany.airtunes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URL;

public class SongDisplayActivity extends ActionBarActivity {
    String songTitle = "";
    String albumCover = "";
    String artistName = "";
    Song song = null;
    public static FirebaseCalls fb = FirebaseCalls.getInstance();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_display);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            songTitle = extras.getString("songTitle");
            albumCover = extras.getString("albumCover");
            artistName = extras.getString("artistName");
            song = (Song) extras.get("song");
            //System.out.println("songTitle is hallo there : " + songTitle);
            //System.out.println("albumCover is hallo there : " + albumCover);
            //System.out.println("artistName is hallo there : " + artistName);
        }

        new RetrieveImageBitmap().execute();

        TextView tv = (TextView) findViewById(R.id.songTitle);
        tv.setText(songTitle);

        tv = (TextView) findViewById(R.id.artistName);
        tv.setText(artistName);
    }

    public void onFavoriteButtonClick(View view) {
        Song currentSong = null;
//        if (PlaylistActivity.model != null) {
//            //        for (Song song : PlaylistActivity.model.getSongs()) {
////            //System.out.println(song.getName());
////            //System.out.println(songTitle);
////            if (song.getName().equals(songTitle)) {
////                currentSong = song;
////                //System.out.println(currentSong);
////            }
////        }
//        } else {
//
//        }
        currentSong = song;
        fb.currentUser.addSongs(currentSong);
        fb.users.put(fb.currentUser.getUsername(), fb.currentUser);
        fb.updateUserSongs(fb.currentUser);
        //System.out.println("Favorite songs are now: " + PlaylistActivity.me.favSongs);
    }

    class RetrieveImageBitmap extends AsyncTask<Void, Void, Void> {
        Bitmap bitMap;

        private Exception exception;

        protected void onPreExecute() { }

        protected Void doInBackground(Void... urls) {
            try {
                URL url = new URL(albumCover);
                HttpGet httpRequest = null;
                httpRequest = new HttpGet(url.toURI());
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                InputStream input = b_entity.getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                bitMap = bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void hallo) {
            ImageView imageView = (ImageView) findViewById(R.id.albumCover);
            System.out.println(bitMap);
            imageView.setImageBitmap(bitMap);
        }
    }

}
