package edu.rosehulman.pughck.simplecrypto.ciphers.alphabets;

import android.util.Log;

import java.util.List;

import edu.rosehulman.pughck.simplecrypto.utilities.Constants;

/**
 * Created by pughck on 1/18/2016.
 */
public abstract class Alphabet {

    private List<Character> mAlphabet;

    public void setAlphabet(List<Character> alphabet) {

        mAlphabet = alphabet;
    }

    public boolean containsChar(char c) {

        return mAlphabet.indexOf(c) != -1;
    }

    public int getIndex(char c) {

        if (!containsChar(c)) {
            Log.e(Constants.error, "Alphabet does not contain the character");

            return -1;
        }

        return mAlphabet.indexOf(c);
    }

    public char getCharacter(int index) {

        if (index >= mAlphabet.size()) {
            Log.e(Constants.error, "Invalid index");

            // TODO throw exception ??
            return 0;
        }

        return mAlphabet.get(index);
    }

    public int size() {

        return mAlphabet.size();
    }
}
