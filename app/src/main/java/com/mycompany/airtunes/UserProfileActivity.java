package com.mycompany.airtunes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String accountType;
    private String profilePic;
    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("fullName");
            accountType = extras.getString("accountType");
            profilePic = extras.getString("profilePic");

        }

        TextView tv = (TextView) findViewById(R.id.fullName);
        tv.setText(MainActivity.fullName);

        tv = (TextView) findViewById(R.id.accountType);
        tv.setText(MainActivity.accountType);
        new RetrieveFeedTask().execute();




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
