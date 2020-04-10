package com.example.self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import util.JournalApi;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText emailtxt;
    private EditText passwordtxt;
    private EditText usernametxt;
    private ProgressBar progressBar;
    private Button createAccountButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser CurrentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        firebaseAuth = FirebaseAuth.getInstance();
        emailtxt = findViewById(R.id.email_account);
        passwordtxt = findViewById(R.id.password_account);
        usernametxt = findViewById(R.id.username_account);
        progressBar = findViewById(R.id.create_acc_progress);
        createAccountButton = findViewById(R.id.create_acc_btn);
        getSupportActionBar().setElevation(0);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                CurrentUser = firebaseAuth.getCurrentUser();
                if (CurrentUser != null) {

                } else {

                }
            }
        };
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(usernametxt.getText().toString()) &&
                        !TextUtils.isEmpty(emailtxt.getText().toString()) &&
                        !TextUtils.isEmpty(passwordtxt.getText().toString())) {
                    String email = emailtxt.getText().toString().trim();
                    String password = passwordtxt.getText().toString().trim();
                    String username = usernametxt.getText().toString().trim();
                    createUserAccount(email, password, username);
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Empty Fields Not Allowed", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        CurrentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void createUserAccount(String email, String password, final String username) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                CurrentUser = firebaseAuth.getCurrentUser();
                                final String currentUserId = CurrentUser.getUid();
                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userId", currentUserId);
                                userObj.put("username", username);
                                collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.getResult().exists()) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    String user = task.getResult().getString("username");
                                                    JournalApi journalApi=JournalApi.getInstance();
                                                    journalApi.setUserId(currentUserId);
                                                    journalApi.setUsername(user);
                                                    Intent intent = new Intent(CreateAccountActivity.this, PostJournalActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });

                            } else {

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {

        }
    }
}
