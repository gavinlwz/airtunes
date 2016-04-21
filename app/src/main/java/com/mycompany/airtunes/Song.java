package com.mycompany.airtunes;

import java.io.Serializable;

/**
 * Song class to represent a song object -- depicted mainly by its uniform resource identifier
 * */
public class Song implements Serializable {

    String uri; //unique identifier of song
    String name; //title of song
    String artist; //artist of song
    String pictureUrl; //url to song cover
    boolean isExplicit; //explicity of song

    public Song() {

    }
    /**
     * @param uri String
     * */
    public Song(String uri) {
        this.uri = uri;
    }

    /**
     * constructor setting instance variables to inital values
     * @param uri String identifier of song
     * @param name String name of song
     * @param artist String artist of song
     * @param pictureUrl String url of song album cover
     * */
    public Song(String uri, String name, String artist, String pictureUrl) {
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.pictureUrl = pictureUrl;
    }

    /*
    * ==========================================
    * ==========================================
    * GETTER AND SETTER FUNCTIONS FOR THIS CLASS
    * ==========================================
    * ==========================================
    * */
    public String getUri() { return uri; }

    public void setExplicit(boolean e) {
        this.isExplicit = e;
    }

    public boolean getExplicit() {
        return this.isExplicit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) { this.uri = uri; }


    public String getName() { return name; }



    public String getArtist() { return artist; }

    public void setArtist(String artist) { this.artist = artist; }

    public String getPictureUrl() { return pictureUrl; }

    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }
}
