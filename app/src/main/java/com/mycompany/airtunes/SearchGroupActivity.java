package com.mycompany.airtunes;

import android.app.Activity;
import android.os.Handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class SearchGroupActivity extends Activity {
    public static final int SearchButtonActivity_ID = 1; //request code to create new Activity
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

        sc = new SearchController();
        grouplist = (ListView) findViewById(R.id.listOfGroups);
        groupNames = new ArrayList<>();
        queueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groupNames);
        grouplist.setAdapter(queueAdapter);
        grouplist.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String groupName = (String) grouplist.getItemAtPosition(position);
                //System.out.println("Clicked on: " + groupName);
                Group group = fb.groups.get(groupName);
                //System.out.println("Transitioning with group :" +  group );
                transition(group);
            }
        });

        //firebase
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();

        mHandler = new Handler();
        this.startRepeatingTask();
    }

    //WC code
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus(); //this function can change value of mInterval
            } finally {
                //even if update method throws an exception, this will happen
                mHandler.postDelayed(mStatusChecker, 4000);
            }
        }
    };


    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void updateStatus() {
        if (fb.testGroup != null) {
            System.out.println("FB test group not null");
            Intent goToRoom = new Intent(this, PlaylistActivity.class);
            goToRoom.putExtra("Group", fb.testGroup);
            fb.testGroup = null;
            startActivityForResult(goToRoom, SearchButtonActivity_ID);
        }
    }

    public void transition(Group group) {
        Intent goToRoom = new Intent(this, PlaylistActivity.class);
        goToRoom.putExtra("Group", group);
        startActivityForResult(goToRoom, SearchButtonActivity_ID);
    }


    //add new group
    public void onCreateButtonClick(View view) {

        String groupName = ((SearchView) findViewById(R.id.searchView)).getQuery() + "";
        if (fb.groups.containsKey(groupName)) {
            Context context = getApplicationContext();
            CharSequence text = "A group with that name already exists!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Group g = new Group(groupName, fb.currentUser.getUsername(), false);
            fb.groups.put(groupName, g);
            fb.createRoom(g);
            transition(g);
            System.out.println("Created group");
        }
    }



    //search for existing group
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
        } else {
            System.out.println("groups have been found");

        }
    }

    public void onSearchUserClick(View view) {
        String search = ((SearchView) findViewById(R.id.userSearch)).getQuery() + "";
        User user = sc.searchUser(search, fb.users);
        Context context = getApplicationContext();

        if (user == null) {
            //System.out.println("User not found!");
            Toast toast = Toast.makeText(context, "User not found!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            CharSequence text = "User found: " + user.getFirstName() + " " + user.getLastName();

            Intent i = new Intent(getApplicationContext(), UserSearchResultActivity.class);
            i.putExtra("firstName", user.getFirstName());
            i.putExtra("lastName", user.getLastName());
            i.putExtra("privacy", user.getPrivacy());
            startActivity(i);

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
    }
}
