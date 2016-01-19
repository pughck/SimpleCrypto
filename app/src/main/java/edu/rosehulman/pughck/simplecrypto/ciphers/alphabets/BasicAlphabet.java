package edu.rosehulman.pughck.simplecrypto.ciphers.alphabets;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pughck on 1/18/2016.
 */
public class BasicAlphabet extends Alphabet {

    private final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '0', ' '};

    public BasicAlphabet() {

        // TODO maybe not the best way to do this
        List<Character> alphabetList = new ArrayList<>();
        for (char c : alphabet) {
            alphabetList.add(c);
        }

        setAlphabet(alphabetList);
    }
}
