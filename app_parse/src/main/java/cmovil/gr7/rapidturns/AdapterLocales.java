package cmovil.gr7.rapidturns;

/**
 * Created by hildy on 11/11/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterLocales extends ArrayAdapter {

    public Object[][] records;
    public String color;
    public AdapterLocales(Context context, Object[][] A, String a) {
        super(context, 0, A);
        records = A;
        color = a;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo con image_list_view.xml
            listItemView = inflater.inflate(
                    R.layout.item_locales,
                    parent,
                    false);
        }


        TextView nombre = (TextView)listItemView.findViewById(R.id.name);
        nombre.setText(String.valueOf(records[position][0]));

        return listItemView;

    }
}

