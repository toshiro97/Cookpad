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
import com.example.cookpad.interfaces.ItemOnClickListener;
import com.example.cookpad.model.Food;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<Food> foodList;
    private Context context;
    private ItemOnClickListener itemOnClickListener;

    public FoodAdapter(List<Food> foodList, Context context, ItemOnClickListener itemOnClickListener) {
        this.foodList = foodList;
        this.context = context;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imgFood = itemView.findViewById(R.id.imgFood);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
