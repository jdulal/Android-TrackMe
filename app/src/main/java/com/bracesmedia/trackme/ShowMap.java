package com.bracesmedia.trackme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;

public class ShowMap extends AppCompatActivity {

    Button showMap;
    String value=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference  myRef= database.getReference("Location");

        showMap=(Button) findViewById(R.id.btnShowMap);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value=dataSnapshot.getValue(String.class);
                TextView textView=(TextView) findViewById(R.id.txtMyPos);
                textView.setText(value);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShowMap.this, ShowMapPosition.class);
                intent.putExtra("LOCATION", value);
                startActivity(intent);
            }
        });
    }
}
