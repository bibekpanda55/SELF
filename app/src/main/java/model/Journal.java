package model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thought;
    private Timestamp date;
    private String username;
    private String userId;
    private String imageUrl;
    private  String documentId;

    public Journal() {
    }

    public Journal(String title, String thought, Timestamp date, String username, String userId, String imageUrl,String documentId) {
        this.title = title;
        this.thought = thought;
        this.date = date;
        this.username = username;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.documentId=documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getThought() {
        return thought;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
