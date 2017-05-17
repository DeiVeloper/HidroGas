package mx.com.desoft.hidrogas;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.com.desoft.hidrogas.bussines.LiquidacionBussines;
import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.bussines.UnidadesBussines;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.PersonalTO;
import mx.com.desoft.hidrogas.to.PipasTO;
import mx.com.desoft.hidrogas.to.ViajesTO;
import mx.com.desoft.utils.Utils;

import static mx.com.desoft.hidrogas.bussines.PipasBussines.*;

public class TapLiquidacionUnidades extends Fragment{
    private static final Integer CHOFER = 1;
    private static final Integer AYUDANTE = 2;

    private ViewGroup viewGroup;
    private LiquidacionBussines liquidacionBussines;
    private UnidadesBussines unidadesBussines;
    private LiquidacionesTO liquidacionesTO;
    private PipasBussines pipasBussines;
    private List<ViajesTO> viajesTO;
    private Float variacion = 0F;
    private PipasTO pipa;
    private Integer idPipa;
    private Integer ventaPorcentual;
    private Long idLiquidacion;
    private Integer porcentajeLlenado;
    private Componentes componentes;
    private Utils utils;
    private Bundle bundle;
    private LiquidacionesTO liquidacion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_liquidacion, container, false);
        liquidacionBussines = new LiquidacionBussines();
        unidadesBussines = new UnidadesBussines();
        componentes = new Componentes(viewGroup);
        inicializarEventos();
        componentes.deshabilitarComponentes();
        utils = new Utils();
        editarLiquidacion();
        return viewGroup;
    }

    private void inicializarEventos()   {
        pipasBussines = new PipasBussines();
        List<String> listSpinner = pipasBussines.getAllPipas();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(viewGroup.getContext(), android.R.layout.simple_spinner_item, listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        componentes.getSpinner().setAdapter(adapter);
        componentes.getSpinner().setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,View v, int position, long id) {
                        if (position > 0) {
                            idPipa = spinnerMap.get(parent.getSelectedItemPosition());
                            liquidacionesTO = new LiquidacionesTO();
                            viajesTO = new ArrayList<>();
                            pipa = new PipasTO();
                            List<PersonalTO> listaPersonal = unidadesBussines.obtenerPersonal(idPipa);
                            if (!TextUtils.isEmpty(componentes.getEconomico().getText().toString())) {
                                pipa = unidadesBussines.getCapacidadPipa(Integer.parseInt(componentes.getEconomico().getText().toString()));
                            }
                            setEmpleadosPipa(listaPersonal);
                            componentes.getClave().setText(pipa.getClavePipa());
                            porcentajeLlenado = pipasBussines.getCapacidadDiaAnteriorPipa(idPipa);
                            ViajesTO viaje = liquidacionBussines.getPorcentajeInicial(idPipa);
                            setPorcentajeTotalizador(viaje);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );

        componentes.getEconomico().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            public void afterTextChanged(Editable s) {
                componentes.habilitarCampos();
            }
        });

        componentes.getImprimir().setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                try {
                    if(!TextUtils.isEmpty(componentes.getFolio().getText())) {
                        LoginActivity.conexionBlueTooth.sendData(idLiquidacion);
                        componentes.limpiarCampos();
                        liquidacion = new LiquidacionesTO();
                        liquidacionesTO = new LiquidacionesTO();
                    }else{
                        Toast.makeText(viewGroup.getContext(), "No se ha generado un folio, favor de guardar la liquidación.", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(viewGroup.getContext(), "Error al imprimir el ticket" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        componentes.getGuardar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarLiquidacion()) {
                    calcularVariacion();
                    setLiquidacion();
                    setViajes();
                    if (bundle != null && bundle.getBoolean("bandera")) {
                        liquidacionesTO.setIdLiquidacion(liquidacion.getIdLiquidacion());
                    }
                    Toast.makeText(viewGroup.getContext(), "Se guardaron los datos con éxito.", Toast.LENGTH_LONG).show();
                    idLiquidacion = liquidacionBussines.guardarLiquidacion(liquidacionesTO, viajesTO, bundle);
                    componentes.getFolio().setText("Folio: " + idLiquidacion.toString());
                    componentes.getImprimir().setEnabled(true);
                    Toast.makeText(viewGroup.getContext(), "Se guardaron los datos con éxito.", Toast.LENGTH_LONG).show();
                }
            }
        });

        componentes.getLimpíar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                componentes.limpiarCampos();
                liquidacion = new LiquidacionesTO();
                liquidacionesTO = new LiquidacionesTO();
                bundle = null;
            }
        });

        componentes.getChofer().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    PersonalTO ayudante = unidadesBussines.getChoferPipa(Integer.valueOf(v.getText().toString()));
                    if (ayudante != null){
                        componentes.getNombreChofer().setText(ayudante.getNombre() + " " + ayudante.getApellidoPaterno() + " " + ayudante.getApellidoMaterno());
                    }else{
                        componentes.getNombreChofer().setText("");
                    }
                    procesado = true;
                }
                return procesado;
            }
        });

        componentes.getAyudante().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean procesado = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    PersonalTO ayudante = unidadesBussines.getAyudantePipa(viewGroup,v.getText().toString());
                    if (ayudante != null){
                        componentes.getNombreAyudante().setText(ayudante.getNombre() + " " + ayudante.getApellidoPaterno() + " " + ayudante.getApellidoMaterno());
                    }else{
                        componentes.getNombreAyudante().setText("");
                    }
                    procesado = true;
                }
                return procesado;
            }
        });

        componentes.getSalida2().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            public void afterTextChanged(Editable s) {
                componentes.getTotalizadorInicial2().setText(componentes.getTotalizadorFinal1().getText().toString());
            }
        });

        componentes.getSalida3().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            public void afterTextChanged(Editable s) {
                componentes.getTotalizadorInicial3().setText(componentes.getTotalizadorFinal2().getText().toString());
            }
        });
    }

    private boolean validarLiquidacion(){
        if (componentes.getSpinner().getSelectedItemId() == 0 ){
            Toast.makeText(viewGroup.getContext(),"Favor de seleccionar una Ruta.", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(componentes.getEconomico().getText().toString())){
            Toast.makeText(viewGroup.getContext(),"El campo Economico no puede ir vacio, favor de validar.", Toast.LENGTH_LONG).show();
            return false;
        } else if(TextUtils.isEmpty(componentes.getSalida1().getText().toString()) || TextUtils.isEmpty(componentes.getLlegada1().getText().toString())
                || TextUtils.isEmpty(componentes.getTotalizadorInicial1().getText().toString()) || TextUtils.isEmpty(componentes.getTotalizadorFinal1().getText().toString())){
            Toast.makeText(viewGroup.getContext(),"Debe de ingresar al menos un viaje, favor de validar.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void setLiquidacion(){
        liquidacionesTO.setNoPipa(((Long)componentes.getSpinner().getSelectedItemId()).intValue());
        liquidacionesTO.setNominaChofer(componentes.getChofer().getText().toString());
        liquidacionesTO.setChofer(componentes.getNombreChofer().getText().toString());
        liquidacionesTO.setNominaAyudante(componentes.getAyudante().getText().toString());
        liquidacionesTO.setAyudante(componentes.getNombreAyudante().getText().toString());
        liquidacionesTO.setFechaRegistro(utils.getFechaActual());
        liquidacionesTO.setNominaRegistro(LoginActivity.personalTO.getNominaRegistro().toString());
        liquidacionesTO.setPorcentajeVariacion(Float.valueOf(componentes.getPorcentajeVariacion().getText().toString()));
        liquidacionesTO.setClave(componentes.getClave().getText().toString());
        liquidacionesTO.setEconomico(componentes.getEconomico().getText().toString());

        liquidacionesTO.setAutoconsumo(TextUtils.isEmpty(componentes.getAutoconsumo().getText().toString()) ? null :
                Integer.parseInt(componentes.getAutoconsumo().getText().toString()));

        liquidacionesTO.setMedidores(TextUtils.isEmpty(componentes.getMedidores().getText().toString()) ? null :
                Integer.parseInt(componentes.getMedidores().getText().toString()));

        liquidacionesTO.setTraspasosRecibidos(TextUtils.isEmpty(componentes.getTraspasosRecibidos().getText().toString()) ? null :
                Integer.parseInt(componentes.getTraspasosRecibidos().getText().toString()));

        liquidacionesTO.setTraspasosRealizados(TextUtils.isEmpty(componentes.getTraspasosRealizados().getText().toString()) ? null :
                Integer.parseInt(componentes.getTraspasosRealizados().getText().toString()));

        if (variacion < 0 || variacion >2){
            liquidacionesTO.setVariacion(variacion.intValue());
            liquidacionesTO.setAlerta(1);
        } else{
            liquidacionesTO.setVariacion(variacion.intValue());
            liquidacionesTO.setAlerta(0);
        }
    }

    private void setViajes(){
        ViajesTO viaje1 = new ViajesTO();
        viaje1.setIdViaje(componentes.getIdViaje1());
        viaje1.setPorcentajeInicial(Integer.valueOf(componentes.getSalida1().getText().toString()));
        viaje1.setPorcentajeFinal(Integer.valueOf(componentes.getLlegada1().getText().toString()));
        viaje1.setTotalizadorInicial(Integer.valueOf(componentes.getTotalizadorInicial1().getText().toString()));
        viaje1.setTotalizadorFinal(Integer.valueOf(componentes.getTotalizadorFinal1().getText().toString()));
        viajesTO.add(viaje1);

        if (!TextUtils.isEmpty(componentes.getSalida2().getText().toString())) {
            ViajesTO viaje2 = new ViajesTO();
            viaje2.setIdViaje(componentes.getIdViaje2());
            viaje2.setPorcentajeInicial(Integer.valueOf(componentes.getSalida2().getText().toString()));
            viaje2.setPorcentajeFinal(Integer.valueOf(componentes.getLlegada2().getText().toString()));
            viaje2.setTotalizadorInicial(Integer.valueOf(componentes.getTotalizadorInicial2().getText().toString()));
            viaje2.setTotalizadorFinal(Integer.valueOf(componentes.getTotalizadorFinal2().getText().toString()));
            viajesTO.add(viaje2);
        }

        if (!TextUtils.isEmpty(componentes.getSalida3().getText().toString())) {
            ViajesTO viaje3 = new ViajesTO();
            viaje3.setIdViaje(componentes.getIdViaje3());
            viaje3.setPorcentajeInicial(Integer.valueOf(componentes.getSalida3().getText().toString()));
            viaje3.setPorcentajeFinal(Integer.valueOf(componentes.getLlegada3().getText().toString()));
            viaje3.setTotalizadorInicial(Integer.valueOf(componentes.getTotalizadorInicial3().getText().toString()));
            viaje3.setTotalizadorFinal(Integer.valueOf(componentes.getTotalizadorFinal3().getText().toString()));
            viajesTO.add(viaje3);
        }
    }

    private void setEmpleadosPipa(List<PersonalTO> listaPersonal) {
        if (listaPersonal.isEmpty()){
            componentes.getChofer().setText("");
            componentes.getNombreChofer().setText("");
            componentes.getAyudante().setText("");
            componentes.getNombreAyudante().setText("");
        }
        for (PersonalTO personal: listaPersonal) {
            if (personal.getTipoEmpleado().equals(CHOFER)){
                componentes.getChofer().setText(personal.getNomina().toString());
                componentes.getNombreChofer().setText(personal.getNombre() + " " + personal.getApellidoPaterno() + " " + personal.getApellidoMaterno());

            }   else  if (personal.getTipoEmpleado().equals(AYUDANTE)) {
                componentes.getAyudante().setText(personal.getNomina().toString());
                componentes.getNombreAyudante().setText(personal.getNombre() + " " + personal.getApellidoPaterno() + " " + personal.getApellidoMaterno());
            }
        }
    }

    private void setPorcentajeTotalizador(ViajesTO viaje){
        if (viaje != null) {
            if(viaje.getTotalizadorFinal() != 0){
                componentes.getTotalizadorInicial1().setText(viaje.getTotalizadorFinal().toString());
            }else{
                if(bundle == null){
                    componentes.getTotalizadorInicial1().setText("");
                }

            }
        }
        if(porcentajeLlenado != null && porcentajeLlenado != 0){
            componentes.getSalida1().setText(porcentajeLlenado.toString());
        }
    }

    private void calcularVariacion(){
        Integer salida_1 = !TextUtils.isEmpty(componentes.getSalida1().getText().toString()) ? Integer.valueOf(componentes.getSalida1().getText().toString()) : 0;
        Integer llegada_1 = !TextUtils.isEmpty(componentes.getLlegada1().getText().toString()) ? Integer.valueOf(componentes.getLlegada1().getText().toString()) : 0;
        Integer totIni_1 = !TextUtils.isEmpty(componentes.getTotalizadorInicial1().getText().toString()) ? Integer.valueOf(componentes.getTotalizadorInicial1().getText().toString()) : 0;
        Integer salida_2 = !TextUtils.isEmpty(componentes.getSalida2().getText().toString()) ? Integer.valueOf(componentes.getSalida2().getText().toString()) : 0;
        Integer llegada_2 = !TextUtils.isEmpty(componentes.getLlegada2().getText().toString()) ? Integer.valueOf(componentes.getLlegada2().getText().toString()) : 0;
        Integer salida_3 = !TextUtils.isEmpty(componentes.getSalida3().getText().toString()) ? Integer.valueOf(componentes.getSalida3().getText().toString()) : 0;
        Integer llegada_3 = !TextUtils.isEmpty(componentes.getLlegada3().getText().toString()) ? Integer.valueOf(componentes.getLlegada3().getText().toString()) : 0;
        Integer totFinal_1 = !TextUtils.isEmpty(componentes.getTotalizadorFinal1().getText().toString()) ? Integer.valueOf(componentes.getTotalizadorFinal1().getText().toString()) : 0;
        Integer totFinal_2 = !TextUtils.isEmpty(componentes.getTotalizadorFinal2().getText().toString()) ? Integer.valueOf(componentes.getTotalizadorFinal2().getText().toString()) : 0;
        Integer totFinal_3 = !TextUtils.isEmpty(componentes.getTotalizadorFinal3().getText().toString()) ? Integer.valueOf(componentes.getTotalizadorFinal3().getText().toString()) : 0;
        Integer autoconsumo = !TextUtils.isEmpty(componentes.getAutoconsumo().getText().toString()) ? Integer.valueOf(componentes.getAutoconsumo().getText().toString()): 0;
        Integer medidores = !TextUtils.isEmpty(componentes.getMedidores().getText().toString()) ? Integer.valueOf(componentes.getMedidores().getText().toString()) : 0;
        Integer trasRecibidos = !TextUtils.isEmpty(componentes.getTraspasosRecibidos().getText().toString()) ? Integer.valueOf(componentes.getTraspasosRecibidos().getText().toString()) : 0;

        ventaPorcentual = (totFinal_1 != 0 ? totFinal_1 : totFinal_2 != 0 ? totFinal_2 : totFinal_3 != 0 ? totFinal_3 : 0) - totIni_1;
        Integer venta = totIni_1 - (totFinal_1 != 0 ? totFinal_1 : totFinal_2 != 0 ? totFinal_2 : totFinal_3 != 0 ? totFinal_3 : 0);
        variacion = ((getPorcentaje((float)salida_1)  + getPorcentaje((float)salida_2) + getPorcentaje((float)salida_3))
                - (getPorcentaje((float)llegada_1) + getPorcentaje((float)llegada_2) + getPorcentaje((float)llegada_3))) * pipa.getCapacidad();
        if(venta < 0){
            venta = venta * (-1);
        }
        variacion = (venta - autoconsumo - medidores - trasRecibidos) - variacion;

        componentes.getVariacion().setText(variacion.toString());
        if (variacion < 0 || variacion > 2){
            componentes.getAlerta().setText("¡ALERTA!");
        }   else{
            componentes.getAlerta().setText("");
        }
        porcentaje();
    }

    private Float getPorcentaje(Float numero){
        return (numero / 100F);
    }

    private void porcentaje(){
        Float variacionPorcentaje = (variacion / ventaPorcentual) * 100;
        componentes.getPorcentajeVariacion().setText(variacionPorcentaje.toString());
    }

    private void editarLiquidacion(){
        int  cont = 0;
        bundle= getActivity().getIntent().getExtras();
        if (bundle != null && bundle.getBoolean("bandera")){
            liquidacion = liquidacionBussines.getLiquidacionByIdLiquidacion(bundle.getInt("folio"));
            componentes.getEconomico().setText(liquidacion.getEconomico());
            componentes.getSpinner().setSelection(liquidacion.getNoPipa());
            componentes.getChofer().setText(liquidacion.getNominaChofer());
            componentes.getAyudante().setText(liquidacion.getNominaAyudante());
            componentes.getNombreChofer().setText(liquidacion.getChofer());
            componentes.getNombreAyudante().setText(liquidacion.getAyudante());
            componentes.getFolio().setText(String.valueOf(bundle.getInt("folio")));

            if (liquidacion.getAutoconsumo() != null) {
                componentes.getAutoconsumo().setText(liquidacion.getAutoconsumo());
            }
            if (liquidacion.getMedidores() != null){
                componentes.getMedidores().setText(liquidacion.getAutoconsumo());
            }
            if (liquidacion.getTraspasosRecibidos() != null){
                componentes.getTraspasosRecibidos().setText(liquidacion.getTraspasosRecibidos());
            }
            if (liquidacion.getTraspasosRealizados() != null){
                componentes.getTraspasosRealizados().setText(liquidacion.getTraspasosRealizados());
            }

            for (ViajesTO viajes: liquidacion.getViajes()) {
                if(cont == 0){
                    componentes.setIdViaje1(viajes.getIdViaje());
                    componentes.getSalida1().setText(viajes.getPorcentajeInicial().toString());
                    componentes.getLlegada1().setText(viajes.getPorcentajeFinal().toString());
                    componentes.getTotalizadorInicial1().setText(viajes.getTotalizadorInicial().toString());
                    componentes.getTotalizadorFinal1().setText(viajes.getTotalizadorFinal().toString());
                }else if(cont == 1) {
                    componentes.setIdViaje2(viajes.getIdViaje());
                    componentes.getSalida2().setText(viajes.getPorcentajeInicial().toString());
                    componentes.getLlegada2().setText(viajes.getPorcentajeFinal().toString());
                    componentes.getTotalizadorInicial2().setText(viajes.getTotalizadorInicial().toString());
                    componentes.getTotalizadorFinal2().setText(viajes.getTotalizadorFinal().toString());
                }else {
                    componentes.setIdViaje3(viajes.getIdViaje());
                    componentes.getSalida3().setText(viajes.getPorcentajeInicial().toString());
                    componentes.getLlegada3().setText(viajes.getPorcentajeFinal().toString());
                    componentes.getTotalizadorInicial3().setText(viajes.getTotalizadorInicial().toString());
                    componentes.getTotalizadorFinal3().setText(viajes.getTotalizadorFinal().toString());
                }
                cont++;
            }
            componentes.getVariacion().setText(liquidacion.getVariacion().toString());
            componentes.getAlerta().setText(liquidacion.getAlerta().toString());
            componentes.getPorcentajeVariacion().setText(liquidacion.getPorcentajeVariacion().toString());
            componentes.getEconomico().setEnabled(false);
            componentes.getSpinner().setEnabled(false);
            componentes.getSpinner().setClickable(false);
        }
    }

}
