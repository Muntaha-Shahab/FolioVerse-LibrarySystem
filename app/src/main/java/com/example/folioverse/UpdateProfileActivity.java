package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateProfileActivity extends AppCompatActivity {

    private TextView tvEdit, avatarText, fullNameText, usernameText;
    private EditText etEmail, etName, etDob;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale, rbOther;
    private Button btnSaveChanges, btnBack;

    private boolean isEditing = false;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile); // ✅ uses your XML name

        // Bind views
        tvEdit = findViewById(R.id.tvEdit);
        avatarText = findViewById(R.id.avatarText);
        fullNameText = findViewById(R.id.fullNameText);
        usernameText = findViewById(R.id.usernameText);

        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etDob = findViewById(R.id.etDob);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnBack = findViewById(R.id.btnBack);

        // Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        disableEditing();
        loadUserData();

        // Edit button logic
        tvEdit.setOnClickListener(v -> {
            if (!isEditing) {
                enableEditing();
                tvEdit.setText("Save");
                isEditing = true;
            } else {
                saveChanges();
            }
        });

        // Save button also triggers saveChanges()
        btnSaveChanges.setOnClickListener(v -> saveChanges());

        // Back button
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadUserData() {
        String uid = auth.getCurrentUser().getUid();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String name = doc.getString("username");
                        String dob = doc.getString("birthday");
                        String gender = doc.getString("gender");
                        String email = auth.getCurrentUser().getEmail();

                        if (email != null) {
                            etEmail.setText(email);
                            usernameText.setText("@" + email.split("@")[0]);
                            avatarText.setText(email.substring(0, 1).toUpperCase());
                        }

                        if (name != null) {
                            fullNameText.setText(name);
                            etName.setText(name);
                        }

                        if (dob != null) etDob.setText(dob);
                        if ("Male".equalsIgnoreCase(gender)) rbMale.setChecked(true);
                        else if ("Female".equalsIgnoreCase(gender)) rbFemale.setChecked(true);
                        else if ("Other".equalsIgnoreCase(gender)) rbOther.setChecked(true);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void saveChanges() {
        String uid = auth.getCurrentUser().getUid();
        String name = etName.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String gender;

        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId == R.id.rbMale) gender = "Male";
        else if (selectedId == R.id.rbFemale) gender = "Female";
        else if (selectedId == R.id.rbOther) gender = "Other";
        else {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dob)) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(uid)
                .update("username", name,
                        "birthday", dob,
                        "gender", gender)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateProfileActivity.this, ShowProfileActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void disableEditing() {
        etName.setEnabled(false);
        etDob.setEnabled(false);
        rbMale.setEnabled(false);
        rbFemale.setEnabled(false);
        rbOther.setEnabled(false);
    }

    private void enableEditing() {
        etName.setEnabled(true);
        etDob.setEnabled(true);
        rbMale.setEnabled(true);
        rbFemale.setEnabled(true);
        rbOther.setEnabled(true);
    }
}
