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
import com.example.parseinstagram.models.Comment;

import org.json.JSONException;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    public static final String TAG = "CommentAdapter";
    public static Context context;
    List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        try {
            holder.bind(comment, holder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView  cmUsername, cmComment;
        ImageView cmProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cmComment = itemView.findViewById(R.id.cmDescription);
            cmUsername = itemView.findViewById(R.id.cmUsername);
            cmProfile = itemView.findViewById(R.id.cmProfile);
        }

        public void bind(Comment comment, ViewHolder holder) throws JSONException {
            cmUsername.setText(comment.getUser().getUsername());
            cmComment.setText(comment.getDescription());

            Glide.with(holder.itemView.getContext())
                    .load(comment.getUser()
                            .getParseFile("Profile")
                            .getUrl())
                    .transform(new CircleCrop())
                    .into(cmProfile);
        }
    }
}
