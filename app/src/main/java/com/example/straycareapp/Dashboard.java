package com.example.straycareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Objects.requireNonNull(getSupportActionBar())
                .setBackgroundDrawable(getDrawable(R.color.taskbar));



        /** Assigning variables with input fields Ids*/
        Button registration =findViewById(R.id.registrationBtn);




        /** registration button to open registration page*/
        registration.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(),Registration.class);
            startActivity(intent);
        });

    }

    /**methods to show menu in action*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.menuAdmin:
                Toast.makeText(getApplicationContext(),"Admin pressed",Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }
}