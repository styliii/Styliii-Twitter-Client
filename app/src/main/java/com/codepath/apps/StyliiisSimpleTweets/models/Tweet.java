package com.codepath.apps.StyliiisSimpleTweets.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Table(name = "Tweets")
public class Tweet extends Model implements Parcelable {
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;
    @Column(name = "Body")
    public String body;
    @Column(name = "CreatedAt")
    public String createdAt;
    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public User user;

    public Tweet() {
        super();
    }

    public Tweet(int remoteId, String body, long uid, String createdAt, User user) {
        super();
        this.remoteId = remoteId;
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public String getBody() {
        return body;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public static Tweet findOrCreateFromJSONObject(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            Tweet existingTweet = new Select().from(Tweet.class)
                    .where("remote_id = ?", jsonObject.getLong("id"))
                    .executeSingle();
            if (existingTweet != null) {
               tweet = existingTweet;
            } else {
                tweet.body = jsonObject.getString("text");
                tweet.remoteId = jsonObject.getLong("id");
                tweet.createdAt = jsonObject.getString("created_at");
                tweet.user = User.findOrCreateFromJsonObject(jsonObject.getJSONObject("user"));
                tweet.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Tweet tweet = null;
            try {
                tweet = Tweet.findOrCreateFromJSONObject(jsonArray.getJSONObject(i));
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

        }
        return tweets;
    }

    public String getRelativeTimeAgo() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(getCreatedAt()).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static List<Tweet> getAll() {
        return new Select()
                .from(Tweet.class)
                .execute();
    }

    public static Tweet findById(long remoteId) {
        Tweet tweet = null;
        if (tweet != null) {
            tweet = new Select().from(Tweet.class)
                    .where("remoteId = ?", remoteId).executeSingle();

        }
        return tweet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.remoteId);
        dest.writeString(this.body);
        dest.writeString(this.createdAt);
        dest.writeParcelable(this.user, flags);
    }

    private Tweet(Parcel in) {
        this.remoteId = in.readLong();
        this.body = in.readString();
        this.createdAt = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
