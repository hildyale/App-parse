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

    public class AdapterEmpleados extends ArrayAdapter {

        public Object[][] records;
        public String color;
        public AdapterEmpleados(Context context,Object[][] A,String a) {
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
                        R.layout.item_empleados,
                        parent,
                        false);
            }

            //Obteniendo instancias de los elementos
            TextView nombre = (TextView)listItemView.findViewById(R.id.nombre);
            TextView tiempo = (TextView)listItemView.findViewById(R.id.tiempo);
            TextView horario = (TextView)listItemView.findViewById(R.id.horario);
            ImageView icono = (ImageView)listItemView.findViewById(R.id.icono);


            //Obteniendo instancia de la Tarea en la posición actual
            nombre.setText(String.valueOf(records[position][0]));
            horario.setText(String.valueOf(records[position][1]));
            horario.setTextColor(Color.parseColor(color));
            tiempo.setText(String.valueOf(records[position][2]));
            if(String.valueOf(records[position][3]).equals("M")) {
                icono.setImageResource(R.drawable.man);
            }else{
                icono.setImageResource(R.drawable.woman);
            }

            //Devolver al ListView la fila creada
            return listItemView;

        }
    }

