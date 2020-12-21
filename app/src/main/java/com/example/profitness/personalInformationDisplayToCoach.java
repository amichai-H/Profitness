package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profitness.objects.DBshort;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class personalInformationDisplayToCoach extends AppCompatActivity implements View.OnClickListener {
    String userFirstNameString,userLastNameString,dateBorn,phone,emaill;
    String uid;
    TextView mFullName,mAge,mPhone,mSex,email;
    DBshort mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_display_to_coach);
        mFullName = findViewById(R.id.id_full_name);
        mAge = findViewById(R.id.age_id);
        mPhone = findViewById(R.id.phone_id);
        mSex = findViewById(R.id.sex_id);
        email = findViewById(R.id.email_id);
        uid = (String) getIntent().getExtras().get("Uid");
        mPhone.setOnClickListener( this);
        email.setOnClickListener(this);
        mydb = new DBshort();
        takeDataFromDB();
    }

    private void takeDataFromDB() {
        mydb.getUser(uid,this::updateUI);
    }

    private void updateUI(DocumentSnapshot document) {
        long sex;
       // if (document != null) {
            Map<String, Object> data = document.getData();
           // if (data != null) {
        assert data != null;

        userFirstNameString= (String) data.get("first");
                userLastNameString = (String) data.get("last");
                dateBorn = (String) data.get("born");
                phone =  (String) data.get("phone");
                emaill = (String) data.get("email");
                sex = (long) data.get("sex");
                mFullName.setText("Trainee " + userFirstNameString + " " + userLastNameString);
                updateAge(dateBorn);
                mPhone.setText("Phone: " +phone);
                if (sex==0)
                    mSex.setText("Sex: Male");
                else
                    mSex.setText("Sex: female");
                email.setText("Email: "+emaill);
    }

    private void updateAge(String dateBorn) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = sdf.parse(dateBorn);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int date = c.get(Calendar.DATE);
            mAge.setText("Age: " + getAge(year,month,date));

        }
        catch (Exception e){
            System.out.println("problem at AGE");
        }
    }
    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    @Override
    public void onClick(View v) {
        if (v == email){
            String[] adress = new String[1];
            adress[0] = emaill;
            composeEmail(adress,"Hello from your coach");
        }
        else if (v == mPhone) {
            callPhoneNumber();
        }

    }
    public void callPhoneNumber()
    {
        try
        {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                ActivityCompat.requestPermissions(personalInformationDisplayToCoach.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                return;
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);

        }
        catch (Exception ex)
        {
            Toast.makeText(personalInformationDisplayToCoach.this, "Permission not Granted",
                    Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
            else
            {
                Toast.makeText(personalInformationDisplayToCoach.this, "Permission not Granted",
                        Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Permission not Granted");
            }
        }
    }
    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(intent);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
    }
}