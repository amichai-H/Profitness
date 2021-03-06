package com.example.profitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.profitness.objects.DBshort;
import com.example.profitness.objects.MyUser;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class register extends AppCompatActivity implements View.OnClickListener {
    FirebaseFirestore db;
    DBshort mydb;
    MyUser myUser;
    String dateString;
    EditText mLastName, mFirstNAme, mEmail, mPassword, mPhone;
    TextView mDayOfBirth;
    RadioGroup mSex;
    RadioGroup mtrain;
    Button mRegisterBtn;
    Button pick_btn;
    FirebaseAuth mAuth;
    List<String> availableCoachesIdList;
    List<String> availableCoachesNameList;
    Spinner coachesSpinner;
    String coachId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /* set variables */
        db = FirebaseFirestore.getInstance();
        mydb = new DBshort();
        System.out.println(db);
        mLastName = findViewById(R.id.lastName);
        mFirstNAme = findViewById(R.id.firstName);
        mPhone = findViewById(R.id.phoneNumber);
        mDayOfBirth = findViewById(R.id.dateofbirth);
        mSex = findViewById(R.id.sexgender);
        mtrain = findViewById(R.id.trainRadio);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();
        pick_btn = findViewById(R.id.choosdate);
        pick_btn.setOnClickListener(this);
        coachesSpinner = findViewById(R.id.coachesSpinner);
        availableCoachesIdList = new ArrayList();
        availableCoachesNameList = new ArrayList<>();
        mRegisterBtn.setOnClickListener(this);
        coachId = "";


        RadioButton radioSexButton = (RadioButton) findViewById(mSex.getCheckedRadioButtonId());
        RadioButton radioTrainButton = (RadioButton) findViewById(mtrain.getCheckedRadioButtonId());

        /* spinner*/
        setCoachesList();
        coachesSpinner.setVisibility(View.VISIBLE);
/* need to do if the radio button of trainee is selected, to show the coaches list, else hide it */
        /* the code below does not work */
//        if (radioTrainButton.getTag().toString().equals("0"))
//            coachesSpinner.setVisibility(View.VISIBLE);
//
//        else
//            coachesSpinner.setVisibility(View.INVISIBLE);



    }


    @Override
    public void onClick(View v) {
        if (v == pick_btn) {
            handleDateButton();
        }
        if (v == mRegisterBtn) {
            //
            // test5();
            if (chackData()) {
                CreateUser();
                mAuth.createUserWithEmailAndPassword(myUser.getEmail(), myUser.getPassword())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("create user", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user!=null)
                                        updateUser(user);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("fail create", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(register.this, "Authentication failed.",
                                          Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });

            } else {
                Toast.makeText(register.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void CreateUser() {
        RadioButton radioSexButton = (RadioButton) findViewById(mSex.getCheckedRadioButtonId());
        RadioButton radioTrainButton = (RadioButton) findViewById(mtrain.getCheckedRadioButtonId());
        int sex = 2;
        if (radioSexButton.getTag().toString().equals("0"))
            sex = 0;
        else
            sex = 1;
        int train = 0;
        if (radioTrainButton.getTag().toString().equals("0"))
            train = 0;
        else
            train = 1;
        myUser = new MyUser(mFirstNAme.getText().toString()
                , mLastName.getText().toString(), mEmail.getText().toString()
                , mPassword.getText().toString(), mPhone.getText().toString(), mDayOfBirth.getText().toString(), sex, train,coachId);
    }

    private boolean chackData() {
        //RadioButton radioSexButton = (RadioButton) findViewById(mSex.getCheckedRadioButtonId());
        boolean select =  true;
        System.out.println(select);
        boolean stringNotNullOrBlank = (!mLastName.getText().toString().equals("")) &&
                (!mFirstNAme.getText().toString().equals("") )&&
                (!mEmail.getText().toString().equals("")) &&
                (!mPhone.getText().toString().equals("") )&&
                (!mPassword.getText().toString().equals(""));
        return select&&stringNotNullOrBlank;
    }


    void updateUser(FirebaseUser user) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(mLastName.getText().toString())
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();
        assert user != null;
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("updateUser", "User profile updated.");
                        }
                    }
                });
        Map<String, Object> userDB = new HashMap<>();
        userDB.put("first", myUser.getFirstName());
        userDB.put("last", myUser.getLastName());
        userDB.put("born", myUser.getBirthDAy());
        userDB.put("sex", myUser.getGsex());
        userDB.put("trainer", myUser.getTraining());
        userDB.put("phone", myUser.getPhone());
        userDB.put("email", myUser.getEmail());
        if (myUser.isTrainee()) {
            userDB.put("coach", myUser.getCoach());
        }
        else {
            userDB.put("coach", "");
        }
        if (myUser.getTraining()==1){
            mydb.insertDoc("coaches/"+user.getUid(),userDB, ()->{Toast.makeText(this, "Hello new coach", Toast.LENGTH_SHORT).show();});

        }
// Add a new document with a generated ID
            mydb.insertDocToUser(user.getUid(),userDB,()->{
                Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();

                finish();
            });


    }

    private void handleDateButton() {
        //Toast.makeText(this, "handleDateButton", Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH)+1;
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                month++;
                dateString = date + "/" + month + "/" + year;
                mDayOfBirth.setText(dateString);

            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    private void setCoachesList(){
        mydb.getCollection("coaches", (doc)->{

            availableCoachesIdList.add(doc.getId());
            availableCoachesNameList.add(doc.get("first") + " " + doc.get("last"));

        }, this::coachesSpinnerInit );


    }

    private void coachesSpinnerInit(){//create the date spinner using the dates inside availableDatesList (need to reload the dates from the db first)

        ArrayAdapter aa = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                availableCoachesNameList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coachesSpinner.setAdapter(aa);
        coachesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                coachId = availableCoachesIdList.get(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                coachId = null;

            }
        });
    }
}