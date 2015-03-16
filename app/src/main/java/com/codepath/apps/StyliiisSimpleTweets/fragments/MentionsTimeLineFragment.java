package com.codepath.apps.StyliiisSimpleTweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.StyliiisSimpleTweets.EndlessScrollListener;
import com.codepath.apps.StyliiisSimpleTweets.R;
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
    private SwipeRefreshLayout swipeContainer;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeLine(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        ListView lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Tweet last_tweet = getLastTweet();
                Toast.makeText(getActivity(), "loading more now", Toast.LENGTH_SHORT).show();
                if (last_tweet == null) {
                    populateTimeLine(0);
                } else {
                    populateTimeLine(last_tweet.getRemoteId());
                }
            }
        });
        setupSwipeContainer();
        return view;
    }


    private void populateTimeLine(long last_id) {
        if (last_id == 0) {
            clear();
        } else {
            showProgressBar();
        }
        if (NetworkUtility.isNetworkAvailable(getActivity())) {
            client.getMentionsTimeline(last_id, new JsonHttpResponseHandler() {
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
    private void setupSwipeContainer() {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeLine(0);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }
}
