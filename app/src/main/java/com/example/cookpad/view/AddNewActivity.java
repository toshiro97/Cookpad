package com.example.cookpad.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.model.Comment;
import com.example.cookpad.model.Food;
import com.example.cookpad.model.StepCook;
import com.example.cookpad.until.PrefManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.UUID;

public class AddNewActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 11;
    @BindView(R.id.btnClose)
    ImageView btnClose;
    @BindView(R.id.btnAired)
    Button btnAired;
    @BindView(R.id.imgAvatarFood)
    ImageView imgAvatarFood;
    @BindView(R.id.linearSetImage)
    LinearLayout linearSetImage;

    @BindView(R.id.imgAvatarUser)
    CircleImageView imgAvatarUser;
    @BindView(R.id.tvNameUser)
    TextView tvNameUser;
    @BindView(R.id.edDescription)
    EditText edDescription;
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
    EditText eDStep3;
    @BindView(R.id.imgStep3)
    ImageView imgStep3;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseFirestore db;
    CollectionReference documentReference;
    CollectionReference documentReferenceFood;
    @BindView(R.id.edNameFood)
    EditText edNameFood;

    private Uri filePath1;
    private Uri filePath2;
    private Uri filePath3;
    private Uri filePathAvatar;

    Boolean bImgStep1 = false;
    Boolean bImgStep2 = false;
    Boolean bImgStep3 = false;

    Boolean bImgAvatar;
    PrefManager prefManager;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        ButterKnife.bind(this);
        prefManager = new PrefManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đợi xíu nhé ...");
        progressDialog.setCancelable(false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("foods");

        db = FirebaseFirestore.getInstance();
        documentReferenceFood = db.collection("foods");
        documentReference = db.collection("foodDoing");
        tvNameUser.setText(prefManager.getUser().getName());
        Picasso.get().load(prefManager.getUser().getImageUrl()).placeholder(R.drawable.placeholder).into(imgAvatarUser);
    }


    @OnClick({R.id.btnClose, R.id.btnAired, R.id.linearSetImage, R.id.imgStep1, R.id.imgStep2, R.id.imgStep3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                finish();
                break;
            case R.id.btnAired:
                uploadToFood();
                break;
            case R.id.linearSetImage:
                bImgAvatar = true;
                chooseImage();
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
            } else if (bImgAvatar) {
                filePathAvatar = data.getData();
                uploadStep(filePathAvatar);
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


                } else if (bImgAvatar) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathAvatar);
                    imgAvatarFood.setImageBitmap(bitmap);
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
            }).addOnSuccessListener(uri -> Toast.makeText(AddNewActivity.this, "Uploaded", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(AddNewActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show()).addOnCompleteListener(task -> {
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
                    } else if (bImgAvatar) {
                        filePathAvatar = task.getResult();
                        progressDialog.dismiss();
                        bImgAvatar = false;
                    }

                }
            });

        } else {
            Toast.makeText(this, "Mời thêm vào các file ảnh !", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadToFood() {
        if (edDescription.getText().toString().length() > 0 && edDescription.getText().toString().length() > 0 && edNameFood.getText().toString().length() > 0
                && edResources1.getText().toString().length() > 0 && edResources2.getText().toString().length() > 0
                && edResources3.getText().toString().length() > 0 && eDStep1.getText().toString().length() > 0
                && eDStep2.getText().toString().length() > 0 && eDStep3.getText().toString().length() > 0 && filePathAvatar.toString().length() > 0
                && filePath1.toString().length() > 0
                && filePath2.toString().length() > 0
                && filePath3.toString().length() > 0
        ) {

            progressDialog.show();
            String id = UUID.randomUUID().toString();
            ArrayList<StepCook> stepCookList = new ArrayList<>();
            ArrayList<Comment> listComment = new ArrayList<>();
            ArrayList<String> listResoures = new ArrayList<>();

            listResoures.add(0, edResources1.getText().toString());
            listResoures.add(1, edResources2.getText().toString());
            listResoures.add(2, edResources3.getText().toString());

            stepCookList.add(new StepCook(0, eDStep1.getText().toString(), filePath1.toString()));
            stepCookList.add(new StepCook(0, eDStep2.getText().toString(), filePath2.toString()));
            stepCookList.add(new StepCook(0, eDStep3.getText().toString(), filePath3.toString()));

            Food food = new Food(id, filePathAvatar.toString(), edNameFood.getText().toString(), prefManager.getUser()
                    , stepCookList, edDescription.getText().toString(), listComment, listResoures);

            documentReferenceFood.document(id).get().addOnSuccessListener(documentSnapshot -> {

                documentReferenceFood.document(id).set(
                        food
                ).addOnSuccessListener(aVoid -> {


                    documentReference.document(id).get().addOnSuccessListener(documentSnapshot1 -> {
                        documentReference.document(id).set(
                                food
                        ).addOnSuccessListener(aVoid1 -> {
                            Toast.makeText(AddNewActivity.this, "Thêm món ăn thành công", Toast.LENGTH_SHORT).show();
                            finish();
                            progressDialog.dismiss();

                        });
                    });

                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Thêm móm ăn thất bại", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });

            });
        } else {
            Toast.makeText(this, "Thêm vào các trường", Toast.LENGTH_SHORT).show();
        }

    }


}
