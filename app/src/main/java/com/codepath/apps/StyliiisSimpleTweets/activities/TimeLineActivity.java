package com.codepath.apps.StyliiisSimpleTweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.StyliiisSimpleTweets.R;
import com.codepath.apps.StyliiisSimpleTweets.fragments.HomeTimeLineFragment;
import com.codepath.apps.StyliiisSimpleTweets.fragments.MentionsTimeLineFragment;
import com.codepath.apps.StyliiisSimpleTweets.helpers.NetworkUtility;
import com.codepath.apps.StyliiisSimpleTweets.models.Tweet;

public class TimeLineActivity extends ActionBarActivity {
    private TweetsPageAdapter tweetsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        tweetsAdapter = new TweetsPageAdapter(getSupportFragmentManager());
        vpPager.setAdapter(tweetsAdapter);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
    }

    public void onProfileView(MenuItem mi) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_line, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void launchComposeTweet(MenuItem mi) {
        Intent intent = new Intent(this, ComposeTweetActivity.class);
        startActivityForResult(intent, NetworkUtility.getREQUEST_CODE());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == NetworkUtility.getREQUEST_CODE()) {
            Tweet tweet = data.getParcelableExtra("tweet");
            tweetsAdapter.addTweet(0, 0, tweet);
        }
    }


    public class TweetsPageAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};
        private HomeTimeLineFragment homeTimeLineFragment;
        private MentionsTimeLineFragment mentionsTimeLineFragment;

        public TweetsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addTweet(int tabPosition, int position, Tweet tweet) {
            if (tabPosition == 0) {
                homeTimeLineFragment.insert(tweet, position);
            }
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                homeTimeLineFragment =  new HomeTimeLineFragment();
                return homeTimeLineFragment;
            } else if (position == 1 ) {
                mentionsTimeLineFragment = new MentionsTimeLineFragment();
                return mentionsTimeLineFragment;
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}
