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
    public static final int SearchButtonActivity_ID = 1; //request code to create new Activity
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
            i.putExtra("firstName", user.getName());
            i.putExtra("privacy", user.getPrivacy());
            startActivity(i);

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
    }
}
