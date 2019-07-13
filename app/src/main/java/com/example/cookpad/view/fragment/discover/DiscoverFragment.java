package com.example.cookpad.view.fragment.discover;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.cookpad.R;
import com.example.cookpad.adapter.FoodAdapter;
import com.example.cookpad.model.Food;
import com.example.cookpad.view.DetailActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;

public class DiscoverFragment extends Fragment {

    private static final String TAG = "";
    private static final int RESULT_OK = 1;
    @BindView(R.id.recycler_food)
    RecyclerView recyclerFood;
    @BindView(R.id.shimmerLayout)
    ShimmerFrameLayout shimmerLayout;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.logo);

        setHasOptionsMenu(true);
        initSearchView();
        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);
        recyclerFood.setVisibility(View.INVISIBLE);

        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("foods");

        foodList = new ArrayList<>();
        cookDoingAdapter = new FoodAdapter(foodList, getContext(), position -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

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

    private void initSearchView(){
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                if (newText != null && !newText.isEmpty()){
                    ArrayList<Food> listFound = new ArrayList<>();
                    for (Food food: foodList) {
                        if (food.getTitle().contains(newText)){
                            listFound.add(food);
                        }
                    }

                     FoodAdapter cookDoingAdapterSearch = new FoodAdapter(listFound, getContext(), position -> {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        Food food = listFound.get(position);
                        intent.putExtra("food_item", food);
                        startActivity(intent);
                        customType(getContext(), "fadein-to-fadeout");

                    });

                    recyclerFood.setAdapter(cookDoingAdapterSearch);

                }
                else {
                    FoodAdapter cookDoingAdapterSearch = new FoodAdapter(foodList, getContext(), position -> {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        Food food = foodList.get(position);
                        intent.putExtra("food_item", food);
                        startActivity(intent);
                        customType(getContext(), "fadein-to-fadeout");

                    });

                    recyclerFood.setAdapter(cookDoingAdapterSearch);
                }
                return true;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

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
