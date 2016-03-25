package com.mycompany.airtunes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
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
import com.wrapper.spotify.methods.AlbumRequest;
import com.wrapper.spotify.methods.CurrentUserRequest;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.Album;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Image;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.models.Track;
//import com.wrapper.spotify.models.User;
import com.mycompany.airtunes.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "669bf6828431431c8b5c90f729921077";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "airtunes-login://callback";
    //private static final String CLIENT_SECRET = "06d91d09593e46a78ca86fe7a118d10d";
    private static final int REQUEST_CODE = 1337;
    private static String access_token;
    private static String refresh_token;
    private final String USER_AGENT = "Mozilla/5.0";

    public static String fullName;
    public static String accountType;
    public static String profilePic;


    private static final String CLIENT_SECRET = "06d91d09593e46a78ca86fe7a118d10d";





    public static Player mPlayer;

    final static String clientId = "669bf6828431431c8b5c90f729921077";
    final static String clientSecret = "06d91d09593e46a78ca86fe7a118d10d";
    final static String redirectURI = "airtunes-login://callback";

    final static Api api = Api.builder()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .redirectURI(redirectURI)
            .build();

    /* Set the necessary scopes that the application will need from the user */
    final List<String> scopes = Arrays.asList("user-read-private", "user-read-email");

    /* Set a state. This is used to prevent cross site request forgeries. */
    final String state = "someExpectedStateString";

    String authorizeURL = api.createAuthorizeURL(scopes, state);

    //Firebase stuff
    Firebase myFirebaseRef;
    User testUser;
    Group testRoom;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Hallo", "hi Wai");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming", "user-read-birthdate", "user-read-email", "user-read-private", "playlist-modify-private"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


//
//        // Testing code
//        testUser = new User("Wai", "Wu", "ihugacownow");
//        Firebase userRef = myFirebaseRef.child("users");
//        Firebase userIDOne = userRef.child("1");
//        System.out.println("Just before saving data");
//        userIDOne.setValue(testUser,  new Firebase.CompletionListener() {
//            @Override
//            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                if (firebaseError != null) {
//                    System.out.println("Data could not be saved. " + firebaseError.getMessage());
//                } else {
//                    System.out.println("Data saved successfully.");
//                }
//            }
//        });







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("goodness", "graciousme");

        System.out.println(resultCode);


        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                access_token = response.getAccessToken();
                new RetrieveFeedTask().execute();


                api.setAccessToken(access_token);

                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);

                Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer = player;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        //mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");

                    }


                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }

        }





    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("goodness", "graciousme");
        String code = "";


        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                access_token = response.getAccessToken();
                System.out.println("codeeeeeeee " + response.getCode());
                code = response.getCode();

                api.setAccessToken(access_token);

                System.out.println("access_token " + access_token);
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);

                Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer = player;
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        //mPlayer.play("spotify:track:2TpxZ7JUBn3uw46aR7qd6V");

                    }


                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
            final CurrentUserRequest request = api.getMe().build();

            try {
                final User user = request.get();

                System.out.println("Display name: " + user.getDisplayName());
                System.out.println("Email: " + user.getEmail());


                System.out.println("This account is a " + user.getProduct() + " account");
            } catch (Exception e) {
                System.out.println("Something went wrong!" + e.getMessage());
            }
        }
    }*/




    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");


    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

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

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {

        }

        protected String doInBackground(Void... urls) {
            // Do some validation here

            HttpURLConnection urlConnection = null;
            URL url = null;
            JSONObject object = null;
            InputStream inStream = null;
            try {
                url = new URL("https://api.spotify.com/v1/me");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.setRequestProperty("Authorization", "Bearer " + access_token);
                urlConnection.setDoOutput(false);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                while ((temp = bReader.readLine()) != null) {
                    response += temp;
                    System.out.println("hallo " + temp);
                }
                object = (JSONObject) new JSONTokener(response).nextValue();
                fullName = (String) object.get("display_name");
                accountType = (String) object.get("product");
                profilePic = ((JSONArray) object.get("images")).getJSONObject(0).getString("url");
                System.out.println("my full name is " + fullName);
                System.out.println("my account type is " + accountType);
                System.out.println("my uri is " + profilePic);

                if (accountType.equals("premium")) {
                    Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                    i.putExtra("fullName", fullName);
                    i.putExtra("accountType", accountType);
                    i.putExtra("profilePic", profilePic);
                    startActivity(i);
                } else {
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






    /**
     * Created by akashsubramanian on 3/4/16.
     */

}

