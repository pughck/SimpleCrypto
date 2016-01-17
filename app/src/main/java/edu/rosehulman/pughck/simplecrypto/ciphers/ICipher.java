package edu.rosehulman.pughck.simplecrypto.ciphers;

/**
 * TODO
 * <p/>
 * Created by gateslm on 1/17/2016.
 */
public interface ICipher {

    String encrypt(String message);

    String decrypt(String message);
}
