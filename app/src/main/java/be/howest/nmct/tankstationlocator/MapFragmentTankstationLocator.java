package be.howest.nmct.tankstationlocator;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

        LatLng rekkem = new LatLng(50.782807, 3.164113);

        if(latitude != 0 && longitude != 0){
            rekkem = new LatLng(latitude, longitude);
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(rekkem, 13));

        googleMap.addMarker(new MarkerOptions()
                .title("Rekkem")
                .snippet("City of Rekkem")
                .position(rekkem));

    }

    public void btnZoekTankstationClicked(){
        TankstationListFragment tankstationListFragment = new TankstationListFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainLayout,tankstationListFragment,"tankstationListFragment");
        fragmentTransaction.addToBackStack("tankstationListFragment");
        fragmentTransaction.commit();
    }
}
