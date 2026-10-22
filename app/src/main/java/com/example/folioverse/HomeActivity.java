package com.example.folioverse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Profile icon click
        ImageView profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        // Notification icon click
        ImageView notificationIcon = findViewById(R.id.nav_notifications);
        notificationIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AlertsActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;

            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                return true;

            } else if (itemId == R.id.nav_notifications) {
                startActivity(new Intent(HomeActivity.this, AlertsActivity.class));
                return true;

            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
                return true;
            }

            return false;
        });
    }

    // 📚 Handle Book Cover Clicks
    public void openBookDetail(View view) {
        Intent intent = new Intent(HomeActivity.this, BookDetailActivity.class);
        int id = view.getId();

        // 🎭 Fantasy
        if (id == R.id.fantasyBook1) {
            putBookExtras(intent, R.drawable.cover_fantasy_1, "The Enchanted Realm", "Ava Willow", "Fantasy", "A magical world where dragons and destiny collide.");
        } else if (id == R.id.fantasyBook2) {
            putBookExtras(intent, R.drawable.cover_fantasy_2, "Realm Breaker", "Sebastian Hart", "Fantasy", "A warrior rises to defy the gods.");
        } else if (id == R.id.fantasyBook3) {
            putBookExtras(intent, R.drawable.cover_fantasy_3, "Mystic Legends", "Lila Greene", "Fantasy", "Ancient prophecies and hidden powers unfold.");
        } else if (id == R.id.fantasyBook4) {
            putBookExtras(intent, R.drawable.cover_fantasy_4, "Crown of Shadows", "Eli Storm", "Fantasy", "A dark prince returns for his throne.");

            // 😂 Humor
        } else if (id == R.id.humorBook1) {
            putBookExtras(intent, R.drawable.cover_humor_1, "Laugh Out Loud", "Nina Joy", "Humor", "A hilarious ride through life's awkward moments.");
        } else if (id == R.id.humorBook2) {
            putBookExtras(intent, R.drawable.cover_humor_2, "Comic Relief", "Sam Smiles", "Humor", "Jokes that will tickle your funny bone.");
        } else if (id == R.id.humorBook3) {
            putBookExtras(intent, R.drawable.cover_humor_3, "Funny Side Up", "Tom Bright", "Humor", "Everyday life seen through a comedic lens.");
        } else if (id == R.id.humorBook4) {
            putBookExtras(intent, R.drawable.cover_humor_4, "Wit Happens", "Sally Quirk", "Humor", "Sharp wit and laugh-out-loud observations.");

            // 😱 Horror
        } else if (id == R.id.horrorBook1) {
            putBookExtras(intent, R.drawable.cover_horror_1, "Whispers in the Dark", "Riley Black", "Horror", "An abandoned house with secrets that breathe.");
        } else if (id == R.id.horrorBook2) {
            putBookExtras(intent, R.drawable.cover_horror_2, "The Grinning Man", "Tara Moon", "Horror", "He’s always smiling. Always watching.");
        } else if (id == R.id.horrorBook3) {
            putBookExtras(intent, R.drawable.cover_horror_3, "Voices Below", "Henry Knox", "Horror", "What if the basement really talks back?");
        } else if (id == R.id.horrorBook4) {
            putBookExtras(intent, R.drawable.cover_horror_4, "The Forgotten Ones", "Mira Vale", "Horror", "Some spirits don’t forget... or forgive.");

            // 🕵 Mystery
        } else if (id == R.id.mysteryBook1) {
            putBookExtras(intent, R.drawable.cover_mystery_1, "The Silent Clue", "Jamie Cross", "Mystery", "A detective follows a trail of vanishing evidence.");
        } else if (id == R.id.mysteryBook2) {
            putBookExtras(intent, R.drawable.cover_mystery_2, "Locked Room", "Oliver Shade", "Mystery", "The key to the murder lies inside the room.");
        } else if (id == R.id.mysteryBook3) {
            putBookExtras(intent, R.drawable.cover_mystery_3, "The Case Files", "Nora Truth", "Mystery", "Each case brings her closer to the truth.");
        } else if (id == R.id.mysteryBook4) {
            putBookExtras(intent, R.drawable.cover_mystery_4, "Whodunit?", "Derek Twist", "Mystery", "Everyone has secrets. One has a motive.");

            // 👽 Sci-Fi
        } else if (id == R.id.sci_fiBook1) {
            putBookExtras(intent, R.drawable.cover_scifi_1, "Beyond the Stars", "Lana Sky", "Sci-Fi", "A journey through galaxies and time.");
        } else if (id == R.id.sci_fiBook2) {
            putBookExtras(intent, R.drawable.cover_scifi_2, "Time Lock", "Reid Quantum", "Sci-Fi", "He controls time, but not the future.");
        } else if (id == R.id.sci_fiBook3) {
            putBookExtras(intent, R.drawable.cover_scifi_3, "Neon Earth", "Zane Nova", "Sci-Fi", "A cyberpunk world of rebels and AI.");
        } else if (id == R.id.sci_fiBook4) {
            putBookExtras(intent, R.drawable.cover_scifi_4, "Cosmic Shift", "Vera Ray", "Sci-Fi", "One decision alters the entire universe.");

            // 📘 Non-Fiction
        } else if (id == R.id.nonfictionBook1) {
            putBookExtras(intent, R.drawable.cover_nonfiction_1, "Mind Over Matter", "Dr. Ayesha Khan", "Non-Fiction", "Explore the power of the human brain.");
        } else if (id == R.id.nonfictionBook2) {
            putBookExtras(intent, R.drawable.cover_nonfiction_2, "History Rewritten", "Liam Gray", "Non-Fiction", "What your textbooks didn't tell you.");
        } else if (id == R.id.nonfictionBook3) {
            putBookExtras(intent, R.drawable.cover_nonfiction_3, "The Science Behind Success", "Erica Wells", "Non-Fiction", "Secrets of top achievers revealed.");
        } else if (id == R.id.nonfictionBook4) {
            putBookExtras(intent, R.drawable.cover_nonfiction_4, "Unlocking Potential", "Maya Noor", "Non-Fiction", "Mastering habits for greatness.");
        }

        startActivity(intent);
    }

    private void putBookExtras(Intent intent, int image, String title, String author, String genre, String description) {
        intent.putExtra("image", image);
        intent.putExtra("title", title);
        intent.putExtra("author", author);
        intent.putExtra("genre", genre);
        intent.putExtra("description", description);
    }
}
