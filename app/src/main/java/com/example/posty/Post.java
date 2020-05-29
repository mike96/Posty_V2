package com.example.posty;

import android.graphics.Bitmap;
import android.media.Image;

public class Post {
    private String username;
    private String displayName;
    private String profilePic;
    private String content;
    private String picture;
    private Bitmap pic;
    
    public Post(String username, String displayName, String profilePic, String content, String picture, Bitmap pic){
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.content = content;
        this.picture = picture;
        this.pic = pic;
    }
    public String getUsername(){return username;}
    public String getDisplayName(){return displayName;}
    public String getProfilePic(){return profilePic;}
    public String getContent(){return content;}
    public String getPicture(){return picture;}
    public Bitmap getPic(){return pic;}
}
