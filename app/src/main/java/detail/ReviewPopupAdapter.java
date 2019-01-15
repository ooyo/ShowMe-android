package detail;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import additional.AppController;
import additional.CircledNetworkImageView;
import models.ReviewModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;

/**
 * Created by appleuser on 11/1/16.
 */

public class ReviewPopupAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<ReviewModel> followUserList;
    private ReviewModel followUser;
    private Context context;
    public ReviewPopupAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }
    public ReviewPopupAdapter(Context context, List<ReviewModel> followUserList) {
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
        ReviewPopupAdapter.ViewHolder viewHolder;
        View view = convertView;
        Globals globals = new Globals();
        followUser = followUserList.get(position);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.review_item, parent, false);
            viewHolder = new ReviewPopupAdapter.ViewHolder();
            viewHolder.userName = (TextView) view.findViewById(R.id.reviewUsername);
            viewHolder.imageView = (CircledNetworkImageView) view.findViewById(R.id.reviewUserImage);
            viewHolder.userReview = (TextView) view.findViewById(R.id.reviewUserDescription);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ReviewPopupAdapter.ViewHolder) view.getTag();
        }
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        viewHolder.imageView.setImageUrl(followUser.getPicture(), imageLoader);
        viewHolder.userName.setText(followUser.getName());
        viewHolder.userReview.setText(followUser.getUserReview());
        viewHolder.userName.setTypeface(globals.robotoLight(context));
        viewHolder.userReview.setTypeface(globals.robotoLight(context));
        return view;
    }

    static class ViewHolder {
        TextView userName, userReview;
        CircledNetworkImageView imageView;
    }
}
