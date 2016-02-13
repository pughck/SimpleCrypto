package edu.rosehulman.pughck.simplecrypto.ciphers;

import edu.rosehulman.pughck.simplecrypto.ciphers.alphabets.Alphabet;

/**
 * Created by gateslm on 1/17/2016.
 */
public class CaesarCipher implements ICipher {

    private Alphabet mAlphabet;

    private int mKey;

    public CaesarCipher(int key, Alphabet alphabet) {

        mKey = key;

        mAlphabet = alphabet;
    }

    @Override
    public String encrypt(String message) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {

            char c = message.charAt(i);
            boolean upper = Character.isUpperCase(c);
            c = Character.toLowerCase(c);

            if (mAlphabet.containsChar(c)) {
                int index = mAlphabet.getIndex(c);
                int newIndex = (index + mKey) % mAlphabet.size();
                c = mAlphabet.getCharacter(newIndex);
            }

            if (upper) {
                c = Character.toUpperCase(c);
            }

            result.append(c);
        }

        return result.toString();
    }

    @Override
    public String decrypt(String message) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {

            char c = message.charAt(i);
            boolean upper = Character.isUpperCase(c);
            c = Character.toLowerCase(c);

            if (mAlphabet.containsChar(c)) {
                int index = mAlphabet.getIndex(c);
                int newIndex = (index - mKey) % mAlphabet.size();
                newIndex = newIndex < 0 ? newIndex + mAlphabet.size() : newIndex;

                c = mAlphabet.getCharacter(newIndex);
            }

            if (upper) {
                c = Character.toUpperCase(c);
            }

            result.append(c);
        }

        return result.toString();
    }
}
