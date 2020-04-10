package com.example.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.JournalApi;

public class LoginActivity extends AppCompatActivity {
    private Button createAccount;
    private Button login;
    private AutoCompleteTextView email;
    private EditText password;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        createAccount=findViewById(R.id.create_acc_btn_login);
        login=findViewById(R.id.log_in);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        progressBar=findViewById(R.id.login_progress);
        getSupportActionBar().setElevation(0);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmailPasswordUser(email.getText().toString().trim(),
                        password.getText().toString().trim());
            }
        });
    }

    private void loginEmailPasswordUser(String emailText, String pwd) {
        progressBar.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(emailText) && !TextUtils.isEmpty(pwd))
        {

            firebaseAuth.signInWithEmailAndPassword(emailText,pwd).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            user=firebaseAuth.getCurrentUser();
                            String currentUserId=user.getUid();
                            collectionReference.whereEqualTo("userId",currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                            @Nullable FirebaseFirestoreException e) {
                                            if(e!=null)
                                            {

                                            }
                                            if(!queryDocumentSnapshots.isEmpty())
                                            {progressBar.setVisibility(View.INVISIBLE);
                                                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                                                {
                                                    JournalApi journalApi=JournalApi.getInstance();
                                                    journalApi.setUsername(snapshot.getString("username"));
                                                    journalApi.setUserId(snapshot.getString("userId"));
                                                    startActivity(new Intent(LoginActivity.this,PostJournalActivity.class));
                                                }
                                            }
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                 progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this,"Empty Feilds Not Allowed",Toast.LENGTH_SHORT).show();
        }
    }
}
