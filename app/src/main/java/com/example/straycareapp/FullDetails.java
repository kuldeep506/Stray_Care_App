package com.example.straycareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;

public class FullDetails extends AppCompatActivity {
    private static final String LOGIN_INFO = "username";
    // getting fireStore firebase database instances
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Requests");

    DetailModel detailModel;

    private TextView type;
    private TextView gender;
    private TextView condition;
    private TextView Description;

    private TextView address;
    private TextView city;
    private TextView name;
    private TextView phone;

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        type = findViewById(R.id.fulDetailsID);
        name = findViewById(R.id.fullDetailsName);
        phone = findViewById(R.id.fullDetailsPhone);
        Description = findViewById(R.id.fullDetailsStop);

        address = findViewById(R.id.fullDetailsCourse);
        city = findViewById(R.id.fullDetaillsYear);
        name = findViewById(R.id.fullDetailsDate);

        phone = findViewById(R.id.fullDetailsDues);

        image = findViewById(R.id.fullDetailsImage);
        Button clearDues = findViewById(R.id.fullDetailsClearDues);

        // retrieving ID from list cardView adapter and setting value to the full list page
        String url = getIntent().getStringExtra("imageurl");

        // getting user email from shared preferences
        SharedPreferences getSharedEmail=getSharedPreferences(LOGIN_INFO,MODE_PRIVATE);
        String userEmail=getSharedEmail.getString("username","Enter Email");



        /** setting all fields of a students using his ID */
        collectionReference.whereEqualTo("imageUri", url).get().addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot student : task.getResult()) {

                    detailModel = student.toObject(DetailModel.class);
                }
                //setting values to full details editTexts
                type.setText(detailModel.getAnimalType());
                name.setText(detailModel.getSenderName());
                phone.setText(detailModel.getSenderName());
                Description.setText(detailModel.getDescription());
                address.setText(detailModel.getAddress());
                city.setText(detailModel.getCity());
                phone.setText(String.valueOf(detailModel.getPhoneNumber()));
//                if (phone.getText().toString().equals("1700")) {
//                    clearDues.setVisibility(View.INVISIBLE);
//                }
                Picasso.get().load(detailModel.getImageUri()).fit()
                        .placeholder(R.drawable.ic_baseline_image_24).into(image);
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(), "Failed To Fetch value at Moment", Toast.LENGTH_LONG)
                        .show());
    }

    @Override
    public void onBackPressed() {
        Intent intent =new Intent(getApplicationContext(),RequestList.class);
        startActivity(intent);
//        super.onBackPressed();
    }
}