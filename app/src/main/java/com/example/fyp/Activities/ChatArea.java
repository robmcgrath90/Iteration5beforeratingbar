package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public class ChatArea extends AppCompatActivity {


    //reference https://www.youtube.com/watch?v=wVCz1a3ogqk
    //****
    private ListView listView;

    private EditText room_name;
    private Button add_room;
    private String name;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    //chatroom code
    //****



    //used to get user ID
    //reference https://github.com/bikashthapa01/firebase-authentication-android
    //refernce https://www.youtube.com/watch?v=pAhYEy6s9wQ
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView fullName;
    String userId;



    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_area);

        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);




        //reference https://www.youtube.com/watch?v=pAhYEy6s9wQ
        //used to get id
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullName = findViewById(R.id.profileName3);
        name =  fullName.getText().toString();


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






        //reference https://www.youtube.com/watch?v=wVCz1a3ogqk
        //chat room code
        //****
        add_room = (Button) findViewById(R.id.btn_add_room);
        room_name = (EditText) findViewById(R.id.room_name_edittext);
        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);

        listView.setAdapter(arrayAdapter);

        request_user_name();

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> map = new HashMap<String, Object>();
                map.put(room_name.getText().toString(), "");
                root.updateChildren(map);

            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = snapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());

                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ChatRoomSelected.class);
                intent.putExtra("room_name", ((TextView)view).getText().toString() );
                intent.putExtra("user_name", name);
                startActivity(intent);
            }
        });
        //chatroom code
        //****








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


    } //end of onCreate method







    //reference https://www.youtube.com/watch?v=wVCz1a3ogqk
    //chat room code
    //****
    private void request_user_name() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name =  fullName.getText().toString();
            }
        });

        builder.show();
    }

    //chatroom code
    //***



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