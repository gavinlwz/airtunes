package com.mycompany.airtunes;

/**
 * Created by akashsubramanian on 3/24/16.
 */
public class Song {
    private String uri;
    private String name;
    private String artist;
    private String pictureUrl;

    public Song(String uri) {
        this.uri = uri;
    }

    public Song(String uri, String name, String artist, String pictureUrl) {
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.pictureUrl = pictureUrl;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getUri() {
        return uri;
    }

    public String getName() {
       return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }



}
