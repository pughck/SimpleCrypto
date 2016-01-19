package edu.rosehulman.pughck.simplecrypto.ciphers;

/**
 * TODO
 * <p/>
 * Created by gateslm on 1/17/2016.
 */
public interface ICipher {

    /**
     * Encrypts the given message using the cipher's values
     *
     * @param message - Message to encrypt with cipher's values
     * @return - Encrypted message
     */
    String encrypt(String message);

    /**
     * Decrypts the given message using the cipher's values
     *
     * @param message - Message to encrypt with cipher's values
     * @return - Decrypted message
     */
    String decrypt(String message);
}
