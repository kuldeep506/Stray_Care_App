package com.example.straycareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class Registration extends AppCompatActivity {
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

        Button captureImage =findViewById(R.id.imageCaptureBtn);
        Button sendOTP =findViewById(R.id.sendOTPBtn);
        Button validateOTP =findViewById(R.id.validateOTPBtn);
        Button finalSubmit =findViewById(R.id.SubmitBtn);

    }
}