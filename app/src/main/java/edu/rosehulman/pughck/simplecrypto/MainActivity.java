package edu.rosehulman.pughck.simplecrypto;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import edu.rosehulman.pughck.simplecrypto.fragments.AboutFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.CryptoWriterFragment;
import edu.rosehulman.pughck.simplecrypto.fragments.MenuFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            TextView abTv = (TextView) findViewById(R.id.action_bar_text);
            abTv.setText(Html.fromHtml(getString(R.string.title_bar)));
        } else {
            Log.e(Constants.error, "Problem finding the action bar");
        }

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new MenuFragment());
            ft.commit();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.messaging:
                // messaging fragment
                Log.d("TTT", "messaging");
                return;

            case R.id.writer:
                //writer fragment
                Log.d("TTT", "writer");
                FragmentTransaction cwft = getSupportFragmentManager().beginTransaction();
                cwft.replace(R.id.fragment_container, new CryptoWriterFragment());
                cwft.addToBackStack(Constants.menu_added);
                cwft.commit();
                return;

            case R.id.scheme_library:
                // scheme library fragment
                Log.d("TTT", "scheme library");
                return;

            case R.id.saved_strings:
                //saved strings fragment
                Log.d("TTT", "saved strings");
                return;

            case R.id.lessons:
                // lessons fragment
                Log.d("TTT", "lessons");
                return;

            case R.id.settings:
                // settings fragment
                Log.d("TTT", "settings");
                return;

            case R.id.about:
                // about fragment
                Log.d("TTT", "about");

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, new AboutFragment());
                ft.addToBackStack(Constants.menu_added);
                ft.commit();

                return;

            default:
                Log.e(Constants.error, "invalid main menu click");
        }
    }
}
