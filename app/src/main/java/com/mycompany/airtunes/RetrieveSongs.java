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
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.Track;

import java.util.Random;

class RetrieveSongs extends AsyncTask<String, Void, String> {
    private Exception exception;

    protected String doInBackground(String... query) {
        for (String s : query) {
            System.out.println(s);
        }
        if (query[query.length - 1].equals("track")) {
            StringBuilder songName = new StringBuilder();
            for (int i = 0; i < query.length - 1; i++) {
                String q = query[i];
                songName.append(q);
            }
            final TrackSearchRequest requestquery = MainActivity.api.searchTracks(
                    songName.toString()).market("US").offset(0).limit(1).build();
            Page<Track> trackSearchResult = null;
            try {
                trackSearchResult = requestquery.get();
            } catch (Exception e) {
                System.out.println("Something went wrong!" + e.getMessage());
            }
            Track track = new Track();
            if (trackSearchResult.getItems() != null && trackSearchResult.getItems().size() > 0) {
                //final TrackRequest request = MainActivity.api.getTrack("0eGsygTp906u18L0Oimnem").build();
                String[] uri = trackSearchResult.getItems().get(0).getUri().split(":");
                final TrackRequest request = MainActivity.api.getTrack(uri[2]).build();
                try {
                    track = request.get();
                    track.setUri(trackSearchResult.getItems().get(0).getUri());

                    System.out.println("Retrieved track " + track.getName());
                    System.out.println("Its popularity is " + track.getPopularity());

                    Song song = new Song(track.getUri(), track.getName(), track.getArtists().get(0).getName(), null);
                    if (track.isExplicit()) {
                        song.setExplicit(true);
                    }
                    PlaylistActivity.model.addSong(song);
                    PlaylistActivity.fb.updateRoomSongs(PlaylistActivity.model);
                    PlaylistActivity.songNames.add(track.getName());



                    if (track.isExplicit()) {
                        System.out.println("This track is explicit!");
                        if (!PlaylistActivity.model.isPG13) {

                        } else {
//                            System.out.println("toast to explicity");
//                            PlaylistActivity.pl.makeToast("This track is explicit and cannot be added to playlist");
                        }
                    } else {
                        System.out.println("It's OK, this track isn't explicit.");
//                        PlaylistActivity.model.addSong(song);
//                        PlaylistActivity.fb.updateRoomSongs(PlaylistActivity.model);
//                        PlaylistActivity.songNames.add(track.getName());
                    }
                } catch (Exception e) {
                    System.out.println("Something went wrong!" + e.getMessage());
                }
            }
        }

        if (query[query.length - 1].equals("random")) {
            System.out.println("Generating random song");
            final FeaturedPlaylistsRequest fprequest = MainActivity.api.
                    getFeaturedPlaylists().country("US").build();
            try {
                FeaturedPlaylists fpresults = fprequest.get();
                //System.out.println("Featured playlists = " + fpresults.getPlaylists().getTotal());
                Random r = new Random();
                int n = r.nextInt(fpresults.getPlaylists().getTotal());
                SimplePlaylist p = fpresults.getPlaylists().getItems().get(n);
                p.getTracks();
                //System.out.println("playlist name = " + p.getName());
                //System.out.println("playlist owner = " + p.getOwner().getId());
                //System.out.println("playlist id = " + p.getId());
                //System.out.println("playlist uri = " + p.getUri());
                AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(
                        MainActivity.CLIENT_ID, AuthenticationResponse.Type.TOKEN,
                        MainActivity.REDIRECT_URI);
                builder.setScopes(new String[]{"user-read-private", "streaming",
                        "user-read-birthdate", "user-read-email", "user-read-private",
                        "playlist-modify-private"});
                AuthenticationRequest request = builder.build();
                System.out.println("refreshed token");
                final PlaylistTracksRequest ptrequest = MainActivity.api.getPlaylistTracks(p.getOwner().getId(), p.getId()).build();
                //System.out.println("success");
                Page<PlaylistTrack> ptracks = ptrequest.get();
                //System.out.println("Number of tracks = " + ptracks.getTotal());
                n = r.nextInt(ptracks.getTotal());
                PlaylistTrack t = ptracks.getItems().get(n);
                Track track = t.getTrack();
                //System.out.println("track name = " + track.getName());
                Song song = new Song(track.getUri(), track.getName(), track.getArtists().get(0).getName(), null);


                if (PlaylistActivity.currentSong == null) {
                    PlaylistActivity.currentSong = song;
                }
                if (track.isExplicit()) {
                    song.setExplicit(true);
                }
                PlaylistActivity.model.addSong(song);
                PlaylistActivity.fb.updateRoomSongs(PlaylistActivity.model);
                PlaylistActivity.songNames.add(track.getName());


                if (track.isExplicit()) {
                    if (!PlaylistActivity.model.isPG13) {

                    } else {
                      //  new PlaylistActivity().makeToast("This track is explicit and cannot be added to playlist");
                    }
                } else {
//                    if (PlaylistActivity.currentSong == null) {
//                        PlaylistActivity.currentSong = song;
//                    }
//                    PlaylistActivity.model.addSong(song);
//                    PlaylistActivity.fb.updateRoomSongs(PlaylistActivity.model);
//                    PlaylistActivity.songNames.add(track.getName());
                }
               // MainActivity.mPlayer.queue(track.getUri());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}


