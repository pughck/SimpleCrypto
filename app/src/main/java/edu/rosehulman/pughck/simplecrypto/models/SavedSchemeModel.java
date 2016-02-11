package edu.rosehulman.pughck.simplecrypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.rosehulman.pughck.simplecrypto.ciphers.AffineCipher;
import edu.rosehulman.pughck.simplecrypto.ciphers.CaesarCipher;
import edu.rosehulman.pughck.simplecrypto.ciphers.ICipher;
import edu.rosehulman.pughck.simplecrypto.ciphers.alphabets.Alphabet;
import edu.rosehulman.pughck.simplecrypto.ciphers.alphabets.BasicAlphabet;
import edu.rosehulman.pughck.simplecrypto.ciphers.alphabets.ExtendedAlphabet;

/**
 * Created by gateslm on 1/17/2016.
 */
public class SavedSchemeModel {

    @JsonIgnore
    private String key;

    private String name;
    private String type;
    private String key1;
    private String key2;
    private String uid;
    private String alphabet;

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

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {

        this.uid = uid;
    }

    public String getAlphabet() {

        return alphabet;
    }

    public void setAlphabet(String alphabet) {

        this.alphabet = alphabet;
    }

    public void setValues(SavedSchemeModel newScheme) {

        this.name = newScheme.name;
        this.type = newScheme.type;
        this.key1 = newScheme.key1;
        this.key2 = newScheme.key2;
        this.uid = newScheme.uid;
    }

    @Override
    public String toString() {

        return this.name;
    }

    @JsonIgnore
    public String getInfoString() {

        StringBuilder sb = new StringBuilder();
        sb.append("\ttype: ").append(this.type).append("\n");

        if (this.key1 != null) {
            sb.append("\tkey: ").append(this.key1);
        }

        if (this.key2 != null) {
            sb.append("\t\t").append(this.key2).append("\n");
        }

        sb.append("\talphabet: ").append(this.alphabet);

        return sb.toString();
    }

    public ICipher convertToCipher() {

        ICipher cipher;

        Alphabet alphabet;
        if (this.alphabet.equals("basic")) {
            alphabet = new BasicAlphabet();
        } else {
            alphabet = new ExtendedAlphabet();
        }

        if (this.type.equals("caesar")) {
            cipher = new CaesarCipher(Integer.parseInt(this.key1), alphabet);
        } else {
            cipher = new AffineCipher(Integer.parseInt(this.key1), Integer.parseInt(this.key2), alphabet);
        }

        return cipher;
    }
}
