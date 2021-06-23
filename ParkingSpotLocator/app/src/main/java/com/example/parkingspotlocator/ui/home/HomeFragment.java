package com.example.parkingspotlocator.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingspotlocator.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    CardView male,female;
    TextInputLayout fullname,phno,eid,address;
    TextView fullnamelabel,usernamelabel;
    Button update;
    DatabaseReference reference;
    String fn,email,add,uid,sex,ph;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        fullname=root.findViewById(R.id.fullname);
        phno=root.findViewById(R.id.phno);
        eid=root.findViewById(R.id.eid);
        address=root.findViewById(R.id.address);
        fullnamelabel=root.findViewById(R.id.full_name);
        usernamelabel=root.findViewById(R.id.user_id);
        update=root.findViewById(R.id.update);
        male=root.findViewById(R.id.mm);
        female=root.findViewById(R.id.ff);

        reference = FirebaseDatabase.getInstance().getReference("User");
        getdata();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEidChanged() || isAddChanged()){
                    Toast.makeText(getContext(), "Data Has been Updated.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getContext(), "Phone Number Can't be changed.", Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }

    private void getdata() {
        Query data = reference.orderByChild("userid").equalTo(3);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    fn = ds.child("Fullname").getValue(String.class);
                    fullnamelabel.setText(fn);
                    fullname.getEditText().setText(fn);

//                    ph = ds.child("phno").getValue(String.class);
//                    phno.getEditText().setText(String.valueOf(ph));
//
                    email = ds.child("eid").getValue(String.class);
                    eid.getEditText().setText(email);
//
                    add = ds.child("address").getValue(String.class);
                    address.getEditText().setText(add);
//
                    uid = ds.child("username").getValue(String.class);
                    usernamelabel.setText(uid);
                    sex = ds.child("sex").getValue(String.class);
                    if(sex.equalsIgnoreCase("Male")){
                        male.setCardBackgroundColor(Color.parseColor("#0080ff"));

                    }else{
                        female.setCardBackgroundColor(Color.parseColor("#0080ff"));

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isEidChanged() {
        String id = "3";
        if(!email.equals(eid.getEditText().getText().toString())){
            reference.child(id).child("eid").setValue(eid.getEditText().getText().toString());
            email = eid.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }

    }

    private boolean isAddChanged() {
        String id = "3";
        if(!add.equals(address.getEditText().getText().toString())){
            reference.child(id).child("address").setValue(address.getEditText().getText().toString());
            add = address.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }
}