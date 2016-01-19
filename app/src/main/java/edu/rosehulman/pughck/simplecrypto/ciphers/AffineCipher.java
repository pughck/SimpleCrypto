package edu.rosehulman.pughck.simplecrypto.ciphers;

import android.util.Log;

import java.math.BigInteger;

import edu.rosehulman.pughck.simplecrypto.Constants;
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

    public AffineCipher(int alpha, int beta, Alphabet alphabet) {

        mAlpha = alpha;
        mBeta = beta;

        mAlphabet = alphabet;
    }

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

        if (mAlphabet.size() > 0) {
            BigInteger b1 = BigInteger.valueOf(mAlpha);
            BigInteger b2 = BigInteger.valueOf(mAlphabet.size());
            BigInteger result = b1.gcd(b2);

            return result.equals(1);
        } else {
            Log.e(Constants.error, "Alphabet did not have any size");

            return false;
        }
    }
}
