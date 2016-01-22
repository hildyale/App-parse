package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostrarCitas extends Fragment {
    private IntentFilter filter;
    private BroadcastReceiver receiver;
    private static Object[][] records;
    private static AdapterCitas adaptador;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ListView lista;
    private TextView text;
    private int mCurrentSelectedPosition=0;
    private static Context mContext;
    private boolean dataexists=false;
    private String days[] = {"Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"};
    private String hours[] = {"hora8","hora9","hora10","hora11","hora12","hora13","hora14","hora15","hora16","hora17","hora18"};
    private static String[] dias;
    private static String[] horas = {"8:00 am","9:00am","10:00 am","11:00 am","12:00 pm","1:00 pm","2:00 pm","3:00 pm","4:00 pm","5:00 pm","6:00 pm"};

    public static MostrarCitas newInstance(int sectionNumber) {
        MostrarCitas fragment = new MostrarCitas();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MostrarCitas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] diasTemp = {getString(R.string.monday),getString(R.string.tuesday),getString(R.string.wednesday),getString(R.string.thursday),getString(R.string.friday),getString(R.string.saturday),getString(R.string.sunday)};
        dias = diasTemp;
        filter = new IntentFilter("ccmovil.gr7.rapidturns.NEW_DATES");
        dataexists();
        View v = inflater.inflate(R.layout.listacliente, container, false);
        mContext = getActivity().getApplicationContext();
        text = (TextView) v.findViewById(R.id.text);
        lista = (ListView) v.findViewById(R.id.ListView);
        if(dataexists) {
            text.setVisibility(View.GONE);
            records();
            lista.setItemChecked(mCurrentSelectedPosition, true);
            registerForContextMenu(lista);
        }else{
            lista.setVisibility(View.GONE);
            String Text = text.getText()+"";
            text.setTextColor(getResources().getColor(R.color.teal3));
            text.setText(Text+getResources().getString(R.string.title_section1));
        }
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.ListView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            Object[] o = (Object[]) lista.getItemAtPosition(info.position);
            menu.setHeaderTitle(o[0] + "");
            String[] menuItems = getResources().getStringArray(R.array.menucitas);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Object[] o = (Object[]) lista.getItemAtPosition(info.position);
        final String Id = (String) o[3];
        switch (item.getItemId()) {
            case 0:
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Cita");
                query1.fromLocalDatastore();
                query1.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> citas, ParseException e) {
                        int size = citas.size();
                        for (int i = 0; i < size; i++) {
                            ParseObject cita = citas.get(i);
                            String id = cita.getObjectId();
                            if(id.equals(Id)){
                                //cita.deleteInBackground();
                                ParseObject semana = cita.getParseObject("semana");
                                int x = cita.getInt("dia");
                                int y = cita.getInt("hora");
                                ParseObject dia = semana.getParseObject(days[x]);
                                dia.put(hours[y], "");
                                dia.saveEventually();
                                dia.pinInBackground();
                                try{
                                cita.unpin();}catch (ParseException ee){e.printStackTrace();}
                                cita.deleteEventually();
                                dataexists();
                                if(dataexists) {
                                    text.setVisibility(View.GONE);
                                    lista.setVisibility(View.VISIBLE);
                                    records();
                                    lista.setItemChecked(mCurrentSelectedPosition, true);

                                }else{
                                    text.setVisibility(View.VISIBLE);
                                    lista.setVisibility(View.GONE);
                                    text.setTextColor(getResources().getColor(R.color.teal3));
                                    text.setText("No " + getResources().getString(R.string.title_section1));
                                }
                            }

                        }
                    }
                });
                return true;

            default:
                return false;
        }
    }


    public void dataexists(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Cita");
        query.fromLocalDatastore();
        try {
            List<ParseObject> citas = query.find();
            if (citas.size() != 0){
                dataexists = true;
            }else{
                dataexists = false;
            }
        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    public static void records() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cita");
        query.fromLocalDatastore();
        query.orderByDescending("createdAt");
        query.include("local");
        query.include("Empleado");
        query.include("Servicio");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> citas, ParseException e) {
                if (e == null) {
                    String NAME = Contract.Column.NAME;
                    int size = citas.size();
                    records = new Object[size][4];
                    for (int i = 0; i < size; i++) {
                        ParseObject cita = citas.get(i);
                        ParseUser local = (ParseUser) cita.get("local");
                        ParseObject empleado = (ParseObject) cita.get("Empleado");
                        String object;
                        if (empleado!=null){
                            object = empleado.getString("name");
                        }else{
                            ParseObject servicio = (ParseObject) cita.get("Servicio");
                            object = servicio.getString("name");
                        }
                        String name = local.getString("name");
                        String dia = dias[cita.getInt("dia")];
                        String hora = horas[cita.getInt("hora")];
                        String id = cita.getObjectId();
                        records[i][0] = name;
                        records[i][1] = object;
                        records[i][2] = dia+" "+hora;
                        records[i][3] = id;
                    }
                        adaptador = new AdapterCitas(
                                mContext,
                                records, "#000000");
                        lista.setAdapter(adaptador);
                        Log.d("adaptador "," setiado ");
                } else {
                    // handle Parse Exception here
                }
            }
        });
    }

    class TimelineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TimelineReceiver", "onReceived");
            dataexists();
            if(dataexists) {
                text.setVisibility(View.GONE);
                lista.setVisibility(View.VISIBLE);
                records();
                lista.setItemChecked(mCurrentSelectedPosition, true);
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                    }
                });
            }else{
                text.setVisibility(View.VISIBLE);
                lista.setVisibility(View.GONE);
                text.setTextColor(getResources().getColor(R.color.teal3));
                text.setText("No " + getResources().getString(R.string.title_section1));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        receiver =  new TimelineReceiver();
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ClientActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
