package com.example.parseinstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(analyze=Post.class)
@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String COMMENT_DESCRIPTION = "description";
    public static final String COMMENT_USER = "person";
    public static final String COMMENT_CREATED_KEY = "createdAt";

    public Comment(){}

    public static List<String> fromJsonArray(JSONArray jsonArray) {
        List<String> commentList = new ArrayList<String>();

        try {
            for (int i = 0; i < jsonArray.length(); i++){
                commentList.add(jsonArray.getJSONObject(i).getString("objectId"));
            }
        }catch (NullPointerException | JSONException e){
            e.printStackTrace();
        }

        return commentList;
    }

    public String getDescription(){
        return getString(COMMENT_DESCRIPTION);
    }

    public void setDescription(String description){
        put(COMMENT_DESCRIPTION, description);
    }

    public ParseUser getUser(){
        return getParseUser(COMMENT_USER);
    }

    public void setUser(ParseUser user){
        put(COMMENT_USER, user);
    }
}
