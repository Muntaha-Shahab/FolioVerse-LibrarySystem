package com.example.folioverse;

public class Book {
    private final int imageResId;
    private final String title;
    private final String author;
    private final String genre;
    private final String description;

    public Book(int imageResId, String title, String author, String genre, String description) {
        this.imageResId = imageResId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
    }

    public int getImageResId() { return imageResId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getDescription() { return description; }
}
