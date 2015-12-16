package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class VerLocal extends Activity {
    private Button empleados,servicios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_local);
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, MostrarEmpleadosCliente.newInstance())
                .commit();

        empleados = (Button) findViewById(R.id.empleados);
        empleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MostrarEmpleadosCliente.newInstance())
                        .commit();
            }
        });

        servicios = (Button) findViewById(R.id.servicios);
        servicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MostrarServiciosCliente.newInstance())
                        .commit();
            }
        });
    }
}
