package com.flaminga.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flaminga.client.R;
import com.flaminga.client.utility.UiUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import twitter4j.Status;

/**
 * Created by mjanes on 2/2/14.
 */
public class TweetArrayAdapter extends ArrayAdapter<Status> {

    Context mContext;

    public TweetArrayAdapter(Context context, List<Status> statuses) {
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
