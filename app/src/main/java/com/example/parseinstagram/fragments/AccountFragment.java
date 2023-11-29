package com.example.parseinstagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parseinstagram.activities.LoginActivity;
import com.example.parseinstagram.R;
import com.example.parseinstagram.adapters.AccountAdapter;
import com.example.parseinstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class AccountFragment extends Fragment {
    public static final String TAG = "AccountFragment";
    private TextView acc_UserName;
    private ImageView acc_Profile;
    private Button logOutbtn;
    private AccountAdapter user_adapter;
    private ArrayList<Post> user_allPosts;
    private GridView gvPost;
    private ParseUser user;
    String profile;


    public AccountFragment() {}

    public static AccountFragment newInstance(String title) {
        AccountFragment frag = new AccountFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        logOutbtn = view.findViewById(R.id.logOutBtn);
        acc_Profile = view.findViewById(R.id.accountProfile);
        acc_UserName = view.findViewById(R.id.accountUsername);

        gvPost = view.findViewById(R.id.gvPost);

        user_allPosts = new ArrayList<>();
        user_adapter = new AccountAdapter(getContext(), user_allPosts);

        // Setting adapter on the recycler view
        gvPost.setAdapter(user_adapter);

        Bundle bundle = getArguments();
//         if bottom navigation profile is clicked get the current username
//         else if username or userprofile is clicked in home, get specific user
        if(bundle == null) {
            acc_UserName.setText(ParseUser.getCurrentUser().getUsername());
            user = ParseUser.getCurrentUser();
            profile = ParseUser.getCurrentUser().getParseFile("Profile").getUrl();

        }else{
            Post post = Parcels.unwrap(bundle.getParcelable("post"));
            acc_UserName.setText(post.getUser().getUsername());
            user = post.getUser();
            profile = post.getUser().getParseFile("Profile").getUrl();

        }

        queryPost();

        if(profile != null){
            Glide.with(getContext())
                    .load(profile)
                    .centerCrop()
                    .transform(new CircleCrop())
                    .into(acc_Profile);
        }



        // Log out button
        logOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                Log.i(TAG, "Logout");
            }
        });

    }

    private void queryPost() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for(Post post: posts){
                    Log.i(TAG, "Post" + post.getDescription() + ",  username: " + post.getUser().getUsername());

                }
                user_allPosts.addAll(posts);
                user_adapter.notifyDataSetChanged();
            }
        });
    }

}
