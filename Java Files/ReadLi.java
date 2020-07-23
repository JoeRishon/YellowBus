package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadLi extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<UserInformation> list;
    AdapterView adapterView;
    View view;
    MyAdapter adapter;
    Spinner spinner;
    String destlat;
    String destlon;
    private android.widget.Button Button;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        firebaseAuth =FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        recyclerView =(RecyclerView)findViewById(R.id.myRe);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        reference= FirebaseDatabase.getInstance().getReference("drivers/");
        list=new ArrayList<UserInformation>();



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list =new ArrayList<UserInformation>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String id=dataSnapshot1.child("id").getValue().toString();
                    String la=dataSnapshot1.child("lat").getValue().toString();
                    String ln=dataSnapshot1.child("lon").getValue().toString();
                    String size=dataSnapshot1.child("size").getValue().toString();

                    UserInformation p=new UserInformation();
                    p.setLat(la);
                    p.setLong(ln);
                    p.setSize(size);
                    p.setId(id);
                    list.add(p);
                }
                adapter = new MyAdapter(ReadLi.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReadLi.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void logout(View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }


}
