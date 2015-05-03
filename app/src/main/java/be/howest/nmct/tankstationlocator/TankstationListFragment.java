package be.howest.nmct.tankstationlocator;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class TankstationListFragment extends Fragment {

    // Data
    List<Tankstation> tankstations;

    // list
    ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tankstation_list,container,false);

        tankstations = MyXmlPullParser.getTankstationsFromFile(getActivity());

        list = (ListView) view.findViewById(R.id.listView);
        TankstationAdapter adapter = new TankstationAdapter(getActivity(),tankstations);
        list.setAdapter(adapter);
        return view;
    }
}

class TankstationAdapter extends ArrayAdapter<Tankstation>
{
    Context context;
    List<Tankstation> tankstations;

    TankstationAdapter(Context c, List<Tankstation> tankstations)
    {
        super(c,R.layout.single_row_tankstation,tankstations);
        this.context = c;
        this.tankstations = tankstations;
    }

    class MyViewHolder
    {
        ImageView imgIcon;
        TextView txtNaam;
        TextView txtAdres;
        TextView txtStad;

        MyViewHolder(View v)
        {
            imgIcon = (ImageView) v.findViewById(R.id.imgIcon);
            txtNaam = (TextView) v.findViewById(R.id.txtNaam);
            txtAdres = (TextView) v.findViewById(R.id.txtAdres);
            txtStad = (TextView) v.findViewById(R.id.txtStad);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Optimalisatie; LayoutInflation is een zware operatie -> enkel doen bij het maken van een new row
        View row = convertView;
        MyViewHolder holder = null;

        if(row ==null) // De eerste keer is row null, als je een rij hergebruikt is row niet null
        {
            // inflater zodat we de 2 textviews kunnen zetten
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row_tankstation, parent, false);

            holder = new MyViewHolder(row);
            row.setTag(holder);
            Log.d("FEL", "Nieuwe rij");
        }
        else    // Dit is als je aan het recycelen bent
        {
            holder = (MyViewHolder) row.getTag();
            Log.d("FEL", "Recycling rij");
        }

        holder.imgIcon.setImageResource(R.drawable.single_row_icon);
        holder.txtNaam.setText(tankstations.get(position).getNaam());
        holder.txtAdres.setText(tankstations.get(position).getAdres());
        holder.txtStad.setText(tankstations.get(position).getStad());

        return row;
    }
}
