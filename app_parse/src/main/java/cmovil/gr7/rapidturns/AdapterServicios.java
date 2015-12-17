package cmovil.gr7.rapidturns;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by hildy on 13/11/2015.
 */
public class AdapterServicios extends ArrayAdapter {

    public Object[][] records;
    public int color;
    public AdapterServicios(Context context,Object[][] A,int a) {
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
                    R.layout.item_servicios,
                    parent,
                    false);
        }

        //Obteniendo instancias de los elementos
        TextView nombre = (TextView)listItemView.findViewById(R.id.nombre);
        TextView horario = (TextView)listItemView.findViewById(R.id.horario);
        ImageView icono = (ImageView)listItemView.findViewById(R.id.icono);


        //Obteniendo instancia de la Tarea en la posición actual
        nombre.setText(String.valueOf(records[position][0]));
        nombre.setTextColor(color);
        horario.setText(String.valueOf(records[position][1]));
        horario.setTextColor(color);
        icono.setImageResource(R.drawable.service);


        //Devolver al ListView la fila creada
        return listItemView;

    }
}
