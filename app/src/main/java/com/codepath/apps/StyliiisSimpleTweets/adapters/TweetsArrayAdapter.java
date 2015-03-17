package com.codepath.apps.StyliiisSimpleTweets.adapters;

// Taking a tweet object, and turning them into views
// that will be displayed into a list

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.StyliiisSimpleTweets.R;
import com.codepath.apps.StyliiisSimpleTweets.activities.ProfileActivity;
import com.codepath.apps.StyliiisSimpleTweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    private static class ViewHolder {
        ImageView profile_image;
        TextView user_name;
        TextView body;
        TextView time_ago;
        TextView handle;
    }
    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1,tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        final ViewHolder v;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            v.profile_image = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            v.user_name = (TextView) convertView.findViewById(R.id.tvUserName);
            v.body = (TextView) convertView.findViewById(R.id.tvTweetBody);
            v.time_ago = (TextView) convertView.findViewById(R.id.tvTimeAgo);
            v.handle = (TextView) convertView.findViewById(R.id.tvHandle);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        v.handle.setText("@" + tweet.getUser().getScreenName());
        v.user_name.setText(tweet.getUser().getName());
        v.body.setText(tweet.getBody());
        v.time_ago.setText(tweet.getAbbrevTime().toString());

        v.profile_image.setImageResource(android.R.color.transparent);
        String profileImageUrl = tweet.getUser().getProfileImageUrl();
        Picasso.with(getContext()).load(profileImageUrl).into(v.profile_image);

        v.profile_image.setTag(tweet.getUser());
        v.profile_image.setTag(tweet.getUser().getScreenName());

        v.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("screen_name", v.getTag().toString());
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}

