package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Collections;

public class RoleSelectionActivity extends AppCompatActivity {

    private Button btnUser, btnAdmin;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        btnUser = findViewById(R.id.user_button);
        btnAdmin = findViewById(R.id.admin_button);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnUser.setOnClickListener(v -> saveRoleAndProceed("user"));
        btnAdmin.setOnClickListener(v -> saveRoleAndProceed("admin"));
    }

    private void saveRoleAndProceed(String role) {
        String userId = auth.getCurrentUser().getUid();

        // Save the role to Firestore under users collection
        db.collection("users")
                .document(userId)
                .set(Collections.singletonMap("role", role), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Intent intent;
                    if (role.equals("user")) {
                        intent = new Intent(RoleSelectionActivity.this, HomeActivity.class);
                    } else {
                        intent = new Intent(RoleSelectionActivity.this, AdminDashboardActivity.class);
                    }

                    intent.putExtra("ROLE", role);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RoleSelectionActivity.this,
                            "Failed to save role: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}

