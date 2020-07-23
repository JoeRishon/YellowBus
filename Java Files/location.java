package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class location extends AppCompatActivity implements LocationListener {

    public  static final int RequestPermissionCode  = 1 ;
    Button buttonEnable, buttonGet, buttonOpen ;
    TextView textViewLongitude, textViewLatitude ;
    Context context;
    Intent intent1 ;
    Location location;
    LocationManager locationManager ;
    boolean GpsStatus = false ;
    Criteria criteria ;
    String Holder;
    String coordinatesHolder ;
    float tempLong , tempLati ;
    private FirebaseAuth firebaseAuth;
    private EditText editTextlat;
    private EditText editTextlon;
    private Button buttonLogout;
    private DatabaseReference databaseReference;
    private Button buttonSave;
    private Button btn;

    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        EnableRuntimePermission();

        firebaseAuth =FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        databaseReference= FirebaseDatabase.getInstance().getReference();



        buttonEnable = (Button)findViewById(R.id.button);
        buttonGet = (Button)findViewById(R.id.button2);
        buttonOpen = (Button)findViewById(R.id.button3);
        btn=(Button)findViewById(R.id.button6);
        buttonOpen.setEnabled(false);
        buttonLogout = findViewById(R.id.buttonLogout);
        textViewLongitude = (TextView)findViewById(R.id.textView);
        textViewLatitude = (TextView)findViewById(R.id.textView2);
        editTextlat=findViewById(R.id.editText2);
        editTextlon=findViewById(R.id.editText3);
        buttonSave = findViewById(R.id.buttonSave);

        //btn.setVisibility(View.INVISIBLE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        Holder = locationManager.getBestProvider(criteria, false);

        context = getApplicationContext();

        CheckGpsStatus();

        buttonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);


            }
        });

        /*buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v == buttonLogout){
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(this,LoginActivity.class));
                }

            }
        });*/

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonOpen.setEnabled(true);

                CheckGpsStatus();

                if(GpsStatus == true) {
                    if (Holder != null) {
                        if (ActivityCompat.checkSelfPermission(
                                location.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                &&
                                ActivityCompat.checkSelfPermission(location.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(Holder);
                        locationManager.requestLocationUpdates(Holder, 10000, 0, location.this);
                    }
                    Toast.makeText(location.this,"Accessing your location please wait",Toast.LENGTH_LONG).show();
                }else {

                    Toast.makeText(location.this, "Please Enable GPS First", Toast.LENGTH_LONG).show();

                }
            }
        });

        buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                coordinatesHolder = String.format(Locale.ENGLISH, "geo:%f,%f", tempLati, tempLong);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(coordinatesHolder));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                final String useruid=user.getUid();
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("drivers").child(useruid);
                DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("students");
                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            if(snapshot.child("driver").getValue().toString().equals(useruid))
                                snapshot.getRef().removeValue();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                ref.removeValue();
               // btn.setVisibility(View.INVISIBLE);
                Toast.makeText(location.this,"Bus has reached college", Toast.LENGTH_LONG).show();


            }
        });

    }

    @Override
    public void onLocationChanged(final Location location) {

        editTextlon.setText(""+location.getLongitude());
        editTextlat.setText("" + location.getLatitude());

        tempLong = (float)location.getLongitude() ;
        tempLati = (float)location.getLatitude() ;

        final String lat = editTextlat.getText().toString().trim();
        final String lon = editTextlon.getText().toString().trim();

        reference= FirebaseDatabase.getInstance().getReference("drivers/");
        //String size = "45";

        //Toast.makeText(this,""+lat,Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,""+lon,Toast.LENGTH_SHORT).show();
        //UserInformation userInformation = new UserInformation(lat,lon,size);

        //FirebaseUser user = firebaseAuth.getCurrentUser();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String useruid=user.getUid();
        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("drivers").child(useruid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    //user exists, do something
                    ref.child("lat").setValue(lat);
                    ref.child("lon").setValue(lon);
                    Toast.makeText(location.this,"Information updated",Toast.LENGTH_LONG).show();


                } else {
                    //user does not exist, do something else
                    //ref.child("lat").setValue(lat);
                    //ref.child("lon").setValue(lon);
                    //ref.child("size").setValue("45");
                    Toast.makeText(location.this,"IT IS NEW TRIP CLICK SAVE BUTTON..",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
       // Toast.makeText(this,"Information updated...",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(location.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            Toast.makeText(location.this,"ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(location.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(location.this,"Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(location.this,"Permission Canceled, Now your application cannot access GPS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public void logout(View view) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void save(View view) {

        String lat = editTextlat.getText().toString().trim();
        String lon = editTextlon.getText().toString().trim();
        String size = "45";
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String User = ""+user.getUid();
        //Toast.makeText(this,""+lat,Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,""+lon,Toast.LENGTH_SHORT).show();
        UserInformation userInformation = new UserInformation(lat,lon,size,User);

        //FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child("drivers/"+user.getUid()).setValue(userInformation);

        //btn.setVisibility(View.VISIBLE);

        Toast.makeText(this,"Information saved...",Toast.LENGTH_SHORT).show();
    }

}


