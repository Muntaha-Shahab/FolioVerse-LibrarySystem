package com.example.folioverse;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPassword, newPassword, confirmPassword, emailForgot;
    private Button btnChangePassword, btnBack;
    private RadioGroup passwordOptionGroup;
    private LinearLayout layoutCurrentPassword, layoutForgotPassword;
    private FirebaseAuth auth;
    private FirebaseUser user;

    // New for avatar section
    private TextView avatarText, fullNameText, usernameText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Initialize views
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        emailForgot = findViewById(R.id.emailForgot);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBack = findViewById(R.id.btnBack);
        passwordOptionGroup = findViewById(R.id.passwordOptionGroup);
        layoutCurrentPassword = findViewById(R.id.layoutCurrentPassword);
        layoutForgotPassword = findViewById(R.id.layoutForgotPassword);

        // Avatar-related views
        avatarText = findViewById(R.id.avatarText);
        fullNameText = findViewById(R.id.fullNameText);
        usernameText = findViewById(R.id.usernameText);

        // Firebase setup
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null) {
            loadUserInfo(); // Load avatar, full name, and username
        }

        // Show/hide inputs based on selected option
        passwordOptionGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCurrent) {
                layoutCurrentPassword.setVisibility(View.VISIBLE);
                layoutForgotPassword.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioForgot) {
                layoutCurrentPassword.setVisibility(View.GONE);
                layoutForgotPassword.setVisibility(View.VISIBLE);

                if (user != null && user.getEmail() != null) {
                    emailForgot.setText(user.getEmail());
                }
            }
        });

        passwordOptionGroup.check(R.id.radioCurrent); // Default selection

        btnChangePassword.setOnClickListener(v -> {
            int selectedOption = passwordOptionGroup.getCheckedRadioButtonId();
            if (selectedOption == R.id.radioCurrent) {
                changePasswordViaCurrentPassword();
            } else if (selectedOption == R.id.radioForgot) {
                sendPasswordResetEmail();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadUserInfo() {
        db.collection("users").document(user.getUid()).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String email = snapshot.getString("email");
                        String name = snapshot.getString("username");

                        if (email != null) {
                            usernameText.setText("@" + email.split("@")[0]);
                            if (!email.isEmpty()) {
                                avatarText.setText(String.valueOf(email.charAt(0)).toUpperCase());
                            }
                        }

                        if (name != null) {
                            fullNameText.setText(name);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load user info", Toast.LENGTH_SHORT).show());
    }

    private void changePasswordViaCurrentPassword() {
        String current = currentPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(current) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), current);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.updatePassword(newPass).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to change password: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPasswordResetEmail() {
        String email = emailForgot.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to send reset email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
