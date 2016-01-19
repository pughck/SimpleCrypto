package edu.rosehulman.pughck.simplecrypto;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.abs_layout);
            TextView abTv = (TextView) findViewById(R.id.action_bar_text);
            abTv.setText(Html.fromHtml(getString(R.string.title_bar)));
        } else {
            Log.d("ERROR", "Problem finding the action bar");
        }
    }
}
