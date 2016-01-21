package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by pughck on 1/21/2016.
 */
public class User {

    @JsonIgnore
    private String key;

    private String email;
    private String username;
    private String fistName;
    private String lastName;

    public User() {

        // empty default constructor for jackson
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getFistName() {

        return fistName;
    }

    public void setFistName(String fistName) {

        this.fistName = fistName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }
}
