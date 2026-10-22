package com.example.folioverse;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowProfileActivity extends AppCompatActivity {

    private TextView avatarText, fullNameText, usernameText;
    private TextView tvEmail, tvUsername, tvBirthday, tvGender, tvRole;
    private Button back_button;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        // UI References
        avatarText = findViewById(R.id.avatarText);
        fullNameText = findViewById(R.id.fullNameText);
        usernameText = findViewById(R.id.usernameText);
        tvEmail = findViewById(R.id.tvEmail);
        tvUsername = findViewById(R.id.tvUsername);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvGender = findViewById(R.id.tvGender);
        tvRole = findViewById(R.id.tvRole);
        back_button = findViewById(R.id.back_button);

        // Firebase Init
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null) {
            loadUserInfo();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        back_button.setOnClickListener(v -> finish());
    }

    private void loadUserInfo() {
        String uid = user.getUid();
        String userEmail = user.getEmail();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String username = snapshot.getString("username");
                        String fullname = snapshot.getString("fullname");
                        String birthday = snapshot.getString("birthday");
                        String gender = snapshot.getString("gender");
                        String role = snapshot.getString("role");

                        // Avatar and Top Section
                        if (username != null && !username.isEmpty()) {
                            avatarText.setText(username.substring(0, 1).toUpperCase());
                            usernameText.setText("@" + username);
                        } else if (userEmail != null) {
                            avatarText.setText(userEmail.substring(0, 1).toUpperCase());
                            usernameText.setText("@" + userEmail.split("@")[0]);
                        } else {
                            avatarText.setText("?");
                            usernameText.setText("@unknown");
                        }

                        // Full Name or fallback
                        if (fullname != null && !fullname.isEmpty()) {
                            fullNameText.setText(fullname);
                        } else if (userEmail != null) {
                            fullNameText.setText(userEmail);
                        } else {
                            fullNameText.setText("No name");
                        }

                        // Detail Fields
                        tvEmail.setText(userEmail != null ? userEmail : "Not Available");
                        tvUsername.setText(username != null ? username : "Not Available");
                        tvBirthday.setText(birthday != null ? birthday : "Not Provided");
                        tvGender.setText(gender != null ? gender : "Not Specified");
                        tvRole.setText(role != null ? role : "Not Assigned");

                    } else {
                        Toast.makeText(this, "User profile not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
