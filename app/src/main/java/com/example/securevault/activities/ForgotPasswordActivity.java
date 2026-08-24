package com.example.securevault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securevault.R;
import com.example.securevault.database.AppDatabase;
import com.example.securevault.models.User;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etUsername;

    private RadioGroup radioGroupRecovery;

    private RadioButton rbSecurityQuestion;
    private RadioButton rbRecoveryPin;

    private Button btnContinue;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(
                R.layout.activity_forgot_password
        );

        initializeViews();

        database = AppDatabase.getDatabase(this);

        btnContinue.setOnClickListener(v -> {

            continueRecovery();
        });
    }

    private void initializeViews() {

        etUsername =
                findViewById(R.id.etUsername);

        radioGroupRecovery =
                findViewById(R.id.radioGroupRecovery);

        rbSecurityQuestion =
                findViewById(R.id.rbSecurityQuestion);

        rbRecoveryPin =
                findViewById(R.id.rbRecoveryPin);

        btnContinue =
                findViewById(R.id.btnContinue);
    }

    private void continueRecovery() {

        String username =
                etUsername.getText()
                        .toString()
                        .trim();

        if (username.isEmpty()) {

            Toast.makeText(
                    this,
                    "Enter username",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        User user =
                database.userDao().getUser(username);

        if (user == null) {

            Toast.makeText(
                    this,
                    "User not found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int selectedId =
                radioGroupRecovery.getCheckedRadioButtonId();

        if (selectedId == -1) {

            Toast.makeText(
                    this,
                    "Select recovery method",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String recoveryType;

        if (selectedId == R.id.rbSecurityQuestion) {

            recoveryType = "security";

        } else {

            recoveryType = "pin";
        }

        Intent intent =
                new Intent(
                        ForgotPasswordActivity.this,
                        ResetPasswordActivity.class
                );

        intent.putExtra(
                "username",
                username
        );

        intent.putExtra(
                "recovery_type",
                recoveryType
        );

        startActivity(intent);
    }
}