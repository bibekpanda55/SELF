package com.example.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.util.Date;

import model.Journal;
import util.JournalApi;

public class PostJournalActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE =1 ;
    private Button saveButton;
private ImageView cameraButton;
private ImageView imageView;
private TextView Currentuser;
private TextView date;
private EditText title;
private EditText thought;
private ProgressBar progressBar;
private String currentusername;
private String currentuserId;
private FirebaseAuth firebaseAuth;
private FirebaseAuth.AuthStateListener authStateListener;
private FirebaseUser user;
private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private Uri imageUri;
    private StorageReference storageReference;
    private CollectionReference collectionReference=db.collection("Journal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);
        firebaseAuth=FirebaseAuth.getInstance();
        title=findViewById(R.id.post_title_ed);
        thought=findViewById(R.id.post_description_ed);
        saveButton=findViewById(R.id.post_saveButton);
        Currentuser=findViewById(R.id.post_username_textview);
        date=findViewById(R.id.post_date_textview);
        progressBar=findViewById(R.id.post_progressBar);
        cameraButton=findViewById(R.id.postCameraButton);
        imageView=findViewById(R.id.post_imageView);
        storageReference= FirebaseStorage.getInstance().getReference();
        getSupportActionBar().setElevation(0);

        saveButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        if(JournalApi.getInstance()!=null)
        {
            currentuserId=JournalApi.getInstance().getUserId();
            currentusername=JournalApi.getInstance().getUsername();
        }

        Currentuser.setText(currentusername);
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 user=firebaseAuth.getCurrentUser();
                 if(user !=null)
                 {

                 }
                 else
                 {

                 }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.post_saveButton:
                saveJournal();
                break;

            case R.id.postCameraButton:
                Intent galleryintent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,GALLERY_CODE);
                break;
        }
    }

    private void saveJournal() {
        final String TITLE=title.getText().toString().trim();
        final String THOUGHT=thought.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(TITLE) && !TextUtils.isEmpty(THOUGHT)
           && imageUri!=null)
        {
               final StorageReference filepath=storageReference
                       .child("journal_image")
                       .child("my_image "+ Timestamp.now().getSeconds());

               filepath.putFile(imageUri)
                       .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageURl=uri.toString();
                                    Timestamp date=new Timestamp(new Date());
                                    Journal journal=new Journal(TITLE,THOUGHT, date,currentusername,currentuserId,
                                            imageURl);

                                    collectionReference.add(journal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                             journal.setDocumentId(documentReference.getId());
                                            documentReference.set(journal);
                                            startActivity(new Intent(PostJournalActivity.this,JournalListActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(PostJournalActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Log.d("TAG", "onFailure: "+e.toString());
                    progressBar.setVisibility(View.INVISIBLE);
                   }
               });
        }
        else
        {
              progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK)
        {
            if(data!=null)
            { 
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }
}
