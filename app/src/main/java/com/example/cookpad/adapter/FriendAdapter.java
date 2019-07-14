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
import com.example.cookpad.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private ArrayList<User> friendList;
    private Context context;
    private ItemOnClickListener itemOnClickListener;


    public FriendAdapter(ArrayList<User> friendList, Context context, ItemOnClickListener itemOnClickListener) {
        this.friendList = friendList;
        this.context = context;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(friendList.get(position).getImageUrl()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).fit().centerCrop().into(holder.imgAvatar);
        holder.tvName.setText(friendList.get(position).getName());
        holder.tvLastMess.setText("Bạn muốn nhắn gì với tớ vậy .....");
        holder.itemView.setOnClickListener(v -> {
            itemOnClickListener.clickItem(position);
        });

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvLastMess;
        ImageView imgAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvLastMess = itemView.findViewById(R.id.tvLastMess);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}
