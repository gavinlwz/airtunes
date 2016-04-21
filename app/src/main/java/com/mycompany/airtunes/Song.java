package com.mycompany.airtunes;

import java.io.Serializable;

/**
 * Created by akashsubramanian on 3/24/16.
 */
public class Song implements Serializable {
    String uri;
    String name;
    String artist;
    String pictureUrl;
    boolean isExplicit;

    public Song(String uri) {
        this.uri = uri;
    }
    public Song() {

    };
    public Song(String uri, String name, String artist, String pictureUrl) {
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.pictureUrl = pictureUrl;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setExplicit(boolean e) {
        this.isExplicit = e;
    }

    public boolean getExplicit() {
        return this.isExplicit;
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
