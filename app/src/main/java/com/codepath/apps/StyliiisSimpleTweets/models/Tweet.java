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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public String getAbbrevTime(){
        SimpleDateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try {
            Date d = f.parse(this.getCreatedAt());
            return getTimeString(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String getTimeString(Date fromdate) {

        long then;
        then = fromdate.getTime();
        Date date = new Date(then);

        StringBuffer dateStr = new StringBuffer();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar now = Calendar.getInstance();

        int days = daysBetween(calendar.getTime(), now.getTime());
        int minutes = hoursBetween(calendar.getTime(), now.getTime());
        int hours = minutes / 60;
        if (days == 0) {

            int second = minuteBetween(calendar.getTime(), now.getTime());
            if (minutes > 60) {

                if (hours >= 1 && hours <= 24) {
                    dateStr.append(hours).append("h");
                }

            } else {

                if (second <= 5) {
                    dateStr.append("Now");
                } else if (second > 5 && second <= 30) {
                    dateStr.append("few seconds ago");
                } else if (second > 30 && second <= 60) {
                    dateStr.append(second).append("s");
                } else if (second >= 60 && minutes <= 60) {
                    dateStr.append(minutes).append("m");
                }
            }
        } else

        if (hours > 24 && days <= 7) {
            dateStr.append(days).append("d");
        } else {
            dateStr.append(oldTweetDate(fromdate));
        }

        return dateStr.toString();
    }

    private static int minuteBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.SECOND_IN_MILLIS);
    }

    private static int hoursBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.MINUTE_IN_MILLIS);
    }

    private static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.DAY_IN_MILLIS);
    }

    private static String oldTweetDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("MMM dd");
        return format.format(date);
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
