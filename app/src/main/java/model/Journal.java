package model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thought;
    private Timestamp date;
    private String username;
    private String userId;
    private String imageUrl;


    public Journal() {
    }

    public Journal(String title, String thought, Timestamp date, String username, String userId, String imageUrl) {
        this.title = title;
        this.thought = thought;
        this.date = date;
        this.username = username;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }
}
