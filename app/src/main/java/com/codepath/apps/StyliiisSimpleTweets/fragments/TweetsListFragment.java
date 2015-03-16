package com.codepath.apps.StyliiisSimpleTweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.StyliiisSimpleTweets.R;
import com.codepath.apps.StyliiisSimpleTweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.StyliiisSimpleTweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetsListFragment extends Fragment {
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private ProgressBar progressBarFooter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);
        setupListWithFooter(inflater);
        lvTweets.setAdapter(aTweets);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
        super.onCreate(savedInstanceState);
    }

    public void setupListWithFooter(LayoutInflater inflater) {
        View footer = inflater.inflate(R.layout.footer_progress, null);
        progressBarFooter = (ProgressBar) footer.findViewById(R.id.pbFooterLoading);
        lvTweets.addFooterView(footer);
    }

    public void showProgressBar() {
        progressBarFooter.setVisibility(View.VISIBLE);
    }

    // Hide progress
    public void hideProgressBar() {
        progressBarFooter.setVisibility(View.GONE);
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }

    public void clear() {
        aTweets.clear();
    }

    public void insert(Tweet tweet, int position) {
        aTweets.insert(tweet, position);
    }

    public Tweet getLastTweet(){
        if (tweets.isEmpty()) {
            return null;
        } else {
            return tweets.get(tweets.size() - 1);
        }
    }
}
