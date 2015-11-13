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


/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class MostrarServiciosCliente extends Fragment {
    private Object[][] records;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private Button add;
    private int mCurrentSelectedPosition=0;

    public static MostrarServiciosCliente newInstance() {
        MostrarServiciosCliente fragment = new MostrarServiciosCliente();
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
        records();
        lista.setAdapter(new AdapterServicios(
                getActivity().getActionBar().getThemedContext(),
                records,"#000000"));
        lista.setItemChecked(mCurrentSelectedPosition, true);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object[] o = (Object[]) lista.getItemAtPosition(position);
                String str = (String) o[0];//As you are using Default String Adapter
                Intent intent = new Intent(getActivity().getActionBar().getThemedContext(), Reservar.class);
                intent.putExtra("nombre",getString(R.string.reservarServicio)+" "+str);
                intent.putExtra("name",str);
                startActivity(intent);
            }
        });


        return v;
    }

    public void records() {

        String NAME = Contract.Column.NAME;
        String HORARIO = Contract.Column.HORARIO;
        String CREATED_AT = Contract.Column.CREATED_AT;
        String FROM = Contract.Column.FROM;
        String TO = Contract.Column.TO;

        DbHelper dbHelper= new DbHelper(getActivity().getActionBar().getThemedContext());//Instancia de DbHelper
        SQLiteDatabase db=dbHelper.getWritableDatabase();//Obtener instancia de BD

        Cursor cursor = db.query(Contract.SERVICIO, null,null, null, null, null, null);
        records = new Object[cursor.getCount()][3];
        int i=0;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String hora = cursor.getString(cursor.getColumnIndex(HORARIO));
                String created_at = cursor.getString(cursor.getColumnIndex(CREATED_AT));
                int from = cursor.getInt(cursor.getColumnIndex(FROM));
                int to = cursor.getInt(cursor.getColumnIndex(TO));

                records[i][0] = name;
                records[i][1] = hora+" "+getString(R.string.from)+" "+from+" "+getString(R.string.to)+" "+to;
                records[i][2] = created_at;

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