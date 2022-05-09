package com.example.straycareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Registration extends AppCompatActivity {

    String[] genderArr={"Male","Female","Unknown"};
    String[] conditionArr={"Normal","Mild","Severe"};

    /***/
    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseStorage storage;
    StorageReference imageStorageReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Objects.requireNonNull(getSupportActionBar()).hide();

        /** connecting to firebase different services*/
        db = FirebaseFirestore.getInstance(); // connection to Firestore
        collectionReference = db.collection("Requests");
        storage = FirebaseStorage.getInstance();
        imageStorageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();

        /**Buttons*/
        Button captureImage =findViewById(R.id.imageCaptureBtn);
        Button sendOTP =findViewById(R.id.sendOTPBtn);
        Button validateOTP =findViewById(R.id.validateOTPBtn);
        Button finalSubmit =findViewById(R.id.SubmitBtn);

        /** EditTexts*/
        AutoCompleteTextView animalType=findViewById(R.id.animalType);
        EditText description=findViewById(R.id.descriptionText);
        EditText address=findViewById(R.id.address);
        EditText senderName=findViewById(R.id.senderName);
        EditText phoneNumber=findViewById(R.id.phoneNumber);
        EditText enterOTPBox=findViewById(R.id.enterOTPBox);

        /** Imageview*/
        ImageView imageView=findViewById(R.id.AnimalImage);

        /**Spinners*/
        Spinner gender=findViewById(R.id.genderSpinner);
        Spinner condition=findViewById(R.id.conditionSpinner);

        /** Initiating and Setting adapters to Spinners*/
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, genderArr);
        gender.setAdapter(genderAdapter);

        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, conditionArr);
        condition.setAdapter(conditionAdapter);


        /** sending OTP using Phone number*/
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}