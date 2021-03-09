package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

public class CreateMarketingComment extends AppCompatActivity {

    //reference https://github.com/bikashthapa01/firebase-authentication-android
    //refernce https://www.youtube.com/watch?v=pAhYEy6s9wQ
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView fullName;
    String userId;


    private EditText mTitle2, mDesc2;
    private Button mSaveBtn2, mShowBtn2;
    private FirebaseFirestore db;


    //declaring variable toolbar typ Toolbar
    private Toolbar toolbar;

    //creating a variable so i can assign the snapshot and assigning in the savebtn
    String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_marketing_comment);


        mTitle2 = findViewById(R.id.edit_title2);
        mDesc2 = findViewById(R.id.edit_desc2);
        mSaveBtn2 = findViewById(R.id.btnSaveMarketingComment);
        mShowBtn2 = findViewById(R.id.btnViewCommentMarketing2);

        db = FirebaseFirestore.getInstance();


        //reference https://www.youtube.com/watch?v=pAhYEy6s9wQ
        //used to get id
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.textView11);


        userId = fAuth.getCurrentUser().getUid();


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){

                    fullName.setText(documentSnapshot.getString("fName"));

                    fullname = documentSnapshot.getString("fName");

                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });



        mShowBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateMarketingComment.this , ViewMarketingComment.class));
            }
        });

        mSaveBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mTitle2.getText().toString();
                String desc = mDesc2.getText().toString();
                String id =  fullname ;
                Log.d("checking id", id);

                saveToFireStore(id, title, desc);

            }
        });




        //linking variable toolbar to the toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);


        //start of bottom nav
        // declaring the bottom nav https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.btm_home);
        //bringing user to selected activity, reference https://suragch.medium.com/how-to-add-a-bottom-navigation-bar-in-android-958ed728ef6c
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btm_home:
                        Intent startMainClassIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(startMainClassIntent);
                        break;
                    case R.id.btm_dashboard:
                        Intent startCommunityIntent = new Intent(getApplicationContext(), CommunitySection.class);
                        startActivity(startCommunityIntent);
                        break;
                    case R.id.btm_maps:
                        Intent startClassIntent = new Intent(getApplicationContext(), ClassFinder.class);
                        startActivity(startClassIntent);
                        break;
                }
                return true;
            }
        });
        //end of bottom nav

    }



    //reference https://drive.google.com/file/d/1LTxVNNs-fnD4tIADfqVllZztUFZe3KNl/view
    //reference https://www.youtube.com/watch?v=4huqTmJ1Ung&t=2s
    private void saveToFireStore(String id, String title, String desc) {

        if (!title.isEmpty() && !desc.isEmpty()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("title", title);
            map.put("desc", desc);

            db.collection("Marketing Comments").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CreateMarketingComment.this, "Data Saved !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateMarketingComment.this, "Failed !!", Toast.LENGTH_SHORT).show();
                }
            });

        } else
            Toast.makeText(this, "Empty Fields not Allowed", Toast.LENGTH_SHORT).show();

    }


    //using inflater to show the items in the menu (toolbar)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }


    // tutorial 2 mobile
    //https://www.youtube.com/watch?v=Pmsd2x-Bksk
    //using intents to to allow items in the menu to complete a task
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())

        {

            case R.id.dropdown1:
                Intent startIntent1 = new Intent(getApplicationContext(), Jobs.class);
                startActivity(startIntent1);
                Toast.makeText(this, "test2", Toast.LENGTH_SHORT).show();
                break;
            //reference https://www.youtube.com/watch?v=TwHmrZxiPA8
            //time 18.00  signs user out when they log out
            case R.id.dropdown2:
                FirebaseAuth.getInstance().signOut(); //logout the current user
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
                Toast.makeText(this, "test2", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}