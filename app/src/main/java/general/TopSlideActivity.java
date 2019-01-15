package general;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.daimajia.slider.library.SliderLayout;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import additional.AppController;
import detail.EventDetailActivity;
import hometab.HomeScreenActivity;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.MainActivity;
import ooyo.mn.showme.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by appleuser on 10/21/16.
 */

public class TopSlideActivity extends PagerAdapter {
    Context context;
    ArrayList<String> images;
    ArrayList<String> title;
    ArrayList<String> address;
    ArrayList<String> _id;
    ArrayList<String> type;
    ArrayList<String> slogan;
    public TopSlideActivity(Context context, ArrayList<String> images, ArrayList<String> title, ArrayList<String> address, ArrayList<String> _id, ArrayList<String> type, ArrayList<String> slogan) {
        this.context = context;
        this.title = title;
        this.images = images;
        this.address = address;
        this._id = _id;
        this.type = type;
        this.slogan = slogan;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
         LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        Globals globals = new Globals();
        View viewItem = inflater.inflate(R.layout.slider_item, container, false);

        if(type.get(position).equalsIgnoreCase("advertise")){
            NetworkImageView imgNetwork = (NetworkImageView) viewItem.findViewById(R.id.imageView);
            imgNetwork.setImageUrl(globals.getAdvertisePathURL() + images.get(position), imageLoader);
            TextView textView1 = (TextView) viewItem.findViewById(R.id.sliderTitle);
            textView1.setText(title.get(position));
            textView1.setTypeface(globals.robotoLight(context));
            textView1.setTextSize(23);
            TextView textView2 = (TextView) viewItem.findViewById(R.id.sliderAddress);
            textView2.setText(slogan.get(position));
            textView2.setTypeface(globals.robotoLight(context));
            textView2.setTextSize(18);
            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(address.get(position).toLowerCase());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
        }else{
            NetworkImageView imgNetwork = (NetworkImageView) viewItem.findViewById(R.id.imageView);
            imgNetwork.setImageUrl(globals.getMainStoragePath() + images.get(position), imageLoader);
            TextView textView1 = (TextView) viewItem.findViewById(R.id.sliderTitle);
            textView1.setText(title.get(position));
            textView1.setTypeface(globals.robotoLight(context));
            textView1.setTextSize(23);
            TextView textView2 = (TextView) viewItem.findViewById(R.id.sliderAddress);
            textView2.setText(address.get(position));
            textView2.setTypeface(globals.robotoLight(context));
            textView2.setTextSize(18);
            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EventDetailActivity.class);
                    intent.putExtra("eventid", _id.get(position));
                    context.startActivity(intent);
                }
            });
        }

        ((ViewPager) container).addView(viewItem);
        return viewItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub

        return view == ((View) object);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }
}