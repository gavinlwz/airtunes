package com.mycompany.airtunes;

import android.os.AsyncTask;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.wrapper.spotify.methods.FeaturedPlaylistsRequest;
import com.wrapper.spotify.methods.PlaylistTracksRequest;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.FeaturedPlaylists;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.PlaylistTracksInformation;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.Track;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;

/**
 * Created by akashsubramanian on 3/4/16.
 */
class RetrieveStuff extends AsyncTask<String, Void, String> {

    private Exception exception;


    protected String doInBackground(String... query) {
        // Syste("kajsdhkajhsd", "QUERY = "+query);
        //System.out.println("QUERY = " + query);

        for (String s : query) {
            System.out.println(s);
        }
        if (query[query.length - 1].equals("track")) {
            StringBuilder songName = new StringBuilder();
            for (int i = 0; i < query.length - 1; i++) {
                String q = query[i];
                songName.append(q);
            }
            System.out.println();
            final TrackSearchRequest requestquery = MainActivity.api.searchTracks(songName.toString()).market("US").offset(0).limit(1).build();
            Page<Track> trackSearchResult = null;
            try {
                trackSearchResult = requestquery.get();
                System.out.println("I got " + trackSearchResult.getTotal() + " results!");
//            for (Track item : trackSearchResult.getItems()) {
//                System.out.println(item.getUri());
//            }
            } catch (Exception e) {
                System.out.println("Something went wrong!" + e.getMessage());
            }
            Track track = new Track();
            if (trackSearchResult.getItems().size() > 0) {

                //final TrackRequest request = MainActivity.api.getTrack("0eGsygTp906u18L0Oimnem").build();
                String[] uri = trackSearchResult.getItems().get(0).getUri().split(":");
                final TrackRequest request = MainActivity.api.getTrack(uri[2]).build();
                try {
                    track = request.get();
                    track.setUri(trackSearchResult.getItems().get(0).getUri());
                    System.out.println("Retrieved track " + track.getName());
                    System.out.println("Its popularity is " + track.getPopularity());
                    //PlaylistActivity.queue.add(track);
                    Song song = new Song(track.getUri(), track.getName(), track.getArtists().get(0).getName(), null);
                    PlaylistActivity.model.addSong(song);
                    PlaylistActivity.fb.updateRoomSongs(PlaylistActivity.model);
                    PlaylistActivity.songNames.add(track.getName());
//                PlaylistActivity.queueAdapter.notifyDataSetChanged();

                    //PlaylistActivity.queueSongs.add(track.getName());

                    MainActivity.mPlayer.queue(track.getUri());


                    if (track.isExplicit()) {
                        System.out.println("This track is explicit!");
                    } else {
                        System.out.println("It's OK, this track isn't explicit.");
                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong!" + e.getMessage());
                }
            }
        }

        if (query[query.length - 1].equals("random")) {
            System.out.println("Generating random song");
//            StringBuilder songName = new StringBuilder();
//            for (int i = 0; i < query.length-1; i++) {
//                String q = query[i];
//                songName.append(q);
//            }

            final FeaturedPlaylistsRequest fprequest = MainActivity.api.getFeaturedPlaylists().country("US").build();
            try {
                FeaturedPlaylists fpresults = fprequest.get();
                System.out.println("Number of featured playlists = " + fpresults.getPlaylists().getTotal());
                Random r = new Random();
                int n = r.nextInt(fpresults.getPlaylists().getTotal());

                SimplePlaylist p = fpresults.getPlaylists().getItems().get(n);
                p.getTracks();
                System.out.println("playlist name = " + p.getName());

                System.out.println("playlist owner = " + p.getOwner().getId());
                System.out.println("playlist id = " + p.getId());
                System.out.println("playlist uri = " + p.getUri());
                //MainActivity.api.
                AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(MainActivity.clientId,
                        AuthenticationResponse.Type.TOKEN,
                        MainActivity.redirectURI);
                builder.setScopes(new String[]{"user-read-private", "streaming", "user-read-birthdate", "user-read-email", "user-read-private", "playlist-modify-private"});
                AuthenticationRequest request = builder.build();

                //MainActivity.api.setAccessToken("BQDzXIddV1mXZyGIbSiC2LZM43iGpsTlT-KYgk-n7zXy85YeOXVZqPoWG3gBYRmjoYiYrKFIGD2KBKui0EVJoYS1qQPRsbf7kNoKXlw6_oJ3NpKbR5f3Ih_ygiDJ_DrxWGfUM-kBZEqQjN0FeVM");
//                HttpURLConnection urlConnection = null;
//                URL url = null;
//                JSONObject object = null;
//                InputStream inStream = null;
//                try {
//                    url = new URL("https://accounts.spotify.com/authorize/?client_id=" +
//                            MainActivity.clientId + "&response_type=code&redirect_uri=" + MainActivity.redirectURI);
//
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.setDoOutput(false);
//                    urlConnection.setDoInput(true);
//                    urlConnection.connect();
//                    inStream = urlConnection.getInputStream();
//                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
//                    String temp, response = "";
//                    while ((temp = bReader.readLine()) != null) {
//                        response += temp;
//                       // System.out.println("hallo " + temp);
//                    }
//                    object = (JSONObject) new JSONTokener(response).nextValue();
//
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (ProtocolException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
//                    System.out.println("I am done with async");
//                    if (inStream != null) {
//                        try {
//                            // this will close the bReader as well
//                            inStream.close();
//                        } catch (IOException ignored) {
//                        }
//                    }
//                    if (urlConnection != null) {
//                        urlConnection.disconnect();
//                    }
//                }

                System.out.println("refreshed token");
                final PlaylistTracksRequest ptrequest = MainActivity.api.getPlaylistTracks(p.getOwner().getId(), p.getId()).build();
                System.out.println("success");
                Page<PlaylistTrack> ptracks = ptrequest.get();
                System.out.println("Number of tracks = " + ptracks.getTotal());
                n = r.nextInt(ptracks.getTotal());
                PlaylistTrack t = ptracks.getItems().get(n);
                Track track = t.getTrack();
                System.out.println("track name = " + track.getName());
                Song song = new Song(track.getUri(), track.getName(), track.getArtists().get(0).getName(), null);
                if (PlaylistActivity.currentSong == null) {
                    PlaylistActivity.currentSong = song;
                }
                PlaylistActivity.model.addSong(song);
                PlaylistActivity.fb.updateRoomSongs(PlaylistActivity.model);
                PlaylistActivity.songNames.add(track.getName());
                MainActivity.mPlayer.queue(track.getUri());
            } catch (Exception e) {
                System.out.println(e);
            }


        }


        return "";
    }
}


