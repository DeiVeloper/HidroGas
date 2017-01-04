package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by erick.martinez on 25/11/2016.
 */

public class LlenarPipa extends Activity {
    Button btnCancelar, btnGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llenar_pipa);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(MainActivity.class);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(LlenarPipa.class);
            }
        });
    }

    public void chargePage(Class clase){
        Intent btnAccion = new Intent (this, clase);
        startActivity(btnAccion);
    }
}
