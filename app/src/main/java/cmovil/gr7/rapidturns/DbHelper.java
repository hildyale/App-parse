package cmovil.gr7.rapidturns;

/**
 * Created by root on 23/10/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG=DbHelper.class.getSimpleName();

    public DbHelper(Context context){
        super(context, Contract.DB_NAME,null, Contract.DB_VERSION);
        //
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String sql=String
                .format("create table %s(%s int primary key, %s text, %s text, %s int)",
                        Contract.EMPLEADO, Contract.Column.ID,
                        Contract.Column.NAME,
                        Contract.Column.HORARIO,
                        Contract.Column.CREATED_AT);
        //Sentencia para crear tabla
        Log.d(TAG, "onCreate with SQL: " + sql);
        db.execSQL(sql);//Ejecución de la sentencia
    }

    //Se llama cada que el schema cambie(nueva versión)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int NewVersion){
        db.execSQL("drop table if exists "+ Contract.EMPLEADO);//Borrar datos
        onCreate(db);//Crear Tabla de nuevo
    }

}
