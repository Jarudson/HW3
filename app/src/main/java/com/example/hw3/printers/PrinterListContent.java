package com.example.hw3.printers;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw3.MainActivity;
import com.example.hw3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PrinterListContent {


    private static final int COUNT = 1;
    public static final List<Printer> PRINTERS = new ArrayList<Printer>();
    public static long maxid;
    private static DatabaseReference databaseReference;
    //static {
        // Add some sample items.
        //for (int i = 1; i <= COUNT; i++) {
            //addItem(createDummyItem(i));
        //}
    //}
    public PrinterListContent(){
    }
    public static void remove(int position){
        for(int i = 0; i < maxid; i++){
            if((i >= position) && (i != maxid-1)){
                int x = i+1;
                DatabaseReference del = FirebaseDatabase.getInstance().getReference().child("Printer").child(Integer.toString(x));
                del.setValue(PRINTERS.get(i+1));
            }
            if(i == maxid-1){
                int x = i+1;
                DatabaseReference del = FirebaseDatabase.getInstance().getReference().child("Printer").child(Integer.toString(x));
                del.removeValue();

                Log.e("size", Integer.toString(PRINTERS.size()));
                Log.e("index", Integer.toString(position));
                if(PRINTERS.size() == 1) {
                    PRINTERS.remove(position);
                }
                else {
                    PRINTERS.remove(position);
                }
            }
        }
        maxid = maxid -1;
    }

    public static void removeItem(final int position){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Printer");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxid = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        remove(position);
    }

    public static void updateItem(Printer item, int position){
        PRINTERS.get(position).setDate(item.getDate());
        PRINTERS.get(position).setDamage(item.getDamage());
        PRINTERS.get(position).setName(item.getName());
        PRINTERS.get(position).setNumber(item.getNumber());
        PRINTERS.get(position).setSn(item.getSn());
        DatabaseReference upd = FirebaseDatabase.getInstance().getReference().child("Printer").child(Integer.toString(position+1));
        upd.setValue(item);
    }

    public static void addItem(Printer item, Long maxid) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Printer");
        databaseReference.child(String.valueOf(maxid+1)).setValue(item);
        PRINTERS.add(item);
    }

//*****************************************************


    public static class Printer implements Parcelable {
        public Long id;
        private String name;
        private String sn;
        private long  date;
        private String number;
        private String damage;

        public Printer(Long id, String name, String sn, long date, String number, String damage) {
            this.id = id;
            this.name = name;
            this.sn = sn;
            this.date = date;
            this.number = number;
            this.damage = damage;
        }
        public Printer(Long id, String name) {
            this.id = id;
            this.name = name;
            this.sn = sn;
            this.date = date;
            this.number = number;
            this.damage = damage;
        }

        protected Printer(Parcel in) {
            id = in.readLong();
            name = in.readString();
            sn = in.readString();
            date = in.readLong();
            number = in.readString();
            damage = in.readString();
        }

        public static final Creator<Printer> CREATOR = new Creator<Printer>() {
            @Override
            public Printer createFromParcel(Parcel in) {
                return new Printer(in);
            }

            @Override
            public Printer[] newArray(int size) {
                return new Printer[size];
            }
        };

        public String getName(){
            return name;
        }
        public String getSn(){
            return sn;
        }
        public long getDate(){
            return date;
        }
        public String getNumber(){
            return number;
        }
        public String getDamage() { return damage; }

        public void setName(String name) {
            this.name = name;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }
        public void setDate(long date){
            this.date = date;
        }
        public void setNumber(String number){
            this.number = number;
        }
        public void setDamage(String damage) { this.damage = damage; }



        @Override
        public String toString() {
            return sn;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(id);
            dest.writeString(name);
            dest.writeString(sn);
            dest.writeLong(date);
            dest.writeString(number);
            dest.writeString(damage);
        }

        public String toStringDate() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = new Date(this.date);
            String dateTxt = simpleDateFormat.format(date);
            return dateTxt;
        }
    }
}
