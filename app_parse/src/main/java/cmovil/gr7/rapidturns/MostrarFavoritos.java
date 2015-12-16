package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostrarFavoritos extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mCurrentSelectedPosition=0;
    private ListView lista;
    private Object[][] records;

    public static MostrarFavoritos newInstance(int sectionNumber) {
        MostrarFavoritos fragment = new MostrarFavoritos();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MostrarFavoritos() {
        // Required empty public constructor
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Object[] o = (Object[]) lista.getItemAtPosition(info.position);
        final String Id = (String) o[1];

        switch (item.getItemId()) {
            case 0:

                String name = (String) o[0];
                Intent intent = new Intent(getActivity().getApplicationContext(), VerLocal.class);
                intent.putExtra("name", name);
                intent.putExtra("Id", Id);
                intent.putExtra("favorite", true);
                startActivity(intent);
                return true;

            case 1:
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Favorites");
                query1.fromLocalDatastore();
                query1.include("local");
                query1.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> favorites, ParseException e) {
                        int size = favorites.size();
                        for (int i = 0; i < size; i++) {
                            ParseObject favorite = favorites.get(i);
                            ParseObject local = (ParseObject)favorite.get("local");
                            String id = local.getObjectId();
                            if(id.equals(Id)){
                                favorite.deleteInBackground();
                                favorite.unpinInBackground();
                            }

                        }
                    }
                });
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.ListView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            Object[] o = (Object[]) lista.getItemAtPosition(info.position);
            menu.setHeaderTitle(o[0]+"");
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lista = (ListView) inflater.inflate(R.layout.lista, container, false);
        records();
        /*lista.setAdapter(new ArrayAdapter<String>(
                getActivity().getActionBar().getThemedContext(),
                R.layout.item_locales,
                R.id.name,
                getResources().getStringArray(R.array.favoritas)));*/
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object[] o = (Object[]) lista.getItemAtPosition(position);
                String Id = (String) o[1];
                String name = (String) o[0];
                Intent intent = new Intent(getActivity().getApplicationContext(), VerLocal.class);
                intent.putExtra("name", name);
                intent.putExtra("Id", Id);
                intent.putExtra("favorite", true);
                startActivity(intent);

            }
        });
        registerForContextMenu(lista);
        lista.setItemChecked(mCurrentSelectedPosition, true);
        return lista;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ClientActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void records() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
        query.fromLocalDatastore();
        query.include("local");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> favorites, ParseException e) {
                if (e == null) {
                    String NAME = Contract.Column.NAME;
                    int size = favorites.size();
                    records = new Object[size][2];
                    for (int i = 0; i < size; i++) {
                        ParseObject favorite = favorites.get(i);
                        ParseObject local = (ParseObject)favorite.get("local");
                        String name = local.getString(NAME);
                        String id = local.getObjectId();
                        records[i][0] = name;
                        records[i][1] = id;
                    }
                    lista.setAdapter(new AdapterLocales(
                            getActivity().getApplicationContext(),
                            records, "#000000"));
                } else {
                    // handle Parse Exception here
                }
            }
        });
    }

}
