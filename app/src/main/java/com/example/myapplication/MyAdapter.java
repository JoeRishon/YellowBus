package com.example.myapplication;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Float.parseFloat;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private FirebaseAuth firebaseAuth;
    Context context;
    ArrayList<UserInformation> users;

    public MyAdapter(Context c,ArrayList<UserInformation> p){
        context=c;
        users=p;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.lat.setText(users.get(position).getLat());
        holder.lon.setText(users.get(position).getLon());
        holder.size.setText(users.get(position).getsize());
        holder.route.setText("ROUTE - "+(position+1));
        holder.btn.setVisibility(View.VISIBLE);
        holder.onClick(position);
        holder.btn2.setVisibility(View.VISIBLE);
        holder.onClick(position);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener {
        TextView lat,lon,size,route;
        String destlat,destlon;
        Button btn;
        Button btn2;

        public MyViewHolder(View itemView){
            super(itemView);

            lat=(TextView)itemView.findViewById(R.id.textView1);
            lon=(TextView)itemView.findViewById(R.id.textView2);
            btn = (Button) itemView.findViewById(R.id.btnmap);
            btn2=(Button)itemView.findViewById(R.id.btnbook);
            size = itemView.findViewById(R.id.textView3);
            route = itemView.findViewById(R.id.textView18);

            Spinner spinner = itemView.findViewById(R.id.spinner1);
            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(context,R.array.location,android.R.layout.simple_spinner_dropdown_item);

            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter1);

            spinner.setOnItemSelectedListener(this);
        }
        public void onClick(final int position)
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, position+" is clicked", Toast.LENGTH_SHORT).show();
                    String uri=("https://maps.google.com/maps?saddr="+parseFloat(users.get(position).getLat())+","+parseFloat(users.get(position).getLon())+"&daddr="+destlat+","+destlon);
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    context.startActivity(intent);
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    String student= firebaseAuth.getCurrentUser().getUid();
                    final DatabaseReference ref1=FirebaseDatabase.getInstance().getReference().child("students").child(student);
                    ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                //user exists, do something
                                Toast.makeText(context,"Already booked seats",Toast.LENGTH_LONG).show();

                            }
                            else{

                                //user does not exist, do something else

                                final String uid = users.get(position).getId();


                                //Toast.makeText(BookActivity.this,"login Successfully",Toast.LENGTH_LONG).show();
                                final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("drivers").child(uid);

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (snapshot.getValue() != null) {
                                            //user exists, do something

                                            String size= String.valueOf(snapshot.child("size").getValue());
                                            int size1 = Integer.parseInt(size);
                                            if(size1>0)
                                            {
                                                Toast.makeText(context,"one seat reserved choose one convenient seat",Toast.LENGTH_SHORT).show();
                                                ref1.child("booked").setValue(1);
                                                ref1.child("driver").setValue(uid);
                                                size1=size1-1;
                                                ref.child("size").setValue(size1);
                                                Intent intent=new Intent(Intent.ACTION_VIEW);
                                                intent.setClassName("com.example.myapplication","com.example.myapplication.BookActivity");
                                                context.startActivity(intent);
                                            }
                                            else
                                                Toast.makeText(context,"SEATS FULL",Toast.LENGTH_LONG).show();


                                            //ref.child("lon").setValue(lon);
                                            //Toast.makeText(location.this,"Information updated",Toast.LENGTH_LONG).show();




                                        }  //user does not exist, do something else
                                        //ref.child("lat").setValue(lat);
                                        //ref.child("lon").setValue(lon);
                                        //ref.child("size").setValue("45");
                                        //Toast.makeText(location.this,"IT IS NEW TRIP CLICK SAVE BUTTON..",Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }


                                });
                            }
                            //ref.child("lat").setValue(lat);
                            //ref.child("lon").setValue(lon);
                            //ref.child("size").setValue("45");
                            //Toast.makeText(location.this,"IT IS NEW TRIP CLICK SAVE BUTTON..",Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });

                    //finish();



                                //Toast.makeText(this,"login Successfully",Toast.LENGTH_LONG).show();
                }
            });
        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int text = (int) parent.getItemIdAtPosition(position);
            if(text==0)
            {
                destlat="12.922978";
                destlon="77.670350";
            }
            if(text==1)
            {
                destlat="12.920506";
                destlon="77.665768";
            }
            if(text==2)
            {
                destlat="12.918930";
                destlon="77.669067";
            }
            if(text==3)
            {
                destlat="12.978390";
                destlon="77.640670";
            }
            if(text==4)
            {
                destlat="12.967199";
                destlon="77.661436";
            }

            Toast.makeText(parent.getContext(),"lat"+destlat+" lon"+destlon, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
