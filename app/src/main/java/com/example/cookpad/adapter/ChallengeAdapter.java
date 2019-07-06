package com.example.cookpad.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cookpad.R;
import com.example.cookpad.interfaces.ItemOnClickListener;
import com.example.cookpad.model.Challenge;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder> {
    private List<Challenge> challengeList;
    private Context context;
    private ItemOnClickListener itemOnClickListener;

    public ChallengeAdapter(List<Challenge> challengeList, Context content, ItemOnClickListener itemOnClickListener) {
        this.challengeList = challengeList;
        this.context = content;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_challenge, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Challenge challenge = challengeList.get(i);
        Picasso.get().load(challenge.getUrlImage()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).fit().centerCrop().into(viewHolder.imgAvatar);
        viewHolder.tvChallenge.setText(challenge.getTitle());
        viewHolder.tvContent.setText(challenge.getContentTitle());
        viewHolder.tvTime.setText(challenge.getDate());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemOnClickListener.clickItem(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvChallenge;
        TextView tvContent;
        TextView tvTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatarChallenge);
            tvChallenge = itemView.findViewById(R.id.tvChallenge);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
