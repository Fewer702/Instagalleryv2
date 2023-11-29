package com.example.parseinstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(analyze=Post.class)
@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_COMMENTS = "userComments";
    public static final String KEY_LIKE_COUNT= "likeCount";
    public static final String KEY_LIKE = "userLikes";


    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return  getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser(){
        return  getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public JSONArray getComments(){
        return getJSONArray(KEY_COMMENTS);
    }

    public void setComments(Comment comments){
        add(KEY_COMMENTS, comments);
    }

    public  int getLikeCount() {return getInt(KEY_LIKE_COUNT);}

    public void setLikeCount(int num){put(KEY_LIKE_COUNT, num);}

    public JSONArray getLike(){return getJSONArray(KEY_LIKE);}

    public void setLike(ParseUser user){add(KEY_LIKE, user);}

    public void removeLike(List<String> userList){
        remove(KEY_LIKE);
        put(KEY_LIKE, userList);
    }

    public static ArrayList<String> fromJsonArray(JSONArray jsonArray) throws JSONException {
        ArrayList<String> userList = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                userList.add(jsonArray.getJSONObject(i).getString("objectId"));
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return userList;
    }

}
