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
    private String key1;
    private String key2;

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

    public String getKey1() {

        return key1;
    }

    public void setKey1(String key1) {

        this.key1 = key1;
    }

    public String getKey2() {

        return key2;
    }

    public void setKey2(String key2) {

        this.key2 = key2;
    }

    public void setValues(SavedSchemeModel newScheme) {

        this.name = newScheme.name;
        this.type = newScheme.type;
        this.key1 = newScheme.key1;
        this.key2 = newScheme.key2;
    }

    public String getInfoString() {

        StringBuilder sb = new StringBuilder();
        sb.append("\t").append(this.type).append("\n");

        if (this.key1 != null) {
            sb.append("\tkey: ").append(this.key1);
        }

        if (this.key2 != null) {
            sb.append("\t").append(this.key2).append("\n");
        }

        return sb.toString();
    }
}
