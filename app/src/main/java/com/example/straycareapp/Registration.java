package com.example.straycareapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.firebase.FirebaseException;
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

        LinearLayout senderNameLayout=findViewById(R.id.layout1);
        LinearLayout phoneNumberLayout=findViewById(R.id.layout2);
        LinearLayout enterLayout=findViewById(R.id.layout3);

        /** EditTexts*/
        AutoCompleteTextView animalType = findViewById(R.id.animalType);
        EditText description = findViewById(R.id.descriptionText);
        EditText address = findViewById(R.id.address);
        EditText city = findViewById(R.id.city);
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

        /** Hiding Views to unhide them during specific condition*/
        animalImage.setVisibility(View.GONE);
        sendOTP.setVisibility(View.GONE);
        senderNameLayout.setVisibility(View.INVISIBLE);
        phoneNumberLayout.setVisibility(View.INVISIBLE);
        enterLayout.setVisibility(View.INVISIBLE);
        validateOTP.setVisibility(View.GONE);
        finalSubmit.setVisibility(View.GONE);



        /** Button to Send OTP */
        sendOTP.setOnClickListener(v -> {
            if(TextUtils.isEmpty(senderName.getText().toString())){
                senderName.setError("Name Can't be Empty");
            }else if(phoneNumber.getText().toString().trim().length()!=10){
                phoneNumber.setError("Input Valid Phone number");
            }else {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+91"+phoneNumber.getText().toString().trim())       // Phone number to verify
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
                                        OTPByFirebase = s;
                                        super.onCodeSent(s, forceResendingToken);
                                        Toast.makeText(getApplicationContext(), "OTP Sent", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

        });

        /** verify OTP Btn*/
        validateOTP.setOnClickListener(v -> {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTPByFirebase, enterOTPBox.getText().toString());

            FirebaseAuth.getInstance().signInWithCredential(credential)

                    .addOnCompleteListener(task -> Toast.makeText(getApplicationContext(), "Sign In", Toast.LENGTH_LONG).show())

                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Sign In Fail", Toast.LENGTH_LONG).show());
        });

        /** Button to Capture Image*/
        captureImage.setOnClickListener(v -> {
                if (TextUtils.isEmpty(animalType.getText().toString().trim())) {
                    animalType.setError("Field can't be Empty");
                } else if (TextUtils.isEmpty(description.getText().toString().trim())) {
                    description.setError("Field can't be Empty");
                } else if (TextUtils.isEmpty(address.getText().toString().trim())) {
                    address.setError("Field can't be Empty");
                }else if (TextUtils.isEmpty(city.getText().toString().trim())) {
                    city.setError("Field can't be Empty");
                }
                else {
                String fileName = "animalPhoto";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                    currentPhotoPath = imageFile.getAbsolutePath();

                    imageUri = FileProvider.getUriForFile(this, "com.example.straycareapp.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    animalImage.setVisibility(View.VISIBLE);
                    startActivityForResult(intent, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Button to send data to the Database
         */
        finalSubmit.setOnClickListener(v -> {

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            animalImage.setImageURI(imageUri);
//            Bitmap bitmapImage = BitmapFactory.decodeFile(currentPhotoPath);
//            imageUri = Util.compress(this, bitmapImage);// reducing the size of image

        }
    }

}