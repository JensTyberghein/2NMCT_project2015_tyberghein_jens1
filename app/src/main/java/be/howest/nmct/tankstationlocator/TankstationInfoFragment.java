package be.howest.nmct.tankstationlocator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.List;

public class TankstationInfoFragment extends Fragment {

    // Data
    List<Tankstation> tankstations;
    int[] images = {R.drawable.imgtankstation0, R.drawable.imgtankstation1, R.drawable.imgtankstation2, R.drawable.imgtankstation3, R.drawable.imgtankstation4, R.drawable.imgtankstation5, R.drawable.imgtankstation6, R.drawable.imgtankstation7, R.drawable.imgtankstation8, R.drawable.imgtankstation9};

    TextView txtInfoNaam;
    ImageView imgTankstation;
    TextView txtInfoAdres;
    TextView txtInfoStad;
    TextView txtTelefoon;
    Button btnBellen;
    TextView txtAfstand;

    Button btnToonOpMap;

    // lat en long
    double latitude;
    double longitude;

    // Globale var om te kunnen bellen
    String telefoonNummer;

    // Afstand berekenen var
    LatLng UwPos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tankstation_info_fragment,container,false);

        txtInfoNaam = (TextView) view.findViewById(R.id.txtInfoNaam);
        imgTankstation = (ImageView) view.findViewById(R.id.imgTankstation);
        txtInfoAdres = (TextView) view.findViewById(R.id.txtInfoAdres);
        txtInfoStad = (TextView) view.findViewById(R.id.txtInfoStad);
        txtTelefoon = (TextView) view.findViewById(R.id.txtTelefoon);
        btnBellen = (Button) view.findViewById(R.id.btnBellen);
        txtAfstand = (TextView) view.findViewById(R.id.txtAfstand);

        btnToonOpMap = (Button) view.findViewById(R.id.btnToonOpMap);

        btnToonOpMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnToonOpMapClicked();
            }
        });

        btnBellen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBellenClicked();
            }
        });

        Bundle bundle = this.getArguments();
        int position = bundle.getInt("TankstationIndex",0);

        changeData(position);

        afstandBerekenen();

        return view;
    }

    public void changeData(int index){
        tankstations = MyXmlPullParser.getTankstationsFromFile(getActivity());
        txtInfoNaam.setText(tankstations.get(index).getNaam());
        imgTankstation.setImageResource(images[tankstations.get(index).getId()]);
        txtInfoAdres.setText(tankstations.get(index).getAdres());
        txtInfoStad.setText(tankstations.get(index).getStad());
        txtTelefoon.setText(tankstations.get(index).getTelefoon());
        telefoonNummer = tankstations.get(index).getTelefoon();

        latitude = tankstations.get(index).getLatitude();
        longitude = tankstations.get(index).getLongitude();
    }

    public void btnToonOpMapClicked(){

        Bundle arguments = new Bundle();
        arguments.putDouble("latitude", latitude);
        arguments.putDouble("longitude", longitude);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MapFragmentTankstationLocator fragment = new MapFragmentTankstationLocator();
        fragment.setArguments(arguments);
        transaction.replace(R.id.mainLayout, fragment, "MapFragmentTankstationLocator");
        transaction.commit();
    }

    public void btnBellenClicked(){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + Uri.encode(telefoonNummer.trim())));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }

    public void afstandBerekenen()
    {
        // Eigen positie ophalen
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);

        if(myLocation != null){
            double lati = myLocation.getLatitude();
            double longi = myLocation.getLongitude();
            UwPos = new LatLng(lati, longi);
        }
        else{
            //standaarWaarde instoppen
            UwPos = new LatLng(50.824350, 3.250059);
        }

        double afstand = distance(UwPos.latitude, UwPos.longitude, latitude, longitude, "K");
        Log.d("FEL", "" + afstand);

        afstand = afstand*1000;

        afstand = roundTwoDecimals(afstand);

        // Textveld setten
        txtAfstand.setText(" " + afstand + " meter");

    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit.equals("K")) {
            dist = dist * 1.609344;
        } else if (unit.equals("N")) {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static double bearing(double lat1, double lon1, double lat2, double lon2){

        double longDiff= lon2-lon1;
        double y = Math.sin(longDiff)*Math.cos(lat2);
        double x = Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(longDiff);

        double result = (Math.toDegrees(Math.atan2(y, x))+360)%360;

        return result;
    }

}
