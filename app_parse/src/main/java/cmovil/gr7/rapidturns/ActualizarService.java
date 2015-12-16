package cmovil.gr7.rapidturns;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;

public class ActualizarService extends IntentService {


    public ActualizarService() {
        super("ActualizarService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if ((ni != null) && (ni.isConnected())) {
                if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    locales();
                    citas();
                    favoritos();
                    empleados();
                    servicios();
                } else {
                    // If we have a network connection but no logged in user, direct
                    // the person to log in or sign up.
                    startActivity(new Intent(this, Login.class));
                }
            } else {
                // If there is no connection, let the user know the sync didn't happen
                Toast.makeText(
                        getApplicationContext(),
                        R.string.nosynced,
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    public void locales() {
        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.fromLocalDatastore();
        query1.whereEqualTo("type", "local");
        query1.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> favorites, ParseException e) {
                int size = favorites.size();
                for (int i = 0; i < size; i++) {
                    ParseObject favorite = favorites.get(i);
                    favorite.unpinInBackground();
                }
            }
        });
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("type", "local");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> locales, ParseException e) {
                if (e == null) {
                    int size = locales.size();
                    for (int i = 0; i < size; i++) {
                        ParseObject local = locales.get(i);
                        local.pinInBackground();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage()+" locales",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void citas() {
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Cita");
        query1.fromLocalDatastore();
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> favorites, ParseException e) {
                int size = favorites.size();
                for (int i = 0; i < size; i++) {
                    ParseObject favorite = favorites.get(i);
                    favorite.unpinInBackground();
                }
            }
        });
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Cita");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.include("local");
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
                            e.getMessage()+" citas",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void favoritos(){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Favorites");
        query1.fromLocalDatastore();
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> favorites, ParseException e) {
                    int size = favorites.size();
                    for (int i = 0; i < size; i++) {
                        ParseObject favorite = favorites.get(i);
                        favorite.unpinInBackground();
                    }
            }
        });
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.include("local");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> favorites, ParseException e) {
                if (e == null) {
                    int size = favorites.size();
                    for (int i = 0; i < size; i++) {
                        ParseObject favorite = favorites.get(i);
                        favorite.pinInBackground();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            e.getMessage()+" favoritos",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void empleados() {
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Empleado");
        query1.fromLocalDatastore();
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> favorites, ParseException e) {
                int size = favorites.size();
                for (int i = 0; i < size; i++) {
                    ParseObject favorite = favorites.get(i);
                    favorite.unpinInBackground();
                }
            }
        });
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Empleado");
        query.findInBackground(new FindCallback<ParseObject>() {
           public void done(List<ParseObject> empleados, ParseException e) {
               if (e == null) {
                   int size = empleados.size();
                   for (int i = 0; i < size; i++) {
                       ParseObject empleado = empleados.get(i);
                       empleado.pinInBackground();
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
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Servicio");
        query1.fromLocalDatastore();
        query1.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> favorites, ParseException e) {
                int size = favorites.size();
                for (int i = 0; i < size; i++) {
                    ParseObject favorite = favorites.get(i);
                    favorite.unpinInBackground();
                }
            }
        });
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Servicio");
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
        Toast.makeText(
                getApplicationContext(),
                R.string.update,
                Toast.LENGTH_LONG).show();
    }
}
