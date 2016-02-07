package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by gateslm on 1/17/2016.
 */
public class ConversationModel {

    @JsonIgnore
    private String key;

    private String user;
    private String conversation;

    public ConversationModel() {

        // empty default constructor
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {

        this.user = user;
    }

    public String getConversation() {

        return conversation;
    }

    public void setConversation(String conversation) {

        this.conversation = conversation;
    }

    public void setValues(ConversationModel conversation) {

        this.user = conversation.user;
        this.conversation = conversation.conversation;
    }
}
