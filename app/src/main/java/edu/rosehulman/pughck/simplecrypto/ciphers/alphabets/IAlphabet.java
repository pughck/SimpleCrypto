package edu.rosehulman.pughck.simplecrypto.ciphers.alphabets;

/**
 * Created by pughck on 1/18/2016.
 */
public interface IAlphabet {

    char shift(char c, int shift);

    int size();
}
