package additional;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RunnableFuture;

import detail.EventDetailActivity;
import models.EventModel;
import models.FollowUser;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;

import static android.content.Context.MODE_PRIVATE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by appleuser on 10/22/16.
 */

public class MainCardAdapter extends RecyclerView.Adapter<MainCardAdapter.MyViewHolder> {

    private Context mContext;
    private Globals globals;
    private List<EventModel> albumList;
    private FollowUserAdapter followUserAdapter;
    private List<FollowUser> followUserList;
    Runnable runnable;
    Handler handler;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cardTitle;
        public NetworkImageView cardMainImage;
        public ImageView cardBadge,  cardCatImage;
        public CircledNetworkImageView cardGoing1, cardGoing2, cardGoing3;
        public RelativeLayout cardBottomView;
        public Button cardTime, cardLike;
        public CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            cardMainImage = (NetworkImageView) view.findViewById(R.id.cardMainImage);
            cardBadge = (ImageView) view.findViewById(R.id.cardMark);
            cardBottomView = (RelativeLayout) view.findViewById(R.id.cardDetailBottom);
            cardTime = (Button) view.findViewById(R.id.cardTime);
            cardLike = (Button) view.findViewById(R.id.cardLike);
            cardGoing1 = (CircledNetworkImageView) view.findViewById(R.id.cardGoing1);
            cardGoing2 = (CircledNetworkImageView) view.findViewById(R.id.cardGoing2);
            cardGoing3 = (CircledNetworkImageView) view.findViewById(R.id.cardGoing3);
            cardCatImage = (ImageView) view.findViewById(R.id.cardCatImage);
            cardTitle = (TextView) view.findViewById(R.id.cardTitle);
            globals = new Globals();
            cardTitle.setTypeface(globals.robotoLight(mContext));
            cardTitle.setTextSize(21);
        }
    }


    public MainCardAdapter(Context mContext, List<EventModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final EventModel event = albumList.get(position);
        Globals g = new Globals();
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        holder.cardMainImage.setImageUrl(g.getMainStoragePath() + event.getImagepath(), imageLoader);
        holder.cardTitle.setText(event.getTitle());
        holder.cardLike.setText(event.getLike());
        countDownStart(holder.cardTime, event.getEnd_time());
        JSONArray eventGoingUserImage = event.getGoingUserImage();
        holder.cardGoing1.setClickable(false);
        holder.cardGoing2.setClickable(false);
        holder.cardGoing3.setClickable(false);

        try {
            Log.i(TAG, "onBindViewHolder: goinguserimage" + eventGoingUserImage.length());
            if(eventGoingUserImage.length() == 0 ){
                holder.cardGoing1.setImageUrl(eventGoingUserImage.get(0).toString(), imageLoader);
            }else if(eventGoingUserImage.length() == 1 ){
                holder.cardGoing1.setImageUrl(eventGoingUserImage.get(0).toString(), imageLoader);
                holder.cardGoing2.setImageUrl(eventGoingUserImage.get(1).toString(), imageLoader);
            }else if(eventGoingUserImage.length() == 2 ){
                holder.cardGoing1.setImageUrl(eventGoingUserImage.get(0).toString(), imageLoader);
                holder.cardGoing2.setImageUrl(eventGoingUserImage.get(1).toString(), imageLoader);
                holder.cardGoing3.setImageUrl(eventGoingUserImage.get(2).toString(), imageLoader);
            }



        }catch (JSONException e) {
            e.printStackTrace();
        }

        switch (event.getGoingUserImage().length()){
            case 0:
                holder.cardGoing1.setVisibility(View.VISIBLE);
                holder.cardGoing1.setClickable(true);
                break;
            case 1:
                holder.cardGoing1.setVisibility(View.VISIBLE);
                holder.cardGoing2.setVisibility(View.VISIBLE);
                holder.cardGoing1.setClickable(true);
                holder.cardGoing2.setClickable(true);
                break;
            case 2:
                holder.cardGoing1.setVisibility(View.VISIBLE);
                holder.cardGoing2.setVisibility(View.VISIBLE);
                holder.cardGoing3.setVisibility(View.VISIBLE);
                holder.cardGoing1.setClickable(true);
                holder.cardGoing2.setClickable(true);
                holder.cardGoing3.setClickable(true);

                break;
            default:
                holder.cardGoing1.setVisibility(View.INVISIBLE);
                holder.cardGoing2.setVisibility(View.INVISIBLE);
                holder.cardGoing3.setVisibility(View.INVISIBLE);


        }

        if(event.getIsGoing().equalsIgnoreCase("1")){
            holder.cardBadge.setImageResource(R.drawable.going_mark);
            holder.cardBadge.setVisibility(View.VISIBLE);
        }else if(event.getIsInterested().equalsIgnoreCase("1")){
            holder.cardBadge.setImageResource(R.drawable.interested_mark);
            holder.cardBadge.setVisibility(View.VISIBLE);
        }else{
            holder.cardBadge.setImageDrawable(null);
        }

        if(event.getSingleCategory().equalsIgnoreCase("Activity")){
            holder.cardCatImage.setImageResource(R.drawable.cat_activity_round);
        }else if(event.getSingleCategory().equalsIgnoreCase("Coffee Shop")){
            holder.cardCatImage.setImageResource(R.drawable.cat_coffee_round);
        }else if(event.getSingleCategory().equalsIgnoreCase("Lounge")){
            holder.cardCatImage.setImageResource(R.drawable.cat_lounge_round);
        }else if(event.getSingleCategory().equalsIgnoreCase("Mall")){
            holder.cardCatImage.setImageResource(R.drawable.cat_mall_round);
        }else if(event.getSingleCategory().equalsIgnoreCase("Night Club")){
            holder.cardCatImage.setImageResource(R.drawable.cat_night_round);
        }else if(event.getSingleCategory().equalsIgnoreCase("Pub")){
            holder.cardCatImage.setImageResource(R.drawable.cat_pub_round);
        }else if(event.getSingleCategory().equalsIgnoreCase("Restaurant")){
            holder.cardCatImage.setImageResource(R.drawable.cat_restaurant_round);
        }else if(event.getSingleCategory().equalsIgnoreCase("Shop")){
            holder.cardCatImage.setImageResource(R.drawable.cat_shop_round);
        }else if(event.getSingleCategory().equalsIgnoreCase("Theatre")){
            holder.cardCatImage.setImageResource(R.drawable.cat_theatre_round);
        }else{
            holder.cardCatImage.setImageResource(R.drawable.cat_activity_round);
        }

        final OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                          case R.id.btnClose:
                            dialog.dismiss();
                            break;
                        }
                        dialog.dismiss();
            }
        };

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra("eventid", event.get_id().toString());
                mContext.startActivity(intent);
            }
        });


        holder.cardGoing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followUserList = new ArrayList<>();
                followUserAdapter =new FollowUserAdapter(mContext, followUserList);
                eventHomeUserGoingList(mContext, event.get_id().toString());
                final DialogPlus dialog = DialogPlus.newDialog(mContext)
                        .setContentHolder(new ListHolder())
                        .setGravity(Gravity.CENTER)
                        .setHeader(R.layout.header)
                        .setAdapter(followUserAdapter)
                        .setOnClickListener(clickListener)
                        .setCancelable(true)
                        .create();

                dialog.show();
            }
        });

        holder.cardGoing2.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View view) {
                    followUserList = new ArrayList<>();
                    followUserAdapter =new FollowUserAdapter(mContext, followUserList);
                    eventHomeUserGoingList(mContext, event.get_id().toString());
                    final DialogPlus dialog = DialogPlus.newDialog(mContext)
                            .setContentHolder(new ListHolder())
                            .setGravity(Gravity.CENTER)
                            .setHeader(R.layout.header)
                            .setAdapter(followUserAdapter)
                            .setOnClickListener(clickListener)
                            //.setOnDismissListener(dismissListener)
                            //.setOnCancelListener(cancelListener)
                            //.setExpanded(expanded)
                            .setCancelable(true)
                            .create();
                    dialog.show();
                }
            }
        );

        holder.cardGoing3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followUserList = new ArrayList<>();
                followUserAdapter =new FollowUserAdapter(mContext, followUserList);
                eventHomeUserGoingList(mContext, event.get_id().toString());
                final DialogPlus dialog = DialogPlus.newDialog(mContext)
                        .setContentHolder(new ListHolder())
                        .setGravity(Gravity.CENTER)
                        .setHeader(R.layout.header)
                        .setAdapter(followUserAdapter)
                        .setOnClickListener(clickListener)
                        //.setOnDismissListener(dismissListener)
                        //.setOnCancelListener(cancelListener)
                        //.setExpanded(expanded)
                        .setCancelable(true)
                        .create();
                dialog.show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return albumList.size();
    }

    private void eventHomeUserGoingList(Context context, final String eventid){
        RequestQueue MyRequestQueue  = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final String topSlideURL = g.getEvengGoingUserListURL();
        SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid =prefs.getString("userid",null);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if(jsonResult.getString("resultCode").equals("0")){
                        JSONArray results = new JSONArray((jsonResult.getString("result")));
                        for(int i=0;i<results.length(); i++){
                            JSONObject singleUser = results.getJSONObject(i);
                            followUserList.add(i, new FollowUser(singleUser.getString("_id"),singleUser.getString("name"), singleUser.getString("picture"), singleUser.getString("follow"), singleUser.getInt("event"), singleUser.getString("amifollow")));
                        }
                        followUserAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                MyData.put("event_id", eventid);
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }


    public void countDownStart(final Button button, final String dateTime) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    Date eventDate = dateFormat.parse(dateTime);
                    Date currentDate = new Date();
                    if (!currentDate.after(eventDate)) {
                        long diff = eventDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff / (60 * 1000);
                        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;

                        if(days >= 1){
                            button.setText(String.format("%02d days", days));
                        }else if(hours >= 1){
                            button.setText(String.format("%02d hours", hours));
                        }else if(minutes >= 1){
                            button.setText(String.format("%02d hours", minutes));
                        }else{
                            button.setText(String.format("%02d hours", seconds));
                        }
                    } else {
                        button.setText("Over");
                        handler.removeCallbacks(runnable);
                        // handler.removeMessages(0);
                    }
                } catch (Exception e) {
                   // e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    public void swap(List<EventModel> eventList){
       // this.albumList.clear();
      //  this.albumList = new
        this.albumList = eventList;
//        if(this.albumList != null){
//            this.albumList.clear();
//            this.albumList.addAll(eventList);
//        }else{
//            this.albumList = eventList;
//        }
        MainCardAdapter.this.notifyDataSetChanged();
        //this.notifyDataSetChanged();
    }
}