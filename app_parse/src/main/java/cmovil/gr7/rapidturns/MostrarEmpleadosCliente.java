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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MostrarEmpleadosCliente extends Fragment {
    private Object[][] records;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private Button add;
    private int mCurrentSelectedPosition=0;

    public static MostrarEmpleadosCliente newInstance(String id) {
        MostrarEmpleadosCliente fragment = new MostrarEmpleadosCliente();
        Bundle args = new Bundle();
        args.putString("Id", id);
        fragment.setArguments(args);
        return fragment;
    }

    public MostrarEmpleadosCliente() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listacliente, container, false);
        lista = (ListView) v.findViewById(R.id.ListView);
        user();
        /*lista.setAdapter(new AdapterEmpleados(
                getActivity().getActionBar().getThemedContext(),
                records,"#000000"));*/
        lista.setItemChecked(mCurrentSelectedPosition, true);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object[] o = (Object[])lista.getItemAtPosition(position);
                String str = (String) o[0];//As you are using Default String Adapter
                Intent intent = new Intent(getActivity().getApplicationContext(), Reservar.class);
                intent.putExtra("nombre",getString(R.string.reservarServicio)+" "+str);
                intent.putExtra("id",o[4]+"");
                intent.putExtra("local",o[5]+"");
                intent.putExtra("type","Empleado");
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
    public void records(ParseUser user) {
        final String Id = getArguments().getString("Id");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Empleado");
        query.whereEqualTo("local", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> empleados, ParseException e) {
                if (e == null) {
                    String NAME = Contract.Column.NAME;
                    String HORARIO = Contract.Column.HORARIO;
                    String SEX = Contract.Column.SEX;
                    String FROM = Contract.Column.FROM;
                    String TO = Contract.Column.TO;
                    int size = empleados.size();
                    records = new Object[size][6];
                    for (int i = 0; i < size; i++) {
                        ParseObject empleado = empleados.get(i);
                        String name = empleado.getString(NAME);
                        String hora = empleado.getString(HORARIO);
                        SimpleDateFormat ft =
                                new SimpleDateFormat("yyyy.MM.dd");
                        String created_at = ft.format(empleado.getCreatedAt());
                        String sex = empleado.getString(SEX);
                        String id = empleado.getObjectId();
                        int from = empleado.getInt(FROM);
                        int to = empleado.getInt(TO);
                        records[i][0] = name;
                        records[i][1] = hora + " " + getString(R.string.from) + " " + from + " " + getString(R.string.to) + " " + to;
                        records[i][2] = created_at;
                        records[i][3] = sex;
                        records[i][4] = id;
                        records[i][5] = Id;
                    }
                    lista.setAdapter(new AdapterEmpleados(
                            getActivity().getApplicationContext(),
                            records, "#ffffff"));
                } else {
                    // handle Parse Exception here
                }
            }
        });
                }

            }
