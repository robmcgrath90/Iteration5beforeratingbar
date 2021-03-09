package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    //reference https://github.com/bikashthapa01/firebase-authentication-android
    //refernce https://www.youtube.com/watch?v=pAhYEy6s9wQ
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView fullName;
    String userId;




    private Toolbar toolbar;

    private static final String TAG = "MainActivity";
    //Creating a constant int that will send back 9001 if the correct version of google play isnt insalled on the device
    private static final int Error_DIALOG_REQUEST = 9001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating a clcikable help popup to help users
        //https://www.youtube.com/watch?v=Bsm-BlXo2SI
        Button btnInfoOnclassfinder = (Button) findViewById(R.id.btnInfoOnclassfinder);
        btnInfoOnclassfinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        //creating a clcikable help popup to help users
        //https://www.youtube.com/watch?v=Bsm-BlXo2SI
        Button btnInfoOnCommunity = (Button) findViewById(R.id.btnInfoOnCommunityButton);
        btnInfoOnCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCommunity();
            }
        });


        //reference https://www.youtube.com/watch?v=pAhYEy6s9wQ
        //used to get id
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.profileName);


        userId = fAuth.getCurrentUser().getUid();


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){

                    fullName.setText(documentSnapshot.getString("fName"));


                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });



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
                        //activity already active
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


        //toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);






        //initialising buttons
        final Button btnCommunity = (Button) findViewById(R.id.btnCommunity);
        final Button btnClassFinder = (Button) findViewById(R.id.btnClassFinder);
        final Button btnLogOut = (Button) findViewById(R.id.btnLogout);


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Intent intent = new Intent(MainActivity.this,login.class);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(MainActivity.this, "Sign Out Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });





        btnCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), CommunitySection.class);
                startActivity(startIntent);
            }
        });

        btnClassFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), ClassFinder.class);
                startActivity(startIntent);
            }
        });


    }





    // reference https://www.youtube.com/watch?v=M0bYvXlhgSI&list=PLgCYzUzKIBE-vInwQhGSdnbyJ62nixHCt&index=3
    //method for checking the version of google play on the device
    public boolean isServicesOK(){
        Log.d(TAG, "checking google service version ");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "onCreate: google play services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "onCreate: ");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, Error_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "you cant make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }





    //using inflater to show the items in the menu (toolbar)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }



    //toolbar
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

                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
                Toast.makeText(this, "test2", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    //reference https://www.youtube.com/watch?v=Bsm-BlXo2SI
    //for showing info on the class finder
    public void openDialog(){
        DialogForclassFinder dialogForclassFinder = new DialogForclassFinder();
        dialogForclassFinder.show(getSupportFragmentManager(), "Class finder dialog");
    }

    //reference https://www.youtube.com/watch?v=Bsm-BlXo2SI
    //for showing info on the class finder
    public void openDialogCommunity(){
        DialogToCommunitySection dialogToCommunitySection = new DialogToCommunitySection();
        dialogToCommunitySection.show(getSupportFragmentManager(), "Class finder dialog");
    }




}


