package com.example.cookpad.view.fragment.discover;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.cookpad.R;
import com.example.cookpad.adapter.CookDoingAdapter;
import com.example.cookpad.adapter.FoodAdapter;
import com.example.cookpad.model.Food;
import com.example.cookpad.view.FoodInformationActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;

public class DiscoverFragment extends Fragment {

    private static final String TAG = "";
    @BindView(R.id.recycler_food)
    RecyclerView recyclerFood;
    @BindView(R.id.shimmerLayout)
    ShimmerFrameLayout shimmerLayout;

    private FirebaseFirestore db;
    private CollectionReference documentReference;
    private FoodAdapter cookDoingAdapter;
    private List<Food> foodList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container,
                false);

        ButterKnife.bind(this, view);

        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);
        recyclerFood.setVisibility(View.INVISIBLE);

        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("foods");

        foodList = new ArrayList<>();
        cookDoingAdapter = new FoodAdapter(foodList, getContext(), position -> {
            Intent intent = new Intent(getContext(), FoodInformationActivity.class);
            Food food = foodList.get(position);
            intent.putExtra("food_item", food);
            startActivity(intent);
            customType(getContext(), "fadein-to-fadeout");

        });

        getFoodList();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext());
        recyclerFood.setLayoutManager(layoutManager);
        recyclerFood.setHasFixedSize(true);
        recyclerFood.setAdapter(cookDoingAdapter);
        cookDoingAdapter.notifyDataSetChanged();

        return view;
    }

    private List<Food> getFoodList() {

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Food food = document.toObject(Food.class);
                    foodList.add(food);

                    cookDoingAdapter.notifyDataSetChanged();
                }

                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    // Do something after 5s = 5000ms
                    shimmerLayout.stopShimmer();
                    shimmerLayout.setVisibility(View.INVISIBLE);
                    recyclerFood.setVisibility(View.VISIBLE);
                }, 3000);
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
