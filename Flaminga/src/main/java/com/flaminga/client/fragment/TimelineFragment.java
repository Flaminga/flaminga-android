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

    private static class TimelineListAdapter extends ArrayAdapter<twitter4j.Status> {

        Context mContext;

        public TimelineListAdapter(Context context, List<twitter4j.Status> statuses) {
            super(context, 0, statuses);
            mContext = context;
        }

        static class ViewHolder {
            ImageView avatar;
            TextView screenName;
            TextView name;
            TextView status;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // get the event corresponding to the list item at position
            twitter4j.Status status = getItem(position);


            // if the view to modify is null, then create a new one
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.status_list_item, null);

                holder = new ViewHolder();
                holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                holder.screenName = (TextView) convertView.findViewById(R.id.screen_name);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.status = (TextView) convertView.findViewById(R.id.status);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String imageUrl = status.getUser().getProfileImageURL();
            if(!imageUrl.equals("")){
                Picasso.Builder build;
                build = new Picasso.Builder(mContext);
                build.memoryCache(UiUtil.getPicassoCache());
                //int avatarDimen = (int) mContext.getResources().getDimension(R.dimen.avatar_width_height_medium);
                int avatarDimen = 64;
                build.build().load(imageUrl).resize(avatarDimen, avatarDimen).centerCrop().into(holder.avatar);
            }
            holder.screenName.setText(status.getUser().getScreenName());
            holder.name.setText(status.getUser().getName());
            holder.status.setText(status.getText());

            return convertView;
        }
    }

    /**
     * Function to get timeline
     * */
    class getTimeLine extends AsyncTask<Void, Void, TimelineListAdapter> {

        /**
         * getting Places JSON
         * */
        protected TimelineListAdapter doInBackground(Void... args) {

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

                return new TimelineListAdapter(getActivity().getApplicationContext(), statuses);

            } catch (TwitterException e) {
                // Error in updating status
                Log.d("Twitter Update Error", e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(TimelineListAdapter adapter) {
            getListView().setAdapter(adapter);
        }

    }
}
