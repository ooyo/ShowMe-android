package ooyo.mn.showme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import detail.EventListActivity;
import hometab.HomeScreenActivity;
import maptab.MapScreenActivity;
import models.EventModel;
import profiletab.ProfileScreenActivity;

/**
 * Created by appleuser on 11/23/16.
 */

public class SearchActivity extends Activity {
    private EventModel eventModel;
    private List<EventModel> albumList;
    private RecyclerView recyclerView;
    private MainCardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        final String TAG = "Search";


        albumList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        cardAdapter = new MainCardAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);

        TabBarSetting(SearchActivity.this);
        final EditText searchTxt = (EditText) findViewById(R.id.searchTxt);
        searchTxt.setHint("Search...");
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "beforeTextChanged: " + searchTxt.getText().toString());
            }
            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                RequestQueue MyRequestQueue  = Volley.newRequestQueue(SearchActivity.this);
                Globals g = new Globals();
                albumList = new ArrayList<>();
                cardAdapter.swap(albumList);
                final String topSlideURL = g.getSearchURL();
                SharedPreferences prefs = SearchActivity.this.getSharedPreferences("SHOWME", MODE_PRIVATE);
                final String userid =prefs.getString("userid",null);
               // albumList.clear();
                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResult = new JSONObject(response);
                            if(jsonResult.getString("resultCode").equals("0")){
                                JSONArray results = new JSONArray((jsonResult.getString("result")));
                                Log.i(TAG, "onResponse: Result " + results.toString());
                                for(int i=0;i<results.length(); i++){
                                    JSONObject singleEvent = results.getJSONObject(i);
                                    eventModel = new EventModel(singleEvent.getString("_id"), singleEvent.getString("title"),singleEvent.getString("start_time"),
                                            singleEvent.getString("end_time"),singleEvent.getString("category"),singleEvent.getString("imagepath"),
                                            singleEvent.getJSONArray("goingUserImage"),singleEvent.getString("like"), singleEvent.getString("isGoing"), singleEvent.getString("isInterested"));
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
                        MyData.put("userfind", charSequence.toString());

                        return MyData;
                    }
                };
                MyRequestQueue.add(MyStringRequest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        }else {
            btnCategoryTab.setSelected(true);
        }

    }
}
