package maptab;

import android.Manifest;
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
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.drawable.CircularProgressDrawable;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import additional.AppController;
import additional.CircledNetworkImageView;
import categorytab.CategoryChoseActivity;
import detail.EventDetailActivity;
import detail.EventListActivity;
import hometab.HomeScreenActivity;
import models.EventModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;
import ooyo.mn.showme.SearchActivity;
import profiletab.ProfileScreenActivity;

/**
 * Created by appleuser on 10/21/16.
 */

public class MapScreenActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Double lat, lon;
    String imagepath, placename,placeSlogan, placeID;
    Globals globals;
    Bitmap placeLogo;
    ProgressView progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tab);
        TabBarSetting(MapScreenActivity.this);
        globals= new Globals();
        lat = 0.0; lon=0.0;
        imagepath = "0 value";
        placename = "";
        placeSlogan ="";
        placeID ="";
        progressBar = (ProgressView) findViewById(R.id.progressView);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        loadData();





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

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                }
            };
            MyLocation myLocation = new MyLocation();
            myLocation.getLocation(this, locationResult);
            addCustomMarker2(lat,lon, imagepath, placename, placeSlogan, placeID);

        } else {
// Show rationale and request permission.
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
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(MapScreenActivity.this, EventListActivity.class);
                        intent.putExtra("from", "map");
                        intent.putExtra("category", marker.getSnippet());
                        startActivity(intent);
                        //Toast.makeText(MapScreenActivity.this, "Name:" + marker.getTitle() + " ID:" + marker.getSnippet(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

    }
    private class myTask extends AsyncTask<String,Void,Bitmap>{


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




    private void TabBarSetting(final Context context){
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
        btnMapTab.setSelected(true);

    }
    public void loadData(){
        progressBar.start();
        RequestQueue MyRequestQueue  = Volley.newRequestQueue(MapScreenActivity.this);
        Globals g = new Globals();
        final String TAG = "MapScreen";
        final String topSlideURL = g.getMapListURL();
        SharedPreferences prefs = MapScreenActivity.this.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid =prefs.getString("userid",null);
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
                           // if(Double.parseDouble(singleEvent.getString("latitude")))
                            try {
                                String slogan = "";
                                if(singleEvent.has("slogan")){
                                    slogan =singleEvent.getString("slogan");
                                }else{
                                    slogan="";
                                }
                                addCustomMarker2(Double.parseDouble(singleEvent.getString("latitude")),Double.parseDouble(singleEvent.getString("longitude")), globals.getPlaceImagePathURL() +  singleEvent.getString("image"), singleEvent.getString("name"), slogan, singleEvent.getString("_id"));
                            }catch (NumberFormatException e){

                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.stop();
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                progressBar.stop();
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
}
