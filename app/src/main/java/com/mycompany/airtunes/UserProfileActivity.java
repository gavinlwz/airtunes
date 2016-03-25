package com.mycompany.airtunes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;
import com.mycompany.airtunes.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserProfileActivity extends ActionBarActivity {
    private String fullName;
    private String username; // this is the spotify user uri
    private String accountType;
    private String profilePic;
    String id;
    private Drawable drawable;

    private static FirebaseCalls fb;
    User me;

    private boolean privacy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("fullName");
            System.out.println("full name is hallo there : " + fullName);
            accountType = extras.getString("accountType");
            profilePic = extras.getString("profilePic");
            username = extras.getString("username");
            id = extras.getString("id");
        }
            //me = new User(fullName, username, id);




        TextView tv = (TextView) findViewById(R.id.fullName);
        tv.setText(MainActivity.fullName);


        new RetrieveFeedTask().execute();

        ToggleButton toggle = (ToggleButton) findViewById(R.id.privacyToggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    System.out.println("it is now true");
                    privacy = true;

                } else {
                    System.out.println("it is now false");
                    privacy = false;
                }
            }
        });




    }

    public void launchSearch(View v) {
        Intent i = new Intent(getApplicationContext(), SearchUserActivity.class);
        startActivity(i);

    }

    public void logout(View v) {
        MainActivity.logout(v);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);

    }




    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            // Do some validation here


            try {
                URL url = null;
                url = new URL(MainActivity.profilePic);
                InputStream content = (InputStream)url.getContent();
                Drawable d = Drawable.createFromStream(content , "src");
                drawable = d;


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            ImageView iv = (ImageView) findViewById(R.id.profilePic);
            iv.setImageDrawable(drawable);
        }
    }

}
