package com.example.cookpad.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.cookpad.R;
import com.example.cookpad.model.User;
import com.example.cookpad.until.Constant;
import com.example.cookpad.until.PrefManager;
import com.facebook.accountkit.*;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static maes.tech.intentanim.CustomIntent.customType;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnLogin)
    Button btnLogin;

    public static int APP_REQUEST_CODE = 99;
    PhoneNumber phoneNumber;
    PrefManager prefManager;

    FirebaseFirestore db;
    CollectionReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("user");

        prefManager = new PrefManager(this);
    }

    @OnClick(R.id.btnLogin)
    public void onViewClicked(View view) {
        phoneLogin(view);
    }


    public void phoneLogin(final View view) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null) {

            } else if (loginResult.wasCancelled()) {

            } else {
                if (loginResult.getAccessToken() != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Loading ...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(Account account) {
                            phoneNumber = account.getPhoneNumber();
                            if (phoneNumber != null) {
                                final String phoneNumberString = phoneNumber.toString().substring(1, 12);


                                documentReference.document(phoneNumberString).get().addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        User user = documentSnapshot.toObject(User.class);
                                        prefManager.setUser(user);
                                        prefManager.saveBol(Constant.STATUS_LOGIN, true);
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        customType(LoginActivity.this,"fadein-to-fadeout");
                                        progressDialog.dismiss();
                                    } else {
                                        User user = new User(phoneNumberString,"","","","",null);
                                        documentReference.document(phoneNumberString)
                                                .set(user)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(LoginActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                    prefManager.saveString(Constant.PHONE_NUMBER_USER, phoneNumberString);
                                                    Intent intent = new Intent(LoginActivity.this, SetInfoActivity.class);
                                                    startActivity(intent);
                                                    customType(LoginActivity.this,"fadein-to-fadeout");
                                                    progressDialog.dismiss();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(LoginActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                });


                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {

                        }
                    });
                } else {

                }

                goToMyLoggedInActivity();
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    "",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void goToMyLoggedInActivity() {
    }


}