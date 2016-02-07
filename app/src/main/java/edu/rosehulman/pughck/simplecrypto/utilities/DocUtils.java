package edu.rosehulman.pughck.simplecrypto.utilities;

import android.content.Context;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import edu.rosehulman.pughck.simplecrypto.R;
import edu.rosehulman.pughck.simplecrypto.models.Doc;

/**
 * Created by boutell on 12/1/2015.
 */
public class DocUtils {

    public static ArrayList<Doc> loadDocs(Context context) {
//        Map<String, Integer> docResources = new TreeMap<>();
        String[] titles = new String[]{
                "Affine Lessons",
                "Caesar Lessons",
                "Modular Arithmetic Lessons",
                "RSA Lessons",
                "Vigenere Lessons",
                "Inverse Lessons"};
        Integer[] resources = new Integer[]{
                R.raw.affine_cipher,
                R.raw.caesar_cipher,
                R.raw.modular_arithmetic,
                R.raw.rsa_algorith,
                R.raw.vigenere_cipher,
                R.raw.inverse
        };
        ArrayList<Doc> docs = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            InputStream is = context.getResources().openRawResource(resources[i]);
            String s = null;
            try {
                s = IOUtils.toString(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            IOUtils.closeQuietly(is); // don't forget to close your streams
            Doc doc = new Doc(titles[i], s);
            docs.add(doc);
        }
        return docs;
    }

    public static Doc getAbout(Context context) {
        String title = "About";
        Integer resource = R.raw.about;
        InputStream is = context.getResources().openRawResource(resource);
        String s = null;
        try {
            s = IOUtils.toString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(is);
        Doc doc = new Doc(title, s);
        return doc;
    }
}