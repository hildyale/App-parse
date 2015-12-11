package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostrarLocales extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private int mCurrentSelectedPosition=0;
    private Object[][] records;

    public static MostrarLocales newInstance(int sectionNumber) {
        MostrarLocales fragment = new MostrarLocales();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MostrarLocales() {
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
                R.id.text1,
                records));*/
        lista.setItemChecked(mCurrentSelectedPosition, true);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object[] o = (Object[]) lista.getItemAtPosition(position);
                String Id = (String) o[1];
                String name = (String) o[0];
                Intent intent = new Intent(getActivity().getApplicationContext(), VerLocal.class);
                intent.putExtra("name", name);
                intent.putExtra("Id", Id);
                startActivity(intent);

            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object[] o = (Object[]) lista.getItemAtPosition(position);
                String Id = (String) o[1];
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.getInBackground(Id, new GetCallback<ParseUser>() {
                    public void done(ParseUser object, ParseException e) {
                        if (e == null) {
                            final ProgressDialog dialog = new ProgressDialog(getActivity().getApplicationContext());
                            dialog.setMessage("Adding...");
                            dialog.show();

                            ParseObject values = new ParseObject("Favorites");
                            values.put("user", ParseUser.getCurrentUser());
                            values.put("local",object);

                            ParseACL acl = new ParseACL();
                            acl.setPublicReadAccess(true);
                            values.setACL(acl);

                            values.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            // something went wrong
                        }
                    }
                });
                return false;
            }
        });
        return lista;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ClientActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void records() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo("type", "local");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> locales, ParseException e) {
                if (e == null) {
                    String NAME = Contract.Column.NAME;
                    int size = locales.size();
                    records = new Object[size][2];
                    for (int i = 0; i < size; i++) {
                        ParseObject local = locales.get(i);
                        String name = local.getString(NAME);
                        String id = local.getObjectId();
                        records[i][0] = name;
                        records[i][1] = id;
                    }
                    lista.setAdapter(new AdapterLocales(
                            getActivity().getBaseContext(),
                            records,"#000000"));
                } else {
                    // handle Parse Exception here
                }
            }
        });
    }

}
