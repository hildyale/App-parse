package cmovil.gr7.rapidturns;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

public class Empleado extends Activity {
    private Button add;
    private EditText name;
    private EditText lastname;
    private EditText horario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado);
        add = (Button) findViewById(R.id.create);
        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.lastname);
        horario = (EditText) findViewById(R.id.horario);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = name.getText()+""+lastname.getText();
                String hora = horario.getText()+"";
                Date fecha = new Date();

                DbHelper dbHelper= new DbHelper(getActionBar().getThemedContext());//Instancia de DbHelper
                SQLiteDatabase db=dbHelper.getWritableDatabase();//Obtener instancia de BD
                ContentValues values = new ContentValues();

                values.clear();
                values.put(Contract.Column.NAME, fullname);
                values.put(Contract.Column.HORARIO, hora);
                values.put(Contract.Column.CREATED_AT, fecha + "");

                db.insertWithOnConflict(Contract.EMPLEADO, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);

                System.exit(2);
            }
        });
    }



}
