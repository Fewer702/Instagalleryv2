package com.example.parseinstagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parseinstagram.activities.CommentActivity;
import com.example.parseinstagram.activities.PostActivity;
//import com.example.parseinstagram.UserAllPostActivity;
import com.example.parseinstagram.fragments.AccountFragment;
import com.example.parseinstagram.helpers.TimeFormatter;
import com.example.parseinstagram.models.Post;
import com.example.parseinstagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    public static final String TAG = "PostsAdapter";
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        try {
            holder.bind(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Method to clean all elements of the recycler
    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }

    // Method to add a list of Posts -- change to type used
    public void addAll(List<Post> postList){
        posts.addAll(postList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userName;
        private TextView description;
        private TextView postCreation;
        private ImageView picture, userProfile;
        private LinearLayout container;
        private TextView comment;
        private ImageView like;
        private int likeCount;
        private TextView likeCounter;
        private ArrayList<String> userLikes;
        ParseUser currentUser;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            description = itemView.findViewById(R.id.postDescription);
            picture = itemView.findViewById(R.id.postImg);
            userProfile = itemView.findViewById(R.id.userProfile);
            postCreation = itemView.findViewById(R.id.accountPostTime);
            container = itemView.findViewById(R.id.userContainer);
            comment = itemView.findViewById(R.id.postComment);
            comment = itemView.findViewById(R.id.postComment);
            like = itemView.findViewById(R.id.postLike);
            likeCounter = itemView.findViewById(R.id.postLikeCounter);

        }

        public void bind(Post post) throws JSONException {
            String timeFormat = TimeFormatter.getTimeDifference(post.getCreatedAt().toString());
            ParseUser currentUser = ParseUser.getCurrentUser();
            userLikes = Post.fromJsonArray(post.getLike());

            // Bind the post data to the view elements
            userName.setText(post.getUser().getUsername());
            description.setText(post.getUser().getUsername() + ": " +post.getDescription());
            postCreation.setText(timeFormat + " ago");

            ParseFile image = post.getImage();
            if(image != null){
                Glide.with(context)
                        .load(image.getUrl())
                        .into(picture);
            }

            Glide.with(context)
                    .load(post.getUser()
                            .getParseFile("Profile").getUrl())
                            .centerCrop()
                            .transform(new CircleCrop())
                            .into(userProfile);

            // check if heart is enable
            try{
                if (userLikes.contains(currentUser.getObjectId())) {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_heart_);
                    like.setImageDrawable(drawable);
                }else {
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_heart_curvy_outline);
                    like.setImageDrawable(drawable);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            // Post is clicked
            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra("Post", Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });

            //When username or profile picture is clicked
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

                    // set parameters
                    AccountFragment accountFragment = AccountFragment.newInstance("Some Title");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", Parcels.wrap(post));
                    accountFragment.setArguments(bundle);

                    fragmentManager.beginTransaction().replace(R.id.flContainer, accountFragment).commit();
                }
            });

            // Comment button is clicked
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("Post", Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });

            // Like button is clicked
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeCount = post.getLikeCount();
                    int index;

                    if (!userLikes.contains(currentUser.getObjectId())){
                        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_heart_);
                        like.setImageDrawable(drawable);
                        likeCount++;
                        index = -1;

                    }else {
                        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_heart_curvy_outline);
                        like.setImageDrawable(drawable);
                        likeCount--;
                        index = userLikes.indexOf(currentUser.getObjectId());
                    }

                    likeCounter.setText(String.valueOf(likeCount));
                    saveLike(post, likeCount, index, currentUser);
                }
            });
        }

        private void saveLike(Post post, int likeCount, int index, ParseUser currentUser) {
            post.setLikeCount(likeCount);

            if (index == -1){
                post.setLike(currentUser);
                userLikes.add(currentUser.getObjectId());
            }else {
                userLikes.remove(index);
                post.removeLike(userLikes);
            }

            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null){
                        Log.e(TAG, "Error while saving", e);
                        Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i(TAG, userLikes.toString());

                }
            });
        }
        }
}



