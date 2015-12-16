package cmovil.gr7.rapidturns;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Register extends Activity {
    Button signupButton;
    RadioButton cliente;
    EditText user,pass,name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cliente = (RadioButton) findViewById(R.id.client);
        cliente.setChecked(true);
        signupButton = (Button) findViewById(R.id.sign_up);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernametxt = user.getText().toString();
                String passwordtxt = pass.getText().toString();
                String emailtxt = email.getText().toString();
                String nametxt = name.getText().toString();

                if (usernametxt.equals("") || passwordtxt.equals("") || emailtxt.equals("") || nametxt.equals("")) {
                    Error(R.string.error_field_required);
                } else {
                    final ProgressDialog dialog = new ProgressDialog(Register.this);
                    dialog.setMessage("Posting...");
                    dialog.show();
                    final ParseUser user = new ParseUser();
                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.setEmail(emailtxt);
                    if(cliente.isChecked()) {
                        user.put("type", "cliente");
                    }else{
                        user.put("type","local");
                    }
                    user.put("name",nametxt);
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo ni = cm.getActiveNetworkInfo();
                    if ((ni != null) && (ni.isConnected())) {
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "Successfully Signed up!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                if(user.getString("type").equals("cliente")){
                                    startActivity(new Intent(Register.this, ClientActivity.class));
                                }else{
                                    startActivity(new Intent(Register.this, LocalActivity.class));
                                }
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Signed up error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        }
                    });
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                "No Internet",
                                Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                    }
                }
            }
        });
    }

    public void Error (int a){
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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
