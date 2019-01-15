package ooyo.mn.showme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.Snackbar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import hometab.HomeScreenActivity;
import additional.GCMRegistrationIntentService;


public class MainActivity extends AppCompatActivity{
    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    public String token;

    //Creating a broadcast receiver for gcm registration
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        Globals g = new Globals();
        final String facebookLoginURL = g.getFacebookLoginURL();

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);


        if(isLoggedIn()){
            startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
            //logged in

        } else{
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        final Bundle bundle = new Bundle();
                        final String id = object.getString("id");
                        try {
                            URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=200");

                            bundle.putString("profile_pic", profile_pic.toString());

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        bundle.putString("idFacebook", id);
                        if (object.has("first_name"))
                            bundle.putString("first_name", object.getString("first_name"));
                        if (object.has("name"))
                            bundle.putString("name", object.getString("name"));
                        if (object.has("last_name"))
                            bundle.putString("last_name", object.getString("last_name"));
                        if (object.has("email"))
                            bundle.putString("email", object.getString("email"));
                        if (object.has("gender"))
                            bundle.putString("gender", object.getString("gender"));
                        if (object.has("age_range"))
                            bundle.putString("age_range", object.getJSONObject("age_range").getString("min"));
                        if (object.has("link"))
                            bundle.putString("face_link", object.getString("link"));

                        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, facebookLoginURL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResult = new JSONObject(response);
                                    if(jsonResult.getString("resultCode").equals("0")){
                                        SharedPreferences.Editor editor = getSharedPreferences("SHOWME", MODE_PRIVATE).edit();
                                        editor.putString("userid", jsonResult.getString("userid"));
                                        editor.apply();
                                        startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //This code is executed if there is an error.
                                Log.d("MyStringRequest Err", error.toString());
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> MyData = new HashMap<String, String>();
                                MyData.put("fid", bundle.getString("idFacebook")); //Add the data you'd like to send to the server.
                                MyData.put("age_range", bundle.getString("age_range"));
                                MyData.put("gender", bundle.getString("gender"));
                                MyData.put("firstname", bundle.getString("first_name"));
                                MyData.put("lastname", bundle.getString("last_name"));
                                MyData.put("name", bundle.getString("name"));
                                MyData.put("email", bundle.getString("email"));
                                MyData.put("picture", bundle.getString("profile_pic"));
                                MyData.put("face_link", bundle.getString("face_link"));
                                MyData.put("user_phone", "appNameAndroid");
                                MyData.put("device_token", token);
                                Log.d("Login Data", MyData.toString());
                                return MyData;
                            }
                        };

                        MyRequestQueue.add(MyStringRequest);


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,first_name,last_name,age_range,link,gender,email,about,relationship_status,bio"); // Parámetros que pedimos a facebook
            request.setParameters(parameters);
            request.executeAsync();
        }

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_about_me", "user_relationships"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                          try {
                            final Bundle bundle = new Bundle();
                            final String id = object.getString("id");

                            try {
                                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=200");
                                  bundle.putString("profile_pic", profile_pic.toString());

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            bundle.putString("idFacebook", id);
                            if (object.has("first_name"))
                                bundle.putString("first_name", object.getString("first_name"));
                            if (object.has("name"))
                                bundle.putString("name", object.getString("name"));
                            if (object.has("last_name"))
                                bundle.putString("last_name", object.getString("last_name"));
                            if (object.has("email"))
                                bundle.putString("email", object.getString("email"));
                            if (object.has("gender"))
                                bundle.putString("gender", object.getString("gender"));
                            if (object.has("age_range"))
                                bundle.putString("age_range", object.getJSONObject("age_range").getString("min"));
                            if (object.has("link"))
                                bundle.putString("face_link", object.getString("link"));

                            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, facebookLoginURL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonResult = new JSONObject(response);
                                        if(jsonResult.getString("resultCode").equals("0")){
                                            Log.d("Facebook Login", "JSON userid:"+ jsonResult.getString("userid"));
                                            SharedPreferences.Editor editor = getSharedPreferences("SHOWME", MODE_PRIVATE).edit();
                                             editor.putString("userid", jsonResult.getString("userid"));
                                            editor.apply();
                                            startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //This code is executed if there is an error.
                                    Log.d("MyStringRequest Err", error.toString());
                                }
                            }) {
                                protected Map<String, String> getParams() {
                                    Map<String, String> MyData = new HashMap<String, String>();
                                    MyData.put("fid", bundle.getString("idFacebook")); //Add the data you'd like to send to the server.
                                    MyData.put("age_range", bundle.getString("age_range"));
                                    MyData.put("gender", bundle.getString("gender"));
                                    MyData.put("firstname", bundle.getString("first_name"));
                                    MyData.put("lastname", bundle.getString("last_name"));
                                    MyData.put("name", bundle.getString("name"));
                                    MyData.put("email", bundle.getString("email"));
                                    MyData.put("picture", bundle.getString("profile_pic"));
                                    MyData.put("face_link", bundle.getString("face_link"));
                                    MyData.put("user_phone", "appNameAndroid");
                                    MyData.put("device_token", token);
                                    Log.d("Login Data", MyData.toString());
                                    return MyData;
                                }
                            };

                            MyRequestQueue.add(MyStringRequest);


                        }catch (Exception e){
                            e.printStackTrace();
                        }



                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,age_range,link,gender,email,about,relationship_status,bio"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

             }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("Facebook", "onError: " + e.getLocalizedMessage());
                info.setText("Login attempt failed. " );
            }
        });


        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        if (isConnected()) {
            //Toast.makeText(getApplication(),"Thank you",Toast.LENGTH_SHORT).show();
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);
        } else {
            Snackbar snackbar = Snackbar.with( MainActivity.this).text("NO INTERNET").textColor(Color.WHITE).textTypeface(tf).color(Color.RED).duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE);
            snackbar.position(Snackbar.SnackbarPosition.TOP );
            SnackbarManager.show( snackbar, this);
        }


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                      token = intent.getStringExtra("token");
                    Log.i("TOKEN", "onReceive: " + token);
                    //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
    }



    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }




    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

}
