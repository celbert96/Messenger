package edu.uga.cs.messenger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class SelectImageActivity extends AppCompatActivity
{
    private Button selectPhotoBtn;
    private Button finishSetupBtn;
    private String username;
    private Uri selectedPhotoURI = null;
    private String profilePicDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        getSupportActionBar().hide();

        username = getIntent().getStringExtra("username");
        selectPhotoBtn = findViewById(R.id.select_image_btn);
        selectPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);

            }
        });

        finishSetupBtn = findViewById(R.id.finish_setup_btn);


        finishSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Please wait while we finish up. . .", Toast.LENGTH_LONG).show();
                selectPhotoBtn.setEnabled(false);
                finishSetupBtn.setEnabled(false);
                uploadImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null)
        {
            Log.d("ImageViewActivity", "Image Selected!");

            selectedPhotoURI = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedPhotoURI);
                BitmapDrawable bd = new BitmapDrawable(bitmap);
                selectPhotoBtn.setBackground(bd);
                selectPhotoBtn.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(selectedPhotoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("URI = ", uri.toString());
                        profilePicDownloadUrl = uri.toString();
                        saveUserToDatabase();
                    }
                });

                ref.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FAILURE", e.getMessage());
                    }
                });
            }
        });

    }


    private void saveUserToDatabase()
    {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uid);
        Log.d("ProfilePicDownloadURL", " = " + profilePicDownloadUrl);
        User user = new User(uid, username, profilePicDownloadUrl);
        ref.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Added to database successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MessagesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }


   /* private void addImageToUserEntry(String profilePicURL)
    {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uid);

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("/imageURL", profilePicURL);

        ref.updateChildren(userUpdate);

    }*/

}
