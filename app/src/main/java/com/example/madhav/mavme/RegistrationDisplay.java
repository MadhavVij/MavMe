package com.example.madhav.mavme;


/**
 * Created by Madhav on 10/6/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class RegistrationDisplay extends AppCompatActivity {

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_display);
        context = this;

        loadForm();
        addListenerOnButtons();
    }

    public void loadForm() {
        // getting widgets
        Spinner gender = (Spinner) findViewById(R.id.spinnerGender);
        Spinner major = (Spinner) findViewById(R.id.spinnerMajor);
        Spinner degree = (Spinner) findViewById(R.id.spinnerDegree);
        Spinner gradYear = (Spinner) findViewById(R.id.spinnerGradYear);
        Spinner residence = (Spinner) findViewById(R.id.spinnerResidence);


        // populating spinners
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                new ArrayList<String>(Arrays.asList("Male", "Female")));
        gender.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                DBMgr.majors);
        major.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                new ArrayList<String>(Arrays.asList("Bachelors", "Masters", "Doctorate", "Faculty")));
        degree.setAdapter(arrayAdapter);

        ArrayList<String> gradYears = new ArrayList<String>();
        int thisYear = new Date().getYear() + 1900;
        for (int i = thisYear; i < thisYear + 10; i++) {
            gradYears.add("" + i);
        }
        gradYears.add("Faculty");
        arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                gradYears);
        gradYear.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item,
                new ArrayList<String>(Arrays.asList("On Campus", "Off Campus")));
        residence.setAdapter(arrayAdapter);
    }


    public void addListenerOnButtons() {
        // getting widgets
        final Spinner gender = (Spinner) findViewById(R.id.spinnerGender);
        final Spinner major = (Spinner) findViewById(R.id.spinnerMajor);
        final Spinner degree = (Spinner) findViewById(R.id.spinnerDegree);
        final Spinner gradYear = (Spinner) findViewById(R.id.spinnerGradYear);
        final Spinner residence = (Spinner) findViewById(R.id.spinnerResidence);
        final EditText editName = (EditText) findViewById(R.id.editName);
        final EditText editDOB = (EditText) findViewById(R.id.editDOB);
        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editPhone = (EditText) findViewById(R.id.editPhone);
        final CheckBox privacy = (CheckBox) findViewById(R.id.privacy);

        final EditText editPassword1 = (EditText) findViewById(R.id.editPassword1);
        final EditText editPassword2 = (EditText) findViewById(R.id.editPassword2);
        final TextView register = (TextView) findViewById(R.id.register);
        final TextView exit = (TextView) findViewById(R.id.exit);


        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String errorMessage = "";
                String dateString = editDOB.getText().toString();
                Date dob = new Date();


                // changing DOB field to Date object
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                try {
                    dob = df.parse(dateString);
                } catch (ParseException e) {
                    errorMessage = "Error: Please enter a valid date of birth";
                }

                User u = DBMgr.getUserByEmail(editEmail.getText().toString());

                if (u != null) {
                    errorMessage = "Error: Email is already taken.";
                } else if (editPassword1.getText().toString().length() < 8) {
                    errorMessage = "Error: New password length must be at least 8 characters";
                } else if (!editPassword1.getText().toString().equals(editPassword2.getText().toString())) {
                    errorMessage = "Error: Passwords do not match";
                } else if (!editEmail.getText().toString().substring(editEmail.getText().toString().length() - 7).equals("uta.edu")) {
                    errorMessage = "Error: Email must end in 'uta.edu'";
                } else if (editName.getText().toString().length() == 0) {
                    errorMessage = "Error: Name cannot be blank";
                }

                if (errorMessage.length() == 0) {
                    u = new User();
                    u.setName(editName.getText().toString());
                    u.setIsMale(gender.getSelectedItem().toString().equals("Male"));
                    u.setMajor(major.getSelectedItem().toString());
                    u.setDegree(degree.getSelectedItem().toString());
                    u.setGradYear(gradYear.getSelectedItem().toString());
                    u.setOnCampus(residence.getSelectedItem().toString().equals("On Campus"));
                    u.setDOB(dob);
                    u.setEmail(editEmail.getText().toString());
                    u.setPhoneNumber(editPhone.getText().toString());
                    u.setPassword(editPassword1.getText().toString());
                    u.setIsPrivate(privacy.isChecked());

                    u = User.newUser(u.getName(), u.getPassword(), u.getEmail(), u.getIsMale(), u.getRawDOB(), u.getMajor(),
                            u.getDegree(), u.getGradYear(), u.getRawPhoneNumber(), u.getOnCampus(), u.getIsPrivate());
                    HomeDisplay.userID = u.getUserID();

                    Utils.alertDialog(context, "Registration complete!", new Utils.dialogHandler() {
                        @Override
                        public void onButtonClick(boolean click) {
                            Intent i = new Intent("com.example.madhav.mavme.HomeDisplay");
                            startActivity(i);
                        }
                    });


                } else {
                    Utils.alertDialog(context, errorMessage, new Utils.dialogHandler() {
                        public void onButtonClick(boolean click) {
                        }
                    });
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.confirmationDialog(context, "Exit without registering?",
                        new Utils.dialogHandler() {
                            @Override
                            public void onButtonClick(boolean click) {
                                if (click) finish();
                            }
                        });
            }
        });

    }


}
