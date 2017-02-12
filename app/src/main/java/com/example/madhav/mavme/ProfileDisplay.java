package com.example.madhav.mavme;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by madhav on 11/18/16.
 */
public class ProfileDisplay extends BaseActivity {

    private User u;
    private Context context;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_display);
        this.context = this;

        u = DBMgr.getUserByID(HomeDisplay.userID);

        loadMenu(3);
        loadProfile();
        addListenerOnButtons();
    }

    public void loadProfile() {

        // getting widgets
        Spinner gender = (Spinner) findViewById(R.id.spinnerGender);
        Spinner major = (Spinner) findViewById(R.id.spinnerMajor);
        Spinner degree = (Spinner) findViewById(R.id.spinnerDegree);
        Spinner gradYear = (Spinner) findViewById(R.id.spinnerGradYear);
        Spinner residence = (Spinner) findViewById(R.id.spinnerResidence);
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editDOB = (EditText) findViewById(R.id.editDOB);
        EditText editEmail = (EditText) findViewById(R.id.editEmail);
        EditText editPhone = (EditText) findViewById(R.id.editPhone);
        CheckBox privacy = (CheckBox) findViewById(R.id.privacy);

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


        // populating profile
        editName.setText(u.getName());
        editDOB.setText(u.getDOB());
        editEmail.setText(u.getEmail());
        editPhone.setText(u.getPhoneNumber());

        if (u.getIsMale()) {
            gender.setSelection(0);
        } else {
            gender.setSelection(1);
        }

        major.setSelection(u.getMajorIndex());

        if (u.getDegree().equals("Bachelors")) {
            degree.setSelection(0);
        } else if (u.getDegree().equals("Masters")) {
            degree.setSelection(1);
        } else if (u.getDegree().equals("Doctorate")) {
            degree.setSelection(2);
        } else {
            degree.setSelection(3);
        }

        int gradYearIndex;
        if (u.getGradYear().equals("Faculty")) {
            gradYearIndex = 10;
        } else {
            gradYearIndex = Integer.parseInt(u.getGradYear()) - thisYear;
        }
        gradYear.setSelection(gradYearIndex);

        if (u.getOnCampus()) {
            residence.setSelection(0);
        } else {
            residence.setSelection(1);
        }

        privacy.setChecked(u.getIsPrivate());

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

        final TextView changePW = (TextView) findViewById(R.id.changePassword);
        final GridLayout passwordView = (GridLayout) findViewById(R.id.passwordView);
        final EditText editPassword1 = (EditText) findViewById(R.id.editPassword1);
        final EditText editPassword2 = (EditText) findViewById(R.id.editPassword2);
        final TextView saveChanges = (TextView) findViewById(R.id.save);
        final TextView exit = (TextView) findViewById(R.id.exit);


        changePW.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (passwordView.getVisibility() == View.VISIBLE) {
                    passwordView.setVisibility(View.GONE);
                    changePW.setTextColor(Color.parseColor("#555555"));
                } else {
                    passwordView.setVisibility(View.VISIBLE);
                    changePW.setTextColor(Color.parseColor("#ffa500"));
                }
            }
        });


        saveChanges.setOnClickListener(new View.OnClickListener() {
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

                if (passwordView.getVisibility() == View.VISIBLE) {
                    if (editPassword1.getText().toString().length() < 8) {
                        errorMessage = "Error: New password length must be at least 8 characters";
                    } else if (!editPassword1.getText().toString().equals(editPassword2.getText().toString())) {
                        errorMessage = "Error: Passwords do not match";
                    }
                } else if (!editEmail.getText().toString().substring(editEmail.getText().toString().length() - 7).equals("uta.edu")) {
                    errorMessage = "Error: Email must end in 'uta.edu'";
                } else if (editName.getText().toString().length() == 0) {
                    errorMessage = "Error: Name cannot be blank";
                }

                if (errorMessage.length() == 0) {
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
                    loadProfile();
                    Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Utils.alertDialog(context, errorMessage, new Utils.dialogHandler() {
                        @Override
                        public void onButtonClick(boolean click) {
                        }
                    });
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utils.confirmationDialog(context, "Exit without saving?",
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
