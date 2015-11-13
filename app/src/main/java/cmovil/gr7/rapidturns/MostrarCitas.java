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
        lista.setAdapter(new AdapterCitas(
                getActivity().getActionBar().getThemedContext(),
                records,"#000000"));
        lista.setItemChecked(mCurrentSelectedPosition, true);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


            }
        });


        return v;
    }

    public void records() {

        String NAME = Contract.Column.NAME;
        String HORARIO = Contract.Column.HORARIO;
        String CREATED_AT = Contract.Column.CREATED_AT;

        DbHelper dbHelper= new DbHelper(getActivity().getActionBar().getThemedContext());//Instancia de DbHelper
        SQLiteDatabase db=dbHelper.getWritableDatabase();//Obtener instancia de BD

        Cursor cursor = db.query(Contract.CITA, null,null, null, null, null, null);
        records = new Object[cursor.getCount()][3];
        int i=0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String hora = cursor.getString(cursor.getColumnIndex(HORARIO));
                String created_at = cursor.getString(cursor.getColumnIndex(CREATED_AT));

                records[i][0] = name;
                records[i][1] = hora+":00";
                records[i][2] = created_at;
                i++;
            }
        }
    }


}
