package mx.com.desoft.hidrogas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.desoft.hidrogas.bussines.LiquidacionBussines;
import mx.com.desoft.hidrogas.bussines.UnidadesBussines;
import mx.com.desoft.hidrogas.model.Empleado;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.PersonalTO;
import mx.com.desoft.hidrogas.to.ViajesTO;


/**
 * Created by David on 03/12/16.
 */

public class TapLiquidacionUnidades extends Fragment{
    private static final Integer CHOFER = 2;
    private static final Integer AYUDANTE = 3;

    private ViewGroup viewGroup;
    private Button btnImprimir, btnGuardarLiquidacion;
    private EditText editTextEconomico, editTextNoChofer, editTextNoAyudante, editTextSalida_1, editTextLlegada_1, editTextTotInicial_1, editTextTotFinal_1,
        editTextSalida_2, editTextLlegada_2, editTextTotInicial_2, editTextTotFinal_2, editTextSalida_3, editTextLlegada_3, editTextTotInicial_3, editTextTotFinal_3;
    private TextView textViewNombreChofer, textViewNombreAyudante, labelAlerta, textViewVariacion;
    private Spinner spinnerRuta;
    private LiquidacionBussines liquidacionBussines;
    private UnidadesBussines unidadesBussines;
    private LiquidacionesTO liquidacionesTO;
    private List<ViajesTO> viajesTO = new ArrayList<>();
    private LoginActivity login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_liquidacion, container, false);
        liquidacionBussines = new LiquidacionBussines();
        unidadesBussines = new UnidadesBussines();
        inicializarComponentes();
        inicializarEventos();
        inicializarViajes();
        return viewGroup;
    }

    private void inicializarComponentes() {
        btnImprimir = (Button) viewGroup.findViewById(R.id.btnImprimir);
        btnGuardarLiquidacion = (Button) viewGroup.findViewById(R.id.btnGuardarLiquidacion);
        spinnerRuta = (Spinner) viewGroup.findViewById(R.id.spinner_ruta);
        editTextEconomico = (EditText) viewGroup.findViewById(R.id.input_economico);

        editTextSalida_1 = (EditText) viewGroup.findViewById(R.id.input_Salida_1);
        editTextLlegada_1 = (EditText) viewGroup.findViewById(R.id.input_Llegada_1);
        editTextTotInicial_1 = (EditText) viewGroup.findViewById(R.id.input_totInicial_1);
        editTextTotFinal_1 = (EditText) viewGroup.findViewById(R.id.input_totFinal_1);

        editTextSalida_2 = (EditText) viewGroup.findViewById(R.id.input_Salida_2);
        editTextLlegada_2 = (EditText) viewGroup.findViewById(R.id.input_Llegada_2);
        editTextTotInicial_2 = (EditText) viewGroup.findViewById(R.id.input_totInicial_2);
        editTextTotFinal_2 = (EditText) viewGroup.findViewById(R.id.input_totFinal_2);

        editTextSalida_3 = (EditText) viewGroup.findViewById(R.id.input_Salida_3);
        editTextLlegada_3 = (EditText) viewGroup.findViewById(R.id.input_Llegada_3);
        editTextTotInicial_3 = (EditText) viewGroup.findViewById(R.id.input_totInicial_3);
        editTextTotFinal_3 = (EditText) viewGroup.findViewById(R.id.input_totFinal_3);

        editTextNoChofer = (EditText) viewGroup.findViewById(R.id.input_chofer);
        textViewNombreChofer = (TextView) viewGroup.findViewById(R.id.input_nombreChofer);
        editTextNoAyudante = (EditText) viewGroup.findViewById(R.id.input_ayudante);
        textViewNombreAyudante = (TextView) viewGroup.findViewById(R.id.input_nombreAyudante);
        labelAlerta = (TextView) viewGroup.findViewById(R.id.labelAlerta);
        textViewVariacion = (TextView) viewGroup.findViewById(R.id.input_variacion);
    }

    private void inicializarEventos()   {
        spinnerRuta.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,View v, int position, long id) {
                        liquidacionesTO = new LiquidacionesTO();
                        List<PersonalTO> listaPersonal = unidadesBussines.obtenerPersonal(viewGroup, ((Long) parent.getSelectedItemId()).intValue());
                        List<ViajesTO> listaViajes = liquidacionBussines.getPorcentajeInicialAnterior(viewGroup, ((Long) parent.getSelectedItemId()).intValue());
                        setEmpleadosPipa(listaPersonal);
                        setViajesVista(listaViajes);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        btnImprimir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Long fecha = new Date().getTime();

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date fechaS = format.parse(fecha.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String fechal = format.format(fecha);

                editTextEconomico.setText(fechal);
                Toast.makeText(viewGroup.getContext(), "Hola " + fecha.intValue() + "fecha 2: "+ fechal, Toast.LENGTH_LONG).show();
                Log.d("Error "  + fecha, "Fecha " + fechal);

                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
            }
        });

        btnGuardarLiquidacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (validarLiquidacion()) {
                setLiquidacion();
                    setViajes();
                    liquidacionBussines.guardarLiquidacion(viewGroup, liquidacionesTO, viajesTO);
                    Toast.makeText(viewGroup.getContext(), "Se guardaron los datos con éxito.", Toast.LENGTH_LONG).show();
            }
            }
        });
    }

    private boolean validarLiquidacion(){
        if (spinnerRuta.getSelectedItemId() == 0 ){
            Toast.makeText(viewGroup.getContext(),"Favor de seleccionar una Ruta.", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(editTextEconomico.getText().toString())){
            Toast.makeText(viewGroup.getContext(),"El campo Economico no puede ir vacio, favor de validar.", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(editTextSalida_1.getText().toString())){
            Toast.makeText(viewGroup.getContext(),"Debe de ingresar al menos un viaje, favor de validar.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void setLiquidacion(){
        login = new LoginActivity();
        liquidacionesTO = new LiquidacionesTO();
        liquidacionesTO.setNoPipa(((Long)spinnerRuta.getSelectedItemId()).intValue());
        liquidacionesTO.setNominaChofer(editTextNoChofer.getText().toString());
        liquidacionesTO.setNominaAyudante(editTextNoAyudante.getText().toString());
        liquidacionesTO.setFechaRegistro(new Date().getTime());
        liquidacionesTO.setNominaRegistro("00001");
        if (!TextUtils.isEmpty(labelAlerta.getText().toString())){
            liquidacionesTO.setVariacion(Integer.valueOf(textViewVariacion.getText().toString()));
            liquidacionesTO.setAlerta(1);
        } else{
            liquidacionesTO.setVariacion(0);
            liquidacionesTO.setAlerta(0);
        }


    }

    private void setViajes(){
        ViajesTO viaje_1 = new ViajesTO();
        viaje_1.setPorcentajeInicial(Integer.valueOf(editTextSalida_1.getText().toString()));
        viaje_1.setPorcentajeFinal(Integer.valueOf(editTextLlegada_1.getText().toString()));
        viaje_1.setTotalizadorInicial(Integer.valueOf(editTextTotInicial_1.getText().toString()));
        viaje_1.setTotalizadorFinal(Integer.valueOf(editTextTotFinal_1.getText().toString()));
        viajesTO.add(viaje_1);

        ViajesTO viaje_2 = new ViajesTO();
        viaje_2.setPorcentajeInicial(Integer.valueOf(editTextSalida_2.getText().toString()));
        viaje_2.setPorcentajeFinal(Integer.valueOf(editTextLlegada_2.getText().toString()));
        viaje_2.setTotalizadorInicial(Integer.valueOf(editTextTotInicial_2.getText().toString()));
        viaje_2.setTotalizadorFinal(Integer.valueOf(editTextTotFinal_2.getText().toString()));
        viajesTO.add(viaje_2);

        ViajesTO viaje_3 = new ViajesTO();
        viaje_3.setPorcentajeInicial(Integer.valueOf(editTextSalida_3.getText().toString()));
        viaje_3.setPorcentajeFinal(Integer.valueOf(editTextLlegada_3.getText().toString()));
        viaje_3.setTotalizadorInicial(Integer.valueOf(editTextTotInicial_3.getText().toString()));
        viaje_3.setTotalizadorFinal(Integer.valueOf(editTextTotFinal_3.getText().toString()));
        viajesTO.add(viaje_3);
    }

    private void inicializarViajes(){
            editTextSalida_1.setText("0");
            editTextLlegada_1.setText("0");
            editTextTotInicial_1.setText("0");
            editTextTotFinal_1.setText("0");

            editTextSalida_2.setText("0");
            editTextLlegada_2.setText("0");
            editTextTotInicial_2.setText("0");
            editTextTotFinal_2.setText("0");

            editTextSalida_3.setText("0");
            editTextLlegada_3.setText("0");
            editTextTotInicial_3.setText("0");
            editTextTotFinal_3.setText("0");
    }

    private boolean validarViajes(){
        if(TextUtils.isEmpty(editTextSalida_1.getText().toString())){
            editTextSalida_1.setText("0");
            return false;
        }
        if(TextUtils.isEmpty(editTextLlegada_1.getText().toString())){
            editTextLlegada_1.setText("0");
            return false;
        }
        if(TextUtils.isEmpty(editTextTotInicial_1.getText().toString())){
            editTextTotInicial_1.setText("0");
            return false;
        }
        if (TextUtils.isEmpty(editTextTotFinal_1.getText().toString())){
            editTextTotFinal_1.setText("0");
            return false;
        }

        if(TextUtils.isEmpty(editTextSalida_2.getText().toString())){
            editTextSalida_2.setText("0");
            return false;
        }
        if(TextUtils.isEmpty(editTextLlegada_2.getText().toString())){
            editTextLlegada_2.setText("0");
            return false;
        }
        if(TextUtils.isEmpty(editTextTotInicial_2.getText().toString())){
            editTextTotInicial_2.setText("0");
            return false;
        }
        if (TextUtils.isEmpty(editTextTotFinal_2.getText().toString())){
            editTextTotFinal_2.setText("0");
            return false;
        }

        if(TextUtils.isEmpty(editTextSalida_3.getText().toString())){
            editTextSalida_3.setText("0");
            return false;
        }
        if(TextUtils.isEmpty(editTextLlegada_3.getText().toString())){
            editTextLlegada_3.setText("0");
            return false;
        }
        if(TextUtils.isEmpty(editTextTotInicial_3.getText().toString())){
            editTextTotInicial_3.setText("0");
            return false;
        }
        if (TextUtils.isEmpty(editTextTotFinal_3.getText().toString())){
            editTextTotFinal_3.setText("0");
            return false;
        }
        return true;
    }

    private void setEmpleadosPipa(List<PersonalTO> listaPersonal) {
        if (listaPersonal.isEmpty()){
            editTextNoChofer.setText("");
            textViewNombreChofer.setText("");
            editTextNoAyudante.setText("");
            textViewNombreAyudante.setText("");
        }
        for (PersonalTO personal: listaPersonal) {
            if (personal.getTipoEmpleado().equals(CHOFER)){
                editTextNoChofer.setText(personal.getNomina());
                textViewNombreChofer.setText(personal.getNombre() + " " + personal.getApellidoPaterno() + " " + personal.getApellidoMaterno());
            }   else  /*if (personal.getTipoEmpleado().equals(AYUDANTE)) */{
                editTextNoAyudante.setText(personal.getNomina());
                textViewNombreAyudante.setText(personal.getNombre() + " " + personal.getApellidoPaterno() + " " + personal.getApellidoMaterno());
            }
        }
    }

    private void setViajesVista(List<ViajesTO> listaViajes){
        if (listaViajes.isEmpty()){
            editTextSalida_1.setText("");
            editTextTotInicial_1.setText("");
        }
        for (ViajesTO viajes: listaViajes) {

        }
    }

    private Integer getFechaActual(){
        Calendar fecha = Calendar.getInstance();
        int year = fecha.get(Calendar.YEAR);
        int month = fecha.get(Calendar.MONTH) + 1;
        int day = fecha.get(Calendar.DAY_OF_MONTH);
        return Integer.valueOf(day+""+month+""+year);
    }

}
