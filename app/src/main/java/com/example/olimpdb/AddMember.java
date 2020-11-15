package com.example.olimpdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.olimpdb.Data.ClubOlympusContract.MemberEntry;

import java.util.ArrayList;

public class AddMember extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText group;
    private Spinner genderSpinner;
    private int gender = 0;
    private ArrayAdapter spinnerAdapter;
    private ArrayList spinnerArrayList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        group = findViewById(R.id.group);
        genderSpinner = findViewById(R.id.spinner);

        spinnerArrayList = new ArrayList<>();
        spinnerArrayList.add("Unknown");
        spinnerArrayList.add("Male");
        spinnerArrayList.add("Female");

        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArrayList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(spinnerAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = (String)parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selectedGender)) {
                    if(selectedGender.equals("Male")) gender = MemberEntry.GENDER_MALE;
                    else if(selectedGender.equals("Female")) gender = MemberEntry.GENDER_FEMALE;
                    else gender = MemberEntry.GENDER_UNKNOWN;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = 0;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_addmember, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_member:
                return true;
            case R.id.delete_member:
                return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}