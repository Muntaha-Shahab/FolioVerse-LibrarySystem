package com.example.folioverse;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity

public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    String name, role;

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }
}
