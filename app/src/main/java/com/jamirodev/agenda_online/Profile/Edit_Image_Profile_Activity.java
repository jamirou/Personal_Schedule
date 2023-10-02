package com.jamirodev.agenda_online.Profile;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jamirodev.agenda_online.R;

import java.util.HashMap;
import java.util.Objects;

public class Edit_Image_Profile_Activity extends AppCompatActivity {

    ImageView Image_Profile_Update;
    Button BtnChooseImage, BtnUpdateImage;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Dialog dialog_choose_image;
    Uri imageUri = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image_profile);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Select Image");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Image_Profile_Update = findViewById(R.id.Image_Profile_Update);
        BtnChooseImage = findViewById(R.id.BtnChooseImage);
        BtnUpdateImage = findViewById(R.id.BtnUpdateImage);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        dialog_choose_image = new Dialog(Edit_Image_Profile_Activity.this);
        BtnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Edit_Image_Profile_Activity.this, "Chose image", Toast.LENGTH_SHORT).show();
                ChooseImage();
            }
        });

        BtnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri == null){
                    Toast.makeText(Edit_Image_Profile_Activity.this, "Insert new image", Toast.LENGTH_SHORT).show();
                }else {
                    UploadImageToStorage();
                }
            }
        });

        progressDialog = new ProgressDialog(Edit_Image_Profile_Activity.this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setCanceledOnTouchOutside(false);

        ReadImage();
    }

    private void ReadImage() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Image_Profile = "" + snapshot.child("Profile_img").getValue();

                Glide.with(getApplicationContext())
                        .load(Image_Profile)
                        .placeholder(R.drawable.user)
                        .into(Image_Profile_Update);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void UploadImageToStorage() {
        progressDialog.setMessage("Upload image");
        progressDialog.show();
        String ImageFiles = "ProfileImages/";
        String ImageName = ImageFiles+firebaseAuth.getUid();
        StorageReference reference = FirebaseStorage.getInstance().getReference(ImageName);
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        String uriImage = ""+uriTask.getResult();
                        UpdateImageDB(uriImage);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Edit_Image_Profile_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void UpdateImageDB(String uriImage) {
        progressDialog.setMessage("Updating image");
        progressDialog.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        if (imageUri != null) {
            hashMap.put("Profile_img", ""+uriImage);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(user.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(Edit_Image_Profile_Activity.this, "Imagen actualizada", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Edit_Image_Profile_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ChooseImage() {
        Button Btn_ChooseGallery, Btn_ChooseCamera;

        dialog_choose_image.setContentView(R.layout.dialog_box_choose_image);
        Btn_ChooseGallery = dialog_choose_image.findViewById(R.id.Btn_ChooseGallery);
        Btn_ChooseCamera = dialog_choose_image.findViewById(R.id.Btn_ChooseCamera);

        Btn_ChooseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Toast.makeText(Edit_Image_Profile_Activity.this, "Gallery", Toast.LENGTH_SHORT).show(); */
//                SelectImageGallery();
                if (ContextCompat.checkSelfPermission(Edit_Image_Profile_Activity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    SelectImageGallery();
                    dialog_choose_image.dismiss();
                } else {
                    GalleryPermissionsRequest.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    dialog_choose_image.dismiss();
                }
            }
        });

        Btn_ChooseCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(Edit_Image_Profile_Activity.this, "Camera", Toast.LENGTH_SHORT).show();
               /* SelectImageCamara();
                dialog_choose_image.dismiss(); */
                if (ContextCompat.checkSelfPermission(Edit_Image_Profile_Activity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    SelectImageCamara();
                    dialog_choose_image.dismiss();
                }else {
                    RequestPermissionCamera.launch(Manifest.permission.CAMERA);
                    dialog_choose_image.dismiss();
                }
            }
        });

        dialog_choose_image.show();
        dialog_choose_image.setCanceledOnTouchOutside(true);

    }

    private void SelectImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        GET IMAGE URI
                        Intent data = result.getData();
                        imageUri = data.getData();
//                        SET IMAGE SELECTED ON IV
                        Image_Profile_Update.setImageURI(imageUri);

                    } else {
                        Toast.makeText(Edit_Image_Profile_Activity.this, "Cancelled by user", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    //    PERMISSIONS TO ACCESS GALLERY
    private ActivityResultLauncher<String> GalleryPermissionsRequest =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    SelectImageGallery();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    private void SelectImageCamara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New image");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        CameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> CameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Image_Profile_Update.setImageURI(imageUri);
                    } else {
                        Toast.makeText(Edit_Image_Profile_Activity.this, "Cancelled by user", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

//    PERMISSION TO ACCESS CAMERA
    private ActivityResultLauncher<String> RequestPermissionCamera =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
            if (isGranted){
                SelectImageCamara();
            }else {
                Toast.makeText(this, "Denied permission", Toast.LENGTH_SHORT).show();
            }
        });

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}