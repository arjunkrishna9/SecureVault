package com.example.securevault.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securevault.R;
import com.example.securevault.database.AppDatabase;
import com.example.securevault.models.PasswordEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    private TextView tvAppName;
    private TextView tvUpdatedAt;

    private Button btnShowPassword;
    private Button btnCopyUsername;
    private Button btnCopyPassword;
    private Button btnUpdate;
    private Button btnDelete;

    private AppDatabase database;

    private PasswordEntry passwordEntry;

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // BLOCK SCREENSHOTS
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(R.layout.activity_details);

        initializeViews();

        database = AppDatabase.getDatabase(this);

        int passwordId =
                getIntent().getIntExtra(
                        "password_id",
                        -1
                );

        if (passwordId == -1) {

            finish();

            return;
        }

        passwordEntry =
                database.passwordDao()
                        .getPasswordById(passwordId);

        if (passwordEntry == null) {

            finish();

            return;
        }

        loadData();

        setupButtons();
    }

    private void initializeViews() {

        tvAppName =
                findViewById(R.id.tvAppName);

        tvUpdatedAt =
                findViewById(R.id.tvUpdatedAt);

        etUsername =
                findViewById(R.id.etUsername);

        etPassword =
                findViewById(R.id.etPassword);

        btnShowPassword =
                findViewById(R.id.btnShowPassword);

        btnCopyUsername =
                findViewById(R.id.btnCopyUsername);

        btnCopyPassword =
                findViewById(R.id.btnCopyPassword);

        btnUpdate =
                findViewById(R.id.btnUpdate);

        btnDelete =
                findViewById(R.id.btnDelete);
    }

    private void loadData() {

        tvAppName.setText(
                passwordEntry.getAppName()
        );

        etUsername.setText(
                passwordEntry.getUsername()
        );

        etPassword.setText(
                passwordEntry.getPassword()
        );

        SimpleDateFormat sdf =
                new SimpleDateFormat(
                        "dd MMM yyyy | hh:mm a",
                        Locale.getDefault()
                );

        String formattedDate =
                sdf.format(
                        new Date(
                                passwordEntry.getCreatedAt()
                        )
                );

        tvUpdatedAt.setText(
                "Updated: " + formattedDate
        );
    }

    private void setupButtons() {

        btnShowPassword.setOnClickListener(v -> {

            if (isPasswordVisible) {

                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD
                );

                btnShowPassword.setText("Show");

            } else {

                etPassword.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );

                btnShowPassword.setText("Hide");
            }

            etPassword.setSelection(
                    etPassword.getText().length()
            );

            isPasswordVisible = !isPasswordVisible;
        });

        btnCopyUsername.setOnClickListener(v -> {

            copyToClipboard(
                    etUsername.getText().toString(),
                    "Username copied"
            );
        });

        btnCopyPassword.setOnClickListener(v -> {

            copyToClipboard(
                    etPassword.getText().toString(),
                    "Password copied"
            );
        });

        btnUpdate.setOnClickListener(v -> {

            passwordEntry.setUsername(
                    etUsername.getText().toString().trim()
            );

            passwordEntry.setPassword(
                    etPassword.getText().toString().trim()
            );

            // UPDATE TIMESTAMP SAFELY
            passwordEntry.setCreatedAt(
                    System.currentTimeMillis()
            );

            database.passwordDao().update(passwordEntry);

            // REFRESH TIMESTAMP UI
            loadData();

            Toast.makeText(
                    this,
                    "Updated Successfully",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnDelete.setOnClickListener(v -> {

            database.passwordDao().delete(passwordEntry);

            Toast.makeText(
                    this,
                    "Deleted Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });
    }

    private void copyToClipboard(
            String text,
            String message
    ) {

        ClipboardManager clipboard =
                (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip =
                ClipData.newPlainText(
                        "SecureVault",
                        text
                );

        clipboard.setPrimaryClip(clip);

        Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}