package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.TextView;
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
    private Context mContext;
    private boolean dataexists=false;
    private ParseUser user;
    private TextView text;
    private int color;


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
        user();
        dataexists();
        color = getResources().getColor(R.color.darkgrey);
        View v = inflater.inflate(R.layout.listacliente, container, false);
        mContext = getActivity().getApplicationContext();
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
                    Intent intent = new Intent(getActivity().getApplicationContext(), Reservar.class);
                    intent.putExtra("nombre", str);
                    intent.putExtra("id", o[3] + "");
                    intent.putExtra("local", o[4] + "");
                    intent.putExtra("type", "Empleado");
                    startActivity(intent);
                }
            });
        }else{
            lista.setVisibility(View.GONE);
            String Text = text.getText()+"";
            text.setTextColor(getResources().getColor(R.color.teal3));
            text.setText(Text+getResources().getString(R.string.title_section5));
        }
        return v;
    }

    public void dataExistsTrue(){
        dataexists = true;
    }

    public void dataexists(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Empleado");
        query.fromLocalDatastore();
        query.whereEqualTo("local", user);
        try{
            List<ParseObject> empleados = query.find();
            if (empleados.size() != 0){
                dataExistsTrue();
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public void setUser(ParseUser a){
        user = a;
    }

    public void user(){
        String Id = getArguments().getString("Id");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.fromLocalDatastore();
        try{
            ParseUser object = query.get(Id);
            setUser(object);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public void records() {
        final String Id = getArguments().getString("Id");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Empleado");
        query.fromLocalDatastore();
        query.whereEqualTo("local", user);
        if (isAdded()) {
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> empleados, ParseException e) {
                    if (e == null) {
                        String NAME = Contract.Column.NAME;
                        String SEX = Contract.Column.SEX;
                        int size = empleados.size();
                        records = new Object[size][5];
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
                            records[i][4] = Id;
                        }
                        lista.setAdapter(new AdapterEmpleados(
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
