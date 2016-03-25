package com.mycompany.airtunes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mycompany.airtunes.R;

import java.util.ArrayList;

public class SearchUserActivity extends ActionBarActivity {

    SearchController sc;
    public static ArrayAdapter<String> queueAdapter;
    public static ArrayList<String> queueSongs;
    ListView playlist;
    FirebaseCalls fb;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        sc = new SearchController();
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();

        /*playlist = (ListView) findViewById(R.id.listOfGroups);
        queueSongs = new ArrayList<String>();
        queueAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, queueSongs);
        playlist.setAdapter(queueAdapter);
        playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String groupName = (String) playlist.getItemAtPosition(position);
                System.out.println("Clicked on: " + groupName);
                Group group = sc.groups.get(groupName);
                transition(group);
            }
        });*/


    }

    public void transition(Group group) {
        Intent goToRoom = new Intent(this, PlaylistActivity.class);
        goToRoom.putExtra("Group", group);
        startActivityForResult(goToRoom, SearchButtonActivity_ID);
    }


    // Add group
    public void onCreateButtonClick(View view) {
        String groupName = ((SearchView) findViewById(R.id.searchView)).getQuery() + "";
        sc.addGroup(groupName, fb.currentUser.getUsername());
        transition(sc.groups.get(groupName));
        System.out.println("Created group");
    }



    // Request code to create new Activity
    public static final int SearchButtonActivity_ID = 1;


    public void onSearchUserClick(View view) {
        String search = ((SearchView) findViewById(R.id.userSearch)).getQuery() + "";
        User user = sc.searchUser(search);
        if (user == null) {
            System.out.println("User not found!");
            Context context = getApplicationContext();
            CharSequence text = "User not found!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        } else {
            Context context = getApplicationContext();
            CharSequence text = "User found: " + user.getFirstName() + " " + user.getLastName();
            int duration = Toast.LENGTH_SHORT;

            Intent i = new Intent(getApplicationContext(), UserSearchResultActivity.class);
            i.putExtra("firstName", user.getFirstName());
            i.putExtra("lastName", user.getLastName());
            i.putExtra("privacy", user.getPrivacy());
            startActivity(i);

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

    }

}
