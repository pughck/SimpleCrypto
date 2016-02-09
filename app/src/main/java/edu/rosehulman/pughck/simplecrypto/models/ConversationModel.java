package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * Created by pughck on 2/8/2016.
 */
public class ConversationModel {

    @JsonIgnore
    private String key;

    private String encryption;

    private String user1;
    private String user2;

    private Map<String, MessageModel> messages;

    public ConversationModel() {

        // empty default constructor
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getEncryption() {

        return encryption;
    }

    public void setEncryption(String encryption) {

        this.encryption = encryption;
    }

    public String getUser1() {

        return user1;
    }

    public void setUser1(String user1) {

        this.user1 = user1;
    }

    public String getUser2() {

        return user2;
    }

    public void setUser2(String user2) {

        this.user2 = user2;
    }

    public Map<String, MessageModel> getMessages() {

        return messages;
    }

    public void setMessages(Map<String, MessageModel> messages) {

        this.messages = messages;
    }
}
