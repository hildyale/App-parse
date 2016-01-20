package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;


public class VerCalendario extends Activity {
    private TextView name;
    private int time;
    private Button add;
    private ParseUser user;
    private ImageView images[][] = new ImageView[7][11];
    private String dias[] = {"Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"};
    private String horas[] = {"hora8","hora9","hora10","hora11","hora12","hora13","hora14","hora15","hora16","hora17","hora18"};
    private static String[] days;
    private static String[] hours = {"8:00 am","9:00am","10:00 am","11:00 am","12:00 pm","1:00 pm","2:00 pm","3:00 pm","4:00 pm","5:00 pm","6:00 pm"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        String[] diasTemp = {getString(R.string.monday),getString(R.string.tuesday),getString(R.string.wednesday),getString(R.string.thursday),getString(R.string.friday),getString(R.string.saturday),getString(R.string.sunday)};
        days = diasTemp;
        name = (TextView) findViewById(R.id.name);
        name.setText(getIntent().getExtras().getString("nombre"));
        user();
        //lunes
        images[0][0] = (ImageView)findViewById(R.id.l8);
        images[0][1] = (ImageView)findViewById(R.id.l9);
        images[0][2] = (ImageView)findViewById(R.id.l10);
        images[0][3] = (ImageView)findViewById(R.id.l11);
        images[0][4] = (ImageView)findViewById(R.id.l12);
        images[0][5] = (ImageView)findViewById(R.id.l13);
        images[0][6] = (ImageView)findViewById(R.id.l14);
        images[0][7] = (ImageView)findViewById(R.id.l15);
        images[0][8] = (ImageView)findViewById(R.id.l16);
        images[0][9] = (ImageView)findViewById(R.id.l17);
        images[0][10] = (ImageView)findViewById(R.id.l18);
        //martes
        images[1][0] = (ImageView)findViewById(R.id.m8);
        images[1][1] = (ImageView)findViewById(R.id.m9);
        images[1][2] = (ImageView)findViewById(R.id.m10);
        images[1][3] = (ImageView)findViewById(R.id.m11);
        images[1][4] = (ImageView)findViewById(R.id.m12);
        images[1][5] = (ImageView)findViewById(R.id.m13);
        images[1][6] = (ImageView)findViewById(R.id.m14);
        images[1][7] = (ImageView)findViewById(R.id.m15);
        images[1][8] = (ImageView)findViewById(R.id.m16);
        images[1][9] = (ImageView)findViewById(R.id.m17);
        images[1][10] = (ImageView)findViewById(R.id.m18);
        //miercoles
        images[2][0] = (ImageView)findViewById(R.id.w8);
        images[2][1] = (ImageView)findViewById(R.id.w9);
        images[2][2] = (ImageView)findViewById(R.id.w10);
        images[2][3] = (ImageView)findViewById(R.id.w11);
        images[2][4] = (ImageView)findViewById(R.id.w12);
        images[2][5] = (ImageView)findViewById(R.id.w13);
        images[2][6] = (ImageView)findViewById(R.id.w14);
        images[2][7] = (ImageView)findViewById(R.id.w15);
        images[2][8] = (ImageView)findViewById(R.id.w16);
        images[2][9] = (ImageView)findViewById(R.id.w17);
        images[2][10] = (ImageView)findViewById(R.id.w18);
        //Jueves
        images[3][0] = (ImageView)findViewById(R.id.j8);
        images[3][1] = (ImageView)findViewById(R.id.j9);
        images[3][2] = (ImageView)findViewById(R.id.j10);
        images[3][3] = (ImageView)findViewById(R.id.j11);
        images[3][4] = (ImageView)findViewById(R.id.j12);
        images[3][5] = (ImageView)findViewById(R.id.j13);
        images[3][6] = (ImageView)findViewById(R.id.j14);
        images[3][7] = (ImageView)findViewById(R.id.j15);
        images[3][8] = (ImageView)findViewById(R.id.j16);
        images[3][9] = (ImageView)findViewById(R.id.j17);
        images[3][10] = (ImageView)findViewById(R.id.j18);
        //Viernes
        images[4][0] = (ImageView)findViewById(R.id.v8);
        images[4][1] = (ImageView)findViewById(R.id.v9);
        images[4][2] = (ImageView)findViewById(R.id.v10);
        images[4][3] = (ImageView)findViewById(R.id.v11);
        images[4][4] = (ImageView)findViewById(R.id.v12);
        images[4][5] = (ImageView)findViewById(R.id.v13);
        images[4][6] = (ImageView)findViewById(R.id.v14);
        images[4][7] = (ImageView)findViewById(R.id.v15);
        images[4][8] = (ImageView)findViewById(R.id.v16);
        images[4][9] = (ImageView)findViewById(R.id.v17);
        images[4][10] = (ImageView)findViewById(R.id.v18);
        //Sabado
        images[5][0] = (ImageView)findViewById(R.id.s8);
        images[5][1] = (ImageView)findViewById(R.id.s9);
        images[5][2] = (ImageView)findViewById(R.id.s10);
        images[5][3] = (ImageView)findViewById(R.id.s11);
        images[5][4] = (ImageView)findViewById(R.id.s12);
        images[5][5] = (ImageView)findViewById(R.id.s13);
        images[5][6] = (ImageView)findViewById(R.id.s14);
        images[5][7] = (ImageView)findViewById(R.id.s15);
        images[5][8] = (ImageView)findViewById(R.id.s16);
        images[5][9] = (ImageView)findViewById(R.id.s17);
        images[5][10] = (ImageView)findViewById(R.id.s18);
        //Domingo
        images[6][0] = (ImageView)findViewById(R.id.d8);
        images[6][1] = (ImageView)findViewById(R.id.d9);
        images[6][2] = (ImageView)findViewById(R.id.d10);
        images[6][3] = (ImageView)findViewById(R.id.d11);
        images[6][4] = (ImageView)findViewById(R.id.d12);
        images[6][5] = (ImageView)findViewById(R.id.d13);
        images[6][6] = (ImageView)findViewById(R.id.d14);
        images[6][7] = (ImageView)findViewById(R.id.d15);
        images[6][8] = (ImageView)findViewById(R.id.d16);
        images[6][9] = (ImageView)findViewById(R.id.d17);
        images[6][10] = (ImageView)findViewById(R.id.d18);

    }

    public void user(){
        String id = getIntent().getExtras().getString("id");
        String type = getIntent().getExtras().getString("type");
        ParseQuery<ParseObject> query = ParseQuery.getQuery(type);
        query.fromLocalDatastore();
        query.include("horario");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    ParseObject semana = (ParseObject)object.get("horario");
                    setCalendar(semana);
                } else {
                    // something went wrong
                }
            }
        });
    }

    public void setCalendar(ParseObject semana){
        ImageView a;
        for(int i=0;i<7;i++){
            ParseObject dia = (ParseObject)semana.get(dias[i]);
            for(int j=0;j<11;j++){
                a = images[i][j];
                    String cita =  (String)dia.get(horas[j]);
                    if(cita!=null) {
                        a.setImageResource(R.drawable.backfalse);
                        setlistener(a, i, j, cita);
                    }
                    else{
                        a.setImageResource(R.drawable.backtrue);
                    }
            }

        }
    }

    public void setlistener(final ImageView a,final int i,final int j,final String title){
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerCalendario.this);
                String msn = getIntent().getExtras().getString("nombre")+"\n"+days[i]+" "+hours[j];
                builder.setMessage(msn)
                        .setTitle(title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                }
            });
    }

}




