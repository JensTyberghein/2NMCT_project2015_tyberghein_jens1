package be.howest.nmct.tankstationlocator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity {

    // Lat en long ophalen met deze site: http://www.latlong.net/
    // Tankstations: https://www.google.be/maps/search/tankstations+west+vlaanderen/@51.0331726,3.0382399,10z
    // API TURORIAL: https://www.youtube.com/watch?v=mnEjFjQPNHM
    // API website: https://code.google.com/apis/console/b/0/?noredirect#project:650479593702:access
    // API KEY: AIzaSyCYU0zYi2z6j3pMoH1RVniddf9CSS3wx24
    // C:\Users\jens\AppData\Local\Android\sdk1\extras\google\google_play_services

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MapFragmentTankstationLocator fragment = new MapFragmentTankstationLocator();
        transaction.replace(R.id.mainLayout, fragment, "mapFragmentTankstationLocator");
        transaction.commit();
    }
}
