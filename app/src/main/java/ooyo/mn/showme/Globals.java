package ooyo.mn.showme;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by appleuser on 6/28/16.
 */
public class Globals {
    //10.0.2.2
    private String facebookLoginURL ="http://showmeub.com/showme_laravel/public/api/v1/faceregister";
    private String topSlideURL ="http://showmeub.com/showme_laravel/public/client/api/v1/index_android";
    private String eventDetailURL = "http://showmeub.com/showme_laravel/public/client/api/v1/event_detail_android";
    private String eventHomeListURL = "http://showmeub.com/showme_laravel/public/client/api/v1/index_list_android";
    private String evengGoingUserListURL = "http://showmeub.com/showme_laravel/public/client/api/v1/eventusergoing";
    private String evengCategoryEventListURL = "http://showmeub.com/showme_laravel/public/client/api/v1/category_android";
    private String userFollowList = "http://showmeub.com/showme_laravel/public/client/api/v1/userfollowandroid";
    private String userEventList = "http://showmeub.com/showme_laravel/public/client/api/v1/usereventandroid";
    private String userProfile = "http://showmeub.com/showme_laravel/public/client/api/v1/userprofileandroid";
    private String userBonus = "http://showmeub.com/showme_laravel/public/client/api/v1/userbonus";
    private String userFollowUnFollow = "http://showmeub.com/showme_laravel/public/client/api/v1/userfollowpush";
    private String userEventGoingClicked = "http://showmeub.com/showme_laravel/public/client/api/v1/eventgoing";
    private String userEventInterestClicked = "http://showmeub.com/showme_laravel/public/client/api/v1/eventinterest";
    private String userReviewList = "http://showmeub.com/showme_laravel/public/client/api/v1/eventreviewlist";
    private String userReviewSend = "http://showmeub.com/showme_laravel/public/client/api/v1/eventreview";
    private String eventDetailImages = "http://showmeub.com/showme_laravel/public/client/api/v1/eventdetailimages";
    private String mainStoragePath = "http://showmeub.com/showme_laravel/storage/app/";
    private String noImage = "http://showmeub.com/showme_laravel/storage/app/content/place/noimage.jpg";
    private String advertisePathURL = "http://showmeub.com/showme_laravel/storage/app/content/ads/";
    private String placeImagePathURL = "http://showmeub.com/showme_laravel/storage/app/content/place/";
    private String searchURL = "http://showmeub.com/showme_laravel/public/client/api/v1/searchandroid";
    private String mapListURL = "http://showmeub.com/showme_laravel/public/client/api/v1/maplist";
    private String findUserURL = "http://showmeub.com/showme_laravel/public/client/api/v1/finduser";
    private String editUserInfoURL = "http://showmeub.com/showme_laravel/public/client/api/v1/userupdate";
    private String aboutURL = "http://showmeub.com/showme_laravel/public/client/api/v1/about";
    private String placeEventListByID = "http://showmeub.com/showme_laravel/public/client/api/v1/placeeventlistandroid";
    private String userReceivedNotificationURL = "http://showmeub.com/showme_laravel/public/client/api/v1/usernotif";


    public String getFacebookLoginURL() {
        return facebookLoginURL;
    }

    public String getTopSlideURL() {
        return topSlideURL;
    }

    public String getEventDetailURL() {
        return eventDetailURL;
    }

    public String getEventHomeListURL() {
        return eventHomeListURL;
    }

    public String getEvengGoingUserListURL() {
        return evengGoingUserListURL;
    }

    public Typeface robotoLight(Context context){
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface font = Typeface.createFromAsset(am, "fonts/Roboto-Light.ttf");
        return font;
    }

    public Typeface robotoNormal(Context context){
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface font = Typeface.createFromAsset(am, "fonts/Roboto-Medium.ttf");
        return font;
    }

    public Typeface robotoThin(Context context){
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface font = Typeface.createFromAsset(am, "fonts/Roboto-Thin.ttf");
        return font;
    }

    public String getEvengCategoryEventListURL() {
        return evengCategoryEventListURL;
    }

    public String getUserFollowList() {
        return userFollowList;
    }

    public String getUserEventList() {
        return userEventList;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public String getUserBonus() {
        return userBonus;
    }

    public String getUserFollowUnFollow() {
        return userFollowUnFollow;
    }

    public String getUserEventGoingClicked() {
        return userEventGoingClicked;
    }

    public String getUserEventInterestClicked() {
        return userEventInterestClicked;
    }

    public String getUserReviewList() {
        return userReviewList;
    }

    public String getUserReviewSend() {
        return userReviewSend;
    }

    public String getEventDetailImages() {
        return eventDetailImages;
    }

    public String getMainStoragePath() {
        return mainStoragePath;
    }

    public String getNoImage() {
        return noImage;
    }

    public String getAdvertisePathURL() {
        return advertisePathURL;
    }

    public String getSearchURL() {
        return searchURL;
    }

    public String getMapListURL() {
        return mapListURL;
    }

    public String getPlaceImagePathURL() {
        return placeImagePathURL;
    }


    public String getFindUserURL() {
        return findUserURL;
    }

    public String getEditUserInfoURL() {
        return editUserInfoURL;
    }

    public String getAboutURL() {
        return aboutURL;
    }

    public String getPlaceEventListByID() {
        return placeEventListByID;
    }

    public String getUserReceivedNotificationURL() {
        return userReceivedNotificationURL;
    }
}
