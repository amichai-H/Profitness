package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.profitness.objects.DBshort;
import com.example.profitness.objects.MyUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import static com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID;


public class SelfCoachScreen extends AppCompatActivity implements View.OnClickListener {
    Button goToList,goToSchedTimeToWork,scheduledTraining;
    TextView helloCoach;
    FirebaseUser user;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    DBshort mybd;
    MyUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_coach_screen);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mybd = new DBshort();
        user = mAuth.getCurrentUser();

        myUser = new MyUser();

        helloCoach = findViewById(R.id.helloCa);
        goToSchedTimeToWork = findViewById(R.id.timeTT);
        goToList = findViewById(R.id.listOfT);
        scheduledTraining = findViewById(R.id.Scheduled_training_id);

        goToList.setOnClickListener(this);
        goToSchedTimeToWork.setOnClickListener(this);
        scheduledTraining.setOnClickListener(this);
        mybd.getUser(user.getUid(),(doc)->{
            myUser.init(doc);
            setUserName();
        });
        sendTest();


    }

    private void sendTest() {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
                .setMessageId(Integer.toString(0))
                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
                .build());
        System.out.println("messages send");
    }

    @Override
    public void onClick(View v) {
        if (v == goToList){
            Intent intent=new Intent(this,CoachListActivity.class);
            startActivity(intent);
        }
        else if(v== goToSchedTimeToWork){
            Intent intent=new Intent(this,SchedAvailability.class);
            startActivity(intent);

        }
        else if(v==scheduledTraining){
            Intent intent=new Intent(this,CoachSeeAllComingTreining.class);
            startActivity(intent);
        }
    }
    private void setUserName() {
            helloCoach.setText("Hello Coach " + myUser.getFirstName() + " " + myUser.getLastName());
    }
}