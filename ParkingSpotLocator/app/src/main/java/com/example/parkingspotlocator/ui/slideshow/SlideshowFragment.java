package com.example.parkingspotlocator.ui.slideshow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingspotlocator.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SlideshowFragment extends Fragment {
    String status = "0";
    TextInputLayout regid, regpincode, regadd, regloc;
    Button sub;
    private FusedLocationProviderClient mLocationClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        regid = root.findViewById(R.id.nm);
        regpincode = root.findViewById(R.id.pc);
        regadd = root.findViewById(R.id.ad);
        regloc = root.findViewById(R.id.loc);
        sub = root.findViewById(R.id.adsp);
        mLocationClient = new FusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationClient.getLastLocation().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    regloc.getEditText().setText(String.valueOf(location.getLatitude())+", "+String.valueOf(location.getLongitude()));
                    sub.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AddData(location.getLatitude(), location.getLongitude());
                        }
                    });
                }
            });
        }
        return root;
    }

    private void AddData(double latitude, double longitude) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Spots");
        //Get All Values.
        String id = regid.getEditText().getText().toString().trim();
        String pin = regpincode.getEditText().getText().toString().trim();
        String address = regadd.getEditText().getText().toString().trim();
        //String location = regloc.getEditText().getText().toString().trim();
        Query checkid = reference.orderByChild("id").equalTo(id);
        checkid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Toast.makeText(getContext(),"Id Already Exists.", Toast.LENGTH_LONG).show();
                }else{
                    UserHelperClass userHelperClass = new UserHelperClass(id,pin,address,latitude,longitude,status);
                    reference.child(id).setValue(userHelperClass);
                    Toast.makeText(getContext(), "Spot added sucessfully.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}