package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class VerLocal extends Activity {
    private Button empleados,servicios;
    private ParseUser local;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_local);

        String name = getIntent().getExtras().getString("name");
        final String Id =  getIntent().getExtras().getString("Id");
        getActionBar().setTitle(name);
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MostrarEmpleadosCliente.newInstance(Id))
                .commit();

        empleados = (Button) findViewById(R.id.empleados);
        empleados.setBackgroundColor(getResources().getColor(R.color.teal));
        empleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empleados.setBackgroundColor(getResources().getColor(R.color.teal));
                servicios.setBackgroundColor(getResources().getColor(R.color.teal2));
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MostrarEmpleadosCliente.newInstance(Id))
                        .commit();
            }
        });

        servicios = (Button) findViewById(R.id.servicios);
        servicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicios.setBackgroundColor(getResources().getColor(R.color.teal));
                empleados.setBackgroundColor(getResources().getColor(R.color.teal2));
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MostrarServiciosCliente.newInstance(Id))
                        .commit();
            }
        });
    }

}
