package com.codepath.apps.StyliiisSimpleTweets.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.StyliiisSimpleTweets.R;
import com.codepath.apps.StyliiisSimpleTweets.TwitterApplication;
import com.codepath.apps.StyliiisSimpleTweets.TwitterClient;
import com.codepath.apps.StyliiisSimpleTweets.helpers.NetworkUtility;
import com.codepath.apps.StyliiisSimpleTweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileHeaderFragment extends Fragment {
    private User user;
    private String screenName;
    private TwitterClient client;
    private TextView tvFollowing;
    private TextView tvFollowers;
    private TextView tvTagline;
    private TextView tvName;
    private ImageView ivProfileImage;

    public static ProfileHeaderFragment newInstance(String screenName) {
        ProfileHeaderFragment fragment = new ProfileHeaderFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileHeaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();

        screenName = getArguments().getString("screen_name");

        if (screenName == null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            screenName = pref.getString("screen_name", "");
        }
        user = User.findByScreenName(screenName);
        if (user == null) {
            if (NetworkUtility.isNetworkAvailable(getActivity())) {
                client.getUserInfo(screenName, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        user = User.findOrCreateFromJsonObject(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(getActivity(), errorResponse.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(getActivity(), "Network is not available", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile_header, container, false);
        tvFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        tvFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        tvTagline = (TextView) view.findViewById(R.id.tvTagline);
        tvName = (TextView) view.findViewById(R.id.tvName);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        populateProfileHeader(user);
        return view;
    }

    private void populateProfileHeader(User user) {
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Picasso.with(getActivity()).load(user.getProfileImageUrl()).into(ivProfileImage);
    }


}
