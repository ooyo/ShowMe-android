package models;

/**
 * Created by appleuser on 10/25/16.
 */

public class ReviewModel
{
    private String user_id;

    public String getUserId() { return this.user_id; }

    public void setUserId(String user_id) { this.user_id = user_id; }

    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    private String picture;

    public String getPicture() { return this.picture; }

    public void setPicture(String picture) { this.picture = picture; }

    private String user_review;

    public String getUserReview() { return this.user_review; }

    public void setUserReview(String user_review) { this.user_review = user_review; }

    private String created_at;

    public String getCreatedAt() { return this.created_at; }

    public void setCreatedAt(String created_at) { this.created_at = created_at; }

    public ReviewModel(String user_id, String name, String picture, String user_review){
        this.user_id = user_id;
        this.name = name;
        this.picture = picture;
        this.user_review = user_review;
    }
}
