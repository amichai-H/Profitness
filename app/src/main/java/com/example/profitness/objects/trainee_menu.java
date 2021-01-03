package com.example.profitness.objects;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.profitness.Calander;
import com.example.profitness.MainActivity;
import com.example.profitness.R;
import com.example.profitness.WorkoutHistoryLayout;
import com.example.profitness.trainee_main_activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class trainee_menu extends AppCompatActivity {

    public static FirebaseUser user;
    public static FirebaseFirestore db;
    public static FirebaseAuth mAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trainee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                logout();
                return true;
            case R.id.home:
                if(this.getClass().getSimpleName().compareTo(trainee_main_activity.class.getSimpleName()) != 0) {
                    Intent trainee_main_activity = new Intent(this, trainee_main_activity.class);
                    trainee_main_activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.startActivity(trainee_main_activity);
                }
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    public void logout() {

        mAuth.signOut();
        Intent homeActivity = new Intent(this, MainActivity.class);
        homeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(homeActivity);
        Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();

    }

}
