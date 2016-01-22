package cmovil.gr7.rapidturns;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.location.internal.LocationRequestUpdateData;

/**
 * Created by alejo on 22/01/16.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(isOnline(context)){
            Log.d("Internet: ", " Online");
            Intent a = new Intent(context,ActualizarService.class);
            a.putExtra("show", false);
            context.startService(a);
            Intent b = new Intent(context,ActualizarServicelocal.class);
            b.putExtra("show", false);
            context.startService(b);
        }else{
            Log.d("Internet: "," Offline");
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }
}
