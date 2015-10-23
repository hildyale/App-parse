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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Servicios extends Fragment {
    private String[] records;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private Button add;
    private int mCurrentSelectedPosition=0;

    public static Servicios newInstance(int sectionNumber) {
        Servicios fragment = new Servicios();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Servicios() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listalocal, container, false);
        lista = (ListView) v.findViewById(R.id.ListView);
        records();
        lista.setAdapter(new ArrayAdapter<String>(
                getActivity().getActionBar().getThemedContext(),
                R.layout.item_citas,
                R.id.text1, records));
        lista.setItemChecked(mCurrentSelectedPosition, true);
        add = (Button) v.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getActivity().getActionBar().getThemedContext(),Empleado.class);
                startActivity(intent);
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

        String NAME = Contract.Column.NAME;
        String HORARIO = Contract.Column.HORARIO;
        String CREATED_AT = Contract.Column.CREATED_AT;

        DbHelper dbHelper= new DbHelper(getActivity().getActionBar().getThemedContext());//Instancia de DbHelper
        SQLiteDatabase db=dbHelper.getWritableDatabase();//Obtener instancia de BD

        Cursor cursor = db.query(Contract.EMPLEADO, null,null, null, null, null, null);
        records = new String[cursor.getCount()];
        int i=0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String hora = cursor.getString(cursor.getColumnIndex(HORARIO));
                String created_at = cursor.getString(cursor.getColumnIndex(CREATED_AT));

                records[i] = name+" "+hora+" "+created_at;

                /*messages[i] = message;
                Log.d("MESSAGE",message);
                created[i] = created_at;
                Log.d("CREATED",created_at);*/

                i++;
            }
        }else{
            Log.d("ContentProvider", "Cursor:" + cursor.getCount());

        }
    }

}
