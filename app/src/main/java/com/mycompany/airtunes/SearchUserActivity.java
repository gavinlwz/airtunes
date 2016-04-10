package com.mycompany.airtunes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class SearchUserActivity extends ActionBarActivity {
    SearchController sc;
    FirebaseCalls fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        sc = new SearchController();
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();
    }


=    // Search for User
    public void onSearchUserClick(View view) {
        String search = ((SearchView) findViewById(R.id.userSearch)).getQuery() + "";
        User user = sc.searchUser(search, fb.users);
        Context context = getApplicationContext();

        if (user == null) {
            // If user not found, show alert
            Toast toast = Toast.makeText(context, "User not found!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {



            // If user found, transit to UserSearchResultActivity
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
