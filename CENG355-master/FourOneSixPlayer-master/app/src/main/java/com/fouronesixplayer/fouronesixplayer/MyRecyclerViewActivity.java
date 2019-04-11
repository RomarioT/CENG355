package com.fouronesixplayer.fouronesixplayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;


import com.firebase.client.Firebase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyRecyclerViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recycler_view);

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Uploads");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String fileName1=dataSnapshot.getKey();//returns file name
                String url=dataSnapshot.getValue(String.class);//returns url for file name

                ((MyAdapter)recyclerView.getAdapter()).update(fileName1,url);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView=findViewById(R.id.recyclerView);
        //custom adapter
        //populates the recylcer view with items
        recyclerView.setLayoutManager(new LinearLayoutManager(MyRecyclerViewActivity.this));
        MyAdapter myAdapter= new MyAdapter(recyclerView,MyRecyclerViewActivity.this,new ArrayList<String>(),new ArrayList<String>());
        recyclerView.setAdapter(myAdapter);



    }
}

