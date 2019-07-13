package com.example.cookpad.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.model.Food;
import com.example.cookpad.model.StepCook;
import com.example.cookpad.until.PrefManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FoodInformationActivity extends AppCompatActivity {

    @BindView(R.id.btnClose)
    ImageView btnClose;
    @BindView(R.id.btnAired)
    Button btnAired;
    @BindView(R.id.imgAvatarFood)
    ImageView imgAvatarFood;
    @BindView(R.id.imgAvatarUser)
    CircleImageView imgAvatarUser;
    @BindView(R.id.edResources1)
    EditText edResources1;
    @BindView(R.id.edResources2)
    EditText edResources2;
    @BindView(R.id.edResources3)
    EditText edResources3;
    @BindView(R.id.eDStep1)
    EditText eDStep1;
    @BindView(R.id.imgStep1)
    ImageView imgStep1;
    @BindView(R.id.eDStep2)
    EditText eDStep2;
    @BindView(R.id.imgStep2)
    ImageView imgStep2;
    @BindView(R.id.edStep3)
    EditText edStep3;
    @BindView(R.id.imgStep3)
    ImageView imgStep3;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseFirestore db;
    CollectionReference documentReference;
    CollectionReference documentReferenceFood;

    Food food;

    private static final int PICK_IMAGE_REQUEST = 11;

    PrefManager prefManager;
    @BindView(R.id.tvNameFood)
    TextView tvNameFood;
    @BindView(R.id.tvNameUser)
    TextView tvNameUser;

    Boolean bImgStep1;
    Boolean bImgStep2;
    Boolean bImgStep3;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.btnAdd1)
    Button btnAdd1;
    @BindView(R.id.btnAdd2)
    Button btnAdd2;
    @BindView(R.id.btnAdd3)
    Button btnAdd3;
    @BindView(R.id.edDescription)
    EditText edDescription;

    private Uri filePath1;
    private Uri filePath2;
    private Uri filePath3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        ButterKnife.bind(this);

        Food foodIntent = (Food) getIntent().getSerializableExtra("food_item");


        prefManager = new PrefManager(this);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("foods");

        db = FirebaseFirestore.getInstance();
        documentReferenceFood = db.collection("foods");
        documentReference = db.collection("foodDoing");

        documentReference.document(foodIntent.getId()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null){
                food = documentSnapshot.toObject(Food.class);
                initView();
            }
        });




    }

    @OnClick({R.id.btnClose, R.id.btnAired, R.id.imgStep1, R.id.imgStep2, R.id.imgStep3, R.id.btnAdd1, R.id.btnAdd2, R.id.btnAdd3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                finish();
                break;
            case R.id.btnAired:
                saveFoods();
                break;
            case R.id.imgStep1:
                bImgStep1 = true;
                chooseImage();
                break;
            case R.id.imgStep2:
                bImgStep2 = true;
                chooseImage();
                break;
            case R.id.imgStep3:
                bImgStep3 = true;
                chooseImage();
                break;
            case R.id.btnAdd1:
                uploadStep(filePath1, eDStep1.getText().toString(), 0);
                break;
            case R.id.btnAdd2:
                uploadStep(filePath2, eDStep2.getText().toString(), 1);
                break;
            case R.id.btnAdd3:
                uploadStep(filePath3, edStep3.getText().toString(), 2);
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
            if (bImgStep1) {
                filePath1 = data.getData();
            } else if (bImgStep2) {
                filePath2 = data.getData();
            } else if (bImgStep3) {
                filePath3 = data.getData();
            }
            try {
                if (bImgStep1) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                    imgStep1.setImageBitmap(bitmap);
                    bImgStep1 = false;
                } else if (bImgStep2) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    imgStep2.setImageBitmap(bitmap);
                    bImgStep2 = false;
                } else if (bImgStep3) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                    imgStep3.setImageBitmap(bitmap);
                    bImgStep3 = false;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        Picasso.get().load(food.getImageUrl()).fit().centerCrop().into(imgAvatarFood);
        Picasso.get().load(prefManager.getUser().getImageUrl()).fit().centerCrop().into(imgAvatarUser);
        tvNameFood.setText(food.getTitle());
        tvNameUser.setText(prefManager.getUser().getName());
        edDescription.setText(food.getDescription());

        if (food.getResources() != null) {
            edResources1.setText(food.getResources().get(0));
            edResources2.setText(food.getResources().get(1));
            edResources3.setText(food.getResources().get(2));
        }

        if (food.getStepCookList() != null) {
            eDStep1.setText(food.getStepCookList().get(0).getContent());
            eDStep2.setText(food.getStepCookList().get(1).getContent());
            edStep3.setText(food.getStepCookList().get(2).getContent());

            for (int i = 0; i < 3; i++) {
                if (food.getStepCookList().get(i).getUrlImage().length() > 0) {
                    if (i == 0){
                        Picasso.get().load(food.getStepCookList().get(i).getUrlImage()).fit().centerCrop().into(imgStep1);
                    }else if (i == 1){
                        Picasso.get().load(food.getStepCookList().get(i).getUrlImage()).fit().centerCrop().into(imgStep2);
                    }else {
                        Picasso.get().load(food.getStepCookList().get(i).getUrlImage()).fit().centerCrop().into(imgStep3);
                    }


                }
            }



        }


    }


    private void saveFoods() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        documentReference.document(food.getId()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                Food food = documentSnapshot.toObject(Food.class);
                List<String> resourceList = new ArrayList<>();

                String description = edDescription.getText().toString();

                String resource1 = edResources1.getText().toString();
                String resource2 = edResources2.getText().toString();
                String resource3 = edResources3.getText().toString();
                resourceList.add(resource1);
                resourceList.add(resource2);
                resourceList.add(resource3);

                food.setResources(resourceList);
                food.setDescription(description);

                documentReference.document(food.getId()).update(
                        "resources", resourceList,
                        "description",description
                ).addOnSuccessListener(aVoid -> {
                    documentReferenceFood.document(food.getId()).set(
                            food
                    ).addOnSuccessListener(aVoid1 -> {
                        finish();
                        Toast.makeText(this, "Cập nhật món ăn thành công", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }).addOnFailureListener(e -> {
                        finish();
                        Toast.makeText(this, "Cập nhật món ăn thất bại", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    });

                }).addOnFailureListener(e -> {


                });


            }
        });

    }

    private void uploadStep(Uri filePath, String stepString, int id) {

        if (filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("steps/" + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);


            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }).addOnSuccessListener(uri -> Toast.makeText(FoodInformationActivity.this, "Uploaded", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FoodInformationActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    documentReference.document(food.getId()).get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot != null) {
                            Food food = documentSnapshot.toObject(Food.class);

                            ArrayList<StepCook> stepCooks = new ArrayList<>();
                            stepCooks = food.getStepCookList();
                            if (stepCooks.get(id) != null) {
                                stepCooks.set(id, new StepCook(id, stepString, downloadUri.toString()));
                            } else {
                                Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show();
                            }


                            documentReference.document(food.getId()).update(
                                    "stepCookList", stepCooks
                            ).addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Cập nhật các bước thành công", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(this, "Cập nhật các bước thất bại", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            });
                        }
                    });
                }

            });

        }else {
            Toast.makeText(this, "Mời thêm vào các file ảnh !", Toast.LENGTH_SHORT).show();
        }

    }


}