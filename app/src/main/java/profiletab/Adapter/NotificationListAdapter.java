package profiletab.Adapter;

import android.content.Context;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import additional.AppController;
import models.NotificationModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;

/**
 * Created by appleuser on 11/28/16.
 */

public class NotificationListAdapter  extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<NotificationModel> followUserList;
    private NotificationModel followUser;
    private Context context;
    public NotificationListAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }
    public NotificationListAdapter(Context context, List<NotificationModel> followUserList) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.followUserList = followUserList;
    }

    @Override
    public int getCount() {
        return followUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        NotificationListAdapter.ViewHolder viewHolder;
        View view = convertView;
        Globals globals = new Globals();
        followUser = followUserList.get(position);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.notification_item, parent, false);
            viewHolder = new NotificationListAdapter.ViewHolder();
            viewHolder.notificationText = (TextView) view.findViewById(R.id.notificationText);
            viewHolder.notificationImage = (NetworkImageView) view.findViewById(R.id.notificationImage);
            view.setTag(viewHolder);

        } else {
            viewHolder = (NotificationListAdapter.ViewHolder) view.getTag();
        }
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        if(followUser.getType().equals("user")){
            viewHolder.notificationImage.setImageUrl(followUser.getPicture(), imageLoader);
            viewHolder.notificationText.setText(followUser.getName());
            viewHolder.notificationText.setTypeface(globals.robotoLight(context));
        }else if(followUser.getType().equals("event")){
            viewHolder.notificationImage.setImageUrl(globals.getMainStoragePath() + followUser.getPicture(), imageLoader);
            viewHolder.notificationText.setText(followUser.getName() + " " + followUser.getMessage());
            viewHolder.notificationText.setTypeface(globals.robotoLight(context));
        }else if(followUser.getType().equals("admin")){
            viewHolder.notificationImage.setVisibility(View.VISIBLE);
            viewHolder.notificationImage.setImageUrl(followUser.getPicture(), imageLoader);
            viewHolder.notificationText.setText(followUser.getMessage());
            viewHolder.notificationText.setTypeface(globals.robotoLight(context));
        }else if(followUser.getType().equals("place")){
            viewHolder.notificationImage.setImageUrl(globals.getPlaceImagePathURL() + followUser.getPicture(), imageLoader);
            viewHolder.notificationText.setText(followUser.getName() + " " + followUser.getMessage());
            viewHolder.notificationText.setTypeface(globals.robotoLight(context));
        }
        return view;
    }

    static class ViewHolder {
        TextView notificationText;
        NetworkImageView notificationImage;
    }
}
