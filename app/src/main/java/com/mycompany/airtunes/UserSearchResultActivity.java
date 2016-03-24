package com.mycompany.airtunes;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.mycompany.airtunes.R;

public class UserSearchResultActivity extends ActionBarActivity {
    private String fullName;
    private String privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search_result);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String firstName = extras.getString("firstName");
            String lastName = extras.getString("lastName");
            fullName = firstName + lastName;
            privacy = extras.getString("privacy");

        }

        TextView tv = (TextView) findViewById(R.id.fullName);
        tv.setText(fullName);

        tv = (TextView) findViewById(R.id.privacy);
        tv.setText(privacy);
    }

}
