package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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
    private int color;
    private String msn;
    private String title;
    private TextView text;
    ProgressDialog dialog;

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
        color = getResources().getColor(R.color.darkgrey);
        msn = getActivity().getString(R.string.removeMsn);
        mContext = getActivity().getApplicationContext();
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getResources().getString(R.string.login));
        title = getActivity().getString(R.string.removeTitle);
        dataexists();
        dataexists();
        View v = inflater.inflate(R.layout.listalocal, container, false);
        text = (TextView) v.findViewById(R.id.text);
        lista = (ListView) v.findViewById(R.id.ListView);
        if(dataexists) {
            text.setVisibility(View.GONE);
            records();
            lista.setItemChecked(mCurrentSelectedPosition, true);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Object[] o = (Object[]) lista.getItemAtPosition(position);
                    String str = (String) o[0];//As you are using Default String Adapter
                    Intent intent = new Intent(getActivity().getApplicationContext(), VerCalendario.class);
                    intent.putExtra("nombre", str);
                    intent.putExtra("id", o[2] + "");
                    intent.putExtra("type", "Servicio");
                    startActivity(intent);
                }
            });
            registerForContextMenu(lista);
        }else{
            lista.setVisibility(View.GONE);
            String Text = text.getText()+"";
            text.setTextColor(getResources().getColor(R.color.amber2));
            text.setText(Text+getResources().getString(R.string.title_section6));
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
        final String Id = (String) o[2];

        switch (item.getItemId()) {
            case 0:
                String str = (String) o[0];//As you are using Default String Adapter
                Intent intent = new Intent(getActivity().getApplicationContext(), VerCalendario.class);
                intent.putExtra("nombre", str);
                intent.putExtra("id", o[2] + "");
                intent.putExtra("type", "Servicio");
                startActivity(intent);
                return true;

            case 1:
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Servicio");
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
                                        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                                        NetworkInfo ni = cm.getActiveNetworkInfo();
                                        if ((ni != null) && (ni.isConnected())) {
                                        dialog.show();
                                        ParseObject semana = empleado.getParseObject("horario");
                                        borrar(semana);
                                        borrarCitas(empleado);
                                        List<ParseObject> list = Arrays.asList(empleado, semana);
                                        ParseObject.unpinAllInBackground(list, new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                dialog.dismiss();
                                                dataexists();
                                                if (dataexists) {
                                                    text.setVisibility(View.GONE);
                                                    lista.setVisibility(View.VISIBLE);
                                                    records();
                                                    lista.setItemChecked(mCurrentSelectedPosition, true);
                                                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        public void onItemClick(AdapterView<?> parent, View view,
                                                                                int position, long id) {
                                                            Object[] o = (Object[]) lista.getItemAtPosition(position);
                                                            String str = (String) o[0];//As you are using Default String Adapter
                                                            Intent intent = new Intent(getActivity().getApplicationContext(), VerCalendario.class);
                                                            intent.putExtra("nombre", str);
                                                            intent.putExtra("id", o[2] + "");
                                                            intent.putExtra("type", "Servicio");
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    registerForContextMenu(lista);
                                                } else {
                                                    text.setVisibility(View.VISIBLE);
                                                    lista.setVisibility(View.GONE);
                                                    String Text = text.getText() + "";
                                                    text.setTextColor(getResources().getColor(R.color.teal3));
                                                    text.setText(Text + getResources().getString(R.string.title_section5));
                                                }
                                            }
                                        });
                                        ParseObject.deleteAllInBackground(list);
                                        }else{
                                            Toast.makeText(mContext, "NO INTERNET" , Toast.LENGTH_SHORT).show();
                                        }
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

    public void borrarCitas(ParseObject servicio){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Cita");
        query1.fromLocalDatastore();
        query1.whereEqualTo("Servicio", servicio);
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                int size = objects.size();
                for (int i = 0; i < size; i++) {
                    ParseObject cita = objects.get(i);
                    cita.unpinInBackground();
                    cita.saveInBackground();
                }
            }
        });
    }


    public void dataexists(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Servicio");
        query.whereEqualTo("local", ParseUser.getCurrentUser());
        query.fromLocalDatastore();
        try{
            List<ParseObject> servicios = query.find();
            if (servicios.size() != 0){
                dataexists = true;
            }else{
                dataexists = false;
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
                            SimpleDateFormat ft =
                                    new SimpleDateFormat("yyyy.MM.dd");
                            String created_at = ft.format(servicio.getCreatedAt());
                            String id = servicio.getObjectId();
                            records[i][0] = name;
                            records[i][1] = created_at;
                            records[i][2] = id;
                        }
                        lista.setAdapter(new AdapterServicios(
                                mContext,
                                records, color));
                    } else {
                        // handle Parse Exception here
                    }
                }
            });
        }
    }

}