package com.example.hw3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.print.PrinterId;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hw3.printers.PrinterListContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        PrinterList.OnListPrinterClickInteraction,
        DeleteDialog.OnDeleteDialogInteractionListener
{
    private int currentItemPosition = -1;
    private int checkCurrentItemPosition = -1;
    public static int pos;
    public static long maxid;
    private static DatabaseReference databaseReference;
    public static final String printerExtra = "printerExtra";
    public static PrinterListContent.Printer printer_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Printer");
        long z = 2;

        // - DELETING !!!!!!!!!!!!!
        //DatabaseReference del = FirebaseDatabase.getInstance().getReference().child("Printer").child("4");
        //del.removeValue();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    maxid = snapshot.getChildrenCount();
                    if(PrinterListContent.PRINTERS.size() == 0 && maxid !=0 ) {
                        getPrinters();
                    }
                }
                ((PrinterList) getSupportFragmentManager().findFragmentById(R.id.printerList)).notifyDataChange(); // czasem dziala czasem nie, nie wiem dlaczego
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && currentItemPosition == checkCurrentItemPosition){
            displayPrinterInFragment(null);
        }


        final FloatingActionButton floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPrinter.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    public static void getPrinters(){
        for(int i = 0; i < maxid; i++) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Printer").child(Integer.toString(i+1));
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        String idStr = snapshot.child("id").getValue().toString();
                        Long id = Long.parseLong(idStr);
                        String name = snapshot.child("name").getValue().toString();
                        String number = snapshot.child("number").getValue().toString();
                        String sn = snapshot.child("sn").getValue().toString();
                        String damage = snapshot.child("damage").getValue().toString();
                        String dateStr = snapshot.child("date").getValue().toString();
                        Long datel = Long.parseLong(dateStr);
                        PrinterListContent.Printer printer = new PrinterListContent.Printer(id, name, sn, datel, number, damage);
                        PrinterListContent.PRINTERS.add(printer);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void startSecondActivity(PrinterListContent.Printer printer, int position){
        Intent intent = new Intent(this, PrinterInfoActivity.class);
        intent.putExtra(printerExtra, printer);
        startActivity(intent);
    }

    private void displayPrinterInFragment(PrinterListContent.Printer printer){
        PrinterInfo printerInfo = ((PrinterInfo) getSupportFragmentManager().findFragmentById(R.id.printerInfo));
        if(printerInfo != null){
            printerInfo.displayPrinter(printer);
        }
    }

    @Override
    public void onListPrinterClickInteraction(PrinterListContent.Printer printer, int position) {
        //Toast.makeText(getApplicationContext(), "single click", Toast.LENGTH_SHORT).show();
        checkCurrentItemPosition = position;
        printer_main = printer;
        pos = position;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            displayPrinterInFragment(printer);
        }
        else {
            startSecondActivity(printer, position);
        }
    }

    @Override
    public void onListPrinterLongClickInteraction(PrinterListContent.Printer printer) {
    }

    @Override
    public void onButtonTrashClick (int position){
        showDeleteDialog();
        currentItemPosition = position;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1){
                ((PrinterList) getSupportFragmentManager().findFragmentById(R.id.printerList)).notifyDataChange();
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Add printer error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDeleteDialog(){
        DeleteDialog.newInstance().show(getSupportFragmentManager(), getString(R.string.delete_dialog_tag));
    }

    @Override
    public void onDeleteDialogPositivieClick(DialogFragment dialog) {
        if(currentItemPosition != -1 && currentItemPosition < PrinterListContent.PRINTERS.size()){
            PrinterListContent.removeItem(currentItemPosition);
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && currentItemPosition == checkCurrentItemPosition){
                displayPrinterInFragment(null);
            }
            ((PrinterList) getSupportFragmentManager().findFragmentById(R.id.printerList)).notifyDataChange();
        }
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "Delete not confirmed", Toast.LENGTH_SHORT).show();
    }
}