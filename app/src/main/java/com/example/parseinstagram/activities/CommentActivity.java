package com.example.parseinstagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.parseinstagram.MainActivity;
import com.example.parseinstagram.R;
import com.example.parseinstagram.models.Comment;
import com.example.parseinstagram.models.Post;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class CommentActivity extends AppCompatActivity {
    public static final String TAG = "CommentActivity";

    private ImageView cProfile;
    private TextView cUserName;
    private TextInputEditText commentField;
    private Button button;
    Post post;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        post = Parcels.unwrap(getIntent().getParcelableExtra("Post"));

        String profile = ParseUser.getCurrentUser().getParseFile("Profile").getUrl();

        cProfile = findViewById(R.id.commentProfile);
        cUserName = findViewById(R.id.commentUsername);
        commentField = findViewById(R.id.commentTxt);
        button = findViewById(R.id.commentBtn);

        cUserName.setText(ParseUser.getCurrentUser().getUsername());

        if(profile != null){
            Glide.with(this)
                    .load(profile)
                    .transform(new CircleCrop())
                    .into(cProfile);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = commentField.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(CommentActivity.this, "Comment is empty", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    sendComment(description, currentUser);
                    Intent intent = new Intent(CommentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        });
    }

    private void sendComment(String description, ParseUser currentUser) {
        Comment comment = new Comment();

        comment.setUser(currentUser);
        comment.setDescription(description);
        post.setComments(comment);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show();
                    return;
                }
                commentField.setText("");
            }
        });
    }
}
