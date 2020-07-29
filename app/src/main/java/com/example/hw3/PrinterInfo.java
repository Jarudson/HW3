package com.example.hw3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.hw3.printers.PrinterListContent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrinterInfo extends Fragment {

    public PrinterInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_printer_info, container, false);
    }
    @SuppressLint("SetTextI18n")
    public void displayPrinter(PrinterListContent.Printer printer){
        FragmentActivity activity = getActivity();

        TextView name = activity.findViewById(R.id.nameInfo);
        TextView sn = activity.findViewById(R.id.snInfo);
        TextView date_acc = activity.findViewById(R.id.dateInfo);
        TextView phoneNumber = activity.findViewById(R.id.phoneNumberInfo);
        TextView damage = activity.findViewById(R.id.damageInfo);
        if(printer == null){
            name.setText(null);
            sn.setText(null);
            date_acc.setText(null);
            phoneNumber.setText(null);
            damage.setText(null);
            return;
        }
        else {
            name.setText("Name: " + printer.getName());
            sn.setText("Serial number: " + printer.getSn());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = new Date(printer.getDate());
            String dateTxt = simpleDateFormat.format(date);
            date_acc.setText("Date Acceptance: " + dateTxt);
            phoneNumber.setText("Client phone Number: " + printer.getNumber());
            damage.setText("Damage description: " + printer.getDamage());
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if(intent != null){
            PrinterListContent.Printer receivedPrinter = intent.getParcelableExtra(MainActivity.printerExtra);
            if(receivedPrinter != null){
                displayPrinter(receivedPrinter);
            }
        }
    }
}
