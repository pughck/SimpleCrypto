package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class UserModel {

    @JsonIgnore
    private String key;

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String profilePic;
    private Map<String, SavedStringModel> saved_strings;
    private Map<String, MessagesModel> conversations;

    public UserModel() {

        // empty default constructor
    }

    public UserModel(String email, String firstName, String lastName, String username, String profilePic) {

        this.email = email;
        this.firstName = firstName;
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

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
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

    public Map<String, MessagesModel> getConversations() {

        return conversations;
    }

    public void setConversations(Map<String, MessagesModel> conversations) {

        this.conversations = conversations;
    }

    @JsonIgnore
    public void setValues(UserModel user) {

        this.email = user.email;
        this.username = user.username;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.profilePic = user.profilePic;
        this.saved_strings = user.saved_strings;
        this.conversations = user.conversations;
    }
}
