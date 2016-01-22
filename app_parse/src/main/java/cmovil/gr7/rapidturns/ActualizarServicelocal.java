package cmovil.gr7.rapidturns;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ActualizarServicelocal extends IntentService {
    private Handler mHandler;
    Boolean show;
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }

    public ActualizarServicelocal() {
        super("ActualizarServicelocal");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent != null) {
            show = intent.getBooleanExtra("show",false);
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if ((ni != null) && (ni.isConnected())) {
                if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    borrar();
                    citas();
                    empleados();
                    servicios();
                    semana();
                    dia();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(show) {
                                Toast.makeText(ActualizarServicelocal.this, R.string.update, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    // If we have a network connection but no logged in user, direct
                    // the person to log in or sign up.
                    startActivity(new Intent(this, Login.class));
                }
            } else {
                // If there is no connection, let the user know the sync didn't happen
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(show) {
                            sendBroadcast(new Intent("ccmovil.gr7.rapidturns.NEW_EMPLEADOS"));
                            Toast.makeText(ActualizarServicelocal.this, R.string.nosynced, Toast.LENGTH_LONG).show();
                        }else{
                            sendBroadcast(new Intent("ccmovil.gr7.rapidturns.NEW_EMPLEADOSv2"));
                        }
                    }
                });
            }
        }
    }

    public void borrar(){
        ParseObject.unpinAllInBackground();
    }


    public void citas() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cita");
        query.whereEqualTo("local", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> citas, ParseException e) {
                if (e == null) {
                    int size = citas.size();
                    for (int i = 0; i < size; i++) {
                        ParseObject cita = citas.get(i);
                        cita.pinInBackground();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage() + " citas",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void empleados() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Empleado");
        query.whereEqualTo("local", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
           public void done(List<ParseObject> empleados, ParseException e) {
               if (e == null) {
                   int size = empleados.size();
                   for (int i = 0; i < size; i++) {
                       ParseObject empleado = empleados.get(i);
                       empleado.pinInBackground();
                   }
                   if(show) {
                       sendBroadcast(new Intent("ccmovil.gr7.rapidturns.NEW_EMPLEADOS"));
                   }else{
                       sendBroadcast(new Intent("ccmovil.gr7.rapidturns.NEW_EMPLEADOSv2"));
                   }
               } else {
                   Toast.makeText(
                           getApplicationContext(),
                           e.getMessage()+ "empleados",
                           Toast.LENGTH_LONG).show();
               }
           }
       }

        );
    }

    public void servicios(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Servicio");
        query.whereEqualTo("local", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> servicios, ParseException e) {
                if (e == null) {
                    int size = servicios.size();
                    for (int i = 0; i < size; i++) {
                        ParseObject servicio = servicios.get(i);
                        servicio.pinInBackground();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage()+ "servicios",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void semana(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Semana");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> semanas, ParseException e) {
                if (e == null) {
                    int size = semanas.size();
                    for (int i = 0; i < size; i++) {
                        ParseObject semana = semanas.get(i);
                        semana.pinInBackground();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage()+ "semanas",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void dia(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Dia");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> servicios, ParseException e) {
                if (e == null) {
                    int size = servicios.size();
                    for (int i = 0; i < size; i++) {
                        ParseObject servicio = servicios.get(i);
                        servicio.pinInBackground();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage()+ "servicios",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
