package com.flaminga.client.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaminga.client.Flaminga;
import com.flaminga.client.KeyConstant;
import com.flaminga.client.R;
import com.flaminga.client.model.Account;
import com.flaminga.client.utility.TwitterManager;
import com.flaminga.client.utility.UiUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by mjanes on 2/2/14.
 */
public class TweetArrayAdapter extends ArrayAdapter<Status> {

    public static final String TAG = "TweetArrayAdapter";

    Context mContext;
    final TwitterManager mTwitterManager;

    public TweetArrayAdapter(Context context, List<Status> statuses) {
        super(context, 0, statuses);
        mContext = context;

        mTwitterManager = new TwitterManager();
    }

    static class ViewHolder {
        ImageView avatar;
        TextView screenName;
        TextView name;
        TextView status;
        ImageButton reply;
        ImageButton retweet;
        ImageButton favorite;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // get the event corresponding to the list item at position
        final twitter4j.Status status = getItem(position);


        // if the view to modify is null, then create a new one
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_list_item, null);

            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.screenName = (TextView) convertView.findViewById(R.id.screen_name);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.reply = (ImageButton) convertView.findViewById(R.id.reply_button);
            holder.retweet = (ImageButton) convertView.findViewById(R.id.retweet_button);
            holder.favorite = (ImageButton) convertView.findViewById(R.id.favorite_button);

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


        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Going to need to create a text input area
            }
        });

        holder.retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTwitterManager.retweet(status.getId());
            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTwitterManager.favorite(status.getId());
            }
        });

        return convertView;
    }
}
