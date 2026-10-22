package com.example.folioverse;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "adminBook")
public class AdminBook {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String author;
    private String genre;
    private String description;
    private String coverImageUri;

    // Constructor
    public AdminBook(String title, String author, String genre, String description, String coverImageUri) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
        this.coverImageUri = coverImageUri;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageUri() {
        return coverImageUri;
    }

    public void setCoverImageUri(String coverImageUri) {
        this.coverImageUri = coverImageUri;
    }
}
