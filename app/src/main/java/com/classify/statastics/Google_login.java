package com.classify.statastics;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classify.statastics.MainActivity;
import com.classify.statastics.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Google_login extends AppCompatActivity {

    private SignInButton Gbutton;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Main_Activity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mDatabaseUsers;
    DatabaseReference mDatabaseTotalUsers;
    String name;
    String email;
    String photourl;
    String firebase_name;
    String total;
    int total1;
    int flags=0;

    TextView text;
    ImageView logo;
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseUsers.keepSynced(true);
        mDatabaseTotalUsers = FirebaseDatabase.getInstance().getReference().child("total_users");
        mDatabaseTotalUsers.keepSynced(true);

        text = findViewById(R.id.textview);
        rl = findViewById(R.id.homeView);
        logo = findViewById(R.id.logo);
//        rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Google_login.this,MainActivity.class);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Google_login.this, Pair.create(view,view.getTransitionName()),Pair.create((View)logo,logo.getTransitionName()));
////                ActivityOptionsCompat options1 = ActivityOptionsCompat.makeSceneTransitionAnimation(Google_login.this, logo, "logo");
//                startActivity(i, options.toBundle());
//
//            }
//        });

        Gbutton = (SignInButton) findViewById(R.id.SignIn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    name = firebaseAuth.getCurrentUser().getDisplayName();
                    email = firebaseAuth.getCurrentUser().getEmail();
                    photourl = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
                    mDatabaseTotalUsers.child("no").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            total =  (String) dataSnapshot.getValue();
                            total1 = Integer.parseInt(total);
                            Query query1 = mDatabaseUsers.orderByChild("email").equalTo(email);
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        firebase_name = (String) postSnapshot.child("name").getValue();
                                    }
                                    if(flags==0) {
                                        if (firebase_name == null) {
                                            total1++;
                                            WriteData();
                                            flags++;
                                        } else {
                                            Intent i = new Intent(Google_login.this, MainActivity.class);
                                            i.putExtra("no", total1+"");
                                            i.putExtra("email",email);
                                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Google_login.this, Pair.create((View)rl,rl.getTransitionName()),Pair.create((View)logo,logo.getTransitionName()));
                                            startActivity(i, options.toBundle());
                                            finish();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        };

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Google_login.this,"ERROR In Connection", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        Gbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    public void WriteData() {
        mDatabaseTotalUsers.child("no").setValue(total1+"");
        mDatabaseUsers.child(total1 + "").child("email").setValue(email);
        mDatabaseUsers.child(total1 + "").child("name").setValue(name);
        mDatabaseUsers.child(total1 + "").child("photourl").setValue(photourl);
        mDatabaseUsers.child(total1 + "").child("no").setValue(total1+"");

        Intent intent = new Intent(Google_login.this,CategoryOfUser.class);
        intent.putExtra("no",total1+"");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Google_login.this, Pair.create((View)rl,rl.getTransitionName()),Pair.create((View)logo,logo.getTransitionName()));
        startActivity(intent, options.toBundle());
        Google_login.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else {
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Google_login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}

