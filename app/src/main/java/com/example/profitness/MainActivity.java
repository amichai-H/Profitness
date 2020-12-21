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
import android.widget.Toast;

import com.example.profitness.functionsInterface.TaskToRun;
import com.example.profitness.objects.DBshort;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DBshort mydb;
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
        mydb = new DBshort();
    }

    public TaskToRun nextActivi = (documentSnapshot) -> {
        try {
            long num = documentSnapshot.getLong("trainer");
            if (num == 0)
                openTrainer();
            else
                openCoach();
        } catch (Exception e){
            System.out.println(e.fillInStackTrace());
        }

    };

    @Override
    public void onClick(View v) {
        if(v == mLoginBtn){
            if ( mEmail.getText().toString().equals("")){
                Toast.makeText(MainActivity.this, "Please enter User Name",
                        Toast.LENGTH_SHORT).show();
            }
           else if (mPassword.getText().toString().equals("")){
                Toast.makeText(MainActivity.this, "Please enter Password",
                        Toast.LENGTH_SHORT).show();
            }
           else {
                mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("login", "signInWithEmail:success");
                                    user = mAuth.getCurrentUser();
                                    mydb.getUser(user.getUid(), nextActivi);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("failLogin", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        }
        else if(v == mCreateBtn){

            Intent intent=new Intent(this,register.class);
            startActivity(intent);
        }

    }

    private void openCoach() {
        Intent intent=new Intent(this,SelfCoachScreen.class);
        startActivity(intent);
    }


    private void openTrainer() {
        Intent intent=new Intent(this,trainee_main_activity.class);
        startActivity(intent);

    }
}