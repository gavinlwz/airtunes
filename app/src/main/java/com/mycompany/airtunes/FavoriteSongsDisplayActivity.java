package com.mycompany.airtunes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mycompany.airtunes.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSongsDisplayActivity extends ActionBarActivity {
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

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            ArrayAdapter<String> queueAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, favSongs);
            System.out.println("Getting song names yo " + favSongs);
            ListView playlist = (ListView) findViewById(R.id.favSongsListView);
            playlist.setAdapter(queueAdapter);
        } else {
            final Group model = (Group) getIntent().getSerializableExtra("Group");
            //model = new Group("Why", "Wai");
            System.out.println("Group name received is: " + model.groupName);
            ArrayAdapter<String> queueAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, favSongs);
            System.out.println("Getting song names yo " + favSongs);
            final ListView playlist = (ListView) findViewById(R.id.favSongsListView);
            playlist.setAdapter(queueAdapter);
            playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String songName = (String) playlist.getItemAtPosition(position);
                    System.out.println("Clicked on: " + songName);
                    Song currentSong = null;
                    for (Song s : fb.currentUser.favSongs) {
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
