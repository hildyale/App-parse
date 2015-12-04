package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostrarCitas extends Fragment {
    private Object[][] records;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private int mCurrentSelectedPosition=0;

    public static MostrarCitas newInstance(int sectionNumber) {
        MostrarCitas fragment = new MostrarCitas();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MostrarCitas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listacliente, container, false);
        lista = (ListView) v.findViewById(R.id.ListView);
        records();
        /*lista.setAdapter(new AdapterCitas(
                getActivity().getActionBar().getThemedContext(),
                records,"#000000"));*/
        lista.setItemChecked(mCurrentSelectedPosition, true);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });


        return v;
    }

    public void records() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cita");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.include("local");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> citas, ParseException e) {
                if (e == null) {
                    String NAME = Contract.Column.NAME;
                    int size = citas.size();
                    records = new Object[size][2];
                    for (int i = 0; i < size; i++) {
                        ParseObject cita = citas.get(i);
                        ParseUser local = (ParseUser)cita.get("local");
                        String name = local.getString("name");
                        int id = cita.getInt("horario");
                        records[i][0] = name;
                        records[i][1] = id;
                    }
                    lista.setAdapter(new AdapterCitas(
                            getActivity().getActionBar().getThemedContext(),
                            records,"#000000"));
                } else {
                    // handle Parse Exception here
                }
            }
        });
    }


}
