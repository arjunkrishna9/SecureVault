package com.example.securevault.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.securevault.models.PasswordEntry;
import com.example.securevault.models.User;

@Database(
        entities = {
                User.class,
                PasswordEntry.class
        },
        version = 1
)

public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract PasswordDao passwordDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getDatabase(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "securevault_db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}