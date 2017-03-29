package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.to.PipasTO;
import mx.com.desoft.utils.Utils;

public class AgregarPipas extends Activity {
    EditText txtNoPipa, txtCapacidad;
    Button btnCancelar, btnGuardar;
    private PipasTO pipasTO;
    private PipasBussines pipasBussines;
    private Long fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipas);
        Utils utils = new Utils();
        pipasTO = new PipasTO();
        pipasBussines = new PipasBussines();
        fecha = utils.consultaFechaLong(new Date(), "dd/MM/yyyy");
        inicializarComponentes();
        cargarEventos();
    }

    private void inicializarComponentes() {
        txtNoPipa = (EditText)findViewById(R.id.txtEconomico);
        txtCapacidad = (EditText)findViewById(R.id.txtCapacidad);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
    }

    private void cargarEventos() {
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean guardado = guardar();
                if (guardado){
                    returnTab(2);
                }
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnTab(2);
            }
        });
    }

    private boolean guardar(){
        try{
            boolean resultadoGuardar;
            if (TextUtils.isEmpty(txtNoPipa.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
            } else {
                pipasTO.setNoPipa(Integer.parseInt(txtNoPipa.getText().toString()));
                pipasTO.setCapacidad(Integer.parseInt(txtCapacidad.getText().toString()));
                pipasTO.setFechaRegistro(fecha);
                pipasTO.setNominaRegistro(LoginActivity.personalTO.getNomina());
                resultadoGuardar = pipasBussines.guardar(pipasTO);
                if (resultadoGuardar) {
                    Toast.makeText(getApplicationContext(), "La pipa número: " + pipasTO.getNoPipa() + " se ha registrdo correctamente.", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "La pipa número: " + pipasTO.getNoPipa() + " ya existe.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Ha ocurrido un error al guardar la pipa.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return false;
    }

    private void returnTab(int posicion){
        Intent go = new Intent(getApplicationContext(), MainActivity.class);
        go.putExtra("viewpager_position", posicion);
        startActivity(go);
    }
}
