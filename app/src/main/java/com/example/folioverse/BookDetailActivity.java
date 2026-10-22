package com.example.folioverse;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView detailCoverImage;
    private TextView detailTitle, detailAuthor, detailGenre, detailDescription;
    private Button readNowBtn, addToLibraryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // 🔧 Initialize views
        detailCoverImage = findViewById(R.id.detailCoverImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailAuthor = findViewById(R.id.detailAuthor);
        detailGenre = findViewById(R.id.detailGenre);
        detailDescription = findViewById(R.id.detailDescription);
        readNowBtn = findViewById(R.id.readNowBtn);
        addToLibraryBtn = findViewById(R.id.addToLibraryBtn);

        // 📦 Retrieve data from Intent
        int imageResId = getIntent().getIntExtra("image", R.drawable.cover_fantasy_1);
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String genre = getIntent().getStringExtra("genre");
        String description = getIntent().getStringExtra("description");

        // 📄 Set data to views with fallbacks
        detailCoverImage.setImageResource(imageResId);
        detailTitle.setText(title != null ? title : "Unknown Title");
        detailAuthor.setText(author != null ? "by " + author : "Unknown Author");
        detailGenre.setText(genre != null ? "Genre: " + genre : "Genre: Unknown");
        detailDescription.setText(description != null ? description : "No description available for this book.");

        // 📚 Button Actions
        readNowBtn.setOnClickListener(v -> {
            Toast.makeText(BookDetailActivity.this, "📖 Opening reader for \"" + title + "\"", Toast.LENGTH_SHORT).show();
            // 🔜 Launch ReaderActivity in the future
        });

        addToLibraryBtn.setOnClickListener(v -> {
            Toast.makeText(BookDetailActivity.this, "✅ \"" + title + "\" added to your library!", Toast.LENGTH_SHORT).show();
            // 🔐 Save to local DB or shared preferences in future
        });
    }
}