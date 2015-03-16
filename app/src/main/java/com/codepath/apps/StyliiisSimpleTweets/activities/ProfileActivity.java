package com.codepath.apps.StyliiisSimpleTweets.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.StyliiisSimpleTweets.R;
import com.codepath.apps.StyliiisSimpleTweets.fragments.ProfileHeaderFragment;
import com.codepath.apps.StyliiisSimpleTweets.fragments.UserTimeLineFragment;

public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String screenName = getIntent().getStringExtra("screen_name");
        getSupportActionBar().setTitle("@" + screenName);

        ProfileHeaderFragment profileHeaderFragment = ProfileHeaderFragment.newInstance(screenName);
        UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(screenName);

        if (savedInstanceState ==  null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimeLineFragment);
            ft.replace(R.id.flHeaderContainer, profileHeaderFragment);
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
