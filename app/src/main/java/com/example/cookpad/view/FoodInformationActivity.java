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
    @BindView(R.id.eDStep3)
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

    Boolean bImgStep1 = false;
    Boolean bImgStep2 = false;
    Boolean bImgStep3 = false;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.edDescription)
    EditText edDescription;

    private Uri filePath1;
    private Uri filePath2;
    private Uri filePath3;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đợi tý nhé ...");
        progressDialog.setCancelable(false);

        Food foodIntent = (Food) getIntent().getSerializableExtra("food_item");


        prefManager = new PrefManager(this);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("foods");

        db = FirebaseFirestore.getInstance();
        documentReferenceFood = db.collection("foods");
        documentReference = db.collection("foodDoing");

        documentReference.document(foodIntent.getId()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot != null) {
                food = documentSnapshot.toObject(Food.class);
                initView();
            }
        });


    }

    @OnClick({R.id.btnClose, R.id.btnAired, R.id.imgStep1, R.id.imgStep2, R.id.imgStep3})
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
                uploadStep(filePath1);
            } else if (bImgStep2) {
                filePath2 = data.getData();
                uploadStep(filePath2);
            } else if (bImgStep3) {
                filePath3 = data.getData();
                uploadStep(filePath3);
            }
            try {
                if (bImgStep1) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                    imgStep1.setImageBitmap(bitmap);


                } else if (bImgStep2) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    imgStep2.setImageBitmap(bitmap);


                } else if (bImgStep3) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                    imgStep3.setImageBitmap(bitmap);


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadStep(Uri filePath) {

        if (filePath != null) {

            progressDialog.show();
            StorageReference ref = storageReference.child("steps/" + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);


            Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }).addOnSuccessListener(uri -> Toast.makeText(FoodInformationActivity.this, "Uploaded", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(FoodInformationActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (bImgStep1) {
                        filePath1 = task.getResult();
                        progressDialog.dismiss();
                        bImgStep1 = false;
                    } else if (bImgStep2) {
                        filePath2 = task.getResult();
                        progressDialog.dismiss();
                        bImgStep2 = false;
                    } else if (bImgStep3) {
                        filePath3 = task.getResult();
                        progressDialog.dismiss();
                        bImgStep3 = false;
                    }

                }
            });

        } else {
            Toast.makeText(this, "Mời thêm vào các file ảnh !", Toast.LENGTH_SHORT).show();
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
                    if (i == 0) {
                        Picasso.get().load(food.getStepCookList().get(i).getUrlImage()).fit().centerCrop().into(imgStep1);
                        filePath1 = Uri.parse(food.getStepCookList().get(i).getUrlImage());
                    } else if (i == 1) {
                        Picasso.get().load(food.getStepCookList().get(i).getUrlImage()).fit().centerCrop().into(imgStep2);
                        filePath2 = Uri.parse(food.getStepCookList().get(i).getUrlImage());
                    } else {
                        Picasso.get().load(food.getStepCookList().get(i).getUrlImage()).fit().centerCrop().into(imgStep3);
                        filePath3 = Uri.parse(food.getStepCookList().get(i).getUrlImage());
                    }


                }
            }


        }


    }


    private void saveFoods() {

        if (edDescription.getText().toString().length() > 0
                && edResources1.getText().toString().length() > 0 && edResources2.getText().toString().length() > 0
                && edResources3.getText().toString().length() > 0 && eDStep1.getText().toString().length() > 0
                && eDStep2.getText().toString().length() > 0 && edStep3.getText().toString().length() > 0
                && filePath1.toString().length() > 0
                && filePath2.toString().length() > 0
                && filePath3.toString().length() > 0) {
            progressDialog.show();
            documentReference.document(food.getId()).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot != null) {
                    Food food = documentSnapshot.toObject(Food.class);
                    List<String> resourceList = new ArrayList<>();
                    List<StepCook> stepCookList = new ArrayList<>();

                    String description = edDescription.getText().toString();

                    stepCookList.add(new StepCook(0, eDStep1.getText().toString(), filePath1.toString()));
                    stepCookList.add(new StepCook(0, eDStep2.getText().toString(), filePath2.toString()));
                    stepCookList.add(new StepCook(0, edStep3.getText().toString(), filePath3.toString()));

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
                            "description", description,
                            "stepCookList", stepCookList
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

    }


}