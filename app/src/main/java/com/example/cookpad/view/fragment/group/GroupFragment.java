package com.example.cookpad.view.fragment.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.model.User;
import com.example.cookpad.until.PrefManager;
import com.example.cookpad.view.ListFriendActivity;
import com.example.cookpad.view.SettingActivity;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import static maes.tech.intentanim.CustomIntent.customType;

public class GroupFragment extends Fragment {


    @BindView(R.id.imgAvatar)
    CircleImageView imgAvatar;
    @BindView(R.id.btnKitchen)
    LinearLayout btnKitchen;
    @BindView(R.id.btnBox)
    LinearLayout btnBox;
    @BindView(R.id.btnMessenger)
    LinearLayout btnMessenger;

    PrefManager prefManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, view);

        prefManager = new PrefManager(getContext());
        User user = prefManager.getUser();

        Picasso.get().load(user.getImageUrl()).into(imgAvatar);


        return view;
    }

    @OnClick({R.id.imgAvatar, R.id.btnKitchen, R.id.btnBox, R.id.btnMessenger})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgAvatar:
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                customType(getContext(), "fadein-to-fadeout");
                break;
            case R.id.btnKitchen:
                break;
            case R.id.btnBox:
                break;
            case R.id.btnMessenger:
                Intent intent1 = new Intent(getContext(), ListFriendActivity.class);
                startActivity(intent1);
                customType(getContext(), "fadein-to-fadeout");
                break;
        }
    }
}
