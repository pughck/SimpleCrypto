package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by gateslm on 1/17/2016.
 */
public class MessagesModel {

    @JsonIgnore
    private String key;

    private String uid;
    private String username;
    private String conversation;

    public MessagesModel() {

        // empty default constructor
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {

        this.uid = uid;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getConversation() {

        return conversation;
    }

    public void setConversation(String conversation) {

        this.conversation = conversation;
    }

    public void setValues(MessagesModel conversation) {

        this.uid = conversation.uid;
        this.username = conversation.username;
        this.conversation = conversation.conversation;
    }
}
