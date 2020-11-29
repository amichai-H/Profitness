package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = (Button)findViewById(R.id.btn_login);
        mCreateBtn = (TextView)findViewById(R.id.createText);
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        mLoginBtn.setOnClickListener(this);
        mCreateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mLoginBtn){
            mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("login", "signInWithEmail:success");
                                user = mAuth.getCurrentUser();
                                nextActivity();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("failLogin", "signInWithEmail:failure", task.getException());
                                //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                       // Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
        else if(v == mCreateBtn){

            Intent intent=new Intent(this,register.class);
            startActivity(intent);
        }

    }

    private void nextActivity() {
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    long num = document.getLong("trainer");
                    if (num == 0)
                        openTrainer();
                    else
                        openCoach();
                    if (document.exists()) {
                        Log.d("readData", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("notFound", "No such document");
                    }
                } else {
                    Log.d("NotConnected", "get failed with ", task.getException());
                }
            }
        });
    }

    private void openCoach() {
        Intent intent=new Intent(this,CoachListActivity.class);
        startActivity(intent);
    }


    private void openTrainer() {
        Intent intent=new Intent(this,trainee_main_activity.class);
        startActivity(intent);

    }
}