package cmovil.gr7.rapidturns;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarEmpleado extends Activity {
    private Button add;
    private EditText name;
    private EditText lastname;
    private RadioGroup sex;
    private RadioButton male;
    private Spinner from,to;
    private CheckBox[] days = new CheckBox[6];
    private CheckBox horario;
    private String[] daysString={"L","M","W","J","V","S"};
    private int fromPosition,toPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado);
        add = (Button) findViewById(R.id.create);
        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.lastname);
        days[0] = (CheckBox) findViewById(R.id.monday);
        days[1] = (CheckBox) findViewById(R.id.tuesday);
        days[2] = (CheckBox) findViewById(R.id.wednesday);
        days[3] = (CheckBox) findViewById(R.id.thursday);
        days[4] = (CheckBox) findViewById(R.id.friday);
        days[5] = (CheckBox) findViewById(R.id.saturday);
        sex = (RadioGroup) findViewById(R.id.sex);
        male = (RadioButton) findViewById(R.id.male);
        male.setChecked(true);
        from = (Spinner) findViewById(R.id.from);
        to = (Spinner) findViewById(R.id.to);
       /* final String[] hours = getResources().getStringArray(R.array.hours);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getActionBar().getThemedContext(),
                R.layout.item_spinner,
                R.id.text1,
                hours);*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(adapter);
        to.setAdapter(adapter);

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                fromPosition = pos+5;
                /*String[] a = new String[16-pos];
                for(int i=0;i<16-pos;i++){
                   a[i] = hours[i+pos];
                }
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                        getActionBar().getThemedContext(),
                        R.layout.item_spinner,
                        R.id.text1,
                        a);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                to.setAdapter(adapter);*/
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                toPosition = pos+5;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText()+"";
                String Lastname = lastname.getText()+"";
                String hora = "";
                for(int i=0;i<6;i++){
                    horario = days[i];
                    if(horario.isChecked()){
                        hora = hora+daysString[i];
                    }
                }
                String sexo;
                if (male.isChecked())
                {
                    sexo = "M";
                }else{
                    sexo = "F";
                }

                Date dNow = new Date( );
                SimpleDateFormat ft =
                        new SimpleDateFormat ("yyyy.MM.dd");

                if (Name.equals("") || Lastname.equals("")){
                    Error(R.string.error_field_required);
                }else {
                    if (hora.equals("")) {
                        Error(R.string.errordays);

                    } else {
                        if (toPosition-fromPosition<1) {
                            Error(R.string.error_schedule);
                        } else {

                            DbHelper dbHelper = new DbHelper(getActionBar().getThemedContext());//Instancia de DbHelper
                            SQLiteDatabase db = dbHelper.getWritableDatabase();//Obtener instancia de BD
                            ContentValues values = new ContentValues();

                            values.clear();
                            values.put(Contract.Column.NAME, Name + " " + Lastname);
                            values.put(Contract.Column.HORARIO, hora);
                            values.put(Contract.Column.CREATED_AT, ft.format(dNow) + "");
                            values.put(Contract.Column.SEX, sexo);
                            values.put(Contract.Column.FROM, fromPosition);
                            values.put(Contract.Column.TO, toPosition);

                            db.insertWithOnConflict(Contract.EMPLEADO, null, values,
                                    SQLiteDatabase.CONFLICT_IGNORE);

                            System.exit(1);
                        }
                    }
                }

            }
        });
    }

    public void Error (int a){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActionBar().getThemedContext());
        builder.setMessage(a)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
