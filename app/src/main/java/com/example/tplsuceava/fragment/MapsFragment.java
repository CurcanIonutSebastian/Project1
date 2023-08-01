package com.example.tplsuceava.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tplsuceava.R;
import com.example.tplsuceava.databinding.FragmentMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    FragmentMapsBinding binding;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapInitialize();
        return binding.getRoot();
    }

    private void mapInitialize() {
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(16);
        locationRequest.setFastestInterval(3000);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"eroare"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference soferiRef = database.getReference("ID Soferi");
                                List<Marker> markerList = new ArrayList<>();
                                Map<String, Marker> markerMap = new HashMap<>();
                                soferiRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        List<Marker> markersToRemove = new ArrayList<>(markerList);
                                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                            String userId = userSnapshot.getKey();
                                            final Marker[] marker = {null};
                                            if (markerMap.containsKey(userId)) {
                                                marker[0] = markerMap.get(userId);
                                                markersToRemove.remove(marker[0]);
                                            }
                                            if (userSnapshot.child("Locatie").exists() && userSnapshot.child("Linia").exists()) {
                                                DatabaseReference locatieRef = userSnapshot.child("Locatie").getRef();
                                                locatieRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Double latitudine = dataSnapshot.child("Lat").getValue(Double.class);
                                                        Double longitudine = dataSnapshot.child("Long").getValue(Double.class);
                                                        if (latitudine != null && longitudine != null) {
                                                            DatabaseReference linieRef = userSnapshot.child("Linia").getRef();
                                                            linieRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    String numeLinie = dataSnapshot.getValue(String.class);
                                                                    if (numeLinie != null) {
                                                                        LatLng pozitie = new LatLng(latitudine, longitudine);
                                                                        if (marker[0] == null) {
                                                                            MarkerOptions markerOptions = new MarkerOptions()
                                                                                    .position(pozitie)
                                                                                    .title(numeLinie)
                                                                                    .icon(bitmapDescriptorFromVector(getContext(),R.drawable.bus_28));
                                                                            marker[0] = mMap.addMarker(markerOptions);
                                                                            markerList.add(marker[0]);
                                                                            markerMap.put(userId, marker[0]);
                                                                        } else {
                                                                            marker[0].setPosition(pozitie);
                                                                            marker[0].setTitle(numeLinie);
                                                                        }
                                                                    }
                                                                }
                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                            } else {
                                                if (marker[0] != null) {
                                                    marker[0].remove();
                                                    markerList.remove(marker[0]);
                                                    markerMap.remove(userId);
                                                }
                                            }
                                        }
                                        markersToRemove = new ArrayList<>();
                                        for (Marker m : markersToRemove) {
                                            markerList.remove(m);
                                            m.remove();
                                        }
                                        for (Marker m : markersToRemove) {
                                            markerList.remove(m);
                                            m.remove();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(),"Permisiunea"+ permissionDeniedResponse.getPermissionName()+ ""+"a fost negata!",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId){
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}