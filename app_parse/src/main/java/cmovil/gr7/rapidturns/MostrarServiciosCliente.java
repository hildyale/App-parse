package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MostrarServiciosCliente extends Fragment {
    private Object[][] records;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private Button add;
    private int mCurrentSelectedPosition=0;

    public static MostrarServiciosCliente newInstance(String id) {
        MostrarServiciosCliente fragment = new MostrarServiciosCliente();
        Bundle args = new Bundle();
        args.putString("Id",id);
        fragment.setArguments(args);
        return fragment;
    }

    public MostrarServiciosCliente() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listacliente, container, false);
        lista = (ListView) v.findViewById(R.id.ListView);
        user();
        /*lista.setAdapter(new AdapterServicios(
                getActivity().getActionBar().getThemedContext(),
                records,"#000000"));*/
        lista.setItemChecked(mCurrentSelectedPosition, true);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object[] o = (Object[]) lista.getItemAtPosition(position);
                String str = (String) o[0];//As you are using Default String Adapter
                Intent intent = new Intent(getActivity().getApplicationContext(), Reservar.class);
                intent.putExtra("nombre",getString(R.string.reservarServicio)+" "+str);
                intent.putExtra("id",o[3]+"");
                intent.putExtra("local",o[4]+"");
                intent.putExtra("type","Servicio");
                startActivity(intent);
            }
        });


        return v;
    }

    public void user(){
        String Id = getArguments().getString("Id");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(Id, new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    records(object);
                } else {
                    // something went wrong
                }
            }
        });
    }

    public void records(final ParseUser user) {
        final String Id = getArguments().getString("Id");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Servicio");
        query.whereEqualTo("local", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> servicios, ParseException e) {
                if (e == null) {
                    String NAME = Contract.Column.NAME;
                    String HORARIO = Contract.Column.HORARIO;
                    String FROM = Contract.Column.FROM;
                    String TO = Contract.Column.TO;
                    int size = servicios.size();
                    records = new Object[size][5];
                    for (int i = 0; i < size; i++) {
                        ParseObject servicio = servicios.get(i);
                        String name = servicio.getString(NAME);
                        String hora = servicio.getString(HORARIO);
                        String id = servicio.getObjectId();
                        SimpleDateFormat ft =
                                new SimpleDateFormat("yyyy.MM.dd");
                        String created_at = ft.format(servicio.getCreatedAt());
                        int from = servicio.getInt(FROM);
                        int to = servicio.getInt(TO);
                        records[i][0] = name;
                        records[i][1] = hora + " " + getString(R.string.from) + " " + from + " " + getString(R.string.to) + " " + to;
                        records[i][2] = created_at;
                        records[i][3] = id;
                        records[i][4] = Id;
                    }
                    lista.setAdapter(new AdapterServicios(
                            getActivity().getApplicationContext(),
                            records, "#ffffff"));
                } else {
                    // handle Parse Exception here
                }
            }
        });
    }

}