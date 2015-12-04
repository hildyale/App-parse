package cmovil.gr7.rapidturns;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarServicio extends Activity {
    private Button add;
    private EditText name;
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
        setContentView(R.layout.activity_servicio);
        add = (Button) findViewById(R.id.create);
        name = (EditText) findViewById(R.id.name);
        days[0] = (CheckBox) findViewById(R.id.monday);
        days[1] = (CheckBox) findViewById(R.id.tuesday);
        days[2] = (CheckBox) findViewById(R.id.wednesday);
        days[3] = (CheckBox) findViewById(R.id.thursday);
        days[4] = (CheckBox) findViewById(R.id.friday);
        days[5] = (CheckBox) findViewById(R.id.saturday);
        from = (Spinner) findViewById(R.id.from);
        to = (Spinner) findViewById(R.id.to);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(adapter);
        to.setAdapter(adapter);

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                fromPosition = pos + 5;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                toPosition = pos + 5;
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText() + "";
                String hora = "";
                for (int i = 0; i < 6; i++) {
                    horario = days[i];
                    if (horario.isChecked()) {
                        hora = hora + daysString[i];
                    }
                }

                Date dNow = new Date();
                SimpleDateFormat ft =
                        new SimpleDateFormat("yyyy.MM.dd");

                if (Name.equals("")) {
                    Error(R.string.error_field_required);
                } else {
                    if (hora.equals("")) {
                        Error(R.string.errordays);

                    } else {
                        if (toPosition - fromPosition < 1) {
                            Error(R.string.error_schedule);
                        } else {

                            final ProgressDialog dialog = new ProgressDialog(AgregarServicio.this);
                            dialog.setMessage("Posting...");
                            dialog.show();

                            ParseObject values = new ParseObject("Servicio");
                            values.put(Contract.Column.NAME, Name);
                            values.put(Contract.Column.HORARIO, hora);
                            values.put(Contract.Column.FROM, fromPosition);
                            values.put(Contract.Column.TO, toPosition);
                            values.put("local", ParseUser.getCurrentUser());
                            ParseACL acl = new ParseACL();
                            acl.setPublicReadAccess(true);
                            values.setACL(acl);

                            values.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                        }
                    }
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
