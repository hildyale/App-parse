package cmovil.gr7.rapidturns;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Favoritas extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int mCurrentSelectedPosition=0;
    private ListView lista;

    public static Favoritas newInstance(int sectionNumber) {
        Favoritas fragment = new Favoritas();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Favoritas() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lista = (ListView) inflater.inflate(R.layout.lista, container, false);
        lista.setAdapter(new ArrayAdapter<String>(
                getActivity().getActionBar().getThemedContext(),
                R.layout.item_peluquerias,
                R.id.text1,
                getResources().getStringArray(R.array.favoritas)));
        lista.setItemChecked(mCurrentSelectedPosition, true);
        return lista;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

}
