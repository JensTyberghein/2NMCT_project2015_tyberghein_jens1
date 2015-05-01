package be.howest.nmct.tankstationlocator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    // Lat en long ophalen met deze site: http://www.latlong.net/
    // Tankstations: https://www.google.be/maps/search/tankstations+west+vlaanderen/@51.0331726,3.0382399,10z

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
