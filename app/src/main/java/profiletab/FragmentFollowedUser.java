package profiletab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

import additional.FollowUserAdapter;
import models.FollowUser;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by appleuser on 10/28/16.
 */

public class FragmentFollowedUser extends Fragment {
    private FollowUserAdapter followUserAdapter;
    private List<FollowUser> followUserList;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followuser, container, false);
        SharedPreferences prefs = container.getContext().getSharedPreferences("SHOWME", MODE_PRIVATE);
        String userid;
        String bundleExtra=getArguments().getString("userid");
        if(bundleExtra.isEmpty()){
            userid =prefs.getString("userid",null);
        }else{
            userid = bundleExtra;
        }

        followUserList = new ArrayList<>();
        followUserAdapter =new FollowUserAdapter(container.getContext(), followUserList);
        eventHomeUserGoingList(container.getContext(), userid);

        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(followUserAdapter);

        return view;
    }


    private void eventHomeUserGoingList(Context context, final String userid){
        RequestQueue MyRequestQueue  = Volley.newRequestQueue(context);
        Globals g = new Globals();
        final String topSlideURL = g.getUserFollowList();


        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    if(jsonResult.getString("resultCode").equals("0")){
                        JSONObject results = new JSONObject((jsonResult.getString("result")));
                         JSONArray resultArray = new JSONArray(results.getString("following"));
                        for(int i=0;i<resultArray.length(); i++){
                            JSONObject singleUser = resultArray.getJSONObject(i);
                             followUserList.add(i, new FollowUser(singleUser.getString("_id"),singleUser.getString("name"), singleUser.getString("picture"), singleUser.getString("followersCount"), singleUser.getInt("eventCount"), singleUser.getString("amiFollow")));
                        }
                        followUserAdapter.notifyDataSetChanged();
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
