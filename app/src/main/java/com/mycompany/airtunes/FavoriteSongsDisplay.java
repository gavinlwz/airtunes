package com.mycompany.airtunes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mycompany.airtunes.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSongsDisplay extends ActionBarActivity {
    FirebaseCalls fb = FirebaseCalls.getInstance();
    User me = fb.currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_songs_display);
        List<String> favSongs = new ArrayList<String>();
        for (Song song : me.favSongs) {
            favSongs.add(song.getName());
        }

        ArrayAdapter<String> queueAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, favSongs);
        System.out.println("Getting song names yo " + favSongs);
        ListView playlist = (ListView) findViewById(R.id.favSongsListView);
        playlist.setAdapter(queueAdapter);


    }

}
