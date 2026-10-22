package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetPasswordActivity extends AppCompatActivity {

    private EditText passwordInput, confirmPasswordInput;
    private Button saveButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        passwordInput = findViewById(R.id.set_password);
        confirmPasswordInput = findViewById(R.id.set_confirm_password);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(v -> savePasswordAndContinue());
    }

    private void savePasswordAndContinue() {
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            String role = getIntent().getStringExtra("ROLE");

            // ✅ Update password first
            user.updatePassword(password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    // ✅ Create UserProfile object
                    UserProfile userProfile = new UserProfile(
                            user.getEmail(),
                            user.getDisplayName() != null ? user.getDisplayName() : "", // username fallback
                            null,  // birthday
                            null,  // gender
                            role
                    );

                    // ✅ Store user profile in Firestore
                    db.collection("users")
                            .document(user.getUid())
                            .set(userProfile)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Account setup complete!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SetPasswordActivity.this, UserInfoActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });

                } else {
                    Toast.makeText(this, "Failed to update password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "User is not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
