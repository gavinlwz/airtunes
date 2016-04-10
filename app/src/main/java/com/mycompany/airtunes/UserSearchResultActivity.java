package com.mycompany.airtunes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
/**
 * Activity class that displays the profile of a discovered user and handles interactivity
 * with that profile by the current user.
 * */
public class UserSearchResultActivity extends ActionBarActivity {
    private String fullName;
    private boolean privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_result);

        //check to see if there are any data passed into activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String firstName = extras.getString("firstName");
            privacy = extras.getBoolean("privacy");
            if (privacy == true) {
                fullName = firstName + " " + firstName.charAt(0);
            } else {
                fullName = firstName;
            }
        }

        TextView tv = (TextView) findViewById(R.id.fullName);
        tv.setText(fullName);
        tv = (TextView) findViewById(R.id.privacy);
        tv.setText("" + privacy);
    }
}
