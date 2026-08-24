package com.example.securevault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securevault.R;
import com.example.securevault.adapters.PasswordAdapter;
import com.example.securevault.database.AppDatabase;
import com.example.securevault.models.PasswordEntry;
import com.example.securevault.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPasswords;

    private FloatingActionButton fabAddPassword;

    private EditText etSearch;

    private TextView tvEmptyVault;

    private Button btnLogout;

    private PasswordAdapter adapter;

    private List<PasswordEntry> passwordList;

    private AppDatabase database;

    private SessionManager sessionManager;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // BLOCK SCREENSHOTS
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(R.layout.activity_main);

        initializeViews();

        initializeSystem();

        setupRecyclerView();

        setupSearch();

        setupDragAndDrop();

        loadPasswords();

        fabAddPassword.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            MainActivity.this,
                            AddPasswordActivity.class
                    )
            );
        });

        btnLogout.setOnClickListener(v -> {

            sessionManager.logoutUser();

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            LoginActivity.class
                    );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);

            finish();
        });
    }

    private void initializeViews() {

        recyclerViewPasswords =
                findViewById(R.id.recyclerViewPasswords);

        fabAddPassword =
                findViewById(R.id.fabAddPassword);

        etSearch =
                findViewById(R.id.etSearch);

        tvEmptyVault =
                findViewById(R.id.tvEmptyVault);

        btnLogout =
                findViewById(R.id.btnLogout);
    }

    private void initializeSystem() {

        sessionManager =
                new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            LoginActivity.class
                    );

            startActivity(intent);

            finish();

            return;
        }

        currentUserId =
                sessionManager.getUserId();

        database =
                AppDatabase.getDatabase(this);
    }

    private void setupRecyclerView() {

        passwordList = new ArrayList<>();

        adapter =
                new PasswordAdapter(
                        this,
                        passwordList
                );

        recyclerViewPasswords.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewPasswords.setAdapter(adapter);
    }

    // LOAD PASSWORDS
    private void loadPasswords() {

        List<PasswordEntry> fetchedPasswords =
                database.passwordDao()
                        .getPasswordsByUser(currentUserId);

        passwordList.clear();

        if (fetchedPasswords != null) {

            passwordList.addAll(fetchedPasswords);
        }

        adapter.notifyDataSetChanged();

        updateEmptyState();
    }

    // SEARCH FEATURE
    private void setupSearch() {

        etSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        searchPasswords(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                });
    }

    private void searchPasswords(String query) {

        List<PasswordEntry> searchedList =
                database.passwordDao()
                        .searchPasswords(
                                currentUserId,
                                query
                        );

        adapter.updateList(searchedList);

        updateEmptyState();
    }

    // EMPTY STATE
    private void updateEmptyState() {

        if (adapter.getItemCount() == 0) {

            tvEmptyVault.setText(
                    "No Passwords Found"
            );

            tvEmptyVault.setVisibility(TextView.VISIBLE);

        } else {

            tvEmptyVault.setVisibility(TextView.GONE);
        }
    }

    // DRAG & REORDER
    private void setupDragAndDrop() {

        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP |
                                ItemTouchHelper.DOWN,
                        0
                ) {

                    @Override
                    public boolean onMove(
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            @NonNull RecyclerView.ViewHolder target
                    ) {

                        int fromPosition =
                                viewHolder.getAdapterPosition();

                        int toPosition =
                                target.getAdapterPosition();

                        Collections.swap(
                                passwordList,
                                fromPosition,
                                toPosition
                        );

                        adapter.notifyItemMoved(
                                fromPosition,
                                toPosition
                        );

                        savePositions();

                        return true;
                    }

                    @Override
                    public void onSwiped(
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            int direction
                    ) {
                    }

                    @Override
                    public boolean isLongPressDragEnabled() {
                        return true;
                    }
                };

        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(callback);

        itemTouchHelper.attachToRecyclerView(
                recyclerViewPasswords
        );

        adapter.setItemTouchHelper(itemTouchHelper);
    }

    // SAVE NEW ORDER
    private void savePositions() {

        for (int i = 0; i < passwordList.size(); i++) {

            PasswordEntry entry =
                    passwordList.get(i);

            entry.setPosition(i);

            database.passwordDao().update(entry);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadPasswords();
    }
}