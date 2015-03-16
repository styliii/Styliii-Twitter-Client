package com.codepath.apps.StyliiisSimpleTweets.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.codepath.apps.StyliiisSimpleTweets.TwitterApplication;
import com.codepath.apps.StyliiisSimpleTweets.TwitterClient;
import com.codepath.apps.StyliiisSimpleTweets.helpers.NetworkUtility;
import com.codepath.apps.StyliiisSimpleTweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class MentionsTimeLineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeLine(0);
        Toast.makeText(getActivity(), "trying to laod stuff", Toast.LENGTH_SHORT).show();
    }

    private void populateTimeLine(long last_id) {
        if (last_id == 0) {
            clear();
        }
        if (NetworkUtility.isNetworkAvailable(getActivity())) {
            client.getMentionsTimeline(last_id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    addAll(Tweet.fromJSONArray(response));
//                    swipeContainer.setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(getActivity(), errorResponse.toString(), Toast.LENGTH_SHORT).show();
                    addAll(Tweet.getAll());
//                    swipeContainer.setRefreshing(false);
                }
            });
        } else {
            addAll(Tweet.getAll());
            Toast.makeText(getActivity(), "Network is not available", Toast.LENGTH_SHORT).show();
//            swipeContainer.setRefreshing(false);
        }

    }
}
