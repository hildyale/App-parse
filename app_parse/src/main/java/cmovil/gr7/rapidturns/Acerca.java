package cmovil.gr7.rapidturns;

import android.app.Activity;
import android.os.Bundle;

public class Acerca extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boolean tema = getIntent().getExtras().getBoolean("tema");
        if (!tema) {
            super.setTheme(R.style.teal);
        }
        setContentView(R.layout.activity_acerca);
    }
}
