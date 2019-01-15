package models;

import java.util.ArrayList;

/**
 * Created by appleuser on 10/22/16.
 */

public class ClientModel {
    private String _id;
    public String getId() { return this._id; }
    public void setId(String _id) { this._id = _id; }

    private String fid;
    public String getFid() { return this.fid; }
    public void setFid(String fid) { this.fid = fid; }

    private String age_range;
    public String getAgeRange() { return this.age_range; }
    public void setAgeRange(String age_range) { this.age_range = age_range; }

    private String gender;
    public String getGender() { return this.gender; }
    public void setGender(String gender) { this.gender = gender; }

    private String firstname;
    public String getFirstname() { return this.firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    private String lastname;
    public String getLastname() { return this.lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    private String name;
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    private String email;
    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    private String picture;
    public String getPicture() { return this.picture; }
    public void setPicture(String picture) { this.picture = picture; }

    private String face_link;
    public String getFaceLink() { return this.face_link; }
    public void setFaceLink(String face_link) { this.face_link = face_link; }

    private String updated_at;
    public String getUpdatedAt() { return this.updated_at; }
    public void setUpdatedAt(String updated_at) { this.updated_at = updated_at; }

    private String created_at;
    public String getCreatedAt() { return this.created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }

    private ArrayList<Bonus> bonus;
    public ArrayList<Bonus> getBonus() { return this.bonus; }
    public void setBonus(ArrayList<Bonus> bonus) { this.bonus = bonus; }

    private ArrayList<String> following;
    public ArrayList<String> getFollowing() { return this.following; }
    public void setFollowing(ArrayList<String> following) { this.following = following; }

    private ArrayList<String> followers;
    public ArrayList<String> getFollowers() { return this.followers; }
    public void setFollowers(ArrayList<String> followers) { this.followers = followers; }

    private Device device;
    public Device getDevice() { return this.device; }
    public void setDevice(Device device) { this.device = device; }

    private ArrayList<Notification> notification;
    public ArrayList<Notification> getNotification() { return this.notification; }
    public void setNotification(ArrayList<Notification> notification) { this.notification = notification; }

    public class Bonus
    {
        private String bonus_place_id;

        public String getBonusPlaceId() { return this.bonus_place_id; }

        public void setBonusPlaceId(String bonus_place_id) { this.bonus_place_id = bonus_place_id; }

        private String bonus_place;

        public String getBonusPlace() { return this.bonus_place; }

        public void setBonusPlace(String bonus_place) { this.bonus_place = bonus_place; }

        private String bonus_description;

        public String getBonusDescription() { return this.bonus_description; }

        public void setBonusDescription(String bonus_description) { this.bonus_description = bonus_description; }

        private String bonus_end;

        public String getBonusEnd() { return this.bonus_end; }

        public void setBonusEnd(String bonus_end) { this.bonus_end = bonus_end; }

        private String category;

        public String getCategory() { return this.category; }

        public void setCategory(String category) { this.category = category; }
    }

    public class Device
    {
        private String deviceID;

        public String getDeviceID() { return this.deviceID; }

        public void setDeviceID(String deviceID) { this.deviceID = deviceID; }

        private String deviceType;

        public String getDeviceType() { return this.deviceType; }

        public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    }

    public class Notification
    {
        private String type;

        public String getType() { return this.type; }

        public void setType(String type) { this.type = type; }

        private String userid;

        public String getUserid() { return this.userid; }

        public void setUserid(String userid) { this.userid = userid; }

        private String message;

        public String getMessage() { return this.message; }

        public void setMessage(String message) { this.message = message; }

        private String created_at;

        public String getCreatedAt() { return this.created_at; }

        public void setCreatedAt(String created_at) { this.created_at = created_at; }
    }

}
