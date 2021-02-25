package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.adapters.StudyBuddyAdpaterRV;
import com.example.fyp.StudybuddyrvItems;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;




public class StudyBuddyActiveGroups extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_buddy_active_groups);

        //linking variable toolbar to the actual Toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        //associating variable toolbar to the actual Toolbar
        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);


        //used for the recyclerview

        //reference https://www.youtube.com/watch?v=17NbUcEts9c
        ArrayList<StudybuddyrvItems> exampleList = new ArrayList<>();

        exampleList.add(new StudybuddyrvItems(R.drawable.ic_baseline_view_module_24, "Statistics", "Module"));
        exampleList.add(new StudybuddyrvItems(R.drawable.ic_time, "4PM", "Time"));
        exampleList.add(new StudybuddyrvItems(R.drawable.ic_date, "Monday 30th November", "Date"));
        exampleList.add(new StudybuddyrvItems(R.drawable.ic_blank, "", ""));
        exampleList.add(new StudybuddyrvItems(R.drawable.ic_blank, "", ""));
        exampleList.add(new StudybuddyrvItems(R.drawable.ic_baseline_view_module_24, "Accounting", "Module"));
        exampleList.add(new StudybuddyrvItems(R.drawable.ic_time, "6PM", "Time"));
        exampleList.add(new StudybuddyrvItems(R.drawable.ic_date, "Tuesday 1st December", "Date"));



        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new StudyBuddyAdpaterRV(exampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //start of bottom nav
        //Declare bottom nav, and set correct option as selected, adapted from https://stackoverflow.com/questions/40202294/set-selected-item-in-android-bottomnavigationview
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.btm_home);
        //Setting up navigation to correct activity, adapted from https://suragch.medium.com/how-to-add-a-bottom-navigation-bar-in-android-958ed728ef6c
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
    //everything above this is in the on create method









    //*****toolbar******
    //using an infalter to show the menu items in the toolbar
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
    //******toolbar*******


}