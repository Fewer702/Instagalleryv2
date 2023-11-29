package com.example.parseinstagram;

import android.app.Application;

import com.example.parseinstagram.models.Comment;
import com.example.parseinstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    // Initializes Parse SDK as soon the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        //Register your parse model
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}
