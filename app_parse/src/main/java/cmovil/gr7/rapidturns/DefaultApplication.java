package cmovil.gr7.rapidturns;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by alejandro.isazad on 18/11/15.
 */
public class DefaultApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "MNyzWmBiJ8RMxcBiK4htIzpl8DGfb6mhLch5Bpxd", "n6qExbI9ZIidD0OojVSBuVUHF2PVU0C145nLTNGh");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }

}
