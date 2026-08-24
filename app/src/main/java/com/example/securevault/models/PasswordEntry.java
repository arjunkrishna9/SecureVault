package com.example.securevault.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "password_table")
public class PasswordEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;

    private String appName;

    private String username;

    private String password;

    private long createdAt;

    // FOR DRAG & REORDER PRIORITY
    private int position;

    // EMPTY CONSTRUCTOR REQUIRED BY ROOM
    public PasswordEntry() {
    }

    // MAIN CONSTRUCTOR
    @Ignore
    public PasswordEntry(
            int userId,
            String appName,
            String username,
            String password,
            long createdAt,
            int position
    ) {
        this.userId = userId;
        this.appName = appName;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
        this.position = position;
    }

    // -------------------------
    // GETTERS & SETTERS
    // -------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}