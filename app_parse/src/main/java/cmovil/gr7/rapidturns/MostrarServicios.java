package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.app.Fragment;
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
        View v = inflater.inflate(R.layout.listalocal, container, false);
        lista = (ListView) v.findViewById(R.id.ListView);
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
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Servicio");
        query.whereEqualTo("local", ParseUser.getCurrentUser());
        query.fromLocalDatastore();
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
                        records[i][1] = hora + " " + getString(R.string.from) + " " + from + " " + getString(R.string.to) + " " + to;
                        records[i][2] = created_at;
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