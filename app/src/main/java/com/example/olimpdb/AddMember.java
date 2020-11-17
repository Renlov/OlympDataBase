package com.example.olimpdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.olimpdb.Data.ClubOlympusContract.MemberEntry;

import java.util.ArrayList;

public class AddMember extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText sportEditText;
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
        sportEditText = findViewById(R.id.sportEditText);
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
                insertMember();
                return true;
            case R.id.delete_member:
                return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void insertMember(){
        String name = firstName.getText().toString().trim();
        String last = lastName.getText().toString().trim();
        String sport = sportEditText.getText().toString().trim();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MemberEntry.KEY_FIRST_NAME, name);
        contentValues.put(MemberEntry.KEY_LAST_NAME, last);
        contentValues.put(MemberEntry.KEY_SPORT, sport);
        contentValues.put(MemberEntry.KEY_FIRST_NAME, name);
        contentValues.put(MemberEntry.KEY_GENDER, gender);

        ContentResolver contentResolver = getContentResolver();
        Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI, contentValues);
        if(uri == null){
            Toast.makeText(this, "Insertion of data the table failed for", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
        }
    }
}