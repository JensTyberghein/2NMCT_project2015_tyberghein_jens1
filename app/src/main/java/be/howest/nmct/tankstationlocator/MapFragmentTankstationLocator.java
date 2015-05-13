package be.howest.nmct.tankstationlocator;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.Map;

public class MapFragmentTankstationLocator extends Fragment implements OnMapReadyCallback{

    private MapFragment mMapFragment;
    private Button btnZoekTankstation;

    public  MapFragmentTankstationLocator(){}

    // lat en long
    double latitude;
    double longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        btnZoekTankstation = (Button) view.findViewById(R.id.btnZoekTankstation);
        btnZoekTankstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnZoekTankstationClicked();
            }
        });

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapHolder, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);



        if(this.getArguments() != null){
            Bundle bundle = this.getArguments();
            latitude = bundle.getDouble("latitude", 0);
            longitude = bundle.getDouble("longitude", 0);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setMyLocationEnabled(true);

        LatLng nowPos;

        if(latitude == 0 && longitude == 0){
            // Eigen positie ophalen
            LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location myLocation = locationManager.getLastKnownLocation(provider);

            if(myLocation != null){
                double lati = myLocation.getLatitude();
                double longi = myLocation.getLongitude();
                nowPos = new LatLng(lati, longi);
            }
            else{
                //standaarWaarde instoppen
                nowPos = new LatLng(50.824350, 3.250059);
            }



        }
        else{
            nowPos = new LatLng(latitude, longitude);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPos, 13));

        googleMap.addMarker(new MarkerOptions()
                .title("You")
                .snippet("You Are Here")
                .position(nowPos));

    }

    public void btnZoekTankstationClicked(){
        TankstationListFragment tankstationListFragment = new TankstationListFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainLayout,tankstationListFragment,"tankstationListFragment");
        fragmentTransaction.addToBackStack("tankstationListFragment");
        fragmentTransaction.commit();
    }
}
