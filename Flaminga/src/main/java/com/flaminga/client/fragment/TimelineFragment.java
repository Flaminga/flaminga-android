package com.flaminga.client.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaminga.client.Flaminga;
import com.flaminga.client.R;
import com.flaminga.client.adapter.TweetArrayAdapter;
import com.flaminga.client.model.Account;
import com.flaminga.client.KeyConstant;
import com.flaminga.client.utility.UiUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by mjanes on 2/1/14.
 */
public class TimelineFragment extends ListFragment {

    /***********************************************************************************************
     * Constructors and factory methods
     **********************************************************************************************/

    public TimelineFragment() {}

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }


    /***********************************************************************************************
     * Lifecycle methods
     **********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new getTimeLine().execute();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_timeline, parent, false);

        initializeList(view);

        return view;
    }


    /***********************************************************************************************
     * Utility
     **********************************************************************************************/

    public void initializeList(View view) {

    }

    /**
     * Function to get timeline
     * */
    class getTimeLine extends AsyncTask<Void, Void, TweetArrayAdapter> {

        /**
         * getting Places JSON
         * */
        protected TweetArrayAdapter doInBackground(Void... args) {

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(KeyConstant.TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(KeyConstant.TWITTER_CONSUMER_SECRET);

                // Access Token
                Account account = Flaminga.getAccount();
                String accessToken = account.getAccessToken();
                // Access Token Secret
                String accessTokenSecret = account.getAccessSecret();

                AccessToken fullAccessToken = new AccessToken(accessToken,accessTokenSecret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(fullAccessToken);

                //Twitter twitter = new TwitterFactory().getInstance();
                User user = twitter.verifyCredentials();
                List<twitter4j.Status> statuses = twitter.getHomeTimeline();

                return new TweetArrayAdapter(getActivity().getApplicationContext(), statuses);

            } catch (TwitterException e) {
                // Error in updating status
                Log.d("Twitter Update Error", e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(TweetArrayAdapter adapter) {
            getListView().setAdapter(adapter);
        }

    }
}
