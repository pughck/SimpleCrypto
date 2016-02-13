package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by gateslm on 1/17/2016.
 */
public class MessageModel {

    @JsonIgnore
    private String key;

    private String user;
    private String message;

    public MessageModel() {

        // empty default constructor
    }

    public MessageModel(String user, String message) {

        this.user = user;
        this.message = message;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {

        this.user = user;
    }

    public void setValues(MessageModel message) {

        this.user = message.user;
        this.message = message.message;
    }
}
