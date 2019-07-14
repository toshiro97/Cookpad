package com.example.cookpad.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cookpad.R;
import com.example.cookpad.interfaces.AddCookOnClickListener;
import com.example.cookpad.interfaces.AddFriendOnClickListener;
import com.example.cookpad.interfaces.ItemOnClickListener;
import com.example.cookpad.model.Food;
import com.example.cookpad.until.PrefManager;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<Food> foodList;
    private Context context;
    private ItemOnClickListener itemOnClickListener;
    private AddFriendOnClickListener addFriendOnClickListener;
    private AddCookOnClickListener addCookOnClickListener;
    private List<String> listFriend;
    PrefManager prefManager;

    public FoodAdapter(List<Food> foodList, Context context,
                       ItemOnClickListener itemOnClickListener,
                       AddFriendOnClickListener addFriendOnClickListener,
                       AddCookOnClickListener addCookOnClickListener) {
        this.foodList = foodList;
        this.context = context;
        this.itemOnClickListener = itemOnClickListener;
        this.addCookOnClickListener = addCookOnClickListener;
        this.addFriendOnClickListener = addFriendOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        prefManager = new PrefManager(context);
        Food food = foodList.get(i);

        Picasso.get().load(food.getUser().getImageUrl()).fit().centerCrop().into(viewHolder.imageAvatar);
        viewHolder.tvName.setText(food.getUser().getName());
        viewHolder.tvTime.setText("5 giờ trước");
        viewHolder.tvNameFood.setText(food.getTitle());
        viewHolder.tvDescription.setText(food.getDescription());
        Picasso.get().load(food.getImageUrl()).fit().centerCrop().into(viewHolder.imgFood);
        if (prefManager.getUser().getId().equals(food.getUser().getId())) {
            viewHolder.btnAddFriend.setVisibility(View.INVISIBLE);
        }
        if (prefManager.getUser().getListFriend() != null) {
            if (prefManager.getUser().getListFriend().size() > 0) {
                for (int k = 0; k < prefManager.getUser().getListFriend().size(); k++) {
                    if (prefManager.getUser().getListFriend().get(k).equals(food.getUser().getId())) {
                        viewHolder.btnAddFriend.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }


        viewHolder.btnAdd.setOnClickListener(v -> addCookOnClickListener.clickItem(i));
        viewHolder.btnAddFriend.setOnClickListener(v -> {
            addFriendOnClickListener.clickItem(i);
            viewHolder.btnAddFriend.setVisibility(View.INVISIBLE);

        });
        viewHolder.itemView.setOnClickListener(v -> itemOnClickListener.clickItem(i));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageAvatar;
        TextView tvName;
        TextView tvTime;
        TextView tvNameFood;
        TextView tvDescription;
        ImageView imgFood;
        Button btnAdd;
        Button btnAddFriend;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imgFood = itemView.findViewById(R.id.imgFood);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);
        }
    }
}
