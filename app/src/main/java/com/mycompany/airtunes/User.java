package com.mycompany.airtunes;

/**
 * Created by arvindraju on 3/4/16.
 */
public class User {
    private String firstName;
    private String lastName;
    private int id;
    private String username;
    private String password;


    public User(String firstName, String lastName, String username, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

//    public User() {};



    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }


}
