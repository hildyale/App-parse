package cmovil.gr7.rapidturns;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.parse.ParseObject;

import java.io.FileReader;

public class Actualizar extends IntentService {

    public Actualizar() {
        super("Actualizar");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String Name = intent.getExtras().getString(Contract.Column.NAME) ;
        String Horario = intent.getExtras().getString(Contract.Column.HORARIO) ;
        int From = intent.getExtras().getInt(Contract.Column.FROM) ;
        int To = intent.getExtras().getInt(Contract.Column.TO) ;
        int Id = intent.getExtras().getInt(Contract.Column.ID) ;
        ParseObject testObject = new ParseObject("Servicio");
        testObject.put(Contract.Column.NAME, Name);
        testObject.put(Contract.Column.HORARIO, Horario);
        testObject.put(Contract.Column.FROM, From);
        testObject.put(Contract.Column.TO, To);
        testObject.put(Contract.Column.ID, Id);
                testObject.fetchInBackground();

    }

}
