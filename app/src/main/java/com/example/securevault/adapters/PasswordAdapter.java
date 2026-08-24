package com.example.securevault.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securevault.R;
import com.example.securevault.activities.DetailsActivity;
import com.example.securevault.models.PasswordEntry;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PasswordAdapter
        extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {

    private Context context;

    private List<PasswordEntry> passwordList;

    private ItemTouchHelper itemTouchHelper;

    public PasswordAdapter(
            Context context,
            List<PasswordEntry> passwordList
    ) {

        this.context = context;

        this.passwordList = passwordList;
    }

    // SET TOUCH HELPER
    public void setItemTouchHelper(
            ItemTouchHelper itemTouchHelper
    ) {

        this.itemTouchHelper = itemTouchHelper;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.item_password,
                                parent,
                                false
                        );

        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PasswordViewHolder holder,
            int position
    ) {

        PasswordEntry entry =
                passwordList.get(position);

        // APP NAME
        holder.tvAppName.setText(
                entry.getAppName()
        );

        // USERNAME
        holder.tvUsername.setText(
                entry.getUsername()
        );

        // SAFE TIMESTAMP
        if (entry.getCreatedAt() > 0) {

            SimpleDateFormat sdf =
                    new SimpleDateFormat(
                            "dd MMM yyyy | hh:mm a",
                            Locale.getDefault()
                    );

            String formattedDate =
                    sdf.format(
                            new Date(entry.getCreatedAt())
                    );

            holder.tvUpdatedAt.setText(
                    "Updated: " + formattedDate
            );

        } else {

            holder.tvUpdatedAt.setText(
                    "No Timestamp"
            );
        }

        // OPEN DETAILS
        holder.cardView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            DetailsActivity.class
                    );

            intent.putExtra(
                    "password_id",
                    entry.getId()
            );

            context.startActivity(intent);
        });

        // DRAG & REORDER
        holder.cardView.setOnLongClickListener(v -> {

            if (itemTouchHelper != null) {

                itemTouchHelper.startDrag(holder);
            }

            return true;
        });
    }

    @Override
    public int getItemCount() {

        return passwordList.size();
    }

    // UPDATE SEARCH LIST
    public void updateList(
            List<PasswordEntry> newList
    ) {

        passwordList = newList;

        notifyDataSetChanged();
    }

    // CURRENT LIST
    public List<PasswordEntry> getPasswordList() {

        return passwordList;
    }

    // MOVE ITEMS
    public void moveItem(
            int fromPosition,
            int toPosition
    ) {

        Collections.swap(
                passwordList,
                fromPosition,
                toPosition
        );

        notifyItemMoved(
                fromPosition,
                toPosition
        );
    }

    // VIEW HOLDER
    public static class PasswordViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvAppName;

        TextView tvUsername;

        TextView tvUpdatedAt;

        CardView cardView;

        public PasswordViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvAppName =
                    itemView.findViewById(
                            R.id.tvAppName
                    );

            tvUsername =
                    itemView.findViewById(
                            R.id.tvUsername
                    );

            tvUpdatedAt =
                    itemView.findViewById(
                            R.id.tvUpdatedAt
                    );

            cardView =
                    itemView.findViewById(
                            R.id.cardView
                    );
        }
    }
}