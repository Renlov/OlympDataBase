package com.example.olimpdb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

public class AddMember extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDIT_MEMBER_LOADER = 111;
    Uri currentMemberUri;
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

        Intent intent = getIntent();
        currentMemberUri = intent.getData();

        if(currentMemberUri == null){
            setTitle("Add a member");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the member");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER, null, this);
        }

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(currentMemberUri == null){
            MenuItem menuItem = menu.findItem(R.id.delete_member);
            menuItem.setVisible(false);
        }
        return true;
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
                saveMember();
                return true;
            case R.id.delete_member:
                showDeleteMember();
                return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void saveMember(){

        String name = firstName.getText().toString().trim();
        String last = lastName.getText().toString().trim();
        String sport = sportEditText.getText().toString().trim();

        //Проверка на валидность
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Input name", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(last)){
            Toast.makeText(this, "Input last name", Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(sport)){
            Toast.makeText(this, "Input sport", Toast.LENGTH_LONG).show();
            return;
        } else if(gender == MemberEntry.GENDER_UNKNOWN){
            Toast.makeText(this, "Choose gender", Toast.LENGTH_LONG).show();
            return;
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put(MemberEntry.KEY_FIRST_NAME, name);
        contentValues.put(MemberEntry.KEY_LAST_NAME, last);
        contentValues.put(MemberEntry.KEY_SPORT, sport);
        contentValues.put(MemberEntry.KEY_FIRST_NAME, name);
        contentValues.put(MemberEntry.KEY_GENDER, gender);

        if(currentMemberUri == null){
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI, contentValues);
            if(uri == null){
                Toast.makeText(this, "Insertion of data the table failed for", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsChanged = getContentResolver().update(currentMemberUri, contentValues,
                    null, null);
            if(rowsChanged == 0){
                Toast.makeText(this, "Saving of data in the table failed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Member updated", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                MemberEntry._ID,
                MemberEntry.KEY_FIRST_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_GENDER,
                MemberEntry.KEY_SPORT
        };

        return new CursorLoader(this,
                currentMemberUri, projection,
                null,null,null);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        if(data.moveToFirst()){
            int firstNameColumnIndex = data.getColumnIndex(
                    MemberEntry.KEY_FIRST_NAME
            );
            int lastNameColumnIndex = data.getColumnIndex(
                    MemberEntry.KEY_LAST_NAME
            );
            int genderColumnIndex = data.getColumnIndex(
                    MemberEntry.KEY_GENDER
            );
            int sportColumnIndex = data.getColumnIndex(
                    MemberEntry.KEY_SPORT
            );

            String firstNameEdit = data.getString(firstNameColumnIndex);
            String lastNameEdit = data.getString(lastNameColumnIndex);
            int genderEdit = data.getInt(genderColumnIndex);
            String sportEdit = data.getString(sportColumnIndex);

            firstName.setText(firstNameEdit);
            lastName.setText(lastNameEdit);
            sportEditText.setText(sportEdit);


            switch (genderEdit){
                case  MemberEntry.GENDER_MALE:
                    genderSpinner.setSelection(1);
                    break;
                case MemberEntry.GENDER_FEMALE:
                    genderSpinner.setSelection(2);
                    break;
                case MemberEntry.GENDER_UNKNOWN:
                    genderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
  private void showDeleteMember(){
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage("Do you want delete the member?");
      builder.setPositiveButton("DELETE",
              new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      deleteMember();
                  }
              });
      builder.setNegativeButton("CANCEL",
              new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      if(dialog != null){
                          dialog.dismiss();
                      }
                  }
              });
      AlertDialog alertDialog = builder.create();
      alertDialog.show();
  }
  private void deleteMember(){
        if(currentMemberUri != null) {
            int rowsDeleted = getContentResolver().delete(currentMemberUri,
                    null, null);
            if(rowsDeleted == 0){
                Toast.makeText(this, "Deleting of data in the table failed", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Member is deleted", Toast.LENGTH_LONG).show();
            }
            finish();
        }

  }
}