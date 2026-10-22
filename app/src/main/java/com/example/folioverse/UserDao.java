package com.example.folioverse;

import androidx.room.*;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
