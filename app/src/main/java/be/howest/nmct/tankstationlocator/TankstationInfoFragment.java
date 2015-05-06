package be.howest.nmct.tankstationlocator;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TankstationInfoFragment extends Fragment {

    // Data
    List<Tankstation> tankstations;
    int[] images = {R.drawable.imgtankstation0, R.drawable.imgtankstation1, R.drawable.imgtankstation2, R.drawable.imgtankstation3, R.drawable.imgtankstation4, R.drawable.imgtankstation5, R.drawable.imgtankstation6, R.drawable.imgtankstation7, R.drawable.imgtankstation8, R.drawable.imgtankstation9};

    TextView txtInfoNaam;
    ImageView imgTankstation;
    TextView txtInfoAdres;
    TextView txtInfoStad;

    Button btnToonOpMap;

    // lat en long
    double latitude;
    double longitude;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tankstation_info_fragment,container,false);

        txtInfoNaam = (TextView) view.findViewById(R.id.txtInfoNaam);
        imgTankstation = (ImageView) view.findViewById(R.id.imgTankstation);
        txtInfoAdres = (TextView) view.findViewById(R.id.txtInfoAdres);
        txtInfoStad = (TextView) view.findViewById(R.id.txtInfoStad);

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
        imgTankstation.setImageResource(images[tankstations.get(index).getId()]);
        txtInfoAdres.setText(tankstations.get(index).getAdres());
        txtInfoStad.setText(tankstations.get(index).getStad());

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