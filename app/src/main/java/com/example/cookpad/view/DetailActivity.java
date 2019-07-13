package com.example.cookpad.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.model.Food;
import com.example.cookpad.until.PrefManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.imgFoodHeader)
    ImageView imgFoodHeader;
    @BindView(R.id.imgAvatarUser)
    CircleImageView imgAvatarUser;
    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvFoodName)
    TextView tvFoodName;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.imgAvatarBig)
    CircleImageView imgAvatarBig;
    @BindView(R.id.tvNameBig)
    TextView tvNameBig;
    @BindView(R.id.tvNewFood)
    TextView tvNewFood;
    @BindView(R.id.recycler_last_food)
    RecyclerView recyclerLastFood;
    @BindView(R.id.imgAvatarComment)
    CircleImageView imgAvatarComment;
    @BindView(R.id.edComment)
    EditText edComment;
    @BindView(R.id.btnPost)
    TextView btnPost;
    @BindView(R.id.recycler_comment)
    RecyclerView recyclerComment;


    FirebaseFirestore db;
    CollectionReference documentReference;
    PrefManager prefManager;

    Food food;
    @BindView(R.id.edResources1)
    EditText edResources1;
    @BindView(R.id.edResources2)
    EditText edResources2;
    @BindView(R.id.edResources3)
    EditText edResources3;
    @BindView(R.id.tvStep1)
    TextView tvStep1;
    @BindView(R.id.imgStep1)
    ImageView imgStep1;
    @BindView(R.id.tvStep2)
    TextView tvStep2;
    @BindView(R.id.imgStep2)
    ImageView imgStep2;
    @BindView(R.id.tvStep3)
    TextView tvStep3;
    @BindView(R.id.imgStep3)
    ImageView imgStep3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        prefManager = new PrefManager(this);

        food = (Food) getIntent().getSerializableExtra("food_item");

        initView();
    }

    private void initView() {

        Picasso.get().load(food.getImageUrl()).fit().centerCrop().into(imgAvatarBig);
        Picasso.get().load(prefManager.getUser().getImageUrl()).fit().centerCrop().into(imgAvatarUser);
        tvFoodName.setText(food.getTitle());
        tvUsername.setText(prefManager.getUser().getName());
        tvDescription.setText(food.getDescription());

        if (food.getResources() != null) {
            edResources1.setText(food.getResources().get(0));
            edResources2.setText(food.getResources().get(1));
            edResources3.setText(food.getResources().get(2));
        }

        if (food.getStepCookList() != null) {
            tvStep1.setText(food.getStepCookList().get(0).getContent());
            tvStep2.setText(food.getStepCookList().get(1).getContent());
            tvStep3.setText(food.getStepCookList().get(2).getContent());

            for (int i = 0; i < 3; i++) {
                if (food.getStepCookList().get(i).getUrlImage().length() > 0) {
                    if (i == 0) {
                        Picasso.get().load(food.getStepCookList().get(i).getUrlImage()).fit().centerCrop().into(imgStep1);
                    } else if (i == 1) {
                        Picasso.get().load(food.getStepCookList().get(i).getUrlImage()).fit().centerCrop().into(imgStep2);
                    } else {
                        Picasso.get().load(food.getStepCookList().get(i).getUrlImage()).fit().centerCrop().into(imgStep3);
                    }


                }
            }


        }
    }

    @OnClick(R.id.btnPost)
    public void onViewClicked() {

    }
}
