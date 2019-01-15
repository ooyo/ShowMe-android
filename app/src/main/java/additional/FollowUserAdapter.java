package additional;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.BonusModel;
import models.FollowUser;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;
import profiletab.ProfileScreenActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by appleuser on 10/25/16.
 */

public class FollowUserAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<FollowUser> followUserList;
    private FollowUser followSingleUser;
    private Context context;
    public FollowUserAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }
    public FollowUserAdapter(Context context, List<FollowUser> followUserList) {
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
        final ViewHolder viewHolder;
        View view = convertView;
        Globals globals = new Globals();
        followSingleUser = followUserList.get(position);
        if (view == null) {
             view = layoutInflater.inflate(R.layout.follow_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.followRelative = (Button) view.findViewById(R.id.followRelative);
            viewHolder.textView = (TextView) view.findViewById(R.id.followItemUserName);
            viewHolder.imageView = (CircledNetworkImageView) view.findViewById(R.id.followItemUserImage);
            viewHolder.button = (Button) view.findViewById(R.id.followItemUserButton);

            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //userFollowUnfollow
                    FollowUser selected = followUserList.get(position);
                       userFollowUnfollow(context, selected.get_id());
                    if(viewHolder.button.getText().toString().equalsIgnoreCase("follow")){
                        viewHolder.button.setText(R.string.following);
                        viewHolder.button.setBackgroundColor(context.getResources().getColor(R.color.colorOceanBlue));
                        viewHolder.button.setTextColor(Color.WHITE);
                    }else{
                        viewHolder.button.setText(R.string.follow);
                        viewHolder.button.setBackgroundColor(Color.WHITE);
                        viewHolder.button.setTextColor(context.getResources().getColor(R.color.colorOceanBlue));
                    }
                }
            });
            viewHolder.followRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FollowUser selected = followUserList.get(position);
                     Intent intent = new Intent(context, ProfileScreenActivity.class);
                    intent.putExtra("userid", selected.get_id());
                    context.startActivity(intent);
                }
            });

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        viewHolder.imageView.setImageUrl(followSingleUser.getPicture(), imageLoader);
        viewHolder.textView.setText(followSingleUser.getName());
        if(followSingleUser.getAmifollow().equalsIgnoreCase("1")){
            viewHolder.button.setText(R.string.following);
            viewHolder.button.setBackgroundColor(context.getResources().getColor(R.color.colorOceanBlue));
            viewHolder.button.setTextColor(Color.WHITE);
        }else{
            viewHolder.button.setText(R.string.follow);
        }
        viewHolder.textView.setTypeface(globals.robotoLight(context));
        viewHolder.button.setTypeface(globals.robotoLight(context));
        return view;
    }
    static class ViewHolder {
        TextView textView;
        CircledNetworkImageView imageView;
        Button button;
        Button followRelative;
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

}