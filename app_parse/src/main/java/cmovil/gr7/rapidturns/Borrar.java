package cmovil.gr7.rapidturns;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Borrar extends IntentService {
    private Handler mHandler;
    private String dias[] = {"Lunes","Martes","Miercoles","Jueves","Viernes","Sabado","Domingo"};
    private String horas[] = {"hora8","hora9","hora10","hora11","hora12","hora13","hora14","hora15","hora16","hora17","hora18"};

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }

    public Borrar() {
        super("Borrar");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if ((ni != null) && (ni.isConnected())) {
                if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    ParseObject.unpinAllInBackground();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Borrar.this, R.string.update, Toast.LENGTH_LONG).show();
                            sendBroadcast(new Intent("ccmovil.gr7.rapidturns.NEW_DATES"));
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
                        Toast.makeText(Borrar.this, R.string.nosynced, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }


        @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
