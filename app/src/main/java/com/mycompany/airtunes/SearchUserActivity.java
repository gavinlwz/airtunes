package com.mycompany.airtunes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
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

import com.firebase.client.Firebase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Activity class for searching users
 * */
public class SearchUserActivity extends ActionBarActivity {
    SearchController sc;
    public static ArrayAdapter<String> queueAdapter;
    public static ArrayList<String> userNames;
    ListView userlist;
    FirebaseCalls fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        // Sets up logic and view for table of groups
        sc = new SearchController();
        userlist = (ListView) findViewById(R.id.listOfUsers);
        userNames = new ArrayList<>();

        // Set up queueAdapater backer for the table view
        queueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        userlist.setAdapter(queueAdapter);
        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userName = (String) userlist.getItemAtPosition(position);
                User user = fb.users.get(userName);
                transition(user);
            }
        });

        // Set up Firebase context
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();

        // Set up event listners
    }

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

    /**
     * Transitions to new group
     * @param user User
     * */
    public void transition(User user) {
        Intent i = new Intent(getApplicationContext(), UserSearchResultActivity.class);
        i.putExtra("firstName", user.getFirstName());
        i.putExtra("lastName", user.getLastName());
        i.putExtra("privacy", user.getPrivacy());
        i.putExtra("userName", user.getUsername());
        i.putExtra("user", user);
        startActivity(i);
    }

    /**
     * Search for User
     * @param view View
     * */
    public void onSearchUserClick(View view) {
        // Clear array of results
        queueAdapter.clear();
        queueAdapter.notifyDataSetChanged();
        String search = ((SearchView) findViewById(R.id.userSearch)).getQuery() + "";
        boolean contains = sc.searchUser(search, fb.users);

        // Show alert if group is not found
        if (!contains) {
            Context context = getApplicationContext();
            CharSequence text = "User not found!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
    }
}
