package cmovil.gr7.rapidturns;

/**
 * Created by alejo on 3/12/15.
 */
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;


public class DispatchActivity extends Activity {
    private IntentFilter filter1,filter2;
    private BroadcastReceiver receiver1,receiver2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dispatch_activity);
        Log.d("Dispatch: ", "onCreate");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Dispatch: ","onResume");
        filter1 = new IntentFilter("ccmovil.gr7.rapidturns.NEW_DATESv2");
        receiver1 =  new TimelineReceiver(true);
        registerReceiver(receiver1, filter1);
        filter2 = new IntentFilter("ccmovil.gr7.rapidturns.NEW_EMPLEADOSv2");
        receiver2 =  new TimelineReceiver(false);
        registerReceiver(receiver2, filter2);
        if (ParseUser.getCurrentUser().getUsername() != null) {
            if(ParseUser.getCurrentUser().getString("type").equals("cliente")){
                Intent a = new Intent(DispatchActivity.this,ActualizarService.class);
                a.putExtra("show", false);
                startService(a);
            }else{
                Intent a = new Intent(DispatchActivity.this,ActualizarServicelocal.class);
                a.putExtra("show", false);
                startService(a);
            }
        } else {
            startActivity(new Intent(this, Login.class));
        }
    }

    class TimelineReceiver extends BroadcastReceiver {
        Boolean cliente;
        public TimelineReceiver(Boolean a){
            cliente = a;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(cliente) {
                startActivity(new Intent(DispatchActivity.this, ClientActivity.class));
            }else{
                startActivity(new Intent(DispatchActivity.this, LocalActivity.class));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        Log.d("Dispatch: ", "onPause");
    }

}
