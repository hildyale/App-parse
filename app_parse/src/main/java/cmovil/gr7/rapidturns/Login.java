package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * A login screen that offers login via email/password.
 */
public class Login extends Activity  {
    EditText username,password;
    Button loginButton,signupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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
                    ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                if(user.getString("type").equals("cliente")){
                                    startActivity(new Intent(Login.this, ClientActivity.class));
                                }else{
                                    startActivity(new Intent(Login.this, LocalActivity.class));
                                }
                                finish();
                            } else {
                                Error(R.string.nouser);
                            }
                        }
                    });
                }
            }
        });

    }

        public void Error (int a){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActionBar().getThemedContext());
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



