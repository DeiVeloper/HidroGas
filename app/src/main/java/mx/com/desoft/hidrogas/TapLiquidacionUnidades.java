package mx.com.desoft.hidrogas;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.bussines.UnidadesBussines;
import mx.com.desoft.hidrogas.model.Empleado;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.PersonalTO;
import mx.com.desoft.hidrogas.to.PipasTO;
import mx.com.desoft.hidrogas.to.ViajesTO;

import static mx.com.desoft.hidrogas.R.id.fecha;


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
    private PipasBussines pipasBussines;
    private List<ViajesTO> viajesTO = new ArrayList<>();
    private LoginActivity login;
    private Integer variacion = 0;
    private Integer porcentajePipa = 0;
    private Integer noPipa = 0;
    private Long fecha = 0L;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_liquidacion, container, false);
        liquidacionBussines = new LiquidacionBussines();
        unidadesBussines = new UnidadesBussines();
        inicializarComponentes();
        inicializarEventos();
        //inicializarViajes();
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
        pipasBussines = new PipasBussines();
        List<String> listSpinner = pipasBussines.getAllPipas(viewGroup.getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(viewGroup.getContext(),
                android.R.layout.simple_spinner_item,listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRuta.setAdapter(adapter);

        spinnerRuta.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,View v, int position, long id) {
                        noPipa = ((Long) parent.getSelectedItemId()).intValue();
                        liquidacionesTO = new LiquidacionesTO();
                        List<PersonalTO> listaPersonal = unidadesBussines.obtenerPersonal(viewGroup, ((Long) parent.getSelectedItemId()).intValue());
                        porcentajePipa = unidadesBussines.getPorcentajePipa(viewGroup, ((Long) parent.getSelectedItemId()).intValue());
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
                    try {
                        calcularVariacion();
                        getFechaActual();
                        setLiquidacion();
                        setViajes();
                        liquidacionBussines.guardarLiquidacion(viewGroup, liquidacionesTO, viajesTO);
                        Toast.makeText(viewGroup.getContext(), "Se guardaron los datos con éxito.", Toast.LENGTH_LONG).show();
                        //List<LiquidacionesTO> lista = liquidacionBussines.getAllLiquidaciones(viewGroup);
                        //for (LiquidacionesTO l: lista) {
                          //  Log.d("Error " +l.toString(), "Liquidacion " +l.getIdLiquidacion() + " " + l.getNominaChofer() + " " +l.getNominaAyudante()+" " + l.getPorcentajeInicial()
                           //         +" " + l.getPorcentajeFinal()+ " " +l.getTotalizadorInicial() + " " +l.getTotalizadorFinal());
                       // }
                    } catch (Exception e){
                        Toast.makeText(viewGroup.getContext(), "Error al guardar la liquidación.", Toast.LENGTH_LONG).show();
                        Log.d("Error" + e.getStackTrace(),"" + e.getMessage());
                    }
                }
            }
        });

        editTextNoChofer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    PersonalTO chofer = unidadesBussines.getChoferPipa(viewGroup, noPipa, Integer.valueOf(s.toString()));
                    if (chofer != null)
                    textViewNombreChofer.setText(chofer.getNombre() + " " + chofer.getApellidoPaterno() + " " + chofer.getApellidoMaterno());
                }

                Toast.makeText(viewGroup.getContext(),"Buscando Chofer", Toast.LENGTH_LONG).show();

            }
        });

        editTextNoAyudante.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    PersonalTO ayudante = unidadesBussines.getAyudantePipa(viewGroup, noPipa, Integer.valueOf(s.toString()));
                    if (ayudante != null)
                    textViewNombreAyudante.setText(ayudante.getNombre() + " " + ayudante.getApellidoPaterno() + " " + ayudante.getApellidoMaterno());
                }
                Toast.makeText(viewGroup.getContext(),"Buscando Ayudante", Toast.LENGTH_LONG).show();
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
        } else if(TextUtils.isEmpty(editTextSalida_1.getText().toString()) || TextUtils.isEmpty(editTextLlegada_1.getText().toString())
                || TextUtils.isEmpty(editTextTotInicial_1.getText().toString()) || TextUtils.isEmpty(editTextTotFinal_1.getText().toString())){
            Toast.makeText(viewGroup.getContext(),"Debe de ingresar al menos un viaje, favor de validar.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void setLiquidacion(){
        liquidacionesTO = new LiquidacionesTO();
        liquidacionesTO.setNoPipa(((Long)spinnerRuta.getSelectedItemId()).intValue());
        liquidacionesTO.setNominaChofer(editTextNoChofer.getText().toString());
        liquidacionesTO.setNominaAyudante(editTextNoAyudante.getText().toString());
        liquidacionesTO.setFechaRegistro(fecha);
        liquidacionesTO.setNominaRegistro("00001");
        if (variacion < 0 || variacion >2){
            liquidacionesTO.setVariacion(variacion);
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

        if (!TextUtils.isEmpty(editTextSalida_2.getText().toString())) {
            ViajesTO viaje_2 = new ViajesTO();
            viaje_2.setPorcentajeInicial(Integer.valueOf(editTextSalida_2.getText().toString()));
            viaje_2.setPorcentajeFinal(Integer.valueOf(editTextLlegada_2.getText().toString()));
            viaje_2.setTotalizadorInicial(Integer.valueOf(editTextTotInicial_2.getText().toString()));
            viaje_2.setTotalizadorFinal(Integer.valueOf(editTextTotFinal_2.getText().toString()));
            viajesTO.add(viaje_2);
        }

        if (!TextUtils.isEmpty(editTextSalida_3.getText().toString())) {
            ViajesTO viaje_3 = new ViajesTO();
            viaje_3.setPorcentajeInicial(Integer.valueOf(editTextSalida_3.getText().toString()));
            viaje_3.setPorcentajeFinal(Integer.valueOf(editTextLlegada_3.getText().toString()));
            viaje_3.setTotalizadorInicial(Integer.valueOf(editTextTotInicial_3.getText().toString()));
            viaje_3.setTotalizadorFinal(Integer.valueOf(editTextTotFinal_3.getText().toString()));
            viajesTO.add(viaje_3);
        }
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

    private void calcularVariacion(){
        Integer salida_1 = !TextUtils.isEmpty(editTextSalida_1.getText().toString()) ? Integer.valueOf(editTextSalida_1.getText().toString()) : 0;
        Integer llegada_1 = !TextUtils.isEmpty(editTextLlegada_1.getText().toString()) ? Integer.valueOf(editTextLlegada_1.getText().toString()) : 0;
        Integer totIni_1 = !TextUtils.isEmpty(editTextTotInicial_1.getText().toString()) ? Integer.valueOf(editTextTotInicial_1.getText().toString()) : 0;
        Integer salida_2 = !TextUtils.isEmpty(editTextSalida_2.getText().toString()) ? Integer.valueOf(editTextSalida_2.getText().toString()) : 0;
        Integer llegada_2 = !TextUtils.isEmpty(editTextLlegada_2.getText().toString()) ? Integer.valueOf(editTextLlegada_2.getText().toString()) : 0;
        Integer salida_3 = !TextUtils.isEmpty(editTextSalida_3.getText().toString()) ? Integer.valueOf(editTextSalida_3.getText().toString()) : 0;
        Integer llegada_3 = !TextUtils.isEmpty(editTextLlegada_3.getText().toString()) ? Integer.valueOf(editTextLlegada_3.getText().toString()) : 0;
        Integer totFinal_1 = !TextUtils.isEmpty(editTextTotFinal_1.getText().toString()) ? Integer.valueOf(editTextTotFinal_1.getText().toString()) : 0;
        Integer totFinal_2 = !TextUtils.isEmpty(editTextTotFinal_2.getText().toString()) ? Integer.valueOf(editTextTotFinal_2.getText().toString()) : 0;
        Integer totFinal_3 = !TextUtils.isEmpty(editTextTotFinal_3.getText().toString()) ? Integer.valueOf(editTextTotFinal_3.getText().toString()) : 0;

        variacion = ((salida_1 + salida_2 + salida_3) - (llegada_1 - llegada_2 - llegada_3)) * porcentajePipa  -
                (totIni_1 - (totFinal_1 != 0 ? totFinal_1 : totFinal_2 != 0 ? totFinal_2 : totFinal_3 != 0 ? totFinal_3 :0));

        textViewVariacion.setText(variacion.toString());
        if (variacion < 0 || variacion >2){
            labelAlerta.setText("¡ALERTA!");
        }   else{
            labelAlerta.setText("");
        }

    }

    private void getFechaActual(){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date soloFecha = formato.parse(formato.format(new Date()));
            fecha = soloFecha.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
