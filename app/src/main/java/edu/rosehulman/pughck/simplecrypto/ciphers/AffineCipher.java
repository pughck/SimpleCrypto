package edu.rosehulman.pughck.simplecrypto.ciphers;

import android.util.Log;

import java.math.BigInteger;

import edu.rosehulman.pughck.simplecrypto.Constants;
import edu.rosehulman.pughck.simplecrypto.ciphers.alphabets.IAlphabet;

/**
 * TODO
 * <p/>
 * Created by gateslm on 1/17/2016.
 */
public class AffineCipher implements ICipher {

    private int alpha;
    private int beta;
    private IAlphabet alphabet;

    @Override
    public String encrypt(String message) {

        return null;
    }

    @Override
    public String decrypt(String message) {

        return null;
    }

    /**
     * Determines if the alpha is valid for the cipher
     *
     * @return - True if the alpha is valid for the cipher, false if not or the alphabet was not
     * of size greater than 0.
     */
    private boolean validAlpha() {

        if (this.alphabet.size() > 0) {
            BigInteger b1 = BigInteger.valueOf(this.alpha);
            BigInteger b2 = BigInteger.valueOf(this.alphabet.size());
            BigInteger result = b1.gcd(b2);

            return result.equals(1);
        } else {
            Log.e(Constants.error, "Alphabet did not have any size");

            return false;
        }
    }
}
