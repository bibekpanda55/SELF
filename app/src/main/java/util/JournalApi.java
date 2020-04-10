package util;

import android.app.Application;

public class JournalApi extends Application {
    private String userId;
    private String username;
    public static JournalApi instance;

    public static JournalApi getInstance()
    {
        if(instance==null)
            instance=new JournalApi();
        return instance;
    }

    public JournalApi() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
