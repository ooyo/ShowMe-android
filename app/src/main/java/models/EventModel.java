package models;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class EventModel
{
    private String _id;
    private String title;
    private String start_time;
    private String end_time;
    private String description;
    private ArrayList<String> category;
    private Fields fields;
    private Place place;
    private Bonus bonus;
    private int views;
    private String imagepath;
    private String updated_at;
    private String created_at;
    private ArrayList<String> going;
    private ArrayList<String> interested;
    private ArrayList<ReviewModel> reviews;
    private ArrayList<String> idmphoto;
    private String picture;
    private int imageCount;
    private int goingCount;
    private int interestCount;
    private String bonusVisible;
    private String placeAddress;
    private int reviewCount;


    //used in home tab list
    private String singleCategory;
    private JSONArray goingUserImage;
    private String like;
    private String isGoing;
    private String isInterested;


    public EventModel(String title, int goingCount, int imageCount){
        this.title = title;
        this.goingCount = goingCount;
        this.imageCount = imageCount;
    }

    public EventModel(String id, String title, String start_time, String end_time, String singleCategory, String imagepath, JSONArray goingUserImage, String like, String isGoing, String isInterested){
        this._id = id;
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.imagepath = imagepath;
        this.singleCategory = singleCategory;
        this.goingUserImage = goingUserImage;
        this.like = like;
        this.isGoing = isGoing;
        this.isInterested = isInterested;

    }
    public EventModel(String id, String title, String start_time, String end_time, String description, ArrayList<String> category, Fields fields, Place place, Bonus bonus, int views, String imagepath, String updated_at, String created_at, ArrayList<String> going, ArrayList<String> interested, ArrayList<ReviewModel> reviews, ArrayList<String> idmphoto, String picture, int imageCount, int goingCount, int interestCount, String bonusVisible, String placeAddress, int reviewCount){
        _id = id;
        this.title = title;
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.category = category;
        this.fields = fields;
        this.place = place;
        this.bonus = bonus;

        this.views = views;
        this.imagepath = imagepath;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.going = going;
        this.interested = interested;
        this.reviews = reviews;
        this.idmphoto = idmphoto;
        this.picture = picture;
        this.imageCount = imageCount;
        this.goingCount = goingCount;
        this.interestCount = interestCount;
        this.bonusVisible = bonusVisible;
        this.placeAddress = placeAddress;
        this.reviewCount = reviewCount;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<String> getGoing() {
        return going;
    }

    public void setGoing(ArrayList<String> going) {
        this.going = going;
    }

    public ArrayList<String> getInterested() {
        return interested;
    }

    public void setInterested(ArrayList<String> interested) {
        this.interested = interested;
    }

    public ArrayList<ReviewModel> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<ReviewModel> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getIdmphoto() {
        return idmphoto;
    }

    public void setIdmphoto(ArrayList<String> idmphoto) {
        this.idmphoto = idmphoto;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getGoingCount() {
        return goingCount;
    }

    public void setGoingCount(int goingCount) {
        this.goingCount = goingCount;
    }

    public int getInterestCount() {
        return interestCount;
    }

    public void setInterestCount(int interestCount) {
        this.interestCount = interestCount;
    }

    public String getBonusVisible() {
        return bonusVisible;
    }

    public void setBonusVisible(String bonusVisible) {
        this.bonusVisible = bonusVisible;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getIsGoing() {
        return isGoing;
    }

    public void setIsGoing(String isGoing) {
        this.isGoing = isGoing;
    }

    public String getIsInterested() {
        return isInterested;
    }

    public void setIsInterested(String isInterested) {
        this.isInterested = isInterested;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public JSONArray getGoingUserImage() {
        return goingUserImage;
    }

    public void setGoingUserImage(JSONArray goingUserImage) {
        this.goingUserImage = goingUserImage;
    }

    public String getSingleCategory() {
        return singleCategory;
    }

    public void setSingleCategory(String singleCategory) {
        this.singleCategory = singleCategory;
    }


    public class Fields
    {
        private String field1_name;

        public String getField1Name() { return this.field1_name; }

        public void setField1Name(String field1_name) { this.field1_name = field1_name; }

        private String field1_value;

        public String getField1Value() { return this.field1_value; }

        public void setField1Value(String field1_value) { this.field1_value = field1_value; }

        private String field2_name;

        public String getField2Name() { return this.field2_name; }

        public void setField2Name(String field2_name) { this.field2_name = field2_name; }

        private String field2_value;

        public String getField2Value() { return this.field2_value; }

        public void setField2Value(String field2_value) { this.field2_value = field2_value; }

        private String field3_name;

        public String getField3Name() { return this.field3_name; }

        public void setField3Name(String field3_name) { this.field3_name = field3_name; }

        private String field3_value;

        public String getField3Value() { return this.field3_value; }

        public void setField3Value(String field3_value) { this.field3_value = field3_value; }
    }

    public class Place
    {
        private String place_id;

        public String getPlaceId() { return this.place_id; }

        public void setPlaceId(String place_id) { this.place_id = place_id; }

        private String place_name;

        public String getPlaceName() { return this.place_name; }

        public void setPlaceName(String place_name) { this.place_name = place_name; }

        private String place_logo;

        public String getPlaceLogo() { return this.place_logo; }

        public void setPlaceLogo(String place_logo) { this.place_logo = place_logo; }

        private String place_lat;

        public String getPlaceLat() { return this.place_lat; }

        public void setPlaceLat(String place_lat) { this.place_lat = place_lat; }

        private String place_lon;

        public String getPlaceLon() { return this.place_lon; }

        public void setPlaceLon(String place_lon) { this.place_lon = place_lon; }
    }

    public class Bonus
    {
        private String user_id;

        public String getUserId() { return this.user_id; }

        public void setUserId(String user_id) { this.user_id = user_id; }

        private String name;

        public String getName() { return this.name; }

        public void setName(String name) { this.name = name; }

        private String description;

        public String getDescription() { return this.description; }

        public void setDescription(String description) { this.description = description; }
    }






}
