package com.codepath.apps.StyliiisSimpleTweets;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "GxRDHZjYnsAP06XmLGn5TE5DI";       // Change this
	public static final String REST_CONSUMER_SECRET = "XdjZKr3xt3k83rM3xgSP5X5D2hyTQMDvzI3nbdfZdVBYyg8FFm"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://simpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(long max_id, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        if (max_id != 0) {
            params.put("max_id", max_id);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void postTweet(String tweet, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        getClient().post(apiURL, params,handler);
    }

    public void getTweet(String tweetId, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/show.json");
        RequestParams params = new RequestParams();
        params.put("id", tweetId);
        getClient().get(apiURL, params, handler);
    }

    public void getAccountInfo(AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("account/verify_credentials.json");
        getClient().get(apiURL, null, handler);
    }

    public void getMentionsTimeline(long max_id, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
//        if (max_id != 0) {
//            params.put("max_id", max_id);
//        }
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, long max_id, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("screen_name", screenName);
//        if (max_id != 0) {
//            params.put("max_id", max_id);
//        }
        getClient().get(apiUrl, params, handler);
    }

    public void getUserInfo(String screenName, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }
}