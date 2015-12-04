package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MostrarFavoritos() {
        // Required empty public constructor
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
                Intent intent = new Intent(getActivity().getActionBar().getThemedContext(), VerLocal.class);
                intent.putExtra("name", name);
                intent.putExtra("Id", Id);
                startActivity(intent);

            }
        });
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
        query.whereEqualTo("user", ParseUser.getCurrentUser());
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
                            getActivity().getActionBar().getThemedContext(),
                            records, "#000000"));
                } else {
                    // handle Parse Exception here
                }
            }
        });
    }

}
