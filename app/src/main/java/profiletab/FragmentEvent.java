package profiletab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import detail.EventListActivity;
import models.EventModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by appleuser on 10/28/16.
 */

public class FragmentEvent extends Fragment {
    private EventModel eventModel;
    private List<EventModel> albumList;
    private RecyclerView recyclerView;
    private MainCardAdapter cardAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event, container, false);

        SharedPreferences prefs = container.getContext().getSharedPreferences("SHOWME", MODE_PRIVATE);
        String userid;
        //String bundleExtra=getArguments().getString("userid");
        if(getArguments().getString("userid", null).equalsIgnoreCase("")){
            userid =prefs.getString("userid",null);
        }else{
            userid = getArguments().getString("userid", null);
        }


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        cardAdapter = new MainCardAdapter(container.getContext(), albumList);
        eventHomeList(container.getContext(), userid);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(container.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration( new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cardAdapter);

        return view;
    }

    private void eventHomeList(Context context, final String userid){
        RequestQueue MyRequestQueue  = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final String topSlideURL = g.getUserEventList();


        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);

                    if(jsonResult.getString("resultCode").equals("0")){
                        JSONArray results = new JSONArray((jsonResult.getString("result")));
                        Log.d("JSON", "onResponse: "+results.toString());
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
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }


}
