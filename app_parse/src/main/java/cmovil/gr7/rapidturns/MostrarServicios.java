package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostrarServicios extends Fragment {
    private Object[][] records;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private int mCurrentSelectedPosition=0;
    private Context mContext;
    private boolean dataexists=false;
    private String fromString,toString;


    public static MostrarServicios newInstance(int sectionNumber) {
        MostrarServicios fragment = new MostrarServicios();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MostrarServicios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fromString = getResources().getString(R.string.from);
        toString = getResources().getString(R.string.to);
        dataexists();
        View v;
        if(dataexists) {
            v = inflater.inflate(R.layout.listalocal, container, false);
            lista = (ListView) v.findViewById(R.id.ListView);
            mContext = getActivity().getApplicationContext();

            records();
       /* lista.setAdapter(new AdapterServicios(
                getActivity().getActionBar().getThemedContext(),
                records,"#ffffff"));*/
            lista.setItemChecked(mCurrentSelectedPosition, true);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Object[] o = (Object[]) lista.getItemAtPosition(position);
                    String str = (String) o[0];//As you are using Default String Adapter
                }
            });
        }else{
            v = inflater.inflate(R.layout.vacio,container,false);
            TextView text = (TextView) v.findViewById(R.id.text);
            String Text = text.getText()+"";
            text.setTextColor(getResources().getColor(R.color.amber2));
            text.setText(Text+getResources().getString(R.string.title_section6));
        }

        return v;
    }

    public void dataExistsTrue(){
        dataexists = true;
    }

    public void dataexists(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Servicio");
        query.whereEqualTo("local", ParseUser.getCurrentUser());
        query.fromLocalDatastore();
        try{
            List<ParseObject> servicios = query.find();
            if (servicios.size() != 0){
                dataExistsTrue();
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((LocalActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }


    public void records() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Servicio");
        query.whereEqualTo("local", ParseUser.getCurrentUser());
        query.fromLocalDatastore();
        Activity activity = getActivity();
        if (isAdded() && activity!=null) {
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> servicios, ParseException e) {
                    if (e == null) {
                        String NAME = Contract.Column.NAME;
                        String HORARIO = Contract.Column.HORARIO;
                        String FROM = Contract.Column.FROM;
                        String TO = Contract.Column.TO;
                        int size = servicios.size();
                        records = new Object[size][3];
                        for (int i = 0; i < size; i++) {
                            ParseObject servicio = servicios.get(i);
                            String name = servicio.getString(NAME);
                            String hora = servicio.getString(HORARIO);
                            SimpleDateFormat ft =
                                    new SimpleDateFormat("yyyy.MM.dd");
                            String created_at = ft.format(servicio.getCreatedAt());
                            int from = servicio.getInt(FROM);
                            int to = servicio.getInt(TO);

                            records[i][0] = name;
                            records[i][1] = hora + " " + fromString + " " + from + " " + toString + " " + to;
                            records[i][2] = created_at;
                        }
                        lista.setAdapter(new AdapterServicios(
                                mContext,
                                records, getResources().getColor(R.color.darkgrey)));
                    } else {
                        // handle Parse Exception here
                    }
                }
            });
        }
    }

}