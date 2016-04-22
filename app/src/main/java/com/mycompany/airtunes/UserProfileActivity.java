package com.mycompany.airtunes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;

import java.io.InputStream;
import java.net.URL;
import android.widget.Button;
import android.provider.MediaStore;
/**
 * Activity class that dynamically creates view for user profiles
 * */
public class UserProfileActivity extends ActionBarActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private String fullName;
    private String username; //spotify user uri
    private String accountType; //premium account
    private String profilePic; //url grabbed from facebook
    String id;
    private Drawable drawable;

    private static FirebaseCalls fb;
    User me;

    private boolean privacy;

    EditText editText;
    TextView textView;

    ImageView picture;
    Button upload;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();

        //Check for user info passed into Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fullName = extras.getString("fullName");
            accountType = extras.getString("accountType");
            profilePic = extras.getString("profilePic");
            username = extras.getString("username");
            id = extras.getString("id");
        }
        TextView tv = (TextView) findViewById(R.id.fullName);
        tv.setText(MainActivity.fullName);

        new RetrieveFeedTask().execute();

        ToggleButton toggle = (ToggleButton) findViewById(R.id.privacyToggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    privacy = true;

                } else {
                    privacy = false;
                }
            }
        });

        picture = (ImageView) findViewById(R.id.uploadProfilePic);
        upload = (Button) findViewById(R.id.uploadProfilePicButton);
    }

    //Allows user to change username display
    public void changeUsername(View v) {
        editText = (EditText) findViewById(R.id.username);
        textView = (TextView) findViewById(R.id.textuser);
        textView.setText(editText.getText());
    }

    //Allows user to upload new profile picture from uploads
    public void uploadNewPicture(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    //Handles events following proper login, like grabbing profile pic
    @Override
    public void onActivityResult(int request, int result, Intent i) {
        super.onActivityResult(request, result, i);
        if (request == RESULT_LOAD_IMAGE && result == RESULT_OK) {
            Uri img = i.getData();
            picture.setImageURI(img);
        }
    }

    //Takes user to view where they can search for other users
    public void launchSearch(View v) {
        Intent i = new Intent(getApplicationContext(), SearchUserActivity.class);
        startActivity(i);
    }

    //Takes user to view where they can search for groups
    public void launchGroupSearch(View v) {
        Intent i = new Intent(getApplicationContext(), SearchGroupActivity.class);
        startActivity(i);
    }

    //Handles logout functionality
    public void logout(View v) {
        MainActivity.logout(v);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    //Takes user to appropriate playlist
    public void onGoToGroupButtonClick(View view) {
        Intent i = new Intent(getApplicationContext(), SearchGroupActivity.class);
        startActivity(i);
    }

    //Takes user to view of profile
    public void onGoToProfileButtonClick(View view) {
        Intent i = new Intent(getApplicationContext(), SearchUserActivity.class);
        startActivity(i);
    }

    //Takes user to view of favorite songs list
    public void viewFavSongs(View v) {
        Intent i = new Intent(getApplicationContext(), FavoriteSongsDisplayActivity.class);
        startActivity(i);
    }

    //Retrieves the Spotify user account feed
    //Async to prevent locking of main UI thread
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private Exception exception;

        protected void onPreExecute() { }

        protected String doInBackground(Void... urls) {
            try {
                //Grabs user profile picture
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

    public void onTogglePrivacyButtonClick(View view) {
        fb.currentUser.privacy = !fb.currentUser.privacy;
        fb.toggleUserPrivacy(fb.currentUser);
    }


}
