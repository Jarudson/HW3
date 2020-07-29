package com.example.hw3;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw3.printers.PrinterListContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPrinter extends AppCompatActivity {
    long date;
    int day;
    int month;
    int year;
    PrinterListContent printerListContent;
    DatePickerDialog picker;
    DatabaseReference databaseReference;
    public static long maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_printer);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Printer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid = snapshot.getChildrenCount();
                }
                else {
                    maxid = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final EditText txt3 = findViewById(R.id.addDate);
        txt3.setInputType(InputType.TYPE_NULL);
        txt3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                day = cldr.get(Calendar.DAY_OF_MONTH);
                month = cldr.get(Calendar.MONTH);
                year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddPrinter.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                                txt3.setText(dayOfMonth + "/" + (month +1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

    }

    boolean checkNumber(String x){
        int counter = 0;
        if(x.length() == 9){
        for(int i = 0; i < x.length(); i++){
                if((x.charAt(i) >= 48) && (x.charAt(i) <= 57)){
                    counter++;
                }
            }
            if(counter == x.length()) return true;
            else return false;
        }
        else return false;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addClick(View view) {

        EditText txt1 = findViewById(R.id.addName);
        EditText txt2 = findViewById(R.id.addSerialNumber);
        EditText txt3 = findViewById(R.id.addDate);
        EditText txt4 = findViewById(R.id.addPhoneNumber);
        EditText txt5 = findViewById(R.id.addDamageDesc);

        String name = txt1.getText().toString();
        String sn = txt2.getText().toString();
        String phoneNumber = txt4.getText().toString();
        String damage = txt5.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        date = calendar.getTimeInMillis();
        Date date_cal = new Date(date);
        String date_acc = simpleDateFormat.format(date);
        txt3.setText("Date: " + date_acc);



        if(name.isEmpty() || sn.isEmpty() || date_acc.isEmpty() || phoneNumber.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please input all data", Toast.LENGTH_SHORT).show();
        }
        else {
            if(checkNumber(phoneNumber)){
                Intent intent = new Intent();
                Toast.makeText(getApplicationContext(), "New printer added", Toast.LENGTH_SHORT).show();
                //databaseReference.child(String.valueOf(maxid+1)).setValue(printer);
                PrinterListContent.addItem(new PrinterListContent.Printer(maxid + 1, name, sn, date, phoneNumber, damage), maxid);
                setResult(RESULT_OK, intent);
                //((PrinterList) getSupportFragmentManager().findFragmentById(R.id.printerList)).notifyDataChange();
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Wrong phone number format", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
