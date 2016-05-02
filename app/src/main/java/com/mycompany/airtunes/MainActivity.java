package com.mycompany.airtunes;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.client.Firebase;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.wrapper.spotify.Api;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Main Activity class -- information regarding user / client
 * */
public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {
    static final String CLIENT_ID = "669bf6828431431c8b5c90f729921077";
    static final String CLIENT_SECRET = "06d91d09593e46a78ca86fe7a118d10d";
    static final String REDIRECT_URI = "airtunes-login://callback";
    static final int REQUEST_CODE = 1337;
    public static String accessToken;
    public static String fullName;
    public static String accountType;
    public static String profilePic;
    public static String username;
    public static String id;
    public static FirebaseCalls fb;
    public static Player mPlayer;
    final static Api api = Api.builder()
            .clientId(CLIENT_ID)
            .clientSecret(CLIENT_SECRET)
            .redirectURI(REDIRECT_URI)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("frings");

        // Set up Firebase
        Firebase.setAndroidContext(this);
        fb = FirebaseCalls.getInstance();

        // Set up authentication on login
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming", "user-read-birthdate",
                "user-read-email", "user-read-private", "playlist-modify-private"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                accessToken = response.getAccessToken();
                api.setAccessToken(accessToken);

                // Retrieve user asynchronously
                new RetrieveFeedTask().execute();

                // Retrieve music player from Spotify
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer = player;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Couldn't init player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    /**
     * Goes to SearchGroupActivity page to search for rooms to join
     * @param view View
     * */
    public void onGoToGroupButtonClick(View view) {
        Intent i = new Intent(getApplicationContext(), SearchGroupActivity.class);
        startActivity(i);
    }

    /**
     * Goes to SearchUserActivity page to search for other users
     * @param view View
     * */
    public void onGoToProfileButtonClick(View view) {
        Intent i = new Intent(getApplicationContext(), SearchUserActivity.class);
        startActivity(i);
    }

    /**
     * Allows logout
     * @param view View
     * */
    public static void logout(View view) {
        AuthenticationClient.clearCookies(view.getContext());
    }

    /**
     * Goes to UserProfileActivity page to look at own profile
     * @param view View
     * */
    public void onViewMyProfileClick(View view) {
        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        i.putExtra("fullName", fullName);
        i.putExtra("accountType", accountType);
        i.putExtra("profilePic", profilePic);
        i.putExtra("username", username);
        i.putExtra("id", id);
        startActivity(i);
    }

    /**
     * Create and setup currentUser in app as well as firebase
     * @param username String
     * @param userID String
     * */
    public void createCurrentUser(String username, String userID) {
        User currentUser = new User(username, userID);
        int count = 0;
        // If user exists in database, set current user to existing User object
        for (String u : fb.users.keySet()) {
            if (u.equals(userID)) {
                count = 1;
                currentUser = fb.users.get(userID);
                break;
            }
        }
        // If user doesn't exist in database, create the user in Firebase
        if (count == 0) {
            fb.createUser(currentUser);
        }
        fb.currentUser = currentUser;
    }

    @Override
    public void onLoggedIn() { Log.d("MainActivity", "User logged in"); }

    @Override
    public void onLoggedOut() { Log.d("MainActivity", "User logged out"); }

    @Override
    public void onLoginFailed(Throwable error) { Log.d("MainActivity", "Login failed"); }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    // This class retrieves user profile from Spotify and uses the info to update Users in Firebase
    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... urls) {
            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            try {
                // Create URL Connection and Update name fields
                createURLConnectionAndUpdateFields(urlConnection,url, object,inStream);

                // Create Current User based on updated name fields
                createCurrentUser(username, id);
                // Redirect to Premium Account sign up page
                accountType = (String) object.get("product");
                if (!accountType.equals("premium")) {
                    Intent i = new Intent(getApplicationContext(), PremiumRedirectActivity.class);
                    startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        // this will close the bReader as well
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }

    private void createURLConnectionAndUpdateFields(HttpURLConnection urlConnection,
                                                    URL url, JSONObject object,
                                                    InputStream inStream) throws Exception {
        url = new URL("https://api.spotify.com/v1/me");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
        urlConnection.setDoOutput(false);
        urlConnection.setDoInput(true);
        urlConnection.connect();
        inStream = urlConnection.getInputStream();

        // Read in response from urlConnection
        BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
        String temp, response = "";
        while ((temp = bReader.readLine()) != null) { response += temp; }
        object = (JSONObject) new JSONTokener(response).nextValue();
        if (object.get("display_name") == null) {
            fullName = "unknown";
        } else {
            fullName = object.get("display_name").toString();
        }

        // Create and set currentUser of app
        username = (String) object.get("email");
        id = (String) object.get("id");
    }
}

