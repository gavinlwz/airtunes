package com.mycompany.airtunes;

import android.app.Activity;
import android.os.Handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Activity class for searching groups
 * */
public class SearchGroupActivity extends ActionBarActivity {
    public static final int SearchButtonActivity_ID = 1; // Request code to create new Activity
    SearchController sc;
    public static ArrayAdapter<String> queueAdapter;
    public static ArrayList<String> groupNames;
    ListView grouplist;
    FirebaseCalls fb;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);

        // Sets up logic and view for table of groups
        sc = new SearchController();
        grouplist = (ListView) findViewById(R.id.listOfGroups);
        groupNames = new ArrayList<>();

        // Set up queueAdapater backer for the table view
        queueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groupNames);
        grouplist.setAdapter(queueAdapter);
        grouplist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupName = (String) grouplist.getItemAtPosition(position);
                Group group = fb.groups.get(groupName);
                transition(group);
            }
        });

        // Set up Firebase context
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();

        // Set up event listners
        mHandler = new Handler();
        this.startRepeatingTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group, menu);
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

    /**
     * Starts checking for invites sent from the DJ of a private group
     * */
    Runnable inviteChecker = new Runnable() {
        @Override
        public void run() {
            try {
                goToGroupInvitedTo(); // This function can change value of mInterval
            } finally {
                // Even if update method throws an exception, this will happen
                mHandler.postDelayed(inviteChecker, 4000);
            }
        }
    };

    /**
     * Runs the invitation checker
     * */
    void startRepeatingTask() {
        inviteChecker.run();
    }

    /**
     * Transitions to the room that User is invited to
     * */
    void goToGroupInvitedTo() {
        if (fb.testGroup != null) {
            Intent goToRoom = new Intent(this, PlaylistActivity.class);
            goToRoom.putExtra("Group", fb.testGroup);
            fb.testGroup = null;
            startActivityForResult(goToRoom, SearchButtonActivity_ID);
        }
    }

    /**
     * Transitions to new group
     * @param group Group
     * */
    public void transition(Group group) {
        Intent goToRoom = new Intent(this, PlaylistActivity.class);
        goToRoom.putExtra("Group", group);
        startActivityForResult(goToRoom, SearchButtonActivity_ID);
    }

    /**
     * Create new group
     * @param view View
     * */
    public void onCreateButtonClick(View view) {
        String groupName = ((SearchView) findViewById(R.id.searchView)).getQuery() + "";
        if (fb.groups.containsKey(groupName)) {
            //Show alert that group already exists if the names are the same
            Context context = getApplicationContext();
            CharSequence text = "A group with that name already exists!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            // Adds new group to firebase if no existing groups with same name found
            Group g = new Group(groupName, fb.currentUser.getUsername(), false);
            // Update Firebase
            fb.groups.put(groupName, g);
            fb.createRoom(g);
            // Transition to group page
            transition(g);
        }
    }

    /**
     * Search for existing group
     * @param view View
     * */
    public void onSearchButtonClick(View view) {
        // Clear array of results
        queueAdapter.clear();
        queueAdapter.notifyDataSetChanged();
        String search = ((SearchView) findViewById(R.id.searchView)).getQuery() + "";
        boolean contains = sc.searchGroup(search, fb.groups);
        // Show alert if group is not found
        if (!contains) {
            Context context = getApplicationContext();
            CharSequence text = "Group not found!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
    }
}
