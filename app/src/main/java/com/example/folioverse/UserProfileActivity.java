package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {

    private Button logoutButton, changePasswordButton, updateProfileButton, showProfileButton, backButton;
    private TextView avatarText, fullNameText, userHandleText;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Buttons
        logoutButton = findViewById(R.id.btnLogout);
        changePasswordButton = findViewById(R.id.btnChangePassword);
        updateProfileButton = findViewById(R.id.btnUpdateProfile);
        showProfileButton = findViewById(R.id.btnShowProfile);
        backButton = findViewById(R.id.btnBack); // 🆕 Back button

        // TextViews
        avatarText = findViewById(R.id.avatarText);
        fullNameText = findViewById(R.id.fullNameText);
        userHandleText = findViewById(R.id.usernameText);

        // Fetch and display user info
        fetchUserProfile();

        // Button actions
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
        });

        changePasswordButton.setOnClickListener(v -> {
            startActivity(new Intent(UserProfileActivity.this, ChangePasswordActivity.class));
        });

        updateProfileButton.setOnClickListener(v -> {
            startActivity(new Intent(UserProfileActivity.this, UpdateProfileActivity.class));
        });

        showProfileButton.setOnClickListener(v -> {
            startActivity(new Intent(UserProfileActivity.this, ShowProfileActivity.class));
        });

        // 🔙 Back button logic
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void fetchUserProfile() {
        String uid = auth.getCurrentUser().getUid();

        firestore.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String fullname = documentSnapshot.getString("fullname");
                        String email = auth.getCurrentUser().getEmail();

                        if (username != null) {
                            userHandleText.setText("@" + username);
                            avatarText.setText(username.substring(0, 1).toUpperCase());
                        }

                        if (fullname != null && !fullname.isEmpty()) {
                            fullNameText.setText(fullname);
                        } else if (email != null) {
                            fullNameText.setText(email);
                        }

                    } else {
                        Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
