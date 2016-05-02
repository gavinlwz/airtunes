package com.mycompany.airtunes;

import android.content.Context;
import android.os.AsyncTask;


import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.wrapper.spotify.methods.FeaturedPlaylistsRequest;
import com.wrapper.spotify.methods.PlaylistTracksRequest;
import com.wrapper.spotify.methods.TrackRequest;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.FeaturedPlaylists;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.Track;

import java.util.List;
import java.util.Random;

/**
 * RetrieveSongs class
 * */
class RetrieveSongs extends AsyncTask<String, Void, String> {
    private Exception exception;

    /**
     * @param query String...
     * */
    protected String doInBackground(String... query) {
        if (query[query.length - 1].equals("track")) {
            searchSpecificTrack(query);
        }

        if (query[query.length - 1].equals("random")) {
            System.out.println("Generating random song");
            searchRandomSong();
        }
        return "";
    }


    // query spotify based on specific track request by user
    private void searchSpecificTrack(String[] query) {
        // store user query in string
        StringBuilder songName = new StringBuilder();
        for (int i = 0; i < query.length - 1; i++) {
            String q = query[i];
            songName.append(q);
        }
    // spotify api call based on user query
        TrackSearchRequest.Builder trackSearch = MainActivity.api.searchTracks(songName.toString());
        final TrackSearchRequest requestquery = trackSearch.market("US").offset(0).limit(1).build();
        Page<Track> trackSearchResult = null;

        try {
            trackSearchResult = requestquery.get();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong!" + e.getMessage());
        }

        Track track;
        List<Track> tracks = trackSearchResult.getItems();
        if (tracks != null && tracks.size() > 0) {
            // track found
            String firstTrackUri = tracks.get(0).getUri();
            String[] uri = firstTrackUri.split(":");
            final TrackRequest request = MainActivity.api.getTrack(uri[2]).build();

            try {
                track = request.get();
                track.setUri(firstTrackUri);
                SimpleArtist artist = track.getArtists().get(0);
                String artistName = artist.getName();
                String trackuri = track.getUri();
                Song song = new Song(trackuri, track.getName(), artistName, null);
                if (track.isExplicit()) {
                    song.setExplicit(true); //setting song explicity to true.
                }
                // add song to room's playlist
                PlaylistActivity.model.addSong(song);
                // update room songs on firebase
                PlaylistActivity.fb.updateRoomSongs(PlaylistActivity.model);
            } catch (Exception e) {
                System.out.println("Something went wrong!" + e.getMessage());
            }
        }
    }

    // generate a "random" song. This is done by randomly picking one of the featured playlists,
    // and then picking a song from that playlist at random
    private void searchRandomSong() {

        //randomly pick a featured playlist
        final FeaturedPlaylistsRequest fprequest = MainActivity.api.
                getFeaturedPlaylists().country("US").build();
        try {
            FeaturedPlaylists fpresults = fprequest.get();
            Random r = new Random();
            Page<SimplePlaylist> playlists = fpresults.getPlaylists();
            int n = r.nextInt(playlists.getTotal());
            SimplePlaylist p = playlists.getItems().get(n);

            // now pick a random track from this playlist
            p.getTracks();
            AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                    MainActivity.CLIENT_ID, AuthenticationResponse.Type.TOKEN,
                    MainActivity.REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private", "streaming",
                    "user-read-birthdate", "user-read-email", "user-read-private",
                    "playlist-modify-private"});
            AuthenticationRequest request = builder.build();
            System.out.println("refreshed token");
            final PlaylistTracksRequest ptrequest = MainActivity.api.getPlaylistTracks(p.getOwner()
                    .getId(), p.getId()).build();
            Page<PlaylistTrack> ptracks = ptrequest.get();
            n = r.nextInt(ptracks.getTotal());
            PlaylistTrack t = ptracks.getItems().get(n);
            Track track = t.getTrack();
            String trackuri = track.getUri();
            Song song = new Song(trackuri, track.getName(), track.getArtists().get(0)
                    .getName(), null);
            if (PlaylistActivity.currentSong == null) {
                PlaylistActivity.currentSong = song;
            }
            if (track.isExplicit()) {
                song.setExplicit(true);
            }
            PlaylistActivity.model.addSong(song);
            PlaylistActivity.fb.updateRoomSongs(PlaylistActivity.model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


