package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupEmailActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button createAccountButton, backButton;
    private FirebaseAuth auth;
    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);

        emailInput = findViewById(R.id.signup_email);
        createAccountButton = findViewById(R.id.signup_create_account);
        backButton = findViewById(R.id.btnBack);
        TextView roleDisplay = findViewById(R.id.signup_role_display);

        auth = FirebaseAuth.getInstance();

        selectedRole = getIntent().getStringExtra("ROLE");
        roleDisplay.setText(selectedRole != null ? "Signing up as: " + selectedRole : "Signing up");

        createAccountButton.setOnClickListener(v -> createAccount());
        backButton.setOnClickListener(v -> finish());
    }

    private void createAccount() {
        String email = emailInput.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email");
            return;
        }

        String defaultPassword = "Default123"; // You can let them change this later

        auth.createUserWithEmailAndPassword(email, defaultPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {

                            // 🔥 Save user profile to Firestore
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("email", email);
                            userMap.put("role", selectedRole);

                            FirebaseFirestore.getInstance().collection("userprofile")
                                    .document(firebaseUser.getUid())
                                    .set(userMap)
                                    .addOnSuccessListener(unused -> {
                                        Intent intent = new Intent(SignupEmailActivity.this, SetPasswordActivity.class);
                                        intent.putExtra("ROLE", selectedRole);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
