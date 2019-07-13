package com.example.cookpad.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.model.Food;
import com.example.cookpad.model.StepCook;
import com.example.cookpad.until.Constant;
import com.example.cookpad.until.PrefManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static maes.tech.intentanim.CustomIntent.customType;

public class CookTodayActivity extends AppCompatActivity {

    @BindView(R.id.edFoodName)
    EditText edFooName;
    @BindView(R.id.btnFinish)
    Button btnFinish;


    FirebaseFirestore db;
    CollectionReference documentReference;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_today);
        ButterKnife.bind(this);

        prefManager = new PrefManager(this);
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("foodDoing");

    }

    @OnClick(R.id.btnFinish)
    public void onViewClicked() {
        if (edFooName.getText().toString().length() > 0) {

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            String id = UUID.randomUUID().toString();
            documentReference.document(id).get().addOnSuccessListener(documentSnapshot -> {


                Food food = new Food();
                food.setTitle(edFooName.getText().toString());
                food.setId(id);

                ArrayList<StepCook> stepCookList = new ArrayList<>();

                stepCookList.add(new StepCook(0, "", ""));
                stepCookList.add(new StepCook(1, "", ""));
                stepCookList.add(new StepCook(2, "", ""));

                food.setStepCookList(stepCookList);

                documentReference.document(id).set(
                        food
                ).addOnSuccessListener(aVoid -> {

                    Toast.makeText(this, "Thêm món ăn thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, CookAgainActivity.class);
                    intent.putExtra("food_user", food);
                    startActivity(intent);
                    customType(CookTodayActivity.this, "fadein-to-fadeout");
                    progressDialog.dismiss();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Thêm móm ăn thất bại", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });

            });
        } else {
            Toast.makeText(this, "Nhập vào món ăn", Toast.LENGTH_SHORT).show();
        }
    }
}
