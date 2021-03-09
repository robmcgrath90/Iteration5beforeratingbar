package com.example.fyp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoomSelected extends AppCompatActivity {


    private Toolbar toolbar;


    //reference https://www.youtube.com/watch?v=wVCz1a3ogqk
    //****
    //chat app code
    private String user_name;
    private String room_name;
    private DatabaseReference root;
    private String temp_Key;


    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_converstation;
    //end of chat app code




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_selected);

        toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);





        btn_send_msg = (Button) findViewById(R.id.btn_send);
        input_msg = (EditText) findViewById(R.id.msg_input);
        chat_converstation = (TextView) findViewById(R.id.textView3);

        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle(" Room - "+room_name );


        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //reference https://www.youtube.com/watch?v=wVCz1a3ogqk
                //****
                //chat app code

                Map<String, Object> map = new HashMap<String, Object>();
                temp_Key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_Key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name", user_name);
                map2.put("msg", input_msg.getText().toString());

                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                append_chat_converstation(snapshot);
            }



            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //end of chat app code




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


    //reference https://www.youtube.com/watch?v=wVCz1a3ogqk
    //****
    //chat app code

    private String chat_msg, chat_user_name;
    private void append_chat_converstation(DataSnapshot snapshot) {
        Iterator i = snapshot.getChildren().iterator();

        while (i.hasNext()){
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            chat_converstation.append(chat_user_name +" : "+ chat_msg + " \n");
        }
    }
    //end of chat app code


}