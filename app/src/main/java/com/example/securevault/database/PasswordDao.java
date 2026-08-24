package com.example.securevault.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.securevault.models.PasswordEntry;

import java.util.List;

@Dao
public interface PasswordDao {

    // INSERT PASSWORD
    @Insert
    void insert(PasswordEntry passwordEntry);

    // UPDATE PASSWORD
    @Update
    void update(PasswordEntry passwordEntry);

    // DELETE PASSWORD
    @Delete
    void delete(PasswordEntry passwordEntry);

    // GET ALL PASSWORDS OF USER
    @Query("SELECT * FROM password_table WHERE userId = :userId ORDER BY position ASC")
    List<PasswordEntry> getPasswordsByUser(int userId);

    // GET SINGLE PASSWORD
    @Query("SELECT * FROM password_table WHERE id = :id LIMIT 1")
    PasswordEntry getPasswordById(int id);

    // SEARCH PASSWORDS
    @Query("SELECT * FROM password_table " +
            "WHERE userId = :userId " +
            "AND appName LIKE '%' || :query || '%' " +
            "ORDER BY position ASC")
    List<PasswordEntry> searchPasswords(int userId, String query);

    // UPDATE POSITION FOR DRAG REORDER
    @Query("UPDATE password_table SET position = :position WHERE id = :id")
    void updatePosition(int id, int position);

    // GET TOTAL PASSWORD COUNT
    @Query("SELECT COUNT(*) FROM password_table WHERE userId = :userId")
    int getPasswordCount(int userId);
}