package models;

/**
 * Created by appleuser on 10/25/16.
 */

public class FollowUser {
    private String _id;
    private String name;
    private String picture;
    private String follow;
    private int event;
    private String amifollow;


    public FollowUser(String id, String name, String picture, String follow, int event, String amifollow) {
        _id = id;
        this.name = name;
        this.picture = picture;
        this.follow = follow;
        this.event = event;
        this.amifollow = amifollow;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getAmifollow() {
        return amifollow;
    }

    public void setAmifollow(String amifollow) {
        this.amifollow = amifollow;
    }
}
