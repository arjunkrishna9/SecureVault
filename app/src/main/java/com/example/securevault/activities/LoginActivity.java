package com.example.securevault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securevault.R;
import com.example.securevault.database.AppDatabase;
import com.example.securevault.models.User;
import com.example.securevault.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    private Button btnLogin;
    private TextView tvCreateAccount;

    private TextView tvForgotPassword;

    private AppDatabase database;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(R.layout.activity_login);

        database = AppDatabase.getDatabase(this);

        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            MainActivity.class
                    )
            );

            finish();

            return;
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        tvForgotPassword =
                findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(v -> loginUser());

        tvCreateAccount.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            CreateAccountActivity.class
                    )
            );
        });

        tvForgotPassword.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            ForgotPasswordActivity.class
                    )
            );
        });
    }

    private void loginUser() {

        String username =
                etUsername.getText().toString().trim();

        String password =
                etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {

            Toast.makeText(
                    this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        User user =
                database.userDao().login(
                        username,
                        password
                );

        if (user != null) {

            sessionManager.createLoginSession(user.getId());

            Toast.makeText(
                    this,
                    "Login Successful",
                    Toast.LENGTH_SHORT
            ).show();

            startActivity(
                    new Intent(
                            LoginActivity.this,
                            MainActivity.class
                    )
            );

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Invalid Credentials",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}