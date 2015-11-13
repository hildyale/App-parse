package cmovil.gr7.rapidturns;

import android.provider.BaseColumns;

/**
 * Created by root on 23/10/15.
 */
public class Contract {
    // DB specific constants
    public static final  String DB_NAME = "RapidTurns.db";
    public static final int DB_VERSION = 1;
    public static final String EMPLEADO = "empleado";
    public static final String SERVICIO = "servicio";
    public static final String CITA = "cita";
    public static final String DEFAULT_SORT = Column.CREATED_AT + " DESC";


    public class Column{

        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String HORARIO = "horario";
        public static final String CREATED_AT = "create_at";
        public static final String SEX = "sex";
        public static final String FROM = "_from";
        public static final String TO = "_to";
    }
}