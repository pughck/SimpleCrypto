package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * TODO
 * <p/>
 * Created by gateslm on 1/17/2016.
 */
public class SavedSchemeModel {

    @JsonIgnore
    private String key;

    private String name;
    private String type;
    private int key1;
    private int key2;
    private String key3;

    public SavedSchemeModel() {

        // required empty constructor
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public int getKey1() {

        return key1;
    }

    public void setKey1(int key1) {

        this.key1 = key1;
    }

    public int getKey2() {

        return key2;
    }

    public void setKey2(int key2) {

        this.key2 = key2;
    }

    public String getKey3() {

        return key3;
    }

    public void setKey3(String key3) {

        this.key3 = key3;
    }

    public void setValues(SavedSchemeModel newScheme) {

        this.name = newScheme.name;
        this.type = newScheme.type;
        this.key1 = newScheme.key1;
        this.key2 = newScheme.key2;
        this.key3 = newScheme.key3;
    }
}
