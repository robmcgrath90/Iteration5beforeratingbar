package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fyp.R;
import com.example.fyp.adapters.ManagementAdapter;
import com.example.fyp.data.model.ModelManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewManagementComments extends AppCompatActivity {

    private Toolbar toolbar;
    //RatingBar ratingBar;

    //reference https://drive.google.com/file/d/1LTxVNNs-fnD4tIADfqVllZztUFZe3KNl/view
    //reference https://www.youtube.com/watch?v=yPJ_5ybLkFo&list=PLhhNsarqV6MQ-eMvAOwjuBUDm7hfsTUta&index=10
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ManagementAdapter adapter;
    private List<ModelManagement> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_management_comments);


        //reference https://drive.google.com/file/d/1LTxVNNs-fnD4tIADfqVllZztUFZe3KNl/view
        //reference https://www.youtube.com/watch?v=4huqTmJ1Ung&t=2s
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db= FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        adapter = new ManagementAdapter(this , list);
        recyclerView.setAdapter(adapter);

        showData();



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


        //  reference for the rating bar https://www.youtube.com/watch?v=O5I9cSW31ho
        //creating a ratingbar variable linking it to the rating bar by the id
        // creating a button to submit the rating
        //ratingBar = findViewById(R.id.ratingBar);
       // final Button btnSubmit = (Button) findViewById(R.id.btnSubmitManStar1);

        //button that gets the rating and uses a toast to display it
       // btnSubmit.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View v) {
        //        String s = String.valueOf(ratingBar.getRating());
         //       Toast.makeText(getApplicationContext(), s+"Star",
         //               Toast.LENGTH_SHORT).show();
       //     }
      //  });



    }

    //reference https://drive.google.com/file/d/1LTxVNNs-fnD4tIADfqVllZztUFZe3KNl/view
    //reference https://www.youtube.com/watch?v=4huqTmJ1Ung&t=2s
    public void showData(){

        db.collection("Management Comments").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        for (DocumentSnapshot snapshot : task.getResult()){

                            ModelManagement modelManagement = new ModelManagement(snapshot.getString("id") , snapshot.getString("title") , snapshot.getString("desc"));
                            list.add(modelManagement);
                            //Commas are bad
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewManagementComments.this, "Oops ... something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
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
                FirebaseAuth.getInstance().signOut(); //logout the current user
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
                Toast.makeText(this, "test2", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}