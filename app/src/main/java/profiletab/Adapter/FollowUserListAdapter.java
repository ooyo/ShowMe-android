package profiletab.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import additional.AppController;
import additional.CircledNetworkImageView;
import models.FollowUser;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;
import profiletab.ProfileScreenActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by appleuser on 11/25/16.
 */

public class FollowUserListAdapter extends RecyclerView.Adapter<FollowUserListAdapter.MyViewHolder> {

    private Context mContext;
    private Globals globals;
    private List<FollowUser> followUserList;
    private FollowUser followUser;

    Runnable runnable;
    Handler handler;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView followUserName, followUserDetail;
        public CircledNetworkImageView followUserImage;
        public Button followBtn, relativeBtn;

        public MyViewHolder(View view) {
            super(view);
            followUserName = (TextView) view.findViewById(R.id.followItemUserName);
            followUserDetail = (TextView) view.findViewById(R.id.followUserDetail);
            followUserImage  = (CircledNetworkImageView) view.findViewById(R.id.followItemUserImage);
            followBtn = (Button) view.findViewById(R.id.followItemUserButton);
            relativeBtn = (Button) view.findViewById(R.id.followRelative);

            globals = new Globals();
            followUserName.setTypeface(globals.robotoLight(mContext));
            followUserName.setTextSize(18);
            followUserDetail.setTypeface(globals.robotoLight(mContext));
            followUserDetail.setTextSize(14);
            followBtn.setTypeface(globals.robotoLight(mContext));
            followBtn.setTextSize(16);
        }
    }


    public FollowUserListAdapter(Context mContext, List<FollowUser> albumList) {
        this.mContext = mContext;
        this.followUserList = albumList;

    }

    @Override
    public FollowUserListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_user_item, parent, false);
        return new FollowUserListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FollowUserListAdapter.MyViewHolder holder, int position) {
        final FollowUser event = followUserList.get(position);
        Globals g = new Globals();
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        holder.followUserImage.setImageUrl(event.getPicture(), imageLoader);
        holder.followUserName.setText(event.getName());
        String detail = event.getEvent() + " events/ " + event.getFollow() + " followers";
        holder.followUserDetail.setText(detail);
        if(event.getAmifollow().equalsIgnoreCase("1")){
            holder.followBtn.setText(R.string.following);
            holder.followBtn.setBackgroundColor(mContext.getResources().getColor(R.color.colorOceanBlue));
            holder.followBtn.setTextColor(Color.WHITE);
        }else{
            holder.followBtn.setText(R.string.follow);
        }
        Log.i(TAG, "onBindViewHolder: ID" + event.get_id() + " NAME:" + event.getName());
        holder.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //userFollowUnfollow

                userFollowUnfollow(mContext, event.get_id());
                if(holder.followBtn.getText().toString().equalsIgnoreCase("follow")){
                    holder.followBtn.setText(R.string.following);
                    holder.followBtn.setBackgroundColor(mContext.getResources().getColor(R.color.colorOceanBlue));
                    holder.followBtn.setTextColor(Color.WHITE);
                }else{
                    holder.followBtn.setText(R.string.follow);
                    holder.followBtn.setBackgroundColor(Color.WHITE);
                    holder.followBtn.setTextColor(mContext.getResources().getColor(R.color.colorOceanBlue));
                }
            }
        });
        holder.relativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileScreenActivity.class);
                intent.putExtra("userid", event.get_id());
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return followUserList.size();
    }

    private void userFollowUnfollow(Context context, final String followee_id){
        RequestQueue MyRequestQueue  = Volley.newRequestQueue(context);
        Globals g = new Globals();
        SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid = prefs.getString("userid", null);
        final String topSlideURL = g.getUserFollowUnFollow();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("userid", userid); //Add the data you'd like to send to the server.
                MyData.put("follower_id", followee_id);
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);

    }

    public void swap(List<FollowUser> eventList){
        // this.albumList.clear();
        //  this.albumList = new
        this.followUserList = eventList;
//        if(this.albumList != null){
//            this.albumList.clear();
//            this.albumList.addAll(eventList);
//        }else{
//            this.albumList = eventList;
//        }
        FollowUserListAdapter.this.notifyDataSetChanged();
        //this.notifyDataSetChanged();
    }
}
