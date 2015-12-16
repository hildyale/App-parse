package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Reservar extends Activity {
    private TextView name;
    private Spinner horario;
    private int time;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);
        final String Name = getIntent().getExtras().getString("name") ;
        name = (TextView) findViewById(R.id.name);
        name.setText(getIntent().getExtras().getString("nombre"));

        horario = (Spinner) findViewById(R.id.horario);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        horario.setAdapter(adapter);


        horario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                time = pos + 5;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dNow = new Date( );
                SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy.MM.dd");

                    DbHelper dbHelper = new DbHelper(getActionBar().getThemedContext());//Instancia de DbHelper
                    SQLiteDatabase db = dbHelper.getWritableDatabase();//Obtener instancia de BD
                    ContentValues values = new ContentValues();

                    values.clear();
                    values.put(Contract.Column.NAME, Name);
                    values.put(Contract.Column.HORARIO, time);
                    values.put(Contract.Column.CREATED_AT, ft.format(dNow) + "");

                    db.insertWithOnConflict(Contract.CITA, null, values,
                            SQLiteDatabase.CONFLICT_IGNORE);

                    System.exit(1);
                }

        });
    }

    }




