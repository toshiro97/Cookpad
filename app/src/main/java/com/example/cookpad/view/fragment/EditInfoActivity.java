package com.example.cookpad.view.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.model.User;
import com.example.cookpad.until.Constant;
import com.example.cookpad.until.PrefManager;
import com.example.cookpad.view.CookTodayActivity;
import com.example.cookpad.view.SetInfoActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import static maes.tech.intentanim.CustomIntent.customType;

public class EditInfoActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 999;
    private static final int RESULT_LOAD_IMAGE = 888;

    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.imgTakePicture)
    ImageView imgTakePicture;
    @BindView(R.id.edFirstName)
    EditText edFirstName;
    @BindView(R.id.edLastName)
    EditText edLastName;
    @BindView(R.id.edCalendar)
    EditText edCalendar;
    @BindView(R.id.edPassword)
    EditText edPassword;
    @BindView(R.id.edRePassword)
    EditText edRePassword;
    @BindView(R.id.edDescription)
    EditText edDescription;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    final Calendar myCalendar = Calendar.getInstance();

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseFirestore db;
    CollectionReference documentReference;

    private Uri filePath;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        ButterKnife.bind(this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("avatar");

        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("user");

        prefManager = new PrefManager(this);
        initView();

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edCalendar.setOnClickListener(v -> new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    @OnClick({R.id.imgTakePicture, R.id.btnRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgTakePicture:
                chooseImage();
                break;
            case R.id.btnRegister:
                uploadImage();
                break;
        }
    }
    private void initView() {
        if (prefManager.getUser().getDescription().length() > 0){
            edDescription.setText(prefManager.getUser().getDescription());
        }
        if (prefManager.getUser().getName().length() > 0){
            edFirstName.setText(prefManager.getUser().getName());
        }
        if (prefManager.getUser().getBirthday().length() > 0){
            edCalendar.setText(prefManager.getUser().getBirthday());
        }
        if (prefManager.getUser().getImageUrl().length() > 0){
            Picasso.get().load(prefManager.getUser().getImageUrl()).into(circleImageView);
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
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edCalendar.setText(sdf.format(myCalendar.getTime()));
    }

    private void uploadImage() {
        if (edPassword.getText().toString().length() > 0
                || edPassword.getText().toString().equals(edRePassword.getText().toString())
                || edFirstName.getText().toString().length() > 0
                || edLastName.getText().toString().length() > 0
                || edCalendar.getText().toString().length() > 0
        ) {
            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                UploadTask uploadTask = ref.putFile(filePath);

                Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }).addOnSuccessListener(uri -> Toast.makeText(EditInfoActivity.this, "Uploaded", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditInfoActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        String phoneNumber = prefManager.getUser().getId();
                        documentReference.document(phoneNumber).get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot != null) {
                                User user = documentSnapshot.toObject(User.class);
                                user.setBirthday( edCalendar.getText().toString().trim());
                                user.setDescription(edDescription.getText().toString());
                                user.setName(edFirstName.getText().toString() + edLastName.getText().toString());
                                user.setImageUrl(downloadUri.toString());

                                documentReference.document(phoneNumber).update(
                                        "birthday", edCalendar.getText().toString().trim(),
                                        "name", edFirstName.getText().toString() + edLastName.getText().toString(),
                                        "imageUrl", downloadUri.toString(),
                                        "description", edDescription.getText().toString()

                                ).addOnSuccessListener(aVoid -> {
                                    Toast.makeText(EditInfoActivity.this, "Cập nhật tài khoản thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                    prefManager.setUser(user);
                                    progressDialog.dismiss();
                                })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(EditInfoActivity.this, "Cập nhật tài khoản thất bại", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        });
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(EditInfoActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        } else {
            Toast.makeText(this, "Mời nhập vào đầy đủ các trường !!!", Toast.LENGTH_SHORT).show();
        }
    }


}
