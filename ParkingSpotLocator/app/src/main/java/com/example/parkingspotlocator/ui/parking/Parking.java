package com.example.parkingspotlocator.ui.parking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingspotlocator.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class Parking extends Fragment {
    TextInputLayout parkingId;
    String inDate,outDate,inTime,outTime;
    //Integer id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_parking, container, false);
        parkingId=root.findViewById(R.id.parking);

        root.findViewById(R.id.parking_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spotAllocation();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                inDate = dateFormat.format(date);
                Calendar cal = Calendar.getInstance();
                inTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
                Toast.makeText(getContext(), "Parking ID scanned.", Toast.LENGTH_LONG).show();
                root.findViewById(R.id.parking_image).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogue();
                        spotDeallocate();
                    }
                });
            }
        });

        return root;
    }

    private void spotAllocation() {
        String st ="1";
        String spotid=parkingId.getEditText().getText().toString().trim();
        if (!"".equals(spotid)) {
            Integer id = Integer.parseInt(spotid);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Spots");
            Query data = reference.orderByChild("id").equalTo(id);
            data.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reference.child(String.valueOf(id)).child("status").setValue(st);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void spotDeallocate() {
        String st ="0";
        String spotid=parkingId.getEditText().getText().toString().trim();
        if (!"".equals(spotid)) {
            Integer id = Integer.parseInt(spotid);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Spots");
            Query data = reference.orderByChild("id").equalTo(String.valueOf(spotid));
            data.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reference.child(String.valueOf(spotid)).child("status").setValue(st);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(Parking.this)
//                        .navigate(R.id.action_nav_parking_to_nav_payment);
//long diffSeconds = diff / 1000 % 60;
//            }
//        });
//    }

    private void alertDialogue() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        outDate = dateFormat.format(date);
        Calendar cal = Calendar.getInstance();
        outTime = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(inTime);
             date2 = format.parse(outTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = date2.getTime() - date1.getTime();
        long diffMinutes = difference / (60 * 1000) % 60;
        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
        dialog.setTitle("Payment Receipt");
        String text = "\t\t\t\t\t\t\t\t\t\t\t\tIn  \t\t\t\t\t\t\t\t\t\t\t\t\t\tOut";
        String alert1 = "Date :  "+ inDate + "   " + outDate;
        String alert2 = "Time :  \t\t\t"+ inTime + "  \t\t\t\t\t\t\t\t\t\t" + outTime;
        String alert3 = "Parking Charge :  "+String.valueOf(diffMinutes*0.25)+"  Rs";
        dialog.setMessage(text+"\n\n"+alert1 +"\n\n"+ alert2 +"\n\n"+ alert3);
        dialog.setPositiveButton("Wallet",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getContext(),"Payment Done Sucessfully.",Toast.LENGTH_LONG).show();
                    }
                });
        dialog.setNegativeButton("Pay Cash",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"Pay Cash to Host.",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
}