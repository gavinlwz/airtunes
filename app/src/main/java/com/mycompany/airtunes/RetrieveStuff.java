package com.mycompany.airtunes;

import android.os.AsyncTask;
import android.util.Log;

import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.Track;

import org.json.JSONObject;

/**
 * Created by akashsubramanian on 3/4/16.
 */
class RetrieveStuff extends AsyncTask<String, Void, Track> {

    private Exception exception;

    protected Track doInBackground(String... query) {
       // Syste("kajsdhkajhsd", "QUERY = "+query);
        //System.out.println("QUERY = " + query);
        System.out.println("QUERY");
        StringBuilder songName = new StringBuilder();
        for (String q : query) {
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


        return track;
    }




}
