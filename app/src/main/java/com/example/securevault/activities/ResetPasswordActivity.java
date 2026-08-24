package com.example.securevault.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securevault.R;
import com.example.securevault.database.AppDatabase;
import com.example.securevault.models.User;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private TextView tvOldPassword;

    private EditText etAnswer;
    private EditText etNewPassword;

    private Button btnVerify;
    private Button btnUpdatePassword;

    private LinearLayout layoutOptions;

    private RadioGroup radioGroupOptions;

    private RadioButton rbShowPassword;
    private RadioButton rbCreateNew;

    private AppDatabase database;

    private User user;

    private String username;
    private String recoveryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(
                R.layout.activity_reset_password
        );

        initializeViews();

        database = AppDatabase.getDatabase(this);

        username =
                getIntent().getStringExtra("username");

        recoveryType =
                getIntent().getStringExtra("recovery_type");

        user =
                database.userDao().getUser(username);

        if (user == null) {

            finish();

            return;
        }

        setupUI();

        btnVerify.setOnClickListener(v -> {

            verifyUser();
        });

        setupRadioOptions();
    }

    private void initializeViews() {

        tvQuestion =
                findViewById(R.id.tvQuestion);

        tvOldPassword =
                findViewById(R.id.tvOldPassword);

        etAnswer =
                findViewById(R.id.etAnswer);

        etNewPassword =
                findViewById(R.id.etNewPassword);

        btnVerify =
                findViewById(R.id.btnVerify);

        btnUpdatePassword =
                findViewById(R.id.btnUpdatePassword);

        layoutOptions =
                findViewById(R.id.layoutOptions);

        radioGroupOptions =
                findViewById(R.id.radioGroupOptions);

        rbShowPassword =
                findViewById(R.id.rbShowPassword);

        rbCreateNew =
                findViewById(R.id.rbCreateNew);
    }

    private void setupUI() {

        if (recoveryType.equals("security")) {

            tvQuestion.setText(
                    user.getSecurityQuestion()
            );

            etAnswer.setHint(
                    "Enter Security Answer"
            );

        } else {

            tvQuestion.setText(
                    "Enter Recovery PIN"
            );

            etAnswer.setHint(
                    "4 Digit Recovery PIN"
            );
        }
    }

    private void verifyUser() {

        String enteredValue =
                etAnswer.getText()
                        .toString()
                        .trim();

        boolean verified = false;

        if (recoveryType.equals("security")) {

            verified =
                    enteredValue.equalsIgnoreCase(
                            user.getSecurityAnswer()
                    );

        } else {

            verified =
                    enteredValue.equals(
                            user.getRecoveryPin()
                    );
        }

        if (verified) {

            layoutOptions.setVisibility(View.VISIBLE);

            Toast.makeText(
                    this,
                    "Verification Successful",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Incorrect Details",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void setupRadioOptions() {

        radioGroupOptions.setOnCheckedChangeListener(
                (group, checkedId) -> {

                    if (checkedId == R.id.rbShowPassword) {

                        tvOldPassword.setVisibility(View.VISIBLE);

                        tvOldPassword.setText(
                                "Old Password: "
                                        + user.getPassword()
                        );

                        etNewPassword.setVisibility(View.GONE);

                        btnUpdatePassword.setVisibility(View.GONE);

                    }

                    else if (checkedId == R.id.rbCreateNew) {

                        tvOldPassword.setVisibility(View.GONE);

                        etNewPassword.setVisibility(View.VISIBLE);

                        btnUpdatePassword.setVisibility(View.VISIBLE);
                    }
                });

        btnUpdatePassword.setOnClickListener(v -> {

            String newPassword =
                    etNewPassword.getText()
                            .toString()
                            .trim();

            if (newPassword.isEmpty()) {

                Toast.makeText(
                        this,
                        "Enter new password",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            user.setPassword(newPassword);

            database.userDao().update(user);

            Toast.makeText(
                    this,
                    "Password Updated Successfully",
                    Toast.LENGTH_LONG
            ).show();

            finish();
        });
    }
}