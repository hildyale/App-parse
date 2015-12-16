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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostrarEmpleados extends Fragment {
    private Object[][] records;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private int mCurrentSelectedPosition=0;
    private Context mContext;


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
        View v = inflater.inflate(R.layout.listalocal, container, false);
        lista = (ListView) v.findViewById(R.id.ListView);
        mContext=getActivity().getApplicationContext();

        records();
        /*if(records!=null) {
            lista.setAdapter(new AdapterEmpleados(
                    getActivity().getActionBar().getThemedContext(),
                    records, "#ffffff"));
        }else{Log.d("error1 records","NULLO");}*/
            lista.setItemChecked(mCurrentSelectedPosition, true);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Object[] o = (Object[])lista.getItemAtPosition(position);
                    String str = (String) o[0];//As you are using Default String Adapter
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                }
            });

        return v;
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
        query.whereEqualTo("local", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> empleados, ParseException e) {
                if (e == null) {
                    String NAME = Contract.Column.NAME;
                    String HORARIO = Contract.Column.HORARIO;
                    String CREATED_AT = Contract.Column.CREATED_AT;
                    String SEX = Contract.Column.SEX;
                    String FROM = Contract.Column.FROM;
                    String TO = Contract.Column.TO;
                    int size = empleados.size();
                    records = new Object[size][4];
                    for (int i = 0; i < size; i++) {
                        ParseObject empleado = empleados.get(i);
                        String name = empleado.getString(NAME);
                        String hora = empleado.getString(HORARIO);
                        SimpleDateFormat ft =
                                new SimpleDateFormat("yyyy.MM.dd");
                        String created_at = ft.format(empleado.getCreatedAt());
                        String sex = empleado.getString(SEX);
                        int from = empleado.getInt(FROM);
                        int to = empleado.getInt(TO);
                        records[i][0] = name;
                        records[i][1] = hora + " " + getString(R.string.from) + " " + from + " " + getString(R.string.to) + " " + to;
                        records[i][2] = created_at;
                        records[i][3] = sex;
                    }
                    lista.setAdapter(new AdapterEmpleados(
                            mContext,
                            records, "#ffffff"));
                } else {
                    // handle Parse Exception here
                }
            }
        });
        }


}
