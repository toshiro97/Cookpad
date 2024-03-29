package com.example.cookpad.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.until.PrefManager;
import com.example.cookpad.view.fragment.EditInfoActivity;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import static maes.tech.intentanim.CustomIntent.customType;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.imgAvatar)
    CircleImageView imgAvatar;
    @BindView(R.id.tvNameUser)
    TextView tvNameUser;
    @BindView(R.id.tvViewChicken)
    TextView tvViewChicken;
    @BindView(R.id.tvEditProfile)
    TextView tvEditProfile;
    @BindView(R.id.tvNotification)
    TextView tvNotification;
    @BindView(R.id.tvSecurity)
    TextView tvSecurity;
    @BindView(R.id.tvService)
    TextView tvService;
    @BindView(R.id.tvGroup)
    TextView tvGroup;
    @BindView(R.id.tvFeedback)
    TextView tvFeedback;
    @BindView(R.id.tvCookpad)
    TextView tvCookpad;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        prefManager = new PrefManager(this);
        tvNameUser.setText(prefManager.getUser().getName());
        Picasso.get().load(prefManager.getUser().getImageUrl()).into(imgAvatar);
    }

    @OnClick({R.id.tvViewChicken, R.id.tvEditProfile, R.id.tvNotification, R.id.tvSecurity, R.id.tvService, R.id.tvGroup, R.id.tvFeedback, R.id.tvCookpad, R.id.btnLogout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvViewChicken:
                break;
            case R.id.tvEditProfile:
                Intent intentProfile = new Intent(this, EditInfoActivity.class);
                startActivity(intentProfile);
                customType(SettingActivity.this,"fadein-to-fadeout");
                break;
            case R.id.tvNotification:
                break;
            case R.id.tvSecurity:
                break;
            case R.id.tvService:
                break;
            case R.id.tvGroup:
                break;
            case R.id.tvFeedback:
                break;
            case R.id.tvCookpad:
                break;
            case R.id.btnLogout:
                Intent intent = new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                prefManager.deleteAll();
                startActivity(intent);
                break;
        }
    }

    private void initView(){
        tvNameUser.setText(prefManager.getUser().getName());
        Picasso.get().load(prefManager.getUser().getImageUrl()).into(imgAvatar);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}
