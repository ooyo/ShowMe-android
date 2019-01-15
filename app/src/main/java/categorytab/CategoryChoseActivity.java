package categorytab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import additional.CustomGridView;
import detail.EventListActivity;
import general.TopSlideActivity;
import hometab.HomeScreenActivity;
import maptab.MapScreenActivity;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;
import ooyo.mn.showme.SearchActivity;
import profiletab.ProfileScreenActivity;

/**
 * Created by appleuser on 10/21/16.
 */

public class CategoryChoseActivity extends AppCompatActivity {
    private ArrayList<String> slideTitle;
    private ArrayList<String> slideImages;
    private ArrayList<String> slideAddress;
    private ArrayList<String> slideId;
    private ArrayList<String> slideType;
    private ArrayList<String> slideSlogan;
 ProgressView progressView;


    int[] imageNotID = {
            R.drawable.cat_activity,
            R.drawable.cat_coffee,
            R.drawable.cat_lounge,
            R.drawable.cat_mall,
            R.drawable.cat_night,
            R.drawable.cat_pub,
            R.drawable.cat_restaurant,
            R.drawable.cat_shop,
            R.drawable.cat_theatre
    };
    CustomGridView categoryGrid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_tab);
        SharedPreferences prefs = getSharedPreferences("SHOWME", MODE_PRIVATE);
        progressView = (ProgressView) findViewById(R.id.progressView);
            slideTitle = new ArrayList<String>();
            slideImages= new ArrayList<String>();
            slideAddress= new ArrayList<String>();
            slideId = new ArrayList<String>();
            slideType = new ArrayList<String>();
            slideSlogan = new ArrayList<String>();
            TopSlideDataSet(CategoryChoseActivity.this, slideTitle,slideImages, slideAddress, slideId, slideType, slideSlogan);

        CategoryAdapter categoryAdapter = new CategoryAdapter(CategoryChoseActivity.this, imageNotID);
        categoryGrid=(CustomGridView)findViewById(R.id.gridView);
        categoryGrid.setAdapter(categoryAdapter);
        categoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(CategoryChoseActivity.this, EventListActivity.class);
                switch(position) {
                    case 0:
                        intent.putExtra("category", "Activity");
                        break;
                    case 1:
                        intent.putExtra("category", "Coffee Shop");
                        break;
                    case 2:
                        intent.putExtra("category", "Lounge");
                        break;
                    case 3:
                        intent.putExtra("category", "Mall");
                        break;
                    case 4:
                        intent.putExtra("category", "Night Club");
                        break;
                    case 5:
                        intent.putExtra("category", "Pub");
                        break;
                    case 6:
                        intent.putExtra("category", "Restaurant");
                        break;
                    case 7:
                        intent.putExtra("category", "Shop");
                        break;
                    case 8:
                        intent.putExtra("category", "Theatre");
                        break;
                    default:
                        intent.putExtra("category", "Activity");
                }
                intent.putExtra("from", "category");
                startActivity(intent);
            }
        });

        TabBarSetting(CategoryChoseActivity.this);

        ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryChoseActivity.this, SearchActivity.class);
                intent.putExtra("from", "home");
                startActivity(intent);
            }
        });
    }

    private void TopSlideDataSet (Context context, final ArrayList<String> title, final ArrayList<String> images, final ArrayList<String> address, final ArrayList<String> _id, final ArrayList<String> type, final ArrayList<String> slogan){
        RequestQueue MyRequestQueue  = Volley.newRequestQueue(context);
        this.slideTitle = title;
        this.slideImages = images;
        this.slideAddress = address;
        this.slideId = _id;
        this.slideType = type;
        this.slideSlogan = slogan;
        Globals g = new Globals();
        final String topSlideURL = g.getTopSlideURL();
        progressView.start();

        SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid =prefs.getString("userid",null);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if(jsonResult.getString("resultCode").equals("0")){
                        JSONArray results = new JSONArray((jsonResult.getString("result")));
                        Log.i("Category Slide", "onResponse: "+ results.toString());
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
                        View includedLayout = findViewById(R.id.topSlide);
                        ViewPager homeSlider = (ViewPager) includedLayout.findViewById(R.id.slider);

                        PagerAdapter adapter = new TopSlideActivity(CategoryChoseActivity.this,images, title, address, _id, type, slogan);

                        homeSlider.setAdapter(adapter);
                        progressView.stop();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressView.stop();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                progressView.stop();
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

        btnCategoryTab.setSelected(true);
    }
}
