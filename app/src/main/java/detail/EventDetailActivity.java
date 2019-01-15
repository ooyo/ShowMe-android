package detail;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import additional.AppController;
import additional.FollowUserAdapter;
import additional.MainCardAdapter;
import hometab.HomeScreenActivity;
import maptab.MapScreenActivity;
import maptab.MyLocation;
import models.EventModel;
import models.ReviewModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;

/**
 * Created by appleuser on 10/21/16.
 */

public class EventDetailActivity extends FragmentActivity implements OnMapReadyCallback {
    public CardView eventDetailCard, eventDetailBonusCard, eventDetailAddressCard;
    public NetworkImageView eventMainImage;
    public RelativeLayout eventCardBottom, eventBonusBG;
    public Button eventAddress, eventTime, eventInterested, eventGoing, eventGoingCount, eventInterestCount;
    public Button eventField1, eventField2, eventField3, eventSeeAllReview, eventWriteReview;
    public TextView eventImageCount, eventTitle, eventAboutTitle, eventAboutDescription;
    public TextView eventBonusUser, eventBonusDescription, eventDetailAddressTitle;
    public ImageButton eventDetailShare;
    public RecyclerView eventReviews;
    private List<ReviewModel> reviewList;
    private EventModel eventModel;
    private ReviewModel reviewModel;
    private GoogleMap mMap;
    private Marker customMarker;
    private boolean field1status, field2status, field3status, goingChecker, interestedChecker;
    private String field1name, field1value, field2name, field2value, field3name, field3value;
    public ReviewsAdapter cardAdapter;
    public ReviewPopupAdapter reviewPopupAdapter;
    public  ScrollView detailScroll;
    public String placeImagePath, placeName, placeSlogan;
    private  double latitudeLocation, longitudeLocation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        settingView();

        detailScroll = (ScrollView) findViewById(R.id.detailScroll);


        final Intent extra = getIntent();

        eventMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventDetailActivity.this, GalleryView.class);
                intent.putExtra("eventid", extra.getStringExtra("eventid"));
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences("SHOWME", MODE_PRIVATE);

        final String userid = prefs.getString("userid", null);
        if (userid != null) {
            //Setting cardview
            reviewList = new ArrayList<>();
            cardAdapter = new ReviewsAdapter(this, reviewList);
            reviewPopupAdapter = new ReviewPopupAdapter(this, reviewList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            eventReviews.setLayoutManager(mLayoutManager);
            //recyclerView.addItemDecoration( new GridSpacingItemDecoration(2, dpToPx(10), true));
            eventReviews.setItemAnimator(new DefaultItemAnimator());
            eventReviews.setAdapter(cardAdapter);
            eventDetailDataSet(EventDetailActivity.this);


        } else {
            LoginManager.getInstance().logOut();
            AccessToken.getCurrentAccessToken();
            finish();
        }



        placeImagePath = "";
        placeName = "";
        placeSlogan ="";


        eventField1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(EventDetailActivity.this, R.animator.flipping);
                anim.setTarget(view);
                anim.setDuration(500);
                anim.start();
                if (field1status) {
                    eventField1.setText(field1name);
                    field1status = false;
                } else {
                    eventField1.setText(field1value);
                    field1status = true;
                }

            }
        });

        eventField2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(EventDetailActivity.this, R.animator.flipping);
                anim.setTarget(view);
                anim.setDuration(500);
                anim.start();
                if (field2status) {
                    eventField2.setText(field2name);
                    field2status = false;
                } else {
                    eventField2.setText(field2value);
                    field2status = true;
                }

            }
        });


        eventField3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(EventDetailActivity.this, R.animator.flipping);
                anim.setTarget(view);
                anim.setDuration(500);
                anim.start();
                if (field3status) {
                    eventField3.setText(field3name);
                    field3status = false;
                } else {
                    eventField3.setText(field3value);
                    field3status = true;
                }

            }
        });

        eventGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (goingChecker) {
                    eventGoing.setBackground(ContextCompat.getDrawable(EventDetailActivity.this, R.drawable.border_white));
                    goingChecker = false;
                } else {
                    eventGoing.setText(R.string.going);
                    eventGoing.setBackgroundColor(getResources().getColor(R.color.colorOceanBlue));
                    eventGoing.setTextColor(Color.WHITE);
                    goingChecker = true;
                }

                userGoingClicked(EventDetailActivity.this, extra.getStringExtra("eventid"), userid);
            }
        });

        eventInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interestedChecker) {
                    eventInterested.setBackground(ContextCompat.getDrawable(EventDetailActivity.this, R.drawable.border_white));

                    interestedChecker = false;
                } else {
                    eventInterested.setText(R.string.interested);
                    eventInterested.setBackgroundColor(getResources().getColor(R.color.colorOceanBlue));
                    eventInterested.setTextColor(Color.WHITE);
                    interestedChecker = true;
                }
                userInterestedClicked(EventDetailActivity.this, extra.getStringExtra("eventid"), userid);
            }
        });



        //Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void eventDetailDataSet(Context context) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final String topSlideURL = g.getEventDetailURL();
        final Intent extra = getIntent();
        SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid = prefs.getString("userid", null);

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                jsonDataSet(response);
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
                MyData.put("eventid", extra.getStringExtra("eventid"));
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);

    }

    private void jsonDataSet(String response){
        final Intent extra = getIntent();
        Globals globals = new Globals();
        try {

            JSONObject jsonResult = new JSONObject(response);
            Log.d("JSON EventDetail", "onResponse: "+jsonResult.toString());

            if (jsonResult.getString("resultCode").equals("0")) {
                JSONObject event = new JSONObject((jsonResult.getString("result")));
                if(event.has("fields")){
                    JSONObject fields = new JSONObject(event.getString("fields"));
                    eventField1.setText(fields.getString("field1_name"));
                    field1name = fields.getString("field1_name");
                    field1value = fields.getString("field1_value");
                    eventField2.setText(fields.getString("field2_name"));
                    field2name = fields.getString("field2_name");
                    field2value = fields.getString("field2_value");
                    eventField3.setText(fields.getString("field3_name"));
                    field3name = fields.getString("field3_name");
                    field3value = fields.getString("field3_value");
                }

                if(event.has("reviews")){
                    JSONArray reviews = new JSONArray(event.getString("reviews"));

                    for (int i = 0; i < reviews.length(); i++) {
                        JSONObject review = reviews.getJSONObject(i);
                        String name, picture;
                        if(review.getString("name") != null && !review.getString("name").isEmpty() && review.getString("name").equals("null")){
                            name = " ";
                        }else{
                            name = review.getString("name");
                        }

                        if(review.getString("picture") != null && !review.getString("picture").isEmpty() && review.getString("picture").equals("null")){
                            picture = globals.getNoImage();
                        }else{
                            picture = review.getString("picture");

                        }
                        Log.i("Add review picture", "jsonDataSet: " + picture);
                        reviewModel = new ReviewModel(review.getString("user_id"), name, picture, review.getString("user_review"));

                        reviewList.add(i, reviewModel);
                    }

                    //Log.i("Review List", "jsonDataSet: " + reviewList.toString());
                    cardAdapter.notifyDataSetChanged();
                    reviewPopupAdapter.notifyDataSetChanged();

                    if (reviews.length() < 10) {
                        eventSeeAllReview.setVisibility(View.GONE);
                    } else {
                        eventSeeAllReview.setVisibility(View.VISIBLE);
                    }

                }

                if(event.has("bonus")){
                    JSONObject bonus = new JSONObject(event.getString("bonus"));
                    eventDetailBonusCard.setVisibility(View.VISIBLE);
                    eventBonusUser.setText(bonus.getString("name"));
                    eventBonusDescription.setText(bonus.getString("description"));

                }

                if(event.has("place")){
                    JSONObject place = new JSONObject(event.getString("place"));
                    latitudeLocation = Double.parseDouble(place.getString("place_lat"));
                    longitudeLocation = Double.parseDouble(place.getString("place_lon"));
                    placeImagePath = globals.getPlaceImagePathURL() + place.getString("place_logo");
                    if(place.has("place_name")){
                        placeName = place.getString("place_name");
                    }
                    if(event.has("title")){
                        placeSlogan = event.getString("title");
                    }
                    addCustomMarker2(latitudeLocation,longitudeLocation,placeImagePath,placeName,placeSlogan, "");
                }


                ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                eventMainImage.setImageUrl(globals.getMainStoragePath() + event.getString("picture"), imageLoader);
                eventAddress.setText(event.getString("placeAddress"));
                eventTime.setText(event.getString("start_time"));
                eventInterestCount.setText(event.getString("interestCount"));
                eventGoingCount.setText(event.getString("goingCount"));

                eventImageCount.setText(event.getString("imageCount"));
                eventTitle.setText(event.getString("title"));
                eventAboutTitle.setText(R.string.about);
                eventAboutDescription.setText(event.getString("description"));




                eventDetailAddressTitle.setText(R.string.address);


                if(event.has("isGoing")) {
                    if (event.getString("isGoing").equalsIgnoreCase("1")) {
                        eventGoing.setBackgroundColor(getResources().getColor(R.color.colorOceanBlue));
                        eventGoing.setTextColor(Color.WHITE);
                        goingChecker = true;
                    }
                }

                if(event.has("isInterested")) {
                    if (event.getString("isInterested").equalsIgnoreCase("1")) {
                        eventInterested.setBackgroundColor(getResources().getColor(R.color.colorOceanBlue));
                        eventInterested.setTextColor(Color.WHITE);
                        interestedChecker = true;
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(DialogPlus dialog, View view) {
                switch (view.getId()) {
                    case R.id.userReviewSend:
                        TextView userReviewMessage = (TextView) findViewById(R.id.userReviewText);
                        userFullReviewListShow(EventDetailActivity.this, extra.getStringExtra("eventid"), userReviewMessage.getText().toString());
                        dialog.dismiss();
                        break;
                    case R.id.btnClose:
                        dialog.dismiss();
                        break;
                }
                dialog.dismiss();
            }
        };


        eventWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialog = DialogPlus.newDialog(EventDetailActivity.this)
                        .setContentHolder(new ListHolder())
                        .setGravity(Gravity.CENTER)
                        .setHeader(R.layout.header)
                        .setFooter(R.layout.footer)
                        .setAdapter(reviewPopupAdapter)
                        .setOnClickListener(clickListener)
                        //.setOnDismissListener(dismissListener)
                        //.setOnCancelListener(cancelListener)
                        //.setExpanded(expanded)
                        .setCancelable(true)
                        .create();

                dialog.show();
            }
        });

        eventSeeAllReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialog = DialogPlus.newDialog(EventDetailActivity.this)
                        .setContentHolder(new ListHolder())
                        .setGravity(Gravity.CENTER)
                        .setHeader(R.layout.header)
                        .setFooter(R.layout.footer)
                        .setAdapter(reviewPopupAdapter)
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

    private void settingView() {
        Globals globals = new Globals();
        eventDetailCard = (CardView) findViewById(R.id.eventDetailCard);
        eventDetailBonusCard = (CardView) findViewById(R.id.eventDetailBonusCard);
        eventDetailAddressCard = (CardView) findViewById(R.id.eventDetailAddressCard);
        eventMainImage = (NetworkImageView) findViewById(R.id.eventImage);
        eventAddress = (Button) findViewById(R.id.eventDetailAddress);
        eventTime = (Button) findViewById(R.id.eventDetailTime);
        eventInterested = (Button) findViewById(R.id.eventDetailInterested);
        eventInterestCount = (Button) findViewById(R.id.eventDetailInterestedCount);
        eventGoing = (Button) findViewById(R.id.eventDetailGoing);
        eventGoingCount = (Button) findViewById(R.id.eventDetailGoingCount);
        eventField1 = (Button) findViewById(R.id.eventDetailField1);
        eventField2 = (Button) findViewById(R.id.eventDetailField2);
        eventField3 = (Button) findViewById(R.id.eventDetailField3);
        eventSeeAllReview = (Button) findViewById(R.id.eventDetailSeeAllReviews);
        eventWriteReview = (Button) findViewById(R.id.eventDetailWriteReview);
        eventImageCount = (TextView) findViewById(R.id.eventImageCount);
        eventTitle = (TextView) findViewById(R.id.eventDetailTitle);
        eventAboutTitle = (TextView) findViewById(R.id.eventDetailAboutTitle);
        eventAboutDescription = (TextView) findViewById(R.id.eventDetailAbout);
        eventBonusUser = (TextView) findViewById(R.id.eventDetailBonusUser);
        eventBonusDescription = (TextView) findViewById(R.id.eventDetailBonusDescription);
        eventDetailAddressTitle = (TextView) findViewById(R.id.eventDetailAddressTitle);
        eventDetailShare = (ImageButton) findViewById(R.id.eventDetailShare);
        eventReviews = (RecyclerView) findViewById(R.id.eventDetailUserReviews);

        eventGoing.setTypeface(globals.robotoLight(EventDetailActivity.this));
        eventInterested.setTypeface(globals.robotoLight(EventDetailActivity.this));
        eventGoingCount.setTypeface(globals.robotoLight(EventDetailActivity.this));
        eventInterestCount.setTypeface(globals.robotoLight(EventDetailActivity.this));
        eventGoingCount.setTextSize(20);
        eventInterestCount.setTextSize(20);

        eventTitle.setTypeface(globals.robotoLight(EventDetailActivity.this));
        eventTitle.setTextSize(23);
        eventTime.setTypeface(globals.robotoLight(EventDetailActivity.this));

        eventAddress.setTypeface(globals.robotoLight(EventDetailActivity.this));
        eventTime.setTextSize(14);
        eventAddress.setTextSize(14);

        eventAboutTitle.setTypeface(globals.robotoNormal(EventDetailActivity.this));
        eventAboutTitle.setTextSize(16);

        eventAboutDescription.setTypeface(globals.robotoLight(EventDetailActivity.this));
        eventAboutDescription.setTextSize(14);

        eventDetailAddressTitle.setTypeface(globals.robotoNormal(EventDetailActivity.this));
        eventDetailAddressTitle.setTextSize(16);

        eventBonusUser.setTypeface(globals.robotoLight(EventDetailActivity.this));
        eventBonusUser.setTextSize(14);

        eventBonusDescription.setTypeface(globals.robotoLight(EventDetailActivity.this));
        eventBonusDescription.setTextSize(18);

        eventGoing.setText(R.string.going);
        eventInterested.setText(R.string.interested);

    }

    private  void userGoingClicked(final Context context, final String eventID, final String userid){
        RequestQueue MyRequestQueue  = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final String topSlideURL = g.getUserEventGoingClicked();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if(jsonResult.getString("resultCode").equals("0")){
                        if(jsonResult.getString("result").equalsIgnoreCase("1")){
                            eventGoing.setText(R.string.going);
                            eventGoing.setBackgroundColor(context.getResources().getColor(R.color.colorOceanBlue));
                            eventGoing.setTextColor(Color.WHITE);
                        }else{
                            eventGoing.setText(R.string.going);
                            eventGoing.setBackground(ContextCompat.getDrawable(EventDetailActivity.this, R.drawable.border_white));
                        }
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
                MyData.put("eventID", eventID);
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);

    }
    private void userInterestedClicked(final Context context, final String eventID, final String userid){
        RequestQueue MyRequestQueue  = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final String topSlideURL = g.getUserEventInterestClicked();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if(jsonResult.getString("resultCode").equals("0")){
                        if(jsonResult.getString("result").equalsIgnoreCase("1")){
                            eventInterested.setText(R.string.interested);
                            eventInterested.setBackgroundColor(context.getResources().getColor(R.color.colorOceanBlue));
                            eventInterested.setTextColor(Color.WHITE);
                        }else{
                            eventInterested.setText(R.string.interested);
                            eventGoing.setBackground(ContextCompat.getDrawable(EventDetailActivity.this, R.drawable.border_white));
                        }
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
                MyData.put("eventID", eventID);
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);

    }

    private void userFullReviewListShow(Context context, final String eventID, final String text){
        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final String topSlideURL = g.getUserReviewSend();
        final Intent extra = getIntent();
        SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid = prefs.getString("userid", null);

         StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if (jsonResult.getString("resultCode").equals("0")) {

                        JSONArray reviews = new JSONArray(jsonResult.getString("result"));
                        for (int i = 0; i < reviews.length(); i++) {
                            JSONObject review = reviews.getJSONObject(i);
                            reviewModel = new ReviewModel(review.getString("user_id"), review.getString("name"), review.getString("picture"), review.getString("user_review"));
                            reviewList.add(i, reviewModel);
                        }
                        reviewList = new ArrayList<>();
                        eventDetailDataSet(EventDetailActivity.this);
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
                MyData.put("userID", userid); //Add the data you'd like to send to the server.
                MyData.put("eventID", eventID);
                MyData.put("user_review", text);
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                @Override
                public void gotLocation(Location location){

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                }
            };
            MyLocation myLocation = new MyLocation();
            myLocation.getLocation(this, locationResult);


        }

    }

    private void addCustomMarker2(final Double lat, final Double lon, final String imagepath, final String placeName, final String placeSlogan, final  String placeID)  {
        Log.d("Map", "addCustomMarker()");
        Bitmap myBitmap;
        if (mMap == null) {
            return;
        }
        if(!lat.equals(0.0) || !lon.equals(0.0)){
            try {
                myBitmap = new myTask().execute(imagepath).get();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lon))
                        .title(placeName)
                        .snippet(placeID)
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(myBitmap))));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

    }

    private class myTask extends AsyncTask<String,Void,Bitmap> {


        protected Bitmap doInBackground(String... params) {

            String urldisplay = params[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        @Override
        protected void onPostExecute(Bitmap result) {

        }
    }

    private Bitmap getMarkerBitmapFromView(Bitmap bitmap) {
        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_custom_marker, null);
        CircularImageView markerImageView = (CircularImageView) customMarkerView.findViewById(R.id.eventPlaceLogo);
        markerImageView.setImageBitmap(bitmap);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


}
