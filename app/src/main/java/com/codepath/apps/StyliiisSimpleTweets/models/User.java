package com.codepath.apps.StyliiisSimpleTweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Table(name="Users")
public class User extends Model {
    @Column(name="remote_id", unique=true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;
    @Column(name="Name")
    public String name;
    @Column(name="ScreenName")
    public String screenName;
    @Column(name="ProfileImageUrl")
    public String profileImageUrl;

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
                u.save();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return u;
    }
}
