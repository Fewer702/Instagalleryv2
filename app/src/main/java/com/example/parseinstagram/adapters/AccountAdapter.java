package com.example.parseinstagram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parseinstagram.R;
import com.example.parseinstagram.helpers.TimeFormatter;
import com.example.parseinstagram.models.Post;
import com.parse.ParseFile;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.parseinstagram.models.Post;

import java.util.ArrayList;

public class AccountAdapter extends ArrayAdapter<Post> {

    public AccountAdapter(@NonNull Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_user_post, parent, false);
        }

        ImageView userPostContent = listitemView.findViewById(R.id.userPostContent);
        Post post = getItem(position);
        Glide.with(getContext())
                .load(post.getImage().getUrl())
                .centerCrop()
                .transform(new RoundedCorners(20))
                .into(userPostContent);

        return listitemView;
    }
}