package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.to.PipasTO;

/**
 * Created by erick.martinez on 25/11/2016.
 */

public class AgregarPipas extends Activity {
    EditText txtNoPipa;
    Button btnCancelar, btnGuardar;
    private PipasTO pipasTO;
    private PipasBussines pipasBussines;
    private Long fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipas);
        pipasTO = new PipasTO();
        pipasBussines = new PipasBussines();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date soloFecha = formato.parse(formato.format(new Date()));
            fecha = soloFecha.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        inicializarComponentes();
        cargarEventos();
    }

    private void inicializarComponentes() {
        //acceder a los items de la vista
        txtNoPipa = (EditText)findViewById(R.id.txtEconomico);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
    }

    private void cargarEventos() {
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * Método para guarda la pipa
     */
    private void guardar(){
        try{
            boolean resultadoGuardar;
            if (TextUtils.isEmpty(txtNoPipa.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
            } else {
                pipasTO.setNoPipa(Integer.parseInt(txtNoPipa.getText().toString()));
                pipasTO.setFechaRegistro(fecha);
                pipasTO.setNominaRegistro("20");
                resultadoGuardar = pipasBussines.guardar(getApplicationContext(), pipasTO);
                if (resultadoGuardar) {
                    Toast.makeText(getApplicationContext(), "La pipa número: " + pipasTO.getNoPipa() + " se ha registrdo correctamente.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "La pipa número: " + pipasTO.getNoPipa() + " ya existe.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Ha ocurrido un error al guardar la pipa.", Toast.LENGTH_SHORT).show();
        }
    }

}
