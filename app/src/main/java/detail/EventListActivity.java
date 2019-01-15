package detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import additional.MainCardAdapter;
import categorytab.CategoryChoseActivity;
import hometab.HomeScreenActivity;
import maptab.MapScreenActivity;
import models.EventModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;
import profiletab.ProfileScreenActivity;

/**
 * Created by appleuser on 10/28/16.
 */

public class EventListActivity extends AppCompatActivity {
    private EventModel eventModel;
    private List<EventModel> albumList;
    private RecyclerView recyclerView;
    private MainCardAdapter cardAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        cardAdapter = new MainCardAdapter(this, albumList);
        eventHomeList(EventListActivity.this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration( new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);


        TabBarSetting(EventListActivity.this);



        Intent extras = getIntent();
        ImageView eventListImage = (ImageView) findViewById(R.id.eventListImage);
        if(extras.getStringExtra("category").equalsIgnoreCase("Activity")){
            eventListImage.setBackgroundResource(R.color.colorActivity);
        }else if(extras.getStringExtra("category").equalsIgnoreCase("Coffee Shop")){
            eventListImage.setBackgroundResource(R.color.colorCoffee);
        }else if(extras.getStringExtra("category").equalsIgnoreCase("Lounge")){
            eventListImage.setBackgroundResource(R.color.colorLounge);
        }else if(extras.getStringExtra("category").equalsIgnoreCase("Mall")){
            eventListImage.setBackgroundResource(R.color.colorMall);
        }else if(extras.getStringExtra("category").equalsIgnoreCase("Night Club")){
            eventListImage.setBackgroundResource(R.color.colorNight);
        }else if(extras.getStringExtra("category").equalsIgnoreCase("Pub")){
            eventListImage.setBackgroundResource(R.color.colorPub);
        }else if(extras.getStringExtra("category").equalsIgnoreCase("Restaurant")){
            eventListImage.setBackgroundResource(R.color.colorRestaurant);
        }else if(extras.getStringExtra("category").equalsIgnoreCase("Shop")){
            eventListImage.setBackgroundResource(R.color.colorShop);
        }else if(extras.getStringExtra("category").equalsIgnoreCase("Theatre")){
            eventListImage.setBackgroundResource(R.color.colorTheatre);
        }else{
            eventListImage.setBackgroundResource(R.color.colorPrimary);
        }

    }

    private void eventHomeList(Context context){
        RequestQueue MyRequestQueue  = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final Intent extra=getIntent();
        final String topSlideURL;
        if(extra.getStringExtra("from").equalsIgnoreCase("map")){
            topSlideURL = g.getPlaceEventListByID();
        }else{
            topSlideURL  = g.getEvengCategoryEventListURL();
        }


        SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid =prefs.getString("userid",null);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if(jsonResult.getString("resultCode").equals("0")){
                        JSONArray results = new JSONArray((jsonResult.getString("result")));
                        Log.i("JSON Place Event", "onResponse: " + results.toString());
                        for(int i=0;i<results.length(); i++){
                            JSONObject singleEvent = results.getJSONObject(i);
                            eventModel = new EventModel(singleEvent.getString("_id"), singleEvent.getString("title"),singleEvent.getString("start_time"),
                                    singleEvent.getString("end_time"),singleEvent.getString("category"),singleEvent.getString("imagepath"),
                                    singleEvent.getJSONArray("goingUserImage"), singleEvent.getString("like"), singleEvent.getString("isGoing"), singleEvent.getString("isInterested"));
                            albumList.add(eventModel);


                        }
                        cardAdapter.notifyDataSetChanged();
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
                MyData.put("category", extra.getStringExtra("category"));
                Log.i("JSON Place Event", "onResponse: " + extra.getStringExtra("category"));
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

    private void TabBarSetting(final Context context) {
     Intent extra= getIntent();
        View includedLayout = findViewById(R.id.include);
        Button btnHomeTab = (Button) includedLayout.findViewById(R.id.homeTAB);
        Button btnCategoryTab = (Button) includedLayout.findViewById(R.id.categoryTAB);
        Button btnMapTab = (Button) includedLayout.findViewById(R.id.mapTAB);
        Button btnProfileTab = (Button) includedLayout.findViewById(R.id.profileTAB);

        Globals globals = new Globals();
        btnHomeTab.setTypeface(globals.robotoLight(context));
        btnCategoryTab.setTypeface(globals.robotoLight(context));
        btnMapTab.setTypeface(globals.robotoLight(context));
        btnProfileTab.setTypeface(globals.robotoLight(context));
        btnHomeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, HomeScreenActivity.class));
            }
        });
        btnCategoryTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CategoryChoseActivity.class));
            }
        });

        btnMapTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MapScreenActivity.class));
            }
        });
        btnProfileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ProfileScreenActivity.class));
            }
        });


        if (extra.getStringExtra("from").equalsIgnoreCase("home")) {
            btnHomeTab.setSelected(true);
        }else if(extra.getStringExtra("from").equalsIgnoreCase("map")){
            btnMapTab.setSelected(true);
        }else {
            btnCategoryTab.setSelected(true);
        }

    }
}
