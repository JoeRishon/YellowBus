package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
//import java.util.logging.Handler;

public class BookActivity extends AppCompatActivity {

    int count = 0;
    int i;
    Button but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        //ImageView array[100];
        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        int childCount = grid.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final ImageView container = (ImageView) grid.getChildAt(i);
            container.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    count++;

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if (count == 1)
                                container.setImageResource(R.mipmap.ic_seats_selected_foreground);
                            else if (count == 2) {
                                container.setImageResource(R.mipmap.ic_seats_book_foreground);

                            } else {
                                count = 0;
                                //container.setImageResource(R.mipmap.ic_seats_selected_foreground);
                            }

                        }
                    }, 50);
                }


            });


        }
        Button buttonOne = findViewById(R.id.buttonReview);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),gpay.class);
                startActivity(intent);
            }
        });


    }
}