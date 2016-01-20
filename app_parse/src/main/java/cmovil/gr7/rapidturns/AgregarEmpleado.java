package cmovil.gr7.rapidturns;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AgregarEmpleado extends Activity {
    private Button add;
    private EditText name;
    private EditText lastname;
    private RadioGroup sex;
    private RadioButton male;
    private String[] daysString={"L","M","W","J","V","S"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        add = (Button) findViewById(R.id.create);
        name = (EditText) findViewById(R.id.name);
        lastname = (EditText) findViewById(R.id.lastname);
        sex = (RadioGroup) findViewById(R.id.sex);
        male = (RadioButton) findViewById(R.id.male);
        male.setChecked(true);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText()+"";
                String Lastname = lastname.getText()+"";
                String sexo;
                if (male.isChecked())
                {
                    sexo = "M";
                }else{
                    sexo = "F";
                }

                if (Name.equals("") || Lastname.equals("")){
                    Error(R.string.error_field_required);
                }else {
                    final ProgressDialog dialog = new ProgressDialog(AgregarEmpleado.this);
                    dialog.setMessage("Posting...");
                    dialog.show();

                    ParseObject Lunes = new ParseObject("Dia");
                    ParseACL acl1 = new ParseACL();
                    acl1.setPublicReadAccess(true);
                    acl1.setPublicWriteAccess(true);
                    Lunes.setACL(acl1);

                    ParseObject Martes = new ParseObject("Dia");
                    ParseACL acl2 = new ParseACL();
                    acl2.setPublicReadAccess(true);
                    acl2.setPublicWriteAccess(true);
                    Martes.setACL(acl2);

                    ParseObject Miercoles = new ParseObject("Dia");
                    ParseACL acl3 = new ParseACL();
                    acl3.setPublicReadAccess(true);
                    acl3.setPublicWriteAccess(true);
                    Miercoles.setACL(acl3);

                    ParseObject Jueves = new ParseObject("Dia");
                    ParseACL acl4 = new ParseACL();
                    acl4.setPublicReadAccess(true);
                    acl4.setPublicWriteAccess(true);
                    Jueves.setACL(acl4);

                    ParseObject Viernes = new ParseObject("Dia");
                    ParseACL acl5 = new ParseACL();
                    acl5.setPublicReadAccess(true);
                    acl5.setPublicWriteAccess(true);
                    Viernes.setACL(acl5);

                    ParseObject Sabado = new ParseObject("Dia");
                    ParseACL acl6 = new ParseACL();
                    acl6.setPublicReadAccess(true);
                    acl6.setPublicWriteAccess(true);
                    Sabado.setACL(acl6);

                    ParseObject Domingo = new ParseObject("Dia");
                    ParseACL acl7 = new ParseACL();
                    acl7.setPublicReadAccess(true);
                    acl7.setPublicWriteAccess(true);
                    Domingo.setACL(acl7);

                    /*ParseObject dias[] = {Lunes,Martes,Miercoles,Jueves,Viernes,Sabado,Domingo};
                    for(int i=0;i<7;i++){
                        ParseObject temp = dias[i];
                        temp.put("hora8",null);
                        temp.put("hora9",null);
                        temp.put("hora10",null);
                        temp.put("hora11",null);
                        temp.put("hora12",null);
                        temp.put("hora13",null);
                        temp.put("hora14",null);
                        temp.put("hora15",null);
                        temp.put("hora16",null);
                        temp.put("hora17",null);
                        temp.put("hora18",null);
                    }*/

                    ParseObject semana = new ParseObject("Semana");
                    semana.put("Lunes", Lunes);
                    semana.put("Martes", Martes);
                    semana.put("Miercoles", Miercoles);
                    semana.put("Jueves", Jueves);
                    semana.put("Viernes", Viernes);
                    semana.put("Sabado", Sabado);
                    semana.put("Domingo", Domingo);
                    ParseACL acl8 = new ParseACL();
                    acl8.setPublicReadAccess(true);
                    acl8.setPublicWriteAccess(true);
                    semana.setACL(acl8);

                    ParseObject values = new ParseObject("Empleado");
                    values.put(Contract.Column.NAME, Name + " " + Lastname);
                    values.put(Contract.Column.HORARIO, semana);
                    values.put(Contract.Column.SEX, sexo);
                    values.put("local", ParseUser.getCurrentUser());
                    ParseACL acl = new ParseACL();
                    acl.setPublicReadAccess(true);
                    acl.setPublicWriteAccess(true);
                    values.setACL(acl);
                    values.pinInBackground();
                    values.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                dialog.dismiss();
                                finish();
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }

    public void Error (int a){
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
