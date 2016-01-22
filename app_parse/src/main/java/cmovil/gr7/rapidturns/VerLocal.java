package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class VerLocal extends Activity {
    private Button empleados,servicios;
    private ParseUser local;
    private Context mContext;
    private boolean allready=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_local);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        mContext= getApplicationContext();
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
                servicios.setBackgroundColor(getResources().getColor(R.color.teal3));
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
                empleados.setBackgroundColor(getResources().getColor(R.color.teal3));
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MostrarServiciosCliente.newInstance(Id))
                        .commit();
            }
        });
    }

    public void allready(){
        allready = true;
    }

    public void check(){
        final String Id = getIntent().getExtras().getString("Id");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.fromLocalDatastore();
        query.include("local");
        try {
            List<ParseObject> favorites = query.find();
            int size = favorites.size();
            for (int i = 0; i < size; i++) {
                ParseObject favorite = favorites.get(i);
                ParseUser local = (ParseUser) favorite.get("local");
                String id = local.getObjectId();
                if (id.equals(Id)) {
                    allready();
                }
            }
        }catch (com.parse.ParseException e1) {
            e1.printStackTrace();
        }
    }

    public void addFavorite(){
        check();
        if(!allready) {
            String Id = getIntent().getExtras().getString("Id");
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.fromLocalDatastore();
            query.getInBackground(Id, new GetCallback<ParseUser>() {
                public void done(ParseUser object, ParseException e) {
                    if (e == null) {
                        ParseObject values = new ParseObject("Favorites");
                        values.put("user", ParseUser.getCurrentUser());
                        values.put("local", object);

                        ParseACL acl = new ParseACL();
                        acl.setPublicReadAccess(true);
                        acl.setPublicWriteAccess(true);
                        values.setACL(acl);
                        values.pinInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(
                                        mContext,
                                        "Added",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        values.saveEventually();
                    } else {
                        // something went wrong
                    }
                }
            });
        }else{
            Toast.makeText(
                    mContext,
                    R.string.allready,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Boolean favorite = getIntent().getExtras().getBoolean("favorite");
        if (!favorite) {
            getMenuInflater().inflate(R.menu.menu_ver_local, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.addFavorites:
                addFavorite();
                return true;
            default:
                return false;
        }
    }
}
