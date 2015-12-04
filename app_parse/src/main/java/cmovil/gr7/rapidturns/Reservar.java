package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Reservar extends Activity {
    private TextView name;
    private Spinner horario;
    private int time;
    private Button add;
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);
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
                 user();
                }

        });
    }

    public void user(){
        String id = getIntent().getExtras().getString("id");
        String type = getIntent().getExtras().getString("type");
        ParseQuery<ParseObject> query = ParseQuery.getQuery(type);
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    records(object);
                } else {
                    // something went wrong
                }
            }
        });
    }

    public void records(final ParseObject quien){
        final String type = getIntent().getExtras().getString("type");
        String local = getIntent().getExtras().getString("local");
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(local, new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    final ProgressDialog dialog = new ProgressDialog(Reservar.this);
                    dialog.setMessage("Posting...");
                    dialog.show();

                    ParseObject values = new ParseObject("Cita");
                    values.put(Contract.Column.HORARIO, time);
                    values.put("user", ParseUser.getCurrentUser());
                    values.put(type,quien);
                    values.put("local",object);

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
                } else {
                    // something went wrong
                }
            }
        });
    }

    }




