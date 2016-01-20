package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * A login screen that offers login via email/password.
 */
public class Login extends Activity  {
    EditText username,password;
    Button loginButton,signupButton;
    private IntentFilter filter1,filter2;
    private BroadcastReceiver receiver1,receiver2;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        dialog = new ProgressDialog(Login.this);
        dialog.setMessage(getResources().getString(R.string.login));

        filter1 = new IntentFilter("ccmovil.gr7.rapidturns.NEW_DATESv2");
        receiver1 =  new TimelineReceiver(true);
        registerReceiver(receiver1, filter1);
        filter2 = new IntentFilter("ccmovil.gr7.rapidturns.NEW_EMPLEADOSv2");
        receiver2 =  new TimelineReceiver(false);
        registerReceiver(receiver2, filter2);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.sign_in);
        signupButton = (Button) findViewById(R.id.sign_up);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernametxt = username.getText().toString();
                String passwordtxt = password.getText().toString();
                if (usernametxt.equals("") || passwordtxt.equals("")) {
                    Error(R.string.error_field_required);
                }else{
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo ni = cm.getActiveNetworkInfo();
                    if ((ni != null) && (ni.isConnected())) {
                        dialog.show();
                        ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    if(user.getString("type").equals("cliente")){
                                        Intent a = new Intent(Login.this,ActualizarService.class);
                                        a.putExtra("show", false);
                                        startService(a);
                                    }else{
                                        Intent a = new Intent(Login.this,ActualizarServicelocal.class);
                                        a.putExtra("show", false);
                                        startService(a);
                                    }
                                    finish();
                                } else {
                                    Error(R.string.nouser);
                                }
                            }
                        });
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                "No Internet",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    class TimelineReceiver extends BroadcastReceiver {
        Boolean cliente;
        public TimelineReceiver(Boolean a){
            cliente = a;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(cliente) {
                dialog.dismiss();
                startActivity(new Intent(Login.this, ClientActivity.class));
            }else {
                dialog.dismiss();
                startActivity(new Intent(Login.this, LocalActivity.class));
            }
        }
    }

        public void Error (int a){
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
    }

}



