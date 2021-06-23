package com.example.parkingspotlocator.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkingspotlocator.R;
import com.example.parkingspotlocator.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class login extends Fragment {
    EditText Username,password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Username=view.findViewById(R.id.username);
        password=view.findViewById(R.id.password);
        view.findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(login.this)
                        .navigate(R.id.action_log_to_sign);
            }
        });
        view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
                //get all values
                String user = Username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                Query checkuser = reference.orderByChild("username").equalTo(user);
                checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String password = snapshot.child(user).child("password").getValue(String.class);
                            if (pass.equals(password)){
                                Toast.makeText(getContext(),"Logged in.", Toast.LENGTH_LONG).show();
                                NavHostFragment.findNavController(login.this)
                                        .navigate(R.id.sucess);

//                                String fn = snapshot.child("Fullname").getValue(String.class);
//                                String ph = snapshot.child("phno").getValue(String.class);
//                                String email = snapshot.child("eid").getValue(String.class);
//                                String add = snapshot.child("address").getValue(String.class);
//                                String uid = snapshot.child("username").getValue(String.class);
//                                String sex = snapshot.child("sex").getValue(String.class);
//
//                                Intent intent = new Intent(getContext(), HomeFragment.class);
//                                intent.putExtra("fname",fn);
//                                intent.putExtra("no",ph);
//                                intent.putExtra("mail",email);
//                                intent.putExtra("ad",add);
//                                intent.putExtra("user",uid);
//                                intent.putExtra("gen",sex);
//
//                                startActivity(intent);
                            }else{
                                Toast.makeText(getContext(),"Incorrect Password.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getContext(),"User does not Exist.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}