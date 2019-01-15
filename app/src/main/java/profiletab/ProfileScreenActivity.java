package profiletab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import additional.AppController;
import additional.CircledNetworkImageView;
import additional.FollowUserAdapter;
import additional.GCMPushReceiverService;
import categorytab.CategoryChoseActivity;
import hometab.HomeScreenActivity;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import maptab.MapScreenActivity;
import models.EventModel;
import models.FollowUser;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.MainActivity;
import ooyo.mn.showme.R;
import ooyo.mn.showme.WelcomeActivity;

/**
 * Created by appleuser on 10/21/16.
 */

public class ProfileScreenActivity extends ActionBarActivity implements MaterialTabListener, PopoverView.PopoverViewDelegate {
    MaterialTabHost tabHost;
    ViewPager pager;
    private Resources res;
    private CircledNetworkImageView userImage;
    private TextView userName, userFollow;
    private ViewPagerAdapter pagerAdapter;
    private String userid;
    ProgressView progressView;
    Globals globals =new Globals();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_tab);
        TabBarSetting(ProfileScreenActivity.this);
        progressView = (ProgressView) findViewById(R.id.progressView);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

         userImage = (CircledNetworkImageView) findViewById(R.id.userImage);
         userName = (TextView) findViewById(R.id.userName);
         userFollow = (TextView) findViewById(R.id.userFollow);

         Intent extra = getIntent();
        SharedPreferences prefs = ProfileScreenActivity.this.getSharedPreferences("SHOWME", MODE_PRIVATE);
         if(extra.hasExtra("userid")){
            userid =extra.getStringExtra("userid");
        }else{
            userid =prefs.getString("userid",null);
        }

        settingDate(ProfileScreenActivity.this, userid);
        ImageButton btnMenu = (ImageButton) findViewById(R.id.profileUserMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                   drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        TextView notifBadge = (TextView) findViewById(R.id.notifBadge);
        if(prefs.getString("notif_count", null) != null && !prefs.getString("notif_count", null).isEmpty() && !prefs.getString("notif_count", null).equals("null")) {
            if (Integer.parseInt(prefs.getString("notif_count", null)) == 0) {
                notifBadge.setText("");
                notifBadge.setVisibility(View.INVISIBLE);
            } else if (Integer.parseInt(prefs.getString("notif_count", null)) >= 99) {
                notifBadge.setText("99");
                notifBadge.setVisibility(View.VISIBLE);
            } else {
                notifBadge.setText(prefs.getString("notif_count", null));
                notifBadge.setVisibility(View.VISIBLE);
            }
        }else{
            notifBadge.setVisibility(View.INVISIBLE);
        }

        notifBadge.setTypeface(globals.robotoLight(ProfileScreenActivity.this));

        ImageButton btnNotif = (ImageButton) findViewById(R.id.profileUserNotif);
        btnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            RelativeLayout rootView = (RelativeLayout)findViewById(R.id.rootLayout);
            PopoverView popoverView = new PopoverView(ProfileScreenActivity.this, R.layout.popover);
                SharedPreferences.Editor editor = getSharedPreferences("SHOWME", MODE_PRIVATE).edit();
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
                editor.putString("notif_count", "0");
                editor.putString("notif_date", date);
                editor.apply();

                DisplayMetrics lDisplayMetrics = getResources().getDisplayMetrics();
                int height = lDisplayMetrics.heightPixels;
                int width = lDisplayMetrics.widthPixels;
            popoverView.setContentSizeForViewInPopover(new Point(width - 40, height - 100));
            popoverView.setDelegate(ProfileScreenActivity.this);
            popoverView.showPopoverFromRectInViewGroup(rootView, PopoverView.getFrameForView(view), PopoverView.PopoverArrowDirectionUp, true);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.nav_editprofile) {
                    // Handle the preference  action
//                    AssetManager am = getApplicationContext().getAssets();
//                    Typeface font = Typeface.createFromAsset(am, "fonts/Roboto-Light.ttf");
//                    SpannableStringBuilder nav_editprofile = new SpannableStringBuilder(getApplicationContext().getString(R.string.edit_profile));
//                    nav_editprofile.setSpan(font,0,nav_editprofile.length(), 0);
//                    MenuItem priv = (MenuItem) findViewById(R.id.nav_editprofile);
//                    priv.setTitle(nav_editprofile);
                    startActivity(new Intent(ProfileScreenActivity.this, EditProfileActivity.class));
                }else if (id == R.id.nav_privacy) {
                    // Handle the About action
                }else if (id == R.id.nav_findfriend) {
                    startActivity(new Intent(ProfileScreenActivity.this, FindFriends.class));
                }else if (id == R.id.nav_tutorial) {
                    Intent intent = new Intent(ProfileScreenActivity.this, WelcomeActivity.class);
                    intent.putExtra("clicked", "user");
                    startActivity(intent);
                }else if (id == R.id.nav_likeshow) {
                    Uri uri = Uri.parse("https://www.facebook.com/ShowMeMgl");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }else if (id == R.id.nav_about) {
                    startActivity(new Intent(ProfileScreenActivity.this, AboutActivity.class));
                }else if (id == R.id.nav_logout) {
                    LoginManager.getInstance().logOut();
                    AccessToken.getCurrentAccessToken();
                    startActivity(new Intent(ProfileScreenActivity.this, MainActivity.class));
                    finish();
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                drawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });



        res = this.getResources();
        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        pager = (ViewPager) this.findViewById(R.id.profileContent);


        // init view pager
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });
        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab().setIcon(getIcon(i)).setTabListener(this)
            );
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
// when the tab is clicked the pager swipe content to the tab position
        pager.setCurrentItem(tab.getPosition());
        if(tab.getPosition() == 1){

        }
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {
    }

    @Override
    public void popoverViewWillShow(PopoverView view) {

    }

    @Override
    public void popoverViewDidShow(PopoverView view) {

    }

    @Override
    public void popoverViewWillDismiss(PopoverView view) {

    }

    @Override
    public void popoverViewDidDismiss(PopoverView view) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public Fragment getItem(int num) {
            Log.i("Fragment", "getItem: "+ num);
            switch (num){
                case 0:
                    FragmentFollowedUser followFragment = new FragmentFollowedUser();
                    Bundle bundle = new Bundle();
                    bundle.putString("userid", userid);
                    followFragment.setArguments(bundle);
                    return followFragment;
                case 1:
                    FragmentCoupon couponFragment = new FragmentCoupon();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("userid", userid);
                    couponFragment.setArguments(bundle2);
                    return couponFragment;
                case 2:
                    FragmentEvent eventFragment = new FragmentEvent();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("userid", userid);
                    eventFragment.setArguments(bundle3);
                    return eventFragment;
                default:
                    FragmentFollowedUser followFragment1 = new FragmentFollowedUser();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("userid", userid);
                    followFragment1.setArguments(bundle1);
                    return followFragment1;

            }
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return "tab";
        }
    }
    /*
    * It doesn't matter the color of the icons, but they must have solid colors
    */
    private Drawable getIcon(int position) {
        if(position == 0){
            return res.getDrawable(R.drawable.user_follow);
        }else if(position == 1)
        {
            return res.getDrawable(R.drawable.user_bonus);
        }else{
            return res.getDrawable(R.drawable.user_event);
        }

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

        btnProfileTab.setSelected(true);
    }


    private void settingDate(final Context context, final String userid){

        RequestQueue MyRequestQueue  = Volley.newRequestQueue(context);
        final Globals g = new Globals();
        final String topSlideURL = g.getUserProfile();
        progressView.start();

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if(jsonResult.getString("resultCode").equals("0")){
                        JSONObject results = new JSONObject((jsonResult.getString("result")));
                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                            userImage.setImageUrl(results.getString("picture"), imageLoader);
                            userName.setText(results.getString("name"));
                            userFollow.setText(results.getString("followersCount")+" followers/" + results.getInt("followingCount") + " following");
                            userName.setTypeface(g.robotoLight(context));
                            userName.setTextSize(21);
                            userFollow.setTypeface(g.robotoLight(context));
                            userFollow.setTextSize(14);
                            sideMenuSetting(results.getString("picture"), results.getString("name"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    private void sideMenuSetting(String imagePath, String name){
        final Globals g = new Globals();

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        CircledNetworkImageView userSideImage = (CircledNetworkImageView) findViewById(R.id.userSideImage);
        userSideImage.setImageUrl(imagePath, imageLoader);
        TextView userSideName = (TextView) findViewById(R.id.userSideName);
        userSideName.setText(name);
        userSideName.setTypeface(g.robotoNormal(ProfileScreenActivity.this));
        userSideName.setTextSize(20);

    }


    @Override
    public void onResume() {
        super.onResume();
        ProfileScreenActivity.this.registerReceiver(mMessageReceiver, new IntentFilter("unique_name"));
    }

    //Must unregister onPause()
    @Override
    protected void onPause() {
        super.onPause();
        ProfileScreenActivity.this.unregisterReceiver(mMessageReceiver);
    }


    //This is the handler that will manager to process the broadcast intent
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            String message = intent.getStringExtra("message");

            //do other stuff here
        }
    };

}

