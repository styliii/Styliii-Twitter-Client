package com.codepath.apps.StyliiisSimpleTweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.StyliiisSimpleTweets.R;
import com.codepath.apps.StyliiisSimpleTweets.TwitterApplication;
import com.codepath.apps.StyliiisSimpleTweets.TwitterClient;
import com.codepath.apps.StyliiisSimpleTweets.helpers.NetworkUtility;
import com.codepath.apps.StyliiisSimpleTweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class UserTimeLineFragment extends TweetsListFragment {
    private String screenName;
    private TwitterClient client;
    private SwipeRefreshLayout swipeContainer;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        client = TwitterApplication.getRestClient();
        if (getArguments() != null) {
            screenName = getArguments().getString("screen_name");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  super.onCreateView(inflater, container, savedInstanceState);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        return view;
    }

    @Override
    public void populateTimeLine(long last_id, String screenName ) {
        if (last_id == 0) {
            clear();
        } else {
            showProgressBar();
        }
        if (NetworkUtility.isNetworkAvailable(getActivity())) {
            client.getUserTimeline(screenName, last_id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    addAll(Tweet.fromJSONArray(response));
                    swipeContainer.setRefreshing(false);
                    hideProgressBar();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(getActivity(), errorResponse.toString(), Toast.LENGTH_SHORT).show();
                    addAll(Tweet.getAll());
                    swipeContainer.setRefreshing(false);
                    hideProgressBar();
                }
            });
        } else {
            addAll(Tweet.getAll());
            Toast.makeText(getActivity(), "Network is not available", Toast.LENGTH_SHORT).show();
            swipeContainer.setRefreshing(false);
            hideProgressBar();
        }

    }
    // Creates a new fragment given an int and title
    // UserTimeLineFragment.newInstance(5, "Hello");
    public static UserTimeLineFragment newInstance(String screen_name) {
        UserTimeLineFragment userFragment = new UserTimeLineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

}
