package com.codepath.apps.StyliiisSimpleTweets.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.StyliiisSimpleTweets.R;
import com.codepath.apps.StyliiisSimpleTweets.TwitterApplication;
import com.codepath.apps.StyliiisSimpleTweets.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ComposeTweetActivity extends ActionBarActivity {
    private EditText etTweetBody;
    private TwitterClient client;
    private TextView tvHandle;
    private TextView tvUserName;
    ImageView ivProfileImage;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        client = TwitterApplication.getRestClient();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        setupViews();
    }

    private void setupViews() {
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        etTweetBody = (EditText) findViewById(R.id.etTweetBody);
        Button btnTweet = (Button) findViewById(R.id.btnTweet);

        tvHandle.setText(pref.getString("screen_name", ""));
        tvUserName.setText(pref.getString("name", ""));
        Picasso.with(ComposeTweetActivity.this).load(pref.getString("profile_image_url", "")).into(ivProfileImage);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTweet();
            }
        });
    }

    public void submitTweet() {
        String tweetMsg = etTweetBody.getText().toString();
        client.postTweet(tweetMsg, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Debug", response.toString());
                Intent data = new Intent();
                try {
                    data.putExtra("tweetId", response.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);
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
