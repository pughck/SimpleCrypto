package edu.rosehulman.pughck.simplecrypto.ciphers;

import edu.rosehulman.pughck.simplecrypto.ciphers.alphabets.Alphabet;

/**
 *
 */
public class VigenereCipher implements ICipher {

    private Alphabet mAlphabet;

    private String mKey;

    public VigenereCipher(String key, Alphabet alphabet) {

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
                int newIndex = (index + mAlphabet.getIndex(mKey.charAt(i % mKey.length())))
                        % mAlphabet.size();

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
                int newIndex = (index - mAlphabet.getIndex(mKey.charAt(i % mKey.length())))
                        % mAlphabet.size();
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
