package com.mycompany.airtunes;


import android.app.Activity;


import android.app.Activity;
import android.os.*;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mycompany.airtunes.FirebaseCalls;
import com.mycompany.airtunes.Group;
import com.mycompany.airtunes.PlaylistActivity;
import com.mycompany.airtunes.R;
import com.mycompany.airtunes.SearchController;
import com.mycompany.airtunes.User;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.Track;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class SearchGroupActivity extends Activity {

    SearchController sc;
    public static ArrayAdapter<String> queueAdapter;
    public static ArrayList<String> groupNames;
    ListView grouplist;
    //FirebaseCalls fb;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    FirebaseCalls fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlayout);
        sc = new SearchController();


        grouplist = (ListView) findViewById(R.id.listOfGroups);
        groupNames = new ArrayList<String>();
        queueAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, groupNames);
        grouplist.setAdapter(queueAdapter);
        grouplist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String groupName = (String) grouplist.getItemAtPosition(position);
                System.out.println("Clicked on: " + groupName);
                Group group = fb.groups.get(groupName);
                transition(group);
            }
        });

        //Firebase stuff
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();

        //fb.test();
    }

    public void transition(Group group) {
        Intent goToRoom = new Intent(this, PlaylistActivity.class);
        goToRoom.putExtra("Group", group);
        startActivityForResult(goToRoom, SearchButtonActivity_ID);
    }


    // Add group
    public void onCreateButtonClick(View view) {
        String groupName = ((SearchView) findViewById(R.id.userSearch)).getQuery() + "";
        System.out.println("New Group name is: " + groupName);
        //Firebase stuff
        Group newRoom = fb.groups.get(groupName);
        fb.createRoom(newRoom);
        //sc.addGroup(groupName, fb.currentUser.getUsername());
        transition(newRoom);
        System.out.println("Created group");



    }

//    //Firebase test
//    public void testUpdateRoom(Group group) {
//        fb.updateRoom(group);
//    }




    // Request code to create new Activity
    public static final int SearchButtonActivity_ID = 1;

    public void onSearchButtonClick(View view) {
        queueAdapter.clear();
        queueAdapter.notifyDataSetChanged();
        String search = ((SearchView) findViewById(R.id.searchView)).getQuery() + "";



       boolean contains = sc.searchGroup(search, fb.groups);
        if (!contains) {
            System.out.println("Group not found!");
            Context context = getApplicationContext();
            CharSequence text = "Group not found!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }


//        Log.d("Message: ", "Group found: " + group.groupName);
//
//        Intent goToRoom = new Intent(this, PlaylistActivity.class);
//        goToRoom.putExtra("Group", group);
//        startActivityForResult(goToRoom, SearchButtonActivity_ID);
    }

    public void onSearchUserClick(View view) {
        String search = ((SearchView) findViewById(R.id.userSearch)).getQuery() + "";
        User user = sc.searchUser(search, fb.users);
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
