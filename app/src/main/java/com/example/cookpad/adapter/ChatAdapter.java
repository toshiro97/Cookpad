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
import com.example.cookpad.model.ChatMessenger;
import com.example.cookpad.model.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

class ChatSend extends RecyclerView.ViewHolder{
    public TextView tvSend;
    public ChatSend(View itemView) {
        super(itemView);
        tvSend = itemView.findViewById(R.id.tvSend);
    }
}

class ChatReceive extends RecyclerView.ViewHolder{
    public TextView tvReceive;
    public CircleImageView imgAvatarRecei;
    public ChatReceive(View itemView) {
        super(itemView);
        tvReceive = itemView.findViewById(R.id.tvReceive);
        imgAvatarRecei = itemView.findViewById(R.id.img_avatar_receive);
    }
}

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<ChatMessenger> messengerList;
    Context context;
    User userReceive;

    public ChatAdapter(List<ChatMessenger> messengerList,User userReceive) {
        this.messengerList = messengerList;
        this.userReceive = userReceive;
    }

    @Override
    public int getItemViewType(int position) {
        if (messengerList.get(position).isSend())
            return 1;
        else
            return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        if (viewType == 0){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.list_item_message_recv,parent,false);
            return new ChatReceive(view);
        }else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.list_item_message_send,parent,false);
            return new ChatSend(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:{
                ChatReceive viewHolder = (ChatReceive)holder;
                ChatMessenger chatMessenger = messengerList.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.tvReceive.setText(chatMessenger.getMessenger());
                Picasso.get().load(userReceive.getImageUrl()).fit().centerCrop().into(viewHolder.imgAvatarRecei);
            }
            break;
            case 1:{
                ChatSend viewHolder = (ChatSend) holder;
                ChatMessenger chatMessenger = messengerList.get(position);
                viewHolder.setIsRecyclable(false);
                viewHolder.tvSend.setText(chatMessenger.getMessenger());
            }
            break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messengerList.size();
    }
}
