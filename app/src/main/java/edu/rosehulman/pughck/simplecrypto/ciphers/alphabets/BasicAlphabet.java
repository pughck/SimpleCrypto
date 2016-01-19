package edu.rosehulman.pughck.simplecrypto.ciphers.alphabets;

/**
 * Created by pughck on 1/18/2016.
 */
public class BasicAlphabet implements IAlphabet {

    private final char[] alphabet = {'a', 'b', 'c','d', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n','o','p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3',
            '4', '5', '6', '7', '8', '9' ,'0', ' '};


    @Override
    public char shift(char c, int shift) {

        return 0;
    }

    @Override
    public int size() {
        return this.alphabet.length;
    }
}
