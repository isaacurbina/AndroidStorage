package com.mobileappsco.training.day4;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    Button button1;
    Button button2;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String FILE_NAME = "mytextfile.txt";
    static final int READ_BLOCK_SIZE = 100;
    DBHelper dbHelper;
    SQLiteDatabase db;

    EditText etFirstName;
    EditText etLastName;
    EditText etCountry;
    EditText etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        settings = getSharedPreferences("Preferences", MODE_PRIVATE);
        editor = settings.edit();

        dbHelper = new DBHelper(this);
        dbHelper.getWritableDatabase();
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etCountry = (EditText) findViewById(R.id.etCountry);
        etDate = (EditText) findViewById(R.id.etDate);

        db = dbHelper.getReadableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Read from SharedPreferences
        SharedPreferences settings = getSharedPreferences("Preferences", MODE_PRIVATE);
        editText1.setText(settings.getString("mytxt", ""));
        Log.d("MYAPP", "Reading editText1 = > " + editText2.getText().toString() + " from SharedPreferences");

        // Read from file
        // Create a new file input stream.
        try {
            FileInputStream fIn = openFileInput(FILE_NAME);
            InputStreamReader InputRead= new InputStreamReader(fIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            editText2.setText(s);
            Log.d("MYAPP", "Reading editText2 = > "+editText2.getText().toString()+" from file");
        } catch (Exception e) {
            Log.e("MYAPP", "Error reading editText2 from file >> "+e.getMessage());
        }
    }

    public void closeAppButton(View view) {
        // Saving to SharedPreferences
        editor.putString("mytxt", editText1.getText().toString());
        editor.commit();
        Log.d("MYAPP", "Storing editText1 = > "+editText1.getText().toString());

        // Saving to file
        try {
            // Create a new output file stream
            FileOutputStream fOut = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
            outputWriter.write(editText2.getText().toString());
            outputWriter.close();
            Log.d("MYAPP", "Storing editText2 = > " + editText2.getText().toString()+" in file");
        } catch (Exception e) {
            Log.e("MYAPP", "Error storing editText2 in file >> "+e.getMessage());
        }

        // Save to Database
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_FIRSTNAME, etFirstName.getText().toString());
        values.put(DBHelper.COLUMN_LASTNAME, etLastName.getText().toString());
        values.put(DBHelper.COLUMN_COUNTRY, etCountry.getText().toString());
        values.put(DBHelper.COLUMN_DATE, etDate.getText().toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DBHelper.TABLE_NAME,
                "Contacts",
                values);
        Log.d("MYAPP", "Storing dbHelper = > " + values.toString());
        finish();
    }

    public void readDBButton(View view) {
        String[] cols = new String[] {DBHelper.COLUMN_FIRSTNAME,
                                    DBHelper.COLUMN_LASTNAME,
                                    DBHelper.COLUMN_COUNTRY,
                                    DBHelper.COLUMN_DATE};
        Cursor mCursor = db.query(true,
                                    DBHelper.TABLE_NAME,
                                    cols,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        try {
            etFirstName.setText(mCursor.getString(0));
            etLastName.setText(mCursor.getString(1));
            etCountry.setText(mCursor.getString(2));
            etDate.setText(mCursor.getString(3));
        }
        catch (Exception e) {}
    }
}
