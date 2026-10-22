package com.example.folioverse;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    void insert(AdminBook adminBook);

    @Update
    void update(AdminBook adminBook);

    @Delete
    void delete(AdminBook adminBook);

    @Query("SELECT * FROM adminBook")
    List<AdminBook> getAllBooks();
}
