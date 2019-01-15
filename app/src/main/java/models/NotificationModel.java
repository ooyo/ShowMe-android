package models;

/**
 * Created by appleuser on 11/28/16.
 */

public class NotificationModel {
    private String type;
    private String additional;
    private String event_id;
    private String message;
    private String created_at;
    private String name;
    private String picture;

    public NotificationModel(String type, String additional, String event_id, String message, String created_at, String name, String picture) {
        this.type = type;
        this.additional = additional;
        this.event_id = event_id;
        this.message = message;
        this.created_at = created_at;
        this.name = name;
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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
}
