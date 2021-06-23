package com.example.parkingspotlocator.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.parkingspotlocator.R;
import com.example.parkingspotlocator.ui.slideshow.UserHelperClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class register extends Fragment {
    TextInputLayout Username,password,fullname,sex,phno,addess,emailid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Username=view.findViewById(R.id.un);
        password=view.findViewById(R.id.pswd);
        fullname=view.findViewById(R.id.fn);
        sex=view.findViewById(R.id.sex);
        phno=view.findViewById(R.id.ph);
        addess=view.findViewById(R.id.ad);
        emailid=view.findViewById(R.id.emid);
        view.findViewById(R.id.reg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
                NavHostFragment.findNavController(register.this)
                        .navigate(R.id.action_sign_to_log);
            }
        });
    }

    private void addData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        //Get All Values.
        String username = Username.getEditText().getText().toString().trim();
        String pass = password.getEditText().getText().toString().trim();
        String fname = fullname.getEditText().getText().toString().trim();
        String gen = sex.getEditText().getText().toString().trim();
        String pno = phno.getEditText().getText().toString().trim();
        String add = addess.getEditText().getText().toString().trim();
        String eid = emailid.getEditText().getText().toString().trim();
        Query checkid = reference.orderByChild("username").equalTo(username);
        checkid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(getContext(),"Id Already Exists.", Toast.LENGTH_LONG).show();
                }else{
                    HelperClass userHelperClass =new HelperClass(username,pass,fname,gen,pno,add,eid);
                    reference.child(username).setValue(userHelperClass);
                    Toast.makeText(getContext(), "User added sucessfully.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}