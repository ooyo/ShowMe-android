package detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import additional.AppController;
import additional.CircledNetworkImageView;
import models.EventModel;
import models.ReviewModel;
import ooyo.mn.showme.R;

/**
 * Created by appleuser on 10/24/16.
 */

public class ReviewsAdapter  extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ReviewModel> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reviewUsername, reviewUserDescription;
        public CircledNetworkImageView reviewUserimage;

        public MyViewHolder(View view) {
            super(view);
            reviewUserimage = (CircledNetworkImageView) view.findViewById(R.id.reviewUserImage);
            reviewUsername = (TextView) view.findViewById(R.id.reviewUsername);
            reviewUserDescription = (TextView) view.findViewById(R.id.reviewUserDescription);

        }
    }


    public ReviewsAdapter(Context mContext, List<ReviewModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public ReviewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new ReviewsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReviewsAdapter.MyViewHolder holder, int position) {
        ReviewModel reviews = albumList.get(position);

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        holder.reviewUserimage.setImageUrl(reviews.getPicture(), imageLoader);
        holder.reviewUsername.setText(reviews.getName());
        holder.reviewUserDescription.setText(reviews.getUserReview());

//        if(event.getIsGoing().equalsIgnoreCase("1")){
//            holder.cardBadge.setImageResource(R.drawable.going_mark);
//            holder.cardBadge.setVisibility(View.VISIBLE);
//        }else if(event.getIsInterested().equalsIgnoreCase("1")){
//            holder.cardBadge.setImageResource(R.drawable.interested_mark);
//            holder.cardBadge.setVisibility(View.VISIBLE);
//        }else{
//            holder.cardBadge.setImageDrawable(null);
//        }



//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("Cardview", "Clicked");
//                Intent intent = new Intent(mContext, EventDetailActivity.class);
//                intent.putExtra("eventid", event.get_id().toString());
//                mContext.startActivity(intent);
//            }
//        });
    }



    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
