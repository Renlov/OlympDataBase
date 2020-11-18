package com.example.olimpdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.olimpdb.Data.ClubOlympusContract.MemberEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    TextView dataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataTextView = findViewById(R.id.dataTextView);

        FloatingActionButton floatingActionButton =
                findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMember.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayData();
    }

    private void displayData(){
        String[] projection = {
                MemberEntry.KEY_ID,
                MemberEntry.KEY_FIRST_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_GENDER,
                MemberEntry.KEY_SPORT
        };
        Cursor cursor = getContentResolver().query(
                MemberEntry.CONTENT_URI,
                projection,
                null, null, null
        );

        dataTextView.setText("All members\n\n");
        dataTextView.append(MemberEntry.KEY_ID + " " +
                MemberEntry.KEY_FIRST_NAME + " " +
                MemberEntry.KEY_LAST_NAME + " " +
                MemberEntry.KEY_GENDER + " " +
                MemberEntry.KEY_SPORT);

        int idIndex = cursor.getColumnIndex(MemberEntry.KEY_ID);
        int firstNameIndex = cursor.getColumnIndex(MemberEntry.KEY_FIRST_NAME);
        int lastNameIndex = cursor.getColumnIndex(MemberEntry.KEY_LAST_NAME);
        int genderIndex = cursor.getColumnIndex(MemberEntry.KEY_GENDER);
        int sportIndex = cursor.getColumnIndex(MemberEntry.KEY_SPORT);

        while (cursor.moveToNext()){
            int currentId = cursor.getInt(idIndex);
            String currentFirstName = cursor.getString(firstNameIndex);
            String currentLastName = cursor.getString(lastNameIndex);
            int currentGender = cursor.getInt(genderIndex);
            String currentSport = cursor.getString(sportIndex);

            dataTextView.append("\n" +
                    currentId + " " +
                    currentFirstName + " " +
                    currentLastName + " " +
                    currentGender + " " +
                    currentSport);
        }
            cursor.close();
    }
}