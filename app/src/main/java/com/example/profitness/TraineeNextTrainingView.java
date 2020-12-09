package com.example.profitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TraineeNextTrainingView extends AppCompatActivity {



    String uid;

    List<String> allTrainings;
    List<String> prevTrainings;
    List<String> nextTrainings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_next_training_view);


        uid = (String) getIntent().getExtras().get("Uid");
        allTrainings = new ArrayList<>();
        prevTrainings = new ArrayList<>();
        nextTrainings = new ArrayList<>();



        //getTraineeTrainings
        //splitTo2Lists
        //offer what to show (history or the next trainings)
            //if(history)
                //showTrainingHistory();
            //else
                //showNextTrainings


    }

    private void getTraineeTrainings() {


    }
    private void splitTo2Lists() {


    }

    private void showTrainingHistory() {


    }
    private void showNextTrainings() {


    }
}