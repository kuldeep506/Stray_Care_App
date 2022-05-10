package com.example.straycareapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Registration extends AppCompatActivity {

    EditText enterOTPBox;
    ImageView animalImage;
    String[] genderArr = {"Male", "Female", "Unknown"};
    String[] conditionArr = {"Normal", "Mild", "Severe"};
    /***/
    FirebaseFirestore db;
    CollectionReference collectionReference;
    FirebaseStorage storage;
    StorageReference imageStorageReference;
    FirebaseAuth auth;
    /**
     * Private Useful fileds
     */
    private Uri imageUri;
    private String currentPhotoPath;
    private String OTPByFirebase;

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
        Button captureImage = findViewById(R.id.imageCaptureBtn);
        Button sendOTP = findViewById(R.id.sendOTPBtn);
        Button validateOTP = findViewById(R.id.validateOTPBtn);
        Button finalSubmit = findViewById(R.id.SubmitBtn);

        /** EditTexts*/
        AutoCompleteTextView animalType = findViewById(R.id.animalType);
        EditText description = findViewById(R.id.descriptionText);
        EditText address = findViewById(R.id.address);
        EditText senderName = findViewById(R.id.senderName);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        enterOTPBox = findViewById(R.id.enterOTPBox);

        /** Imageview*/
        animalImage = findViewById(R.id.AnimalImage);

        /**Spinners*/
        Spinner gender = findViewById(R.id.genderSpinner);
        Spinner condition = findViewById(R.id.conditionSpinner);

        /** Initiating and Setting adapters to Spinners*/
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, genderArr);
        gender.setAdapter(genderAdapter);

        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, conditionArr);
        condition.setAdapter(conditionAdapter);


        /** Button to Send OTP */
        sendOTP.setOnClickListener(v -> {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber.getText().toString().trim())       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                    Toast.makeText(getApplicationContext(), "OTP Verified", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                    Toast.makeText(getApplicationContext(), "OTP Failed" + e.getMessage(), Toast.LENGTH_LONG).show();

                                }

                                @Override
                                public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    OTPByFirebase=s;
                                    super.onCodeSent(s, forceResendingToken);
                                    Toast.makeText(getApplicationContext(), "OTP Sent", Toast.LENGTH_LONG).show();
                                }
                            })
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);

        });

        /** verify OTP Btn*/
        validateOTP.setOnClickListener(v -> {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTPByFirebase, enterOTPBox.getText().toString());

            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    Toast.makeText(getApplicationContext(), "Sign In", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Sign In Fail", Toast.LENGTH_LONG).show();
                }
            });
        });

        /** Button to Capture Image*/
        captureImage.setOnClickListener(v -> {

//            if (name.getText().toString().isEmpty() || course.getText().toString().isEmpty() ||
//                    phone.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {
//                String stName = name.getText().toString().trim();
//                String phNO = phone.getText().toString().trim();
//                String StStop = stop.getText().toString().trim();
//                String stCourse = course.getText().toString().trim();
//                String RegistrationDate = date.getText().toString().trim();
//                Toast.makeText(getApplicationContext(), "Fill Above Details", Toast.LENGTH_LONG).show();
//                if (TextUtils.isEmpty(stName)) {
//                    name.setError("Name can't be Empty");
//                } else if (TextUtils.isEmpty(phNO)) {
//                    phone.setError("Phone can't be Empty");
//                } else if (TextUtils.isEmpty(StStop)) {
//                    stop.setError("Stop can't be Empty");
//                } else if (TextUtils.isEmpty(stCourse)) {
//                    course.setError("course can't be Empty");
//                } else if (TextUtils.isEmpty(RegistrationDate)) {
//                    date.setError("Date can't be Empty");
//                }
//            } else {
//                save.setVisibility(View.VISIBLE);
            String fileName = "animalPhoto";
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                currentPhotoPath = imageFile.getAbsolutePath();

                imageUri = FileProvider.getUriForFile(this, "com.example.straycareapp.fileprovider", imageFile);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            animalImage.setImageURI(imageUri);
            Bitmap bitmapImage = BitmapFactory.decodeFile(currentPhotoPath);
//            imageUri = Util.compress(this, bitmapImage);// reducing the size of image

        }
    }

}