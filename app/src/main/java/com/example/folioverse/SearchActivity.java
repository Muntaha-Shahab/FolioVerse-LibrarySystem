package com.example.folioverse;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private StoryAdapter storyAdapter;
    private List<Book> fullStoryList;
    private TextView noResultsText;

    private TextView btnFantasy, btnHorror, btnMystery, btnSciFi, btnHumor, btnNonFiction;

    private RecyclerView trendingBooksRecyclerView;
    private RecyclerView allStoriesRecyclerView;
    private View genreScroll;
    private TextView trendingTitle, suggestedTitle;

    private List<TextView> genreButtons;
    private TextView selectedGenreButton = null; // To track selected genre

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText searchInput = findViewById(R.id.searchInput);
        trendingBooksRecyclerView = findViewById(R.id.trendingBooksRecyclerView);
        allStoriesRecyclerView = findViewById(R.id.allStoriesRecyclerView);
        noResultsText = findViewById(R.id.noResultsText);
        genreScroll = findViewById(R.id.genreScroll);
        trendingTitle = findViewById(R.id.trendingTitle);
        suggestedTitle = findViewById(R.id.suggestedTitle);

        // Genre buttons
        btnFantasy = findViewById(R.id.btnFantasy);
        btnHorror = findViewById(R.id.btnHorror);
        btnMystery = findViewById(R.id.btnMystery);
        btnSciFi = findViewById(R.id.btnSciFi);
        btnHumor = findViewById(R.id.btnHumor);
        btnNonFiction = findViewById(R.id.btnNonFiction);

        // Add genre buttons to list for state change
        genreButtons = new ArrayList<>();
        genreButtons.add(btnFantasy);
        genreButtons.add(btnHorror);
        genreButtons.add(btnMystery);
        genreButtons.add(btnSciFi);
        genreButtons.add(btnHumor);
        genreButtons.add(btnNonFiction);

        // Genre click listeners
        btnFantasy.setOnClickListener(v -> filterByGenre("Fantasy", btnFantasy));
        btnHorror.setOnClickListener(v -> filterByGenre("Horror", btnHorror));
        btnMystery.setOnClickListener(v -> filterByGenre("Mystery", btnMystery));
        btnSciFi.setOnClickListener(v -> filterByGenre("Sci-Fi", btnSciFi));
        btnHumor.setOnClickListener(v -> filterByGenre("Humor", btnHumor));
        btnNonFiction.setOnClickListener(v -> filterByGenre("Non-Fiction", btnNonFiction));

        // Trending Books
        List<Book> trendingBooksList = new ArrayList<>();
        trendingBooksList.add(new Book(R.drawable.cover_fantasy_1, "Realm of Fire", "A. Knight", "Fantasy", "A fantasy tale of dragons and destiny."));
        trendingBooksList.add(new Book(R.drawable.cover_fantasy_2, "Shadowborn", "M. Rivers", "Fantasy", "A mysterious journey through enchanted forests."));
        trendingBooksList.add(new Book(R.drawable.cover_fantasy_3, "Eternal Blade", "D. Winter", "Fantasy", "A warrior’s quest for the blade of legends."));
        trendingBooksList.add(new Book(R.drawable.cover_fantasy_4, "The Last Mage", "L. Phoenix", "Fantasy", "Magic, secrets, and the fall of an empire."));

        // Full Story List
        fullStoryList = new ArrayList<>();

        // Add all books (Fantasy, Humor, Horror, Mystery, Sci-Fi, Non-Fiction)
        fullStoryList.add(new Book(R.drawable.cover_fantasy_1, "The Enchanted Realm", "Ava Willow", "Fantasy", "A magical world where dragons and destiny collide."));
        fullStoryList.add(new Book(R.drawable.cover_fantasy_2, "Realm Breaker", "Sebastian Hart", "Fantasy", "A warrior rises to defy the gods."));
        fullStoryList.add(new Book(R.drawable.cover_fantasy_3, "Mystic Legends", "Lila Greene", "Fantasy", "Ancient prophecies and hidden powers unfold."));
        fullStoryList.add(new Book(R.drawable.cover_fantasy_4, "Crown of Shadows", "Eli Storm", "Fantasy", "A dark prince returns for his throne."));

        fullStoryList.add(new Book(R.drawable.cover_humor_1, "Laugh Out Loud", "Nina Joy", "Humor", "A hilarious ride through life's awkward moments."));
        fullStoryList.add(new Book(R.drawable.cover_humor_2, "Comic Relief", "Sam Smiles", "Humor", "Jokes that will tickle your funny bone."));
        fullStoryList.add(new Book(R.drawable.cover_humor_3, "Funny Side Up", "Tom Bright", "Humor", "Everyday life seen through a comedic lens."));
        fullStoryList.add(new Book(R.drawable.cover_humor_4, "Wit Happens", "Sally Quirk", "Humor", "Sharp wit and laugh-out-loud observations."));

        fullStoryList.add(new Book(R.drawable.cover_horror_1, "Whispers in the Dark", "Riley Black", "Horror", "An abandoned house with secrets that breathe."));
        fullStoryList.add(new Book(R.drawable.cover_horror_2, "The Grinning Man", "Tara Moon", "Horror", "He’s always smiling. Always watching."));
        fullStoryList.add(new Book(R.drawable.cover_horror_3, "Voices Below", "Henry Knox", "Horror", "What if the basement really talks back?"));
        fullStoryList.add(new Book(R.drawable.cover_horror_4, "The Forgotten Ones", "Mira Vale", "Horror", "Some spirits don’t forget... or forgive."));

        fullStoryList.add(new Book(R.drawable.cover_mystery_1, "The Silent Clue", "Jamie Cross", "Mystery", "A detective follows a trail of vanishing evidence."));
        fullStoryList.add(new Book(R.drawable.cover_mystery_2, "Locked Room", "Oliver Shade", "Mystery", "The key to the murder lies inside the room."));
        fullStoryList.add(new Book(R.drawable.cover_mystery_3, "The Case Files", "Nora Truth", "Mystery", "Each case brings her closer to the truth."));
        fullStoryList.add(new Book(R.drawable.cover_mystery_4, "Whodunit?", "Derek Twist", "Mystery", "Everyone has secrets. One has a motive."));

        fullStoryList.add(new Book(R.drawable.cover_scifi_1, "Beyond the Stars", "Lana Sky", "Sci-Fi", "A journey through galaxies and time."));
        fullStoryList.add(new Book(R.drawable.cover_scifi_2, "Time Lock", "Reid Quantum", "Sci-Fi", "He controls time, but not the future."));
        fullStoryList.add(new Book(R.drawable.cover_scifi_3, "Neon Earth", "Zane Nova", "Sci-Fi", "A cyberpunk world of rebels and AI."));
        fullStoryList.add(new Book(R.drawable.cover_scifi_4, "Cosmic Shift", "Vera Ray", "Sci-Fi", "One decision alters the entire universe."));

        fullStoryList.add(new Book(R.drawable.cover_nonfiction_1, "Mind Over Matter", "Dr. Ayesha Khan", "Non-Fiction", "Explore the power of the human brain."));
        fullStoryList.add(new Book(R.drawable.cover_nonfiction_2, "History Rewritten", "Liam Gray", "Non-Fiction", "What your textbooks didn't tell you."));
        fullStoryList.add(new Book(R.drawable.cover_nonfiction_3, "The Science Behind Success", "Erica Wells", "Non-Fiction", "Secrets of top achievers revealed."));
        fullStoryList.add(new Book(R.drawable.cover_nonfiction_4, "Unlocking Potential", "Maya Noor", "Non-Fiction", "Mastering habits for greatness."));

        // Trending RecyclerView Setup
        TrendingAdapter trendingAdapter = new TrendingAdapter(this, trendingBooksList);
        trendingBooksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trendingBooksRecyclerView.setAdapter(trendingAdapter);

        // All Stories RecyclerView Setup
        storyAdapter = new StoryAdapter(this, fullStoryList);
        allStoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allStoriesRecyclerView.setAdapter(storyAdapter);

        // Search Functionality
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStories(s.toString());
            }
        });

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(SearchActivity.this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_notifications) {
                startActivity(new Intent(SearchActivity.this, AlertsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(SearchActivity.this, UserProfileActivity.class));
                finish();
                return true;
            }
            return true;
        });
    }

    private void filterStories(String query) {
        genreScroll.setVisibility(View.VISIBLE);
        trendingTitle.setVisibility(View.GONE);
        trendingBooksRecyclerView.setVisibility(View.GONE);
        suggestedTitle.setVisibility(View.GONE);

        List<Book> filteredList = new ArrayList<>();
        for (Book book : fullStoryList) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(book);
            }
        }

        storyAdapter.updateData(filteredList);
        noResultsText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void filterByGenre(String genre, TextView selectedBtn) {
        genreScroll.setVisibility(View.VISIBLE);
        trendingTitle.setVisibility(View.GONE);
        trendingBooksRecyclerView.setVisibility(View.GONE);
        suggestedTitle.setVisibility(View.GONE);
        noResultsText.setVisibility(View.GONE);

        selectedGenreButton = selectedBtn;

        for (TextView btn : genreButtons) {
            if (btn == selectedBtn) {
                btn.setTextColor(Color.parseColor("#FF5722"));
                btn.setTypeface(null, Typeface.BOLD);
            } else {
                btn.setTextColor(Color.parseColor("#000000"));
                btn.setTypeface(null, Typeface.NORMAL);
            }
        }

        List<Book> filtered = new ArrayList<>();
        for (Book book : fullStoryList) {
            if (book.getGenre().equalsIgnoreCase(genre)) {
                filtered.add(book);
            }
        }

        storyAdapter.updateData(filtered);
        noResultsText.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (selectedGenreButton != null) {
            // Reset genre tab state
            for (TextView btn : genreButtons) {
                btn.setTextColor(Color.parseColor("#000000"));
                btn.setTypeface(null, Typeface.NORMAL);
            }
            selectedGenreButton = null;

            // Restore full view
            genreScroll.setVisibility(View.VISIBLE);
            trendingTitle.setVisibility(View.VISIBLE);
            trendingBooksRecyclerView.setVisibility(View.VISIBLE);
            suggestedTitle.setVisibility(View.VISIBLE);
            noResultsText.setVisibility(View.GONE);

            storyAdapter.updateData(fullStoryList);
        } else {
            super.onBackPressed();
        }
    }
}
