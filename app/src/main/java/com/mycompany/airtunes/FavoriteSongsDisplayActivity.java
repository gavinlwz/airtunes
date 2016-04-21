package com.mycompany.airtunes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class that allows a premium user to view their favorite songs on their user profile
 * */
public class FavoriteSongsDisplayActivity extends ActionBarActivity {
    FirebaseCalls fb = FirebaseCalls.getInstance();
    User me = fb.currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_songs_display);

        //copy favorite songs to a new list
        List<String> favSongs = new ArrayList<>();
        System.out.println("gotdamn " + me.getName());
        System.out.println("myfavsongsare " + me.favSongs);
        if (me.favSongs != null) {
            for (Song song : me.favSongs) {
                System.out.println("fuckinghell " + song.getName());
                favSongs.add(song.getName());
            }


            //checking if we pass in any data from our activity
            Bundle extras = getIntent().getExtras();
            ArrayAdapter<String> queueAdapter;
            if (extras == null) {
                queueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favSongs);
                System.out.println("Getting song names yo " + favSongs);
                ListView playlist = (ListView) findViewById(R.id.favSongsListView);
                playlist.setAdapter(queueAdapter);
            } else {
                final Group model = (Group) getIntent().getSerializableExtra("Group");
                //System.out.println("Group name received is: " + model.groupName);
                queueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favSongs);
                //System.out.println("Getting song names yo " + favSongs);
                final ListView playlist = (ListView) findViewById(R.id.favSongsListView);
                playlist.setAdapter(queueAdapter);
                playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String songName = (String) playlist.getItemAtPosition(position);
                        //System.out.println("Clicked on: " + songName);
                        Song currentSong = null;
                        for (Song s : me.favSongs) {
                            if (s.getName().equals(songName)) {
                                currentSong = s;
                            }
                        }
                        PlaylistActivity.model.addSong(currentSong);
                        PlaylistActivity.fb.updateRoomSongs(PlaylistActivity.model);
                    }
                });
            }
        }
    }
}
