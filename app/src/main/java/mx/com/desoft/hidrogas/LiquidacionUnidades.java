package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by David on 30/11/16.
 */

public class LiquidacionUnidades extends Activity {
    private Button btnImprimir;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_liquidacion);
        btnImprimir = (Button) findViewById(R.id.btnImprimir);
        btnImprimir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Hola ", Toast.LENGTH_LONG);
                toast.show();
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
            }
        });
    }

    public void imprimirTicket(){
        btnImprimir = (Button) findViewById(R.id.btnImprimir);
        btnImprimir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Hola ", Toast.LENGTH_LONG);
                toast.show();
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
            }
        });
    }


}
