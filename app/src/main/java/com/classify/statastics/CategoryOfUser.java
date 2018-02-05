package com.classify.statastics;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CategoryOfUser extends AppCompatActivity {

    ImageButton relative,doctor,nurse;
    DatabaseReference mDatabaseCategory;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String no,name,email,photourl;
    RelativeLayout rl;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_of_user);
        rl = findViewById(R.id.homeView);
        logo = findViewById(R.id.logo);
        no = getIntent().getExtras().getString("no");
        relative = (ImageButton)findViewById(R.id.img_relative);
        doctor = (ImageButton)findViewById(R.id.img_doc);
        nurse = (ImageButton)findViewById(R.id.img_nurse);
        mDatabaseCategory = FirebaseDatabase.getInstance().getReference().child("Category");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    name = firebaseAuth.getCurrentUser().getDisplayName();
                    email = firebaseAuth.getCurrentUser().getEmail();
                    photourl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();

                }
            }
        };

        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseCategory.child(no).child("email").setValue(email);
                mDatabaseCategory.child(no).child("no").setValue(no);
                mDatabaseCategory.child(no).child("category").setValue("Relative");
                mDatabaseCategory.child(no).child("total_patients").setValue("0");
                Intent intent = new Intent(CategoryOfUser.this,MainActivity.class);
                intent.putExtra("no",no);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryOfUser.this, Pair.create((View)rl,rl.getTransitionName()));
                startActivity(intent, options.toBundle());
                finish();
            }
        });

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseCategory.child(no).child("email").setValue(email);
                mDatabaseCategory.child(no).child("no").setValue(no);
                mDatabaseCategory.child(no).child("category").setValue("Doctor");
                mDatabaseCategory.child(no).child("total_patients").setValue("0");
                Intent intent = new Intent(CategoryOfUser.this,MainActivity.class);
                intent.putExtra("no",no);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryOfUser.this, Pair.create((View)rl,rl.getTransitionName()));
                startActivity(intent, options.toBundle());
                finish();
            }
        });

        nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseCategory.child(no).child("email").setValue(email);
                mDatabaseCategory.child(no).child("no").setValue(no);
                mDatabaseCategory.child(no).child("category").setValue("Nurse");
                mDatabaseCategory.child(no).child("total_patients").setValue("0");
                Intent intent = new Intent(CategoryOfUser.this,MainActivity.class);
                intent.putExtra("no",no);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryOfUser.this, Pair.create((View)rl,rl.getTransitionName()));
                startActivity(intent, options.toBundle());
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
