package com.classify.statastics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PatientInfo extends AppCompatActivity {

    String patient_no,blood,breath,pulse,temp,name;
    DatabaseReference mDatabasePatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        patient_no = getIntent().getExtras().getString("patient_no");
        mDatabasePatient = FirebaseDatabase.getInstance().getReference().child("Patient");

        Query q = mDatabasePatient.orderByChild("patient_no").equalTo(patient_no);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    name = (String)snapshot.child("name").getValue();
                    blood = (String)snapshot.child("blood").getValue();
                    breath = (String)snapshot.child("breath").getValue();
                    pulse = (String)snapshot.child("pulse").getValue();
                    temp = (String)snapshot.child("temp").getValue();
                    Log.d("outer",name + " ");
                    Log.d("outer",blood + " ");
                    Log.d("outer",breath + " ");
                    Log.d("outer",pulse + " ");
                    Log.d("outer",temp + " ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
