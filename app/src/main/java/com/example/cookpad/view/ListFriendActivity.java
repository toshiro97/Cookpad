package com.example.cookpad.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.cookpad.R;
import com.example.cookpad.adapter.FriendAdapter;
import com.example.cookpad.model.User;
import com.example.cookpad.until.CustomEditText;
import com.example.cookpad.until.PrefManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

import static maes.tech.intentanim.CustomIntent.customType;

public class ListFriendActivity extends AppCompatActivity {

    @BindView(R.id.imgAvatar)
    CircleImageView imgAvatar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.edSearch)
    CustomEditText edSearch;
    @BindView(R.id.recycler_friend)
    RecyclerView recyclerFriend;

    PrefManager prefManager;
    @BindView(R.id.shimmerLayout)
    ShimmerFrameLayout shimmerLayout;

    private FirebaseFirestore db;
    private CollectionReference documentReference;
    private ArrayList<String> friendsList;
    private ArrayList<User> friendUser;
    private FriendAdapter friendAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friend);
        ButterKnife.bind(this);

        prefManager = new PrefManager(this);


        shimmerLayout.startShimmer();
        shimmerLayout.setVisibility(View.VISIBLE);

        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("user");
        friendUser = new ArrayList<>();
        friendsList = new ArrayList<>();

        friendAdapter = new FriendAdapter(friendUser, this, position -> {
            Intent intent = new Intent(this,ChatActivity.class);
            User user = friendUser.get(position);
            intent.putExtra("user_chat",user);
            startActivity(intent);
            customType(this, "fadein-to-fadeout");
        });


        Picasso.get().load(prefManager.getUser().getImageUrl()).fit().centerCrop().into(imgAvatar);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);
        recyclerFriend.setLayoutManager(layoutManager);
        recyclerFriend.setHasFixedSize(true);
        recyclerFriend.setAdapter(friendAdapter);
        friendAdapter.notifyDataSetChanged();

        if (prefManager.getUser().getListFriend() != null) {
            friendsList = prefManager.getUser().getListFriend();
            for (String id : friendsList) {
                documentReference.document(id).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null) {
                        User user = documentSnapshot.toObject(User.class);
                        friendUser.add(user);
                        friendAdapter.notifyDataSetChanged();
                    }
                });
            }
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.INVISIBLE);
            recyclerFriend.setVisibility(View.VISIBLE);
        } else {
            friendsList = new ArrayList<>();
            shimmerLayout.stopShimmer();
            shimmerLayout.setVisibility(View.INVISIBLE);
            recyclerFriend.setVisibility(View.VISIBLE);
        }


        prefManager = new PrefManager(this);

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
