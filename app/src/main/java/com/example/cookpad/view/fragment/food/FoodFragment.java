package com.example.cookpad.view.fragment.food;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.cookpad.R;
import com.example.cookpad.adapter.CookDoingAdapter;
import com.example.cookpad.model.Food;
import com.example.cookpad.until.CustomEditText;
import com.example.cookpad.until.PrefManager;
import com.example.cookpad.view.FoodInformationActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;


public class FoodFragment extends Fragment {


    private static final int RESULT_OK = 10;
    private static final String TAG = "foodDoing";
    @BindView(R.id.edSearch)
    CustomEditText edSearch;
    @BindView(R.id.btnAddNewFood)
    LinearLayout btnAddNewFood;
    @BindView(R.id.recycler_food)
    RecyclerView recyclerFood;
    @BindView(R.id.shimmerLayout)
    ShimmerFrameLayout shimmerLayout;
    @BindView(R.id.linear)
    LinearLayout linear;

    private FirebaseFirestore db;
    private CollectionReference documentReference;
    private CookDoingAdapter cookDoingAdapter;
    private List<Food> foodList;
    private PrefManager prefManager;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_food, container,
                false);

        ButterKnife.bind(this, view);

        prefManager = new PrefManager(getContext());
        linear.setVisibility(View.INVISIBLE);
        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("foodDoing");

        foodList = new ArrayList<>();
        cookDoingAdapter = new CookDoingAdapter(foodList, getContext(), position -> {
            Intent intent = new Intent(getContext(), FoodInformationActivity.class);
            Food food = foodList.get(position);
            intent.putExtra("food_item", food);
            startActivity(intent);
            customType(getContext(), "fadein-to-fadeout");

        });
        getFoodList();


        edSearch.setDrawableClickListener(target -> {
            switch (target) {
                case RIGHT:
                    getSpeechInput(view);
                    break;

                default:
                    break;
            }
        });
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerFood.setLayoutManager(layoutManager);
        recyclerFood.setHasFixedSize(true);
        recyclerFood.setAdapter(cookDoingAdapter);
        cookDoingAdapter.notifyDataSetChanged();
        return view;
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(getContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edSearch.setText(result.get(0));
                }
                break;
        }
    }

    private List<Food> getFoodList() {
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Food food = (Food) document.toObject(Food.class);
                    if (food.getUser().getId().equals(prefManager.getUser().getId())) {
                        foodList.add(food);
                        cookDoingAdapter.notifyDataSetChanged();
                    }


                }

                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.INVISIBLE);
                linear.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });


        return foodList;
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerLayout.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerLayout.stopShimmer();
        super.onPause();
    }
}
