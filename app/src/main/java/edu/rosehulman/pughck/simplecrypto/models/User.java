package edu.rosehulman.pughck.simplecrypto.models;

import android.net.Uri;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

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
    private String profilePic;

    private Map<String, SavedStringModel> saved_strings;
    private Map<String, ConversationModel> conversations;

    public User() {

        // empty default constructor for jackson
    }

    public User(String email, String fistName, String lastName, String username, String profilePic) {

        this.email = email;
        this.fistName = fistName;
        this.lastName = lastName;
        this.username = username;
        this.profilePic = profilePic;

        this.saved_strings = new HashMap<>();
        this.conversations = new HashMap<>();
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

    public String getProfilePic() {

        return profilePic;
    }

    public void setProfilePic(String profilePic) {

        this.profilePic = profilePic;
    }

    public Map<String, SavedStringModel> getSaved_strings() {

        return saved_strings;
    }

    public void setSaved_strings(Map<String, SavedStringModel> saved_strings) {

        this.saved_strings = saved_strings;
    }

    public Map<String, ConversationModel> getConversations() {

        return conversations;
    }

    public void setConversations(Map<String, ConversationModel> conversations) {

        this.conversations = conversations;
    }
}
