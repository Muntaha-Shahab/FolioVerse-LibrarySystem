package com.example.folioverse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private Button signupWithEmailButton, googleSignupButton;
    private TextView alreadyHaveAccountLink;

    private FirebaseAuth auth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;

    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupWithEmailButton = findViewById(R.id.btnSignupEmail);
        alreadyHaveAccountLink = findViewById(R.id.btnAlreadyHaveAccount);
        googleSignupButton = findViewById(R.id.btnGoogle);

        auth = FirebaseAuth.getInstance();

        selectedRole = getIntent().getStringExtra("ROLE");

        // Google One Tap setup
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(false)
                                .build()
                )
                .setAutoSelectEnabled(true)
                .build();

        signupWithEmailButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, SignupEmailActivity.class);
            intent.putExtra("ROLE", selectedRole);
            startActivity(intent);
            finish();
        });

        googleSignupButton.setOnClickListener(v -> {
            oneTapClient.beginSignIn(signUpRequest)
                    .addOnSuccessListener(result -> {
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                        googleSignupLauncher.launch(intentSenderRequest);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Google Sign-Up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        alreadyHaveAccountLink.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private final ActivityResultLauncher<IntentSenderRequest> googleSignupLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();

                        if (idToken != null) {
                            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                            auth.signInWithCredential(firebaseCredential)
                                    .addOnSuccessListener(authResult -> {
                                        FirebaseUser user = auth.getCurrentUser();
                                        if (user != null) {
                                            // Save user role to Firestore
                                            Map<String, Object> userMap = new HashMap<>();
                                            userMap.put("email", user.getEmail());
                                            userMap.put("role", selectedRole);

                                            FirebaseFirestore.getInstance().collection("userprofile")
                                                    .document(user.getUid())
                                                    .set(userMap)
                                                    .addOnSuccessListener(unused -> {
                                                        Intent intent = new Intent(this, SetPasswordActivity.class);
                                                        intent.putExtra("ROLE", selectedRole);
                                                        startActivity(intent);
                                                        finish();
                                                    })
                                                    .addOnFailureListener(e ->
                                                            Toast.makeText(this, "Failed to save profile: " + e.getMessage(), Toast.LENGTH_LONG).show()
                                                    );
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Firebase Sign-Up failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Google sign-in error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
}
