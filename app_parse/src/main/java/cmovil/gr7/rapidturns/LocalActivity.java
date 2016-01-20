package cmovil.gr7.rapidturns;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class LocalActivity extends Activity
        implements NavigationDrawerFragmentLocal.NavigationDrawerCallbacks {

    private NavigationDrawerFragmentLocal mNavigationDrawerFragment;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        mNavigationDrawerFragment = (NavigationDrawerFragmentLocal)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
            switch (position) {
                case 0:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MostrarEmpleados.newInstance(position + 1))
                            .commit();
                    break;
                case 1:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MostrarServicios.newInstance(position + 1))
                            .commit();
                    break;

            }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section5);
                break;
            case 2:
                mTitle = getString(R.string.title_section6);
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.local, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.actualizar:
                Intent a = new Intent(LocalActivity.this,ActualizarServicelocal.class);
                a.putExtra("show",true);
                startService(a);
                return true;

            case R.id.addemployed:
                Intent employed = new Intent(LocalActivity.this,AgregarEmpleado.class);
                startActivity(employed);
                finish();
                return true;

            case R.id.addservice:
                Intent service = new Intent(LocalActivity.this,AgregarServicio.class);
                startActivity(service);
                finish();
                return true;

            case R.id.action_settings:

                return true;
            case R.id.action_example:

                return true;
            case R.id.salir:
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.cerrar_sesion:
                finish();
            Intent cerrar = new Intent(this, Login.class);
            startActivity(cerrar);
                return true;
            default:
                return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
