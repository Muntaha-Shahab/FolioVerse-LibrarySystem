package com.example.folioverse;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {AdminBook.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    public abstract BookDao bookDao();
}
