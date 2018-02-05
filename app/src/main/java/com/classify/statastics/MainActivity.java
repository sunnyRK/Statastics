package com.classify.statastics;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    private FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabaseCategory,mDatabasePatient;
    FirebaseUser user;
    String no,total_patients,total_pat;
    int total_patients_int,total_pat_int;
    RecyclerView patient_list;
    final ArrayList<String> patient_arraylist =new ArrayList<>();
    final ArrayList<String> patient_name_arraylist =new ArrayList<>();
    String email,patients,category;
    RelativeLayout toolbar;
    patient_adapter adap;
    int flag=0,repeat_control_flag=0,counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adap = new patient_adapter(MainActivity.this,patient_arraylist);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseCategory = FirebaseDatabase.getInstance().getReference().child("Category");
        mDatabasePatient = FirebaseDatabase.getInstance().getReference().child("Patient");
        try{
            firebaseDatabase.setPersistenceEnabled(true);
        }catch (Exception e){}

        toolbar = findViewById(R.id.toolbarsdfs);
        patient_list = (RecyclerView) findViewById(R.id.home_recycle);
        setUpRecyclerView(patient_list);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, Google_login.class));
                } else {
                    email = firebaseAuth.getCurrentUser().getEmail();

                    Query query1 = mDatabaseCategory.orderByChild("email").equalTo(email);
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                total_pat = (String)snapshot.child("total_patients").getValue();
                                total_pat_int = Integer.parseInt(total_pat);
                                Log.d("firebase1",total_pat_int+"");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    Query query = mDatabaseCategory.orderByChild("email").equalTo(email);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                no = (String)snapshot.child("no").getValue();
                                patients = (String)snapshot.child("total_patients").getValue();
                                category = (String)snapshot.child("category").getValue();
                                Log.d("firebase",email+"");
                                Log.d("firebase",patients+"");
                                Log.d("firebase",no+"");
                                if(no!=null)
                                {
                                    addToCard();
                                }

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        };


        //patient_list.setLayoutManager(new LinearLayoutManager(patient_list.getContext()));
        //patient_list.setAdapter(new patient_adapter());

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabs);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                final EditText input = new EditText(MainActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                alertDialogBuilder.setView(input);
                alertDialogBuilder.setMessage("Add patient");
                alertDialogBuilder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                String patient_no = input.getText().toString();
                                int checkDuplicate =0;
                                for(String patient : patient_arraylist)
                                {
                                    if(patient_no.equals(patient))
                                    {
                                        Toast.makeText(MainActivity.this,"You have already added this patient",Toast.LENGTH_SHORT).show();
                                        checkDuplicate=1;
                                        break;
                                    }
                                }
                                if(checkDuplicate==0)
                                {
                                    addPatientInfo(patient_no);
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        patient_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    Log.d("Scroll",dy+"");
                } else {
                    // Scrolling down
                }
            }
        });

        /*Animation */

    }
    private void addPatientInfo(final String patient_no) {
        final int[] flag = {0};
        final int[] flag2 = {0};

        Query q = mDatabasePatient.orderByChild("patient_no").equalTo(patient_no);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String pname = null;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    pname = (String)snapshot.child("patient_no").getValue();
                }
                if(flag2[0]==0)
                {
                    if(pname != null)
                    {
                        Query query = mDatabaseCategory.orderByChild("no").equalTo(no);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    total_patients = (String) postSnapshot.child("total_patients").getValue();
                                    total_patients_int = Integer.parseInt(total_patients);
                                    total_patients_int++;
                                    if(flag[0] ==0)
                                    {
                                        mDatabaseCategory.child(no).child("PatientList").child(total_patients_int+"").child("patient_no").setValue(patient_no);
                                        if(category.equals("Relative"))
                                        {
                                            mDatabaseCategory.child(no).child("PatientList").child(total_patients_int+"").child("status").setValue("Requested");
                                        }
                                        else
                                        {
                                            mDatabaseCategory.child(no).child("PatientList").child(total_patients_int+"").child("status").setValue("granted");
                                        }
                                        mDatabaseCategory.child(no).child("total_patients").setValue(total_patients_int+"");
                                        flag[0] =1;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                    else {
                        Toast.makeText(MainActivity.this,"There is no patient like this",Toast.LENGTH_SHORT).show();
                    }
                    flag2[0]=1;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void addToCard()
    {

        final String[] patient_no = new String[1];
        final String[] name = new String[1];

        Query query = mDatabaseCategory.child(no).child("PatientList");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    patient_arraylist.clear();
                    counter=0;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(repeat_control_flag==0) {
                        patient_no[0] = (String) postSnapshot.child("patient_no").getValue();
                        Log.d("pat",patient_no[0]);
                        patient_name_arraylist.add(patient_no[0]);
                        adap.notifyDataSetChanged();
                        fun(patient_no[0]);
                    }
                }
                /*patient_list.setAdapter(new patient_adapter(getApplicationContext(),patient_arraylist));
                repeat_control_flag=1;*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void fun(final String patient)
    {
        final int[] fun_flag = {0};
        Query q = mDatabasePatient.orderByChild("patient_no").equalTo(patient);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if (fun_flag[0] == 0) {
                        String name = (String)snapshot.child("name").getValue();
                        Log.d("name",name);
                        Log.d("name",dataSnapshot.getChildrenCount()+" hey");
                        patient_arraylist.add(name);
                        counter++;
                    }
                    if(counter==total_pat_int)
                    {
                        Log.d("firebase1",patient_arraylist.get(0));
                        patient_list.setAdapter(new patient_adapter(getApplicationContext(),patient_arraylist));
                    }
                }
                fun_flag[0] =1;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

    static {
        System.loadLibrary("native-lib");
    }

    private void setUpRecyclerView(RecyclerView rv)
    {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(adap );
        Log.d("Firebase-data","user adapter");
    }

    class patient_adapter extends RecyclerView.Adapter<patient_adapter.ViewHolder>
    {
        ArrayList<String> al=new ArrayList<>();
        Context context;


        public patient_adapter(Context c, ArrayList<String> al1)
        {
            context=c;
            al = al1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_card, parent, false);
            return new patient_adapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Log.d("firebase",al.get(position)+"");
            holder.patient_name.setText(patient_arraylist.get(position));
            final ValueAnimator anim = ValueAnimator.ofInt(holder.card.getMeasuredHeight(), holder.card.getLayoutParams().height);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = holder.card.getLayoutParams();
                    layoutParams.height = val;
                    holder.card.setLayoutParams(layoutParams);
                }
            });
            anim.setDuration(1000);
            anim.start();
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this,PatientInfo.class);
                    i.putExtra("patient_no",patient_name_arraylist.get(position));
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return al.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            RelativeLayout card;
            TextView patient_name;

            public ViewHolder(View itemView) {
                super(itemView);
                card = itemView.findViewById(R.id.card_patient);
                patient_name = itemView.findViewById(R.id.patient_name);
            }
        }
    }

    public native String stringFromJNI();
}