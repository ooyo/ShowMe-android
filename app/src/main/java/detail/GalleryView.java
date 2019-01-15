package detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;
import com.veinhorn.scrollgalleryview.loader.DefaultVideoLoader;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import additional.AppController;
import models.ReviewModel;
import ooyo.mn.showme.Globals;
import ooyo.mn.showme.R;

/**
 * Created by appleuser on 10/27/16.
 */

public class GalleryView extends FragmentActivity {

    private ArrayList<String> imageList;
    private ArrayList<String> videoList;
//    private static final ArrayList<String> images = new ArrayList<>(Arrays.asList(
//            "http://img1.goodfon.ru/original/1920x1080/d/f5/aircraft-jet-su-47-berkut.jpg",
//            "http://www.dishmodels.ru/picture/glr/13/13312/g13312_7657277.jpg",
//            "http://img2.goodfon.ru/original/1920x1080/b/c9/su-47-berkut-c-37-firkin.jpg"
//    ));
//    private static final String movieUrl = "https://static.videezy.com/system/resources/previews/000/000/168/original/Record.mp4";

    private ScrollGalleryView scrollGalleryView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventgallery);
        eventDetailDataSet(GalleryView.this);


    }

    private Bitmap toBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }


    private void eventDetailDataSet(Context context) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        final Globals g = new Globals();
        final String topSlideURL = g.getEventDetailImages();
        final Intent extra = getIntent();
        SharedPreferences prefs = context.getSharedPreferences("SHOWME", MODE_PRIVATE);
        final String userid = prefs.getString("userid", null);
        imageList = new ArrayList<String>();
        videoList = new ArrayList<String>();
        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, topSlideURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResult = new JSONObject(response);

                    if (jsonResult.getString("resultCode").equals("0")) {
                        JSONObject result = new JSONObject(jsonResult.getString("result"));
                        JSONArray imagePaths = new JSONArray(result.getString("imagefiles"));
                        JSONArray videoPaths = new JSONArray(result.getString("videofiles"));
                        Log.i("GalleryView", "onResponse: Video" +result.getString("videofiles").toString());
                       for (int i = 0; i < imagePaths.length(); i++) {
                             imageList.add(i, g.getMainStoragePath()+imagePaths.getString(i));

                        }

                        for(int v = 0; v< videoPaths.length(); v++){
                            videoList.add(v, videoPaths.getString(v));
                      //       scrollGalleryView.addMedia(MediaInfo.mediaLoader(new DefaultVideoLoader(g.getMainStoragePath()+ videoPaths.getString(v), R.drawable.placeholder_video)));
                        }
                        List<MediaInfo> infos = new ArrayList<>(Integer.parseInt(result.getString("imageCount")));
                        for (String url : imageList) infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));

                        List<MediaInfo> videos = new ArrayList<>(videoPaths.length());
                        for (String url : videoList) videos.add(MediaInfo.mediaLoader(new DefaultVideoLoader(g.getMainStoragePath()+ url, R.drawable.placeholder_video)));


                        scrollGalleryView
                                .setThumbnailSize(100)
                                .setZoom(true)
                                .setFragmentManager(getSupportFragmentManager())
                               // .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.noimage)))
                                //.addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(toBitmap(R.drawable.no))))
//                                .addMedia(MediaInfo.mediaLoader(new MediaLoader() {
//                                    @Override public boolean isImage() {
//                                        return true;
//                                    }
//
//                                    @Override public void loadMedia(Context context, ImageView imageView,
//                                                                    MediaLoader.SuccessCallback callback) {
//                                        //imageView.setImageBitmap(toBitmap(R.drawable.wallpaper3));
//                                        callback.onSuccess();
//                                    }
//
//                                    @Override public void loadThumbnail(Context context, ImageView thumbnailView,
//                                                                        MediaLoader.SuccessCallback callback) {
//                                        thumbnailView.setImageBitmap(toBitmap(R.drawable.noimage));
//                                        callback.onSuccess();
//                                    }
//                                }))
                                .addMedia(infos)
                                .addMedia(videos);

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
                MyData.put("eventid", extra.getStringExtra("eventid"));
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);

    }
}