package com.bookwallah.asawant.libraryapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ManualEntryActivity extends AppCompatActivity {
    TextView errorMessage;
    EditText bookName;
    EditText bookAuthors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        bookName = (EditText) findViewById(R.id.bookName);
        bookAuthors = (EditText) findViewById(R.id.bookAuthors);
    }


    @Override
    protected void onStart() {
        super.onStart();
        errorMessage.setText("Book details not found! Please enter the book details:");
        bookName.setHint("Book Name");
        bookAuthors.setHint("Book Author(s)");
    }

    public void captureBookDetails() {

    }
}
