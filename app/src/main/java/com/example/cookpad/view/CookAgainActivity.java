package com.example.cookpad.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.model.Food;
import com.example.cookpad.until.Constant;
import com.example.cookpad.until.PrefManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static maes.tech.intentanim.CustomIntent.customType;

public class CookAgainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    @BindView(R.id.btnAddToMenu)
    Button btnAddToMenu;
    Food food;
    @BindView(R.id.imgIconFood)
    ImageView imgIconFood;
    @BindView(R.id.imgFood)
    ImageView imgFood;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseFirestore db;
    CollectionReference documentReference;
    @BindView(R.id.tvFoodName)
    TextView tvFoodName;

    private Uri filePath;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_again);

        food = (Food) getIntent().getSerializableExtra("food_user");
        ButterKnife.bind(this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("foods");

        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("foodDoing");

        prefManager = new PrefManager(this);

        tvFoodName.setText(food.getTitle());


    }


    @OnClick({R.id.imgIconFood, R.id.btnAddToMenu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgIconFood:
                chooseImage();
                break;
            case R.id.btnAddToMenu:
                uploadFood();
                break;
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgFood.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFood() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);

            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return ref.getDownloadUrl();
            }).addOnSuccessListener(uri -> Toast.makeText(CookAgainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(CookAgainActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    documentReference.document(food.getId()).get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot != null) {
                            documentReference.document(food.getId()).update(
                                    "imageUrl", downloadUri.toString(),
                                    "user",prefManager.getUser()

                            ).addOnSuccessListener(aVoid -> {
                                Toast.makeText(CookAgainActivity.this, "Cập nhật món ăn thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CookAgainActivity.this, HomeActivity.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                                customType(CookAgainActivity.this,"fadein-to-fadeout");
                            }).addOnFailureListener(e -> {
                                Toast.makeText(CookAgainActivity.this, "Cập nhật món ăn thất bại", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            });

                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(CookAgainActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
