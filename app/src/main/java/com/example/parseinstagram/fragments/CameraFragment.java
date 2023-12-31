package com.example.parseinstagram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.parseinstagram.models.Post;
import com.example.parseinstagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;


public class CameraFragment extends Fragment {
    public static final String TAG = "CameraFragment";
    private ImageView postImg;
    private EditText descriptionEditText;
    private ImageButton pictureBtn;
    private ImageButton submitBtn;
    private ProgressBar progressBar;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 25;
    private File photoFile;
    public String photoFileName = "photo.jpg";


    public CameraFragment() {}
    

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
        
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        postImg = view.findViewById(R.id.imageInput);
        descriptionEditText = view.findViewById(R.id.descriptionImg);
        pictureBtn = view.findViewById(R.id.picBtn);
        submitBtn = view.findViewById(R.id.submitBtn);
        progressBar = view.findViewById(R.id.pbLoading);


        // Camera button click
        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Launching camera", Toast.LENGTH_SHORT).show();
                launchCamera();
                postImg.setVisibility(View.VISIBLE);
            }
        });

        // Submit button click
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Description = descriptionEditText.getText().toString();
                if(Description.isEmpty()){
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || postImg.getDrawable() == null){
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                    return;

                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(Description, currentUser);
            }
        });
        
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){

                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                postImg.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Error taking picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);


    }

    private void savePost(String description, ParseUser currentUser) {
        // Show progress bar
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post was saved succesfully");
                descriptionEditText.setText("");
                postImg.setImageResource(0);
                // Hide progress bar
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

}