package be.howest.nmct.tankstationlocator;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyXmlPullParser {
    static final String KEY_TANKSTATION = "tankstation";
    static final String KEY_ID = "id";
    static final String KEY_NAAM = "naam";
    static final String KEY_ADRES = "adres";
    static final String KEY_STAD = "stad";
    static final String KEY_LATITUDE = "latitude";
    static final String KEY_LONGITUDE = "longitude";

    public static List<Tankstation> getTankstationsFromFile(Context ctx){

        // List van tankstation dat we gaan returnen
        List<Tankstation> tankstations = new ArrayList<Tankstation>();

        Tankstation curTankstation = null;  // Het object dat we zijn aan het opvullen
        String curText = "";    // De value van 1 tag terwijl we lezen.

        try{

            StringBuffer stringBuffer = new StringBuffer();
            Resources res = ctx.getResources();
            XmlPullParser xpp = res.getXml(R.xml.tankstations);
            int eventType = xpp.getEventType();

            // Loop throus pull eents until we reach END_DOCUMENT
            while(eventType != XmlPullParser.END_DOCUMENT)
            {
                // Get the current tag
                String tagname = xpp.getName();

                // React to different event types appropriately
                switch (eventType){
                    case XmlPullParser.START_TAG:   // Eerste tag <tankstation>  => nieuw object
                        if(tagname.equalsIgnoreCase(KEY_ID)) {
                            curTankstation = new Tankstation();
                        }
                        break;

                    case XmlPullParser.TEXT: // De value ophalen
                        curText = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(tagname.equalsIgnoreCase(KEY_TANKSTATION)){    // if </longitude> -> klaar met dit opbject -> add to list
                            tankstations.add(curTankstation);
                        } else if (tagname.equalsIgnoreCase(KEY_ID)) {
                            // if </id> use setName van cutTankstation
                            curTankstation.setId(Integer.parseInt(curText));
                        } else if (tagname.equalsIgnoreCase(KEY_NAAM)){
                            // if </naam> use setName van cutTankstation
                            curTankstation.setNaam(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_ADRES)){
                            // if </adres> use setName van cutTankstation
                            curTankstation.setAdres(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_STAD)){
                            // if </stad> use setName van cutTankstation
                            curTankstation.setStad(curText);
                        } else if (tagname.equalsIgnoreCase(KEY_LATITUDE)){
                            // if </latitude> use setName van cutTankstation
                            curTankstation.setLatitude(Double.valueOf(curText));
                        } else if (tagname.equalsIgnoreCase(KEY_LONGITUDE)){
                            // if </latitude> use setName van cutTankstation
                            curTankstation.setLongitude(Double.valueOf(curText));
                        }
                        break;

                    default:
                        break;
                }
                // Ga naar volgende tankstation
                eventType = xpp.next();
            }
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d("FEL", "XmlPullParserException");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("FEL", "FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FEL", "IOException");
        }
        // De lijst returnen
        return tankstations;
    }
}
