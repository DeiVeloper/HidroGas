package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListaLlenadoPipas extends Activity {

    Button btnLlenar, btnRegresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llenado_pipas);
        btnLlenar = (Button)findViewById(R.id.btnLlenar);
        btnRegresar = (Button)findViewById(R.id.btnRegresar);
        btnLlenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chargePage(LlenarPipa.class);
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clase = "LlenarPipa";
                chargePage(LoginActivity.class);
            }
        });
    }

    public void chargePage(Class clase){
        Intent btnAccion = new Intent (this, clase);
        startActivity(btnAccion);
    }
}
