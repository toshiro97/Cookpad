package com.example.cookpad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cookpad.R;
import com.example.cookpad.interfaces.ItemOnClickListener;
import com.example.cookpad.model.Food;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CookDoingAdapter extends RecyclerView.Adapter<CookDoingAdapter.ViewHolder> {
    private List<Food> foodList;
    private Context context;
    private ItemOnClickListener itemOnClickListener;

    public CookDoingAdapter(List<Food> foodList, Context context, ItemOnClickListener itemOnClickListener) {
        this.foodList = foodList;
        this.context = context;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_doing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = foodList.get(position);

        holder.tvNameFoodDoing.setText(food.getTitle());
        Picasso.get().load(food.getImageUrl()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).fit().centerCrop().into(holder.imgFoodDoing);

        holder.itemView.setOnClickListener(v -> itemOnClickListener.clickItem(position));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoodDoing;
        TextView tvNameFoodDoing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFoodDoing = itemView.findViewById(R.id.imgFoodDoing);
            tvNameFoodDoing = itemView.findViewById(R.id.tvNameFoodDoing);
        }
    }
}
