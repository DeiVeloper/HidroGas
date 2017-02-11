package mx.com.desoft.hidrogas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import java.util.Date;
import java.util.List;

import mx.com.desoft.hidrogas.bussines.LiquidacionBussines;
import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.bussines.UnidadesBussines;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.PersonalTO;
import mx.com.desoft.hidrogas.to.ViajesTO;


/**
 * Created by David on 03/12/16.
 */

public class TapLiquidacionUnidades extends Fragment{
    private static final Integer CHOFER = 1;
    private static final Integer AYUDANTE = 2;

    private ViewGroup viewGroup;
    private Button btnImprimir, btnGuardarLiquidacion;
    private EditText editTextEconomico, editTextNoChofer, editTextNoAyudante, editTextSalida_1, editTextLlegada_1, editTextTotInicial_1, editTextTotFinal_1,
            editTextSalida_2, editTextLlegada_2, editTextTotInicial_2, editTextTotFinal_2, editTextSalida_3, editTextLlegada_3, editTextTotInicial_3, editTextTotFinal_3,
            editTextAutoconsumo, editTextMedidores, editTextTraspasosRecibidos, editTextTraspasosRealizados;
    private TextView textViewNombreChofer, textViewNombreAyudante, labelAlerta, textViewVariacion;
    private Spinner spinnerRuta;
    private LiquidacionBussines liquidacionBussines;
    private UnidadesBussines unidadesBussines;
    private LiquidacionesTO liquidacionesTO;
    private PipasBussines pipasBussines;
    private List<ViajesTO> viajesTO = new ArrayList<>();
    private Float variacion = 0F;
    private Integer capacidadPipa = 0;
    private Integer idPipa;
    private Long fecha = 0L;
    private ImportarDatos datos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_liquidacion, container, false);
        liquidacionBussines = new LiquidacionBussines();
        unidadesBussines = new UnidadesBussines();
        inicializarComponentes();
        inicializarEventos();
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

        editTextAutoconsumo = (EditText) viewGroup.findViewById(R.id.autoconsumo);
        editTextMedidores= (EditText) viewGroup.findViewById(R.id.medidores);
        editTextTraspasosRecibidos = (EditText) viewGroup.findViewById(R.id.traspasosRecibidos);
        editTextTraspasosRealizados = (EditText) viewGroup.findViewById(R.id.traspasosRealizados);
    }

    private void inicializarEventos()   {
        pipasBussines = new PipasBussines();
        List<String> listSpinner = pipasBussines.getAllPipas(viewGroup.getContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(viewGroup.getContext(), android.R.layout.simple_spinner_item,listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRuta.setAdapter(adapter);

        spinnerRuta.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,View v, int position, long id) {
                        idPipa = pipasBussines.spinnerMap.get(parent.getSelectedItemPosition());
                        liquidacionesTO = new LiquidacionesTO();
                        List<PersonalTO> listaPersonal = unidadesBussines.obtenerPersonal(viewGroup, idPipa);
                        capacidadPipa = unidadesBussines.getCapacidadPipa(viewGroup, idPipa);
                        List<ViajesTO> listaViajes = liquidacionBussines.getPorcentajeInicialAnterior(viewGroup, idPipa);
                        setEmpleadosPipa(listaPersonal);
                        setViajesVista(listaViajes);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );

        btnImprimir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {}
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
                    } catch (Exception e){
                        Toast.makeText(viewGroup.getContext(), "Error al guardar la liquidación.", Toast.LENGTH_LONG).show();
                        Log.d("Error" + e.getStackTrace(),"" + e.getMessage());
                    }
                }
            }
        });

        editTextNoChofer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    PersonalTO ayudante = unidadesBussines.getChoferPipa(viewGroup,Integer.valueOf(v.getText().toString()));
                    if (ayudante != null){
                        textViewNombreChofer.setText(ayudante.getNombre() + " " + ayudante.getApellidoPaterno() + " " + ayudante.getApellidoMaterno());
                    }else{
                        textViewNombreChofer.setText("");
                    }
                    procesado = true;
                }
                return procesado;
            }
        });

        editTextNoAyudante.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    PersonalTO ayudante = unidadesBussines.getAyudantePipa(viewGroup,v.getText().toString());
                    if (ayudante != null){
                        textViewNombreAyudante.setText(ayudante.getNombre() + " " + ayudante.getApellidoPaterno() + " " + ayudante.getApellidoMaterno());
                    }else{
                        textViewNombreAyudante.setText("");
                    }
                    procesado = true;
                }
                return procesado;
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
            liquidacionesTO.setVariacion(variacion.intValue());
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
                editTextNoChofer.setText(personal.getNomina().toString());
                textViewNombreChofer.setText(personal.getNombre() + " " + personal.getApellidoPaterno() + " " + personal.getApellidoMaterno());
            }   else  if (personal.getTipoEmpleado().equals(AYUDANTE)) {
                editTextNoAyudante.setText(personal.getNomina().toString());
                textViewNombreAyudante.setText(personal.getNombre() + " " + personal.getApellidoPaterno() + " " + personal.getApellidoMaterno());
            }
        }
    }

    private void setViajesVista(List<ViajesTO> listaViajes){
        if (!listaViajes.isEmpty()) {
            for (ViajesTO viajes : listaViajes) {
                editTextTotInicial_1.setText(viajes.getTotalizadorFinal().toString());
            }
        }
    }

    private void calcularVariacion(){
        Integer venta = 0;

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
        Integer autoconsumo = !TextUtils.isEmpty(editTextAutoconsumo.getText().toString()) ? Integer.valueOf(editTextAutoconsumo.getText().toString()): 0;
        Integer medidores = !TextUtils.isEmpty(editTextMedidores.getText().toString()) ? Integer.valueOf(editTextMedidores.getText().toString()) : 0;
        Integer trasRecibidos = !TextUtils.isEmpty(editTextTraspasosRecibidos.getText().toString()) ? Integer.valueOf(editTextTraspasosRecibidos.getText().toString()) : 0;


        venta = totIni_1 - (totFinal_1 != 0 ? totFinal_1 : totFinal_2 != 0 ? totFinal_2 : totFinal_3 != 0 ? totFinal_3 :0);
        variacion = ((getPorcentaje((float)salida_1)  + getPorcentaje((float)salida_2) + getPorcentaje((float)salida_3))
                - (getPorcentaje((float)llegada_1) + getPorcentaje((float)llegada_2) + getPorcentaje((float)llegada_3))) * capacidadPipa;
        if(venta < 0){
            venta = venta * (-1);
        }
        variacion = (venta - autoconsumo - medidores - trasRecibidos) - variacion;

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

    private Float getPorcentaje(Float numero){
        return (float)(numero / 100F);
    }

}
