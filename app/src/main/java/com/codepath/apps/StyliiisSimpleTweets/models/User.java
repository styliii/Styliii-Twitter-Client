package com.codepath.apps.StyliiisSimpleTweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Table(name="Users")
public class User extends Model implements Parcelable {
    @Column(name="remote_id", unique=true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long remoteId;
    @Column(name="Name")
    private String name;
    @Column(name="ScreenName")
    private String screenName;
    @Column(name="ProfileImageUrl")
    private String profileImageUrl;
    @Column(name="Tagline")
    private String tagline;
    @Column(name="FollowersCount")
    private int followersCount;
    @Column(name="FollowingCount")
    private int followingCount;

    public User() {
        super();
    }

    public List<Tweet> tweets() {
        return getMany(Tweet.class, "User");
    }
    public String getName() {
        return name;
    }
    public long getRemoteId() {
        return remoteId;
    }
    public String getScreenName() {
        return screenName;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public String getTagline() { return tagline; }
    public int getFollowersCount() { return followersCount; }
    public int getFollowingCount() {  return followingCount; }

    public static User findOrCreateFromJsonObject(JSONObject json) {
        User u = new User();
        try {
            User existingUser = new Select().from(User.class)
                    .where("remote_id = ?", json.getLong("id")).executeSingle();
            if (existingUser != null) {
                u = existingUser;
            } else {
                u.name = json.getString("name");
                u.remoteId = json.getLong("id");
                u.screenName = json.getString("screen_name");
                u.profileImageUrl = json.getString("profile_image_url");
                u.tagline = json.getString("description");
                u.followersCount = json.getInt("followers_count");
                u.followingCount = json.getInt("friends_count");
                u.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }

    public static User findByScreenName(String screenName) {
        User u = null;
        if (screenName != null) {
            u = new Select().from(User.class)
                    .where("screenName = ?", screenName).executeSingle();

        }
        return u;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.remoteId);
        dest.writeString(this.name);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
        dest.writeString(this.tagline);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.followingCount);
    }

    private User(Parcel in) {
        this.remoteId = in.readLong();
        this.name = in.readString();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.tagline = in.readString();
        this.followersCount = in.readInt();
        this.followingCount = in.readInt();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
