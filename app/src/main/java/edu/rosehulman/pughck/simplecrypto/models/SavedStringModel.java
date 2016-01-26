package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * TODO
 * <p/>
 * Created by gateslm on 1/17/2016.
 */
public class SavedStringModel {

    @JsonIgnore
    private String key;

    private String string;
    private String encryption;

    public SavedStringModel() {

        // required empty constructor
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getString() {

        return string;
    }

    public void setString(String string) {

        this.string = string;
    }

    public String getEncryption() {

        return encryption;
    }

    public void setEncryption(String encryption) {

        this.encryption = encryption;
    }

    public void setValues(SavedStringModel newString) {

        this.string = newString.string;
        this.encryption = newString.encryption;
    }
}
