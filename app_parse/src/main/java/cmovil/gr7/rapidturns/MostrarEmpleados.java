package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostrarEmpleados extends Fragment {
    private IntentFilter filter;
    private BroadcastReceiver receiver;
    private Object[][] records;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private int mCurrentSelectedPosition=0;
    private Context mContext;
    private boolean dataexists=false;
    private String fromString,toString;
    private String msn;
    private String title;
    ProgressDialog dialog;
    private int color;


    public static MostrarEmpleados newInstance(int sectionNumber) {
        MostrarEmpleados fragment = new MostrarEmpleados();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MostrarEmpleados() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        filter = new IntentFilter("ccmovil.gr7.rapidturns.NEW_EMPLEADOS");
        mContext = getActivity().getApplicationContext();
        msn = getActivity().getString(R.string.removeMsn);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getResources().getString(R.string.login));
        title = getActivity().getString(R.string.removeTitle);
        fromString = getResources().getString(R.string.from);
        toString= getResources().getString(R.string.to);
        color = getResources().getColor(R.color.darkgrey);
        dataexists();
        View v;
        if(dataexists) {
            v = inflater.inflate(R.layout.listalocal, container, false);
            lista = (ListView) v.findViewById(R.id.ListView);

            records();
            lista.setItemChecked(mCurrentSelectedPosition, true);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Object[] o = (Object[]) lista.getItemAtPosition(position);
                    String str = (String) o[0];//As you are using Default String Adapter
                    Intent intent = new Intent(getActivity().getApplicationContext(), VerCalendario.class);
                    intent.putExtra("nombre", str);
                    intent.putExtra("id", o[3] + "");
                    intent.putExtra("type", "Empleado");
                    startActivity(intent);
                }
            });
            registerForContextMenu(lista);
        }else{
            v = inflater.inflate(R.layout.vacio,container,false);
            TextView text = (TextView) v.findViewById(R.id.text);
            String Text = text.getText()+"";
            text.setTextColor(getResources().getColor(R.color.amber2));
            text.setText(Text+getResources().getString(R.string.title_section5));
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
            String[] menuItems = getResources().getStringArray(R.array.menu);
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
                String str = (String) o[0];//As you are using Default String Adapter
                Intent intent = new Intent(getActivity().getApplicationContext(), VerCalendario.class);
                intent.putExtra("nombre", str);
                intent.putExtra("id", o[3] + "");
                intent.putExtra("type", "Empleado");
                startActivity(intent);
                return true;

            case 1:
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Empleado");
                query1.fromLocalDatastore();
                query1.include("local");
                query1.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> empleados, ParseException e) {
                        int size = empleados.size();
                        for (int i = 0; i < size; i++) {
                            final ParseObject empleado = empleados.get(i);
                            String id = empleado.getObjectId();
                            if(id.equals(Id)){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage(msn+" "+empleado.getString("name"))
                                        .setTitle(title);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialo, int id) {
                                        dialog.show();
                                        ParseObject semana = empleado.getParseObject("horario");
                                        borrar(semana);
                                        List<ParseObject> list = Arrays.asList(empleado, semana);
                                        ParseObject.unpinAllInBackground(list, new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                records();
                                                dialog.dismiss();
                                            }
                                        });
                                        ParseObject.deleteAllInBackground(list);
                                    }
                                });
                                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //nothing
                                    }
                                });
                                AlertDialog dialo = builder.create();
                                dialo.show();
                            }

                        }
                    }
                });
                return true;

            default:
                return false;
        }
    }

    public void borrar(ParseObject semana){
        ParseObject Lunes = semana.getParseObject("Lunes");
        ParseObject Martes = semana.getParseObject("Martes");
        ParseObject Miercoles = semana.getParseObject("Miercoles");
        ParseObject Jueves = semana.getParseObject("Jueves");
        ParseObject Viernes = semana.getParseObject("Viernes");
        ParseObject Sabado = semana.getParseObject("Sabado");
        ParseObject Domingo = semana.getParseObject("Domingo");
        List<ParseObject> lista =  Arrays.asList(Lunes, Martes, Miercoles, Jueves, Viernes, Sabado, Domingo);
        ParseObject.unpinAllInBackground(lista);
        ParseObject.deleteAllInBackground(lista);
    }

    public void dataExistsTrue(){
        dataexists = true;
    }

    public void dataexists(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Empleado");
        query.fromLocalDatastore();
        try{
            List<ParseObject> empleados = query.find();
            if (empleados.size() != 0){
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
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Empleado");
        query.fromLocalDatastore();
        query.orderByAscending("createdAt");
        Activity activity = getActivity();
        if (isAdded() && activity!=null) {
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> empleados, ParseException e) {
                    if (e == null) {
                        String NAME = Contract.Column.NAME;
                        String SEX = Contract.Column.SEX;
                        int size = empleados.size();
                        records = new Object[size][4];
                        for (int i = 0; i < size; i++) {
                            ParseObject empleado = empleados.get(i);
                            String name = empleado.getString(NAME);
                            SimpleDateFormat ft =
                                    new SimpleDateFormat("yyyy.MM.dd");
                            String created_at = ft.format(empleado.getCreatedAt());
                            String sex = empleado.getString(SEX);
                            String id = empleado.getObjectId();
                            records[i][0] = name;
                            records[i][1] = created_at;
                            records[i][2] = sex;
                            records[i][3] = id;
                        }
                        lista.setAdapter(new AdapterEmpleados(
                                mContext,
                                records,color));
                    } else {
                        // handle Parse Exception here
                    }
                }
            });
        }
    }

    class TimelineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("TimelineReceiver", "onReceived");
            records();
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


}
