package hometab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import additional.MainCardAdapter;
import additional.CustomGridView;
import categorytab.CategoryAdapter;
import categorytab.CategoryChoseActivity;
import detail.EventListActivity;
import general.TopSlideActivity;
import maptab.MapScreenActivity;
import models.EventModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;
import ooyo.mn.showme.SearchActivity;
import profiletab.ProfileScreenActivity;

/**
 * Created by appleuser on 10/21/16.
 */

public class HomeScreenActivity extends AppCompatActivity {
    TopSlideActivity topSlide;
    ViewPager homeSlider;
    PagerAdapter adapter;
    ProgressView progressView;

    private RecyclerView recyclerView;
    private MainCardAdapter cardAdapter;
    private ArrayList<String> slideTitle;
    private ArrayList<String> slideImages;
    private ArrayList<String> slideAddress;
    private  ArrayList<String> slideID;
    private ArrayList<String> slideType;
    private ArrayList<String> slideSlogan;
    private EventModel eventModel;
    private List<EventModel> albumList;
    private String date;
    private Handler handler;
    private int delay = 5000; //milliseconds
    private int page = 0;

    Runnable runnable;


    int[] imageId = {
            R.drawable.cat_activity,
            R.drawable.cat_pub,
            R.drawable.cat_restaurant,
            R.drawable.cat_night,
            R.drawable.cat_coffee,
            R.drawable.cat_mall
    };
    CustomGridView categoryGrid;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tab);
        SharedPreferences prefs = getSharedPreferences("SHOWME", MODE_PRIVATE);
        progressView = (ProgressView) findViewById(R.id.progressView);
        handler = new Handler();

        final String userid = prefs.getString("userid", null);
        if (userid != null) {
            slideID = new ArrayList<String>();
            slideTitle = new ArrayList<String>();
            slideImages = new ArrayList<String>();
            slideAddress = new ArrayList<String>();
            slideType = new ArrayList<String>();
            slideSlogan = new ArrayList<String>();
            TopSlideDataSet(HomeScreenActivity.this, slideTitle, slideImages, slideAddress, slideID, slideType, slideSlogan);
        } else {
            LoginManager.getInstance().logOut();
            AccessToken.getCurrentAccessToken();
            finish();
        }
         CategoryAdapter categoryAdapter = new CategoryAdapter(HomeScreenActivity.this, imageId);
        categoryGrid = (CustomGridView) findViewById(R.id.gridView);
        categoryGrid.setAdapter(categoryAdapter);
        categoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(HomeScreenActivity.this, EventListActivity.class);
                intent.putExtra("userid", userid);
                switch(position) {
                    case 0:
                        intent.putExtra("category", "Activity");
                        break;
                    case 1:
                        intent.putExtra("category", "Pub");
                        break;
                    case 2:
                        intent.putExtra("category", "Restaurant");
                        break;
                    case 3:
                        intent.putExtra("category", "Night Club");
                        break;
                    case 4:
                        intent.putExtra("category", "Coffee Shop");
                        break;
                    case 5:
                        intent.putExtra("category", "Mall");
                        break;
                    default:
                        intent.putExtra("category", "Activity");
                }
                intent.putExtra("from", "home");
                startActivity(intent);
            }
        });


        //Setting cardview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        cardAdapter = new MainCardAdapter(this, albumList);
        eventHomeList(HomeScreenActivity.this);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration( new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);

        TabBarSetting(HomeScreenActivity.this);
        settingView(HomeScreenActivity.this);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, SearchActivity.class);
                intent.putExtra("from", "home");
                startActivity(intent);
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("HomeScreen Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * Adding few albums for testing
     */

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void TopSlideDataSet(Context context, final ArrayList<String> title, final ArrayList<String> images, final ArrayList<String> address, final ArrayList<String> _id, final ArrayList<String> type, final ArrayList<String> slogan) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        this.slideID = _id;
        this.slideTitle = title;
        this.slideImages = images;
        this.slideAddress = address;
        this.slideType = type;
        this.slideSlogan = slogan;
        Globals g = new Globals();
        final String topSlideURL = g.getTopSlideURL();
        SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid = prefs.getString("userid", null);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if (jsonResult.getString("resultCode").equals("0")) {
                        JSONArray results = new JSONArray((jsonResult.getString("result")));
                        Log.i("Hello", "onResponse: " + results.toString());
                        for(int i=0;i<results.length(); i++){
                            JSONObject singleEvent = results.getJSONObject(i);
                            if(singleEvent.getString("contentType").equalsIgnoreCase("advertise")){
                                _id.add(i, singleEvent.getString("_id"));
                                title.add(i, singleEvent.getString("name"));
                                images.add(i, singleEvent.getString("image"));
                                address.add(i, singleEvent.getString("connect"));
                                type.add(i, singleEvent.getString("contentType"));
                                slogan.add(i,  singleEvent.getString("slogan"));
                            }else{
                                _id.add(i, singleEvent.getString("_id"));
                                title.add(i, singleEvent.getString("title"));
                                images.add(i, singleEvent.getString("imagepath"));
                                if(singleEvent.has("place")){
                                    JSONObject place = new JSONObject(singleEvent.getString("place"));
                                    address.add(i, place.getString("place_name"));
                                }else{
                                    address.add(i, "ShowMe Sponsored Event");
                                }
                                type.add(i,  singleEvent.getString("contentType"));
                                slogan.add(i, "slogan");
                            }
                        }

                        // String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

                        View includedLayout = findViewById(R.id.topSlide);
                         homeSlider = (ViewPager) includedLayout.findViewById(R.id.slider);

                         adapter = new TopSlideActivity(HomeScreenActivity.this, images, title, address, _id, type, slogan);

                        homeSlider.setAdapter(adapter);

                        homeSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                page = position;
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                       runnable = new Runnable() {
                            public void run() {
                                if (adapter.getCount() == page) {
                                    page = 0;
                                } else {
                                    page++;
                                }
                                homeSlider.setCurrentItem(page, true);
                                handler.postDelayed(this, delay);
                            }
                        };
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
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

    private void eventHomeList(Context context) {
        progressView.start();
        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final String topSlideURL = g.getEventHomeListURL();
         final SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid = prefs.getString("userid", null);
        final String notif_date = prefs.getString("notif_date", "");
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if (jsonResult.getString("resultCode").equals("0")) {
                        JSONArray results = new JSONArray((jsonResult.getString("result")));
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject singleEvent = results.getJSONObject(i);

                            eventModel = new EventModel(singleEvent.getString("_id"), singleEvent.getString("title"), singleEvent.getString("start_time"),
                                    singleEvent.getString("end_time"), singleEvent.getString("category"), singleEvent.getString("imagepath"),
                                    singleEvent.getJSONArray("goingUserImage"), singleEvent.getString("like"), singleEvent.getString("isGoing"), singleEvent.getString("isInterested"));
                            albumList.add(eventModel);
                        }


                        cardAdapter.notifyDataSetChanged();
                        progressView.stop();
                        final SharedPreferences.Editor editor = getSharedPreferences("SHOWME", MODE_PRIVATE).edit();
                        editor.putString("notif_count", jsonResult.getString("notif").toString());
                        editor.apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressView.stop();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                progressView.stop();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("userid", userid); //Add the data you'd like to send to the server.
                MyData.put("notif", notif_date);
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

    private void TabBarSetting(final Context context) {

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

        btnHomeTab.setSelected(true);

    }

    private void settingView(Context context) {
        Globals globals = new Globals();
        TextView explode = (TextView) findViewById(R.id.homeExploreTitle);
        explode.setTypeface(globals.robotoLight(context));
        explode.setTextSize(20);

        TextView explodeSub = (TextView) findViewById(R.id.homeExploreSubtitle);
        explodeSub.setTypeface(globals.robotoLight(context));
        explodeSub.setTextSize(16);

        TextView homeDiscoverTitle = (TextView) findViewById(R.id.homeDiscoverTitle);
        homeDiscoverTitle.setTypeface(globals.robotoLight(context));
        homeDiscoverTitle.setTextSize(20);

        TextView homeDiscoverDescription = (TextView) findViewById(R.id.homeDiscoverDescription);
        homeDiscoverDescription.setTypeface(globals.robotoLight(context));
        homeDiscoverDescription.setTextSize(16);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }


}
