package com.example.parkingspotlocator.ui.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.parkingspotlocator.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    EditText destination;
    ImageView search;
    private ListView listView;
    private ArrayList<String> spots = new ArrayList<>();

    private FusedLocationProviderClient mLocationClient;
    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        destination = root.findViewById(R.id.et_search);
        search = root.findViewById(R.id.search_icon);
        listView = root.findViewById(R.id.list);
        initializeListView();
        mLocationClient = new FusedLocationProviderClient(getActivity());
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrLoc();
                }
            }
        });
        return root;
    }

    private void initializeListView() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Spots");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,spots);
        listView.setAdapter(adapter);
        String cnt ="0";
        Query data = reference.orderByChild("status").equalTo(cnt);
        data.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.child("pincode").getValue(String.class);
                String add = snapshot.child("id").getValue(String.class);
                spots.add("Pincode:  "+value+"\nSpot-ID:  "+add);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                adapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(adapter);
    }
    @SuppressLint("MissingPermission")
    private void getCurrLoc(){
        mLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                DisplayTrack(location.getLatitude(), location.getLongitude());
            }
        });
    }
    private void DisplayTrack(double latitude, double longitude) {
        String sdestination = destination.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Spots");
        Query datapin = reference.orderByChild("id").equalTo(sdestination);
        datapin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Double lat = snapshot.child(sdestination).child("latitude").getValue(Double.class);
                Double lon = snapshot.child(sdestination).child("longitude").getValue(Double.class);

                try {
                    Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + latitude + "," + longitude + "/" + lat + ", " + lon);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}