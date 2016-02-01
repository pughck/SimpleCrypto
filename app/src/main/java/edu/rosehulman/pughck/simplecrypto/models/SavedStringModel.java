package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.rosehulman.pughck.simplecrypto.ciphers.ICipher;

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

    @JsonIgnore
    private ICipher cipher;

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

    public ICipher getCipher() {

        return cipher;
    }

    public void setCipher(ICipher cipher) {

        this.cipher = cipher;
    }
}
