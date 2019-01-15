package profiletab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import additional.AppController;
import additional.CircledNetworkImageView;
import detail.EventDetailActivity;
import models.EventModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;

/**
 * Created by appleuser on 11/25/16.
 */

public class EditProfileActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    CircledNetworkImageView userImage;
    TextView userNameLbl,userAgeLbl,userEmailLbl;
    EditText userNameTxt, userEmailTxt, userAgeTxt;
    Switch userGender;
    Button userSubmit;
    String gender;

    Globals globals = new Globals();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        userImage = (CircledNetworkImageView) findViewById(R.id.userImage);
        userNameLbl = (TextView) findViewById(R.id.userNameLbl);
        userNameLbl.setTypeface(globals.robotoLight(this));
        userNameLbl.setTextSize(18);
        userNameLbl.setText("Name");
        userNameLbl.setTextColor(getResources().getColor(R.color.colorLightGray));
        userNameTxt = (EditText) findViewById(R.id.userNameTxt);
        userNameTxt.setTypeface(globals.robotoLight(this));
        userNameTxt.setTextSize(14);
        userAgeLbl = (TextView) findViewById(R.id.userAgeLbl);
        userAgeLbl.setText("Age");
        userAgeLbl.setTextColor(getResources().getColor(R.color.colorLightGray));
        userAgeLbl.setTypeface(globals.robotoLight(this));
        userAgeLbl.setTextSize(18);
        userAgeTxt = (EditText) findViewById(R.id.userAgeTxt);
        userAgeTxt.setTypeface(globals.robotoLight(this));
        userAgeTxt.setTextSize(14);
        userEmailLbl = (TextView) findViewById(R.id.userEmailLbl);
        userEmailLbl.setTextColor(getResources().getColor(R.color.colorLightGray));
        userEmailLbl.setText("Email");
        userEmailLbl.setTypeface(globals.robotoLight(this));
        userEmailLbl.setTextSize(18);
        userEmailTxt = (EditText) findViewById(R.id.userEmailTxt);
        userEmailTxt.setEnabled(false);
        userEmailTxt.setTypeface(globals.robotoLight(this));
        userEmailTxt.setTextSize(14);
        userGender = (Switch) findViewById(R.id.userGender);
        userGender.setText("Gender");
        userGender.setTextOff("Female");
        userGender.setTextOn("Male");
        userGender.setTypeface(globals.robotoLight(this));
        userGender.setTextSize(18);
        userSubmit = (Button) findViewById(R.id.btnSubmit);
        userSubmit.setText("Submit");
        userSubmit.setTypeface(globals.robotoLight(this));
        userSubmit.setTextSize(18);
        gender ="male";
        eventHomeList(this);

        if(userGender != null){
            userGender.setOnCheckedChangeListener(EditProfileActivity.this);
        }

        userSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userAgeTxt.getText().length() > 0 && userNameTxt.getText().length() > 5){
                    RequestQueue MyRequestQueue  = Volley.newRequestQueue(EditProfileActivity.this);
                    Globals g = new Globals();
                    SharedPreferences prefs = getSharedPreferences("SHOWME", MODE_PRIVATE);
                    final String userid = prefs.getString("userid", null);

                    final String topSlideURL = g.getEditUserInfoURL();

                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResult = new JSONObject(response);
                                if(jsonResult.getString("resultCode").equals("0")){
                                    Toast.makeText(EditProfileActivity.this, jsonResult.getString("result"), Toast.LENGTH_LONG).show();
                                    finish();
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
                            MyData.put("name", userNameTxt.getText().toString());
                            MyData.put("age_range",userAgeTxt.getText().toString());
                            MyData.put("gender", gender);
                            return MyData;
                        }
                    };
                    MyRequestQueue.add(MyStringRequest);
                }else{
                    Toast.makeText(EditProfileActivity.this, "Some fields are missing. Your name must be greater than 5 character.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public  void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if(isChecked){
            gender = "male";
        }else{
            gender = "female";
        }
    }

    private void eventHomeList(Context context) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final String topSlideURL = g.getUserProfile();
        final ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid = prefs.getString("userid", null);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if (jsonResult.getString("resultCode").equals("0")) {
                        JSONObject results = new JSONObject((jsonResult.getString("result")));
                        userAgeTxt.setText(results.getString("age_range"));
                        userNameTxt.setText(results.getString("name"));
                        userImage.setImageUrl(results.getString("picture"), imageLoader);
                        userEmailTxt.setText(results.getString("email"));
                        if(results.getString("gender").equalsIgnoreCase("male")){
                            userGender.setChecked(true);
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
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

}
