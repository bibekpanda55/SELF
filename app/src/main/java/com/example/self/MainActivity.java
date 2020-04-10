package com.example.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.JournalApi;

public class MainActivity extends AppCompatActivity {
private Button start;
private FirebaseAuth firebaseAuth;
private FirebaseAuth.AuthStateListener authStateListener;
private FirebaseFirestore db= FirebaseFirestore.getInstance();
private CollectionReference collectionReference=db.collection("Users");
private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start=findViewById(R.id.start_button);
        getSupportActionBar().setElevation(0);
        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    user=firebaseAuth.getCurrentUser();
                    String currentuserId=user.getUid();
                    collectionReference.whereEqualTo("userid",currentuserId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                               if(e!=null)
                               {
                                   return;
                               }
                               String name;
                               if(!queryDocumentSnapshots.isEmpty())
                               {
                                   for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots)
                                   {
                                       JournalApi journalApi=JournalApi.getInstance();
                                       journalApi.setUserId(snapshot.getString("userId"));
                                       journalApi.setUsername(snapshot.getString("username"));
                                       startActivity(new Intent(MainActivity.this,JournalListActivity.class));
                                       finish();
                                   }
                               }
                                }
                            });
                }

            }
        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(firebaseAuth!=null)
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
