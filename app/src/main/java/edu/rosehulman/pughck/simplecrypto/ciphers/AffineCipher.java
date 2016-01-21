package edu.rosehulman.pughck.simplecrypto.ciphers;

import java.math.BigInteger;

import edu.rosehulman.pughck.simplecrypto.ciphers.alphabets.Alphabet;

/**
 * TODO
 * <p/>
 * Created by gateslm on 1/17/2016.
 */
public class AffineCipher implements ICipher {

    private Alphabet mAlphabet;

    private int mAlpha;
    private int mBeta;

    private int mAlphaInverse;

    public AffineCipher(int alpha, int beta, Alphabet alphabet) {

        mAlpha = alpha;
        mBeta = beta;

        mAlphabet = alphabet;

        mAlphaInverse = BigInteger.valueOf(mAlpha)
                .modInverse(BigInteger.valueOf(mAlphabet.size()))
                .intValue();
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
                int newIndex = (mAlpha * index + mBeta) % mAlphabet.size();
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
                int newIndex = (mAlphaInverse * (index - mBeta)) % mAlphabet.size();
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
