package cmovil.gr7.rapidturns;

/**
 * Created by alejo on 3/12/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.parse.ParseUser;


public class DispatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser().getUsername() != null) {
            if(ParseUser.getCurrentUser().getString("type").equals("cliente")){
                startActivity(new Intent(this, ClientActivity.class));
            }else{
                startActivity(new Intent(this, LocalActivity.class));
            }
        } else {
            startActivity(new Intent(this, Login.class));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ParseUser.getCurrentUser().getUsername() != null) {
            if(ParseUser.getCurrentUser().getString("type").equals("cliente")){
                startActivity(new Intent(this, ClientActivity.class));
            }else{
                startActivity(new Intent(this, LocalActivity.class));
            }
        } else {
            startActivity(new Intent(this, Login.class));
        }
    }
}
