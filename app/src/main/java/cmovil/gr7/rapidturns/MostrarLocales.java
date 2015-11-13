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
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostrarLocales extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView lista;
    private int mCurrentSelectedPosition=0;

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
        lista.setAdapter(new ArrayAdapter<String>(
                getActivity().getActionBar().getThemedContext(),
                R.layout.item_locales,
                R.id.text1,
                getResources().getStringArray(R.array.locales)));
        lista.setItemChecked(mCurrentSelectedPosition, true);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object o = (Object)lista.getItemAtPosition(position);
                String str = (String) o;//As you are using Default String Adapter
                if(position == 0){
                    Intent intent = new Intent(getActivity().getActionBar().getThemedContext(), VerLocal.class);
                    startActivity(intent);
                }
            }
        });
        return lista;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
