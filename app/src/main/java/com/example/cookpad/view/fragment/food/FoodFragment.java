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
import com.example.cookpad.interfaces.ItemOnClickListener;
import com.example.cookpad.model.Food;
import com.example.cookpad.until.CustomEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FoodFragment extends Fragment {


    private static final int RESULT_OK = 10;
    private static final String TAG = "foodDoing";
    @BindView(R.id.edSearch)
    CustomEditText edSearch;
    @BindView(R.id.btnAddNewFood)
    LinearLayout btnAddNewFood;
    @BindView(R.id.recycler_food)
    RecyclerView recyclerFood;

    FirebaseFirestore db;
    private CollectionReference documentReference;
    private CookDoingAdapter cookDoingAdapter;
    private List<Food> foodList;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_food, container,
                false);

        ButterKnife.bind(this, view);

        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("foodDoing");

        foodList = new ArrayList<>();

        getFoodList();

        cookDoingAdapter = new CookDoingAdapter(foodList, getContext(), position -> {

        });

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

    private List<Food> getFoodList(){ ;
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Food food = (Food) document.toObject(Food.class);
                   foodList.add(food);
                }
            }else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        return foodList;
    }
}
