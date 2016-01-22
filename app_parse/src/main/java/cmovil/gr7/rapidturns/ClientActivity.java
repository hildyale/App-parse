package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.parse.Parse;
import com.parse.ParseUser;


public class ClientActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int Position;
    private AlertDialog.Builder builder,builder2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(ClientActivity.this);
        builder.setTitle(R.string.exit);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        builder2 = new AlertDialog.Builder(ClientActivity.this);
        builder2.setTitle(R.string.logout);
        builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ParseUser.logOut();
                finish();
                Intent cerrar = new Intent(ClientActivity.this, DispatchActivity.class);
                startActivity(cerrar);
            }
        });
        builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        mNavigationDrawerFragment = (NavigationDrawerFragment)
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
        Position = position;
        FragmentManager fragmentManager = getFragmentManager();
            switch (position) {
                case 0:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MostrarCitas.newInstance(position + 1))
                            .commit();
                    break;
                case 1:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MostrarLocales.newInstance(position + 1))
                            .commit();
                    break;
                case 2:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MostrarFavoritos.newInstance(position + 1))
                            .commit();
                    break;
            }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
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
            getMenuInflater().inflate(R.menu.global, menu);
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
                Intent a = new Intent(ClientActivity.this,ActualizarService.class);
                a.putExtra("show", true);
                startService(a);
                return true;
            case R.id.acerca:
                Intent c = new Intent(ClientActivity.this,Acerca.class);
                c.putExtra("tema", false);
                startActivity(c);
                return true;
            case R.id.salir:
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.cerrar_sesion:
                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                return true;
            default:
                return false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("Client ", "on resume");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Client ", "on pause " + ParseUser.getCurrentUser().getUsername());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Client ", "on destroy");
    }
}
