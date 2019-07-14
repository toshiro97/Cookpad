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
import com.example.cookpad.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(commentList.get(position).getUser().getImageUrl()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).fit().centerCrop().into(holder.imgAvatar);
        holder.tvName.setText(commentList.get(position).getUser().getName());
        holder.tvComment.setText(commentList.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvName;
        TextView tvComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgReview);
            tvName = itemView.findViewById(R.id.tvUserNameReview);
            tvComment = itemView.findViewById(R.id.tvContentReview);
        }
    }
}
