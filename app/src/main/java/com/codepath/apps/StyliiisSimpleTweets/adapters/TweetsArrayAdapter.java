package com.codepath.apps.StyliiisSimpleTweets.adapters;

// Taking a tweet object, and turning them into views
// that will be displayed into a list

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.StyliiisSimpleTweets.R;
import com.codepath.apps.StyliiisSimpleTweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1,tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvTweetBody = (TextView) convertView.findViewById(R.id.tvTweetBody);
        TextView tvTimeAgo = (TextView) convertView.findViewById(R.id.tvTimeAgo);
        TextView tvHandle = (TextView) convertView.findViewById(R.id.tvHandle);

        tvHandle.setText("@" + tweet.getUser().getScreenName());
        tvUserName.setText(tweet.getUser().getName());
        tvTweetBody.setText(tweet.getBody());
        tvTimeAgo.setText(tweet.getRelativeTimeAgo().toString());

        ivProfileImage.setImageResource(android.R.color.transparent);
        String profileImageUrl = tweet.getUser().getProfileImageUrl();
        Picasso.with(getContext()).load(profileImageUrl).into(ivProfileImage);
        return convertView;
    }
}

