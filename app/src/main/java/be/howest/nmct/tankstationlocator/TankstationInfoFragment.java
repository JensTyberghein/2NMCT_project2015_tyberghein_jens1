package be.howest.nmct.tankstationlocator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TankstationInfoFragment extends Fragment {

    // Data
    List<Tankstation> tankstations;

    TextView txtInfoNaam;
    Button btnToonOpMap;

    // lat en long
    double latitude;
    double longitude;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tankstation_info_fragment,container,false);

        txtInfoNaam = (TextView) view.findViewById(R.id.txtInfoNaam);
        btnToonOpMap = (Button) view.findViewById(R.id.btnToonOpMap);

        btnToonOpMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnToonOpMapClicked();
            }
        });

        Bundle bundle = this.getArguments();
        int position = bundle.getInt("TankstationIndex",0);

        changeData(position);

        return view;
    }

    public void changeData(int index){
        tankstations = MyXmlPullParser.getTankstationsFromFile(getActivity());
        txtInfoNaam.setText(tankstations.get(index).getNaam());

        latitude = tankstations.get(index).getLatitude();
        longitude = tankstations.get(index).getLongitude();


    }

    public void btnToonOpMapClicked(){

        Bundle arguments = new Bundle();
        arguments.putDouble("latitude",latitude);
        arguments.putDouble("longitude", longitude);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MapFragmentTankstationLocator fragment = new MapFragmentTankstationLocator();
        fragment.setArguments(arguments);
        transaction.replace(R.id.mainLayout, fragment, "MapFragmentTankstationLocator");
        transaction.commit();
    }
}
