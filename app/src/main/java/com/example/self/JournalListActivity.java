package com.example.self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import model.Journal;
import ui.JournalRecyclerAdapter;
import util.JournalApi;

public class JournalListActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private RecyclerView recyclerView;
    private JournalRecyclerAdapter adapter;
    private CollectionReference collectionReference=db.collection("Journal");
    private TextView noJournalEntry;
    private List<Journal> journalList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        noJournalEntry=findViewById(R.id.list_no_thoughts);
        getSupportActionBar().setElevation(0);
        recyclerView=findViewById(R.id.recyclerView);
        journalList=new ArrayList<>();
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_add:
                if(user!=null && firebaseAuth!=null)
                    startActivity(new Intent(JournalListActivity.this,PostJournalActivity.class));
                break;
            case R.id.signout:
                if(user!=null && firebaseAuth!=null)
                    firebaseAuth.signOut();
                startActivity(new Intent(JournalListActivity.this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.whereEqualTo("userId", JournalApi.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                          if(queryDocumentSnapshots!=null)
                          {
                              for(QueryDocumentSnapshot journals:queryDocumentSnapshots)
                              {
                                  Journal journal=journals.toObject(Journal.class);
                                  journalList.add(journal);

                              }
                              JournalRecyclerAdapter journalRecyclerAdapter=new JournalRecyclerAdapter(JournalListActivity.this
                                      ,journalList);
                              recyclerView.setAdapter(journalRecyclerAdapter);
                              journalRecyclerAdapter.notifyDataSetChanged();
                          }
                          else
                          {
                                noJournalEntry.setVisibility(View.VISIBLE);
                          }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
