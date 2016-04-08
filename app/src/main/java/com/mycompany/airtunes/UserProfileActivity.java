package com.mycompany.airtunes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.view.inputmethod.EditorInfo;

import com.firebase.client.Firebase;
import com.mycompany.airtunes.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.widget.Button;
import android.provider.MediaStore;

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

    EditText editText;
    TextView textView;

    ImageView picture;
    Button upload;

    private static final int RESULT_LOAD_IMAGE = 1;


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

        picture = (ImageView) findViewById(R.id.uploadProfilePic);
        upload = (Button) findViewById(R.id.uploadProfilePicButton);

       // picture.setOnClickListener(this);

//        editText = (EditText) findViewById(R.id.username);
//        textView = (TextView) findViewById(R.id.textuser);
//        textView.setText(editText.getText());
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                boolean handled = false;
//                if (i == EditorInfo.IME_ACTION_SEND) {
//
//                }
//                return handled;
//            }
//        });
    }


    public void changeUsername(View v) {
        editText = (EditText) findViewById(R.id.username);
        textView = (TextView) findViewById(R.id.textuser);
        textView.setText(editText.getText());
    }

    public void uploadNewPicture(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int request, int result, Intent i) {
        super.onActivityResult(request, result, i);
        if (request == RESULT_LOAD_IMAGE && result == RESULT_OK) {
            Uri img = i.getData();
            picture.setImageURI(img);
        }
    }


    public void launchSearch(View v) {
        Intent i = new Intent(getApplicationContext(), SearchUserActivity.class);
        startActivity(i);
    }

    public void launchGroupSearch(View v) {
        Intent i = new Intent(getApplicationContext(), SearchGroupActivity.class);
        startActivity(i);
    }

    public void logout(View v) {
        MainActivity.logout(v);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public void onGoToGroupButtonClick(View view) {
        Intent i = new Intent(getApplicationContext(), SearchGroupActivity.class);
        startActivity(i);
    }

    public void onGoToProfileButtonClick(View view) {
        Intent i = new Intent(getApplicationContext(), SearchUserActivity.class);
        startActivity(i);
    }

    public void viewFavSongs(View v) {
        Intent i = new Intent(getApplicationContext(), FavoriteSongsDisplay.class);
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
