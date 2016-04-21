package com.mycompany.airtunes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class that displays the profile of a discovered user and handles interactivity
 * with that profile by the current user.
 * */
public class UserSearchResultActivity extends ActionBarActivity {
    private String fullName;
    private String userName;
    private boolean privacy;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_result);
//        FirebaseCalls fb = FirebaseCalls.getInstance();
//        User me = fb.currentUser;

        //copy favorite songs to a new list
        List<String> favSongs = new ArrayList<>();
//        System.out.println("gotdamn " + user.getName());
//        System.out.println("myfavsongsare " + user.favSongs);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            user = (User) extras.getSerializable("user");
            String firstName = extras.getString("firstName");
            String lastName = extras.getString("lastName");
            userName = extras.getString("userName");
            privacy = extras.getBoolean("privacy");
            final ListView favsList = (ListView) findViewById(R.id.favs);
            System.out.println("My name is " + user.getUsername());
            if (privacy == true) {
                fullName = firstName + " " + firstName.charAt(0);
            } else {
                fullName = firstName + " " + lastName;
                favsList.setVisibility(View.VISIBLE);
                ArrayAdapter<String> queueAdapter;
                if (user.favSongs != null) {
                    for (Song song : user.favSongs) {
                        System.out.println("fuckinghell " + song.getName());
                        favSongs.add(song.getName());
                    }
                    queueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favSongs);
                    favsList.setAdapter(queueAdapter);
                    favsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String songName = (String) favsList.getItemAtPosition(position);
                            Song currentSong = null;
                            System.out.println("Clicked on: " + songName);
                            for (Song s : user.getFavSongs()) {
                                if (s.getName().equals(songName)) {
                                    currentSong = s;
                                }
                            }
                            if (currentSong != null) {
                                //Start new intent to view song
                                Intent i = new Intent(getApplicationContext(), SongDisplayActivity.class);
                                i.putExtra("songTitle", currentSong.getName());
                                i.putExtra("albumCover", currentSong.getPictureUrl());
                                i.putExtra("artistName", currentSong.getArtist());
                                startActivity(i);
                            }
                        }
                    });
                }


            }
        } else {

        }

        TextView tv = (TextView) findViewById(R.id.fullName);
        tv.setText(fullName);
        tv = (TextView) findViewById(R.id.privacy);
        if (privacy) {
            tv.setText("This profile is private!");
        }
        tv = (TextView) findViewById(R.id.userName);
        tv.setText(userName);
    }
}
