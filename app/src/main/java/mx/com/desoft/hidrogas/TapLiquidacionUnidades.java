package mx.com.desoft.hidrogas;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import org.apache.poi.ss.usermodel.Name;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.jar.Attributes;

import mx.com.desoft.hidrogas.bussines.LiquidacionBussines;
import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.bussines.UnidadesBussines;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.PersonalTO;
import mx.com.desoft.hidrogas.to.PipasTO;
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
    private TextView textViewNombreChofer, textViewNombreAyudante, labelAlerta, textViewVariacion, labelVariacionPorcentaje, labelClave;
    private Spinner spinnerRuta;
    private LiquidacionBussines liquidacionBussines;
    private UnidadesBussines unidadesBussines;
    private LiquidacionesTO liquidacionesTO;
    private PipasBussines pipasBussines;
    private List<ViajesTO> viajesTO;
    private Float variacion = 0F;
    private PipasTO pipa;
    private Integer idPipa;
    private String fecha;
    private Integer venta = 0;
    private Date fechaHoy;
    private Float variacionPorcentaje;
    private Integer ventaPorcentual;
    private Long idLiquidacion;
    private Integer porcentajeLlenado;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_liquidacion, container, false);
        liquidacionBussines = new LiquidacionBussines();
        unidadesBussines = new UnidadesBussines();

        inicializarComponentes();
        inicializarEventos();
        deshabilitarComponentes();
        return viewGroup;
    }

    private void inicializarComponentes() {
        btnImprimir = (Button) viewGroup.findViewById(R.id.btnImprimir);
        btnImprimir.setEnabled(false);
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

        labelVariacionPorcentaje = (TextView) viewGroup.findViewById(R.id.labelPorcentajeVariacion);
        labelClave = (TextView) viewGroup.findViewById(R.id.labelClave);
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
                        if (position > 0) {
                            idPipa = pipasBussines.spinnerMap.get(parent.getSelectedItemPosition());
                            liquidacionesTO = new LiquidacionesTO();
                            viajesTO = new ArrayList<>();
                            pipa = new PipasTO();
                            List<PersonalTO> listaPersonal = unidadesBussines.obtenerPersonal(viewGroup, idPipa);
                            if (!TextUtils.isEmpty(editTextEconomico.getText().toString())) {
                                pipa = unidadesBussines.getCapacidadPipa(viewGroup, Integer.parseInt(editTextEconomico.getText().toString()));
                            }
                            porcentajeLlenado = pipasBussines.getCapacidadDiaAnteriorPipa(viewGroup.getContext(), idPipa);
                            List<ViajesTO> listaViajes = liquidacionBussines.getPorcentajeInicialAnterior(viewGroup, idPipa);

                            setEmpleadosPipa(listaPersonal);
                            setViajesVista(listaViajes);
                            labelClave.setText(pipa.getClavePipa().toString());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        editTextEconomico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            public void afterTextChanged(Editable s) {
                spinnerRuta.setEnabled(true);
                editTextNoChofer.setEnabled(true);
                editTextNoAyudante.setEnabled(true);
                editTextSalida_1.setEnabled(true);
                editTextSalida_2.setEnabled(true);
                editTextSalida_3.setEnabled(true);
                editTextLlegada_1.setEnabled(true);
                editTextLlegada_2.setEnabled(true);
                editTextLlegada_3.setEnabled(true);
                editTextTotInicial_1.setEnabled(true);
                editTextTotInicial_2.setEnabled(true);
                editTextTotInicial_3.setEnabled(true);
                editTextTotFinal_1.setEnabled(true);
                editTextTotFinal_2.setEnabled(true);
                editTextTotFinal_3.setEnabled(true);
            }
        });

        btnImprimir.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                try {
                    findBT();
                    openBT();
                    sendData();
                } catch (IOException e) {
                    Toast.makeText(viewGroup.getContext(), "Error al imprimir el ticket" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
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
                        idLiquidacion = liquidacionBussines.guardarLiquidacion(viewGroup, liquidacionesTO, viajesTO);
                        btnImprimir.setEnabled(true);
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

        editTextSalida_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            public void afterTextChanged(Editable s) {
                editTextTotInicial_2.setText(editTextTotFinal_1.getText().toString());
            }
        });

        editTextSalida_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            public void afterTextChanged(Editable s) {
                editTextTotInicial_3.setText(editTextTotFinal_2.getText().toString());
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
        liquidacionesTO.setNoPipa(((Long)spinnerRuta.getSelectedItemId()).intValue());
        liquidacionesTO.setNominaChofer(editTextNoChofer.getText().toString());
        liquidacionesTO.setChofer(textViewNombreChofer.getText().toString());
        liquidacionesTO.setNominaAyudante(editTextNoAyudante.getText().toString());
        liquidacionesTO.setAyudante(textViewNombreAyudante.getText().toString());
        liquidacionesTO.setFechaRegistro(fechaHoy.getTime());
        liquidacionesTO.setNominaRegistro(LoginActivity.personalTO.getNominaRegistro().toString());
        liquidacionesTO.setPorcentajeVariacion(Float.valueOf(labelVariacionPorcentaje.getText().toString()));
        liquidacionesTO.setClave(labelClave.getText().toString());
        liquidacionesTO.setEconomico(editTextEconomico.getText().toString());
        if (variacion < 0 || variacion >2){
            liquidacionesTO.setVariacion(variacion.intValue());
            liquidacionesTO.setAlerta(1);
        } else{
            liquidacionesTO.setVariacion(variacion.intValue());
            liquidacionesTO.setAlerta(0);
        }
    }

    private void deshabilitarComponentes(){
        spinnerRuta.setEnabled(false);
        editTextNoChofer.setEnabled(false);
        editTextNoAyudante.setEnabled(false);
        editTextSalida_1.setEnabled(false);
        editTextSalida_2.setEnabled(false);
        editTextSalida_3.setEnabled(false);
        editTextLlegada_1.setEnabled(false);
        editTextLlegada_2.setEnabled(false);
        editTextLlegada_3.setEnabled(false);
        editTextTotInicial_1.setEnabled(false);
        editTextTotInicial_2.setEnabled(false);
        editTextTotInicial_3.setEnabled(false);
        editTextTotFinal_1.setEnabled(false);
        editTextTotFinal_2.setEnabled(false);
        editTextTotFinal_3.setEnabled(false);
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
                if(viajes.getTotalizadorFinal() != 0){
                    editTextTotInicial_1.setText(viajes.getTotalizadorFinal().toString());
                }else{
                    editTextTotInicial_1.setText("");
                }

            }
        }

        if(porcentajeLlenado != null && porcentajeLlenado != 0){
            editTextSalida_1.setText(porcentajeLlenado.toString());
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
        Integer autoconsumo = !TextUtils.isEmpty(editTextAutoconsumo.getText().toString()) ? Integer.valueOf(editTextAutoconsumo.getText().toString()): 0;
        Integer medidores = !TextUtils.isEmpty(editTextMedidores.getText().toString()) ? Integer.valueOf(editTextMedidores.getText().toString()) : 0;
        Integer trasRecibidos = !TextUtils.isEmpty(editTextTraspasosRecibidos.getText().toString()) ? Integer.valueOf(editTextTraspasosRecibidos.getText().toString()) : 0;

        ventaPorcentual = (totFinal_1 != 0 ? totFinal_1 : totFinal_2 != 0 ? totFinal_2 : totFinal_3 != 0 ? totFinal_3 :0) - totIni_1;
        venta = totIni_1 - (totFinal_1 != 0 ? totFinal_1 : totFinal_2 != 0 ? totFinal_2 : totFinal_3 != 0 ? totFinal_3 :0);
        variacion = ((getPorcentaje((float)salida_1)  + getPorcentaje((float)salida_2) + getPorcentaje((float)salida_3))
                - (getPorcentaje((float)llegada_1) + getPorcentaje((float)llegada_2) + getPorcentaje((float)llegada_3))) * pipa.getCapacidad();
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

        porcentaje();

    }

    private void getFechaActual(){
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fecha = formato.format(new Date());
            fechaHoy = formato.parse(formato.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Float getPorcentaje(Float numero){
        return (float)(numero / 100F);
    }

    private void limpiarCampos(){
        editTextSalida_1.setText("");
        editTextLlegada_1.setText("");
        editTextTotInicial_1.setText("");
        editTextTotFinal_1.setText("");

        editTextSalida_2.setText("");
        editTextLlegada_2.setText("");
        editTextTotInicial_2.setText("");
        editTextTotFinal_2.setText("");

        editTextSalida_3.setText("");
        editTextLlegada_3.setText("");
        editTextTotInicial_3.setText("");
        editTextTotFinal_3.setText("");

        labelAlerta.setText("");
        textViewVariacion.setText("");
        labelClave.setText("");
        labelVariacionPorcentaje.setText("");
        editTextEconomico.setText("");

        editTextAutoconsumo.setText("");
        editTextMedidores.setText("");
        editTextTraspasosRecibidos.setText("");
        editTextTraspasosRealizados.setText("");
        editTextNoChofer.setText("");
        editTextNoAyudante.setText("");
        textViewNombreChofer.setText("");
        textViewNombreAyudante.setText("");
        spinnerRuta.setSelection(0);
    }

    private void porcentaje(){
        variacionPorcentaje = (variacion / ventaPorcentual) * 100;
        labelVariacionPorcentaje.setText(variacionPorcentaje.toString());
    }

    // this will find a bluetooth printer device
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                Toast.makeText(viewGroup.getContext(),"No bluetooth adapter available.", Toast.LENGTH_LONG).show();
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("TM-P60II_003133")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            Toast.makeText(viewGroup.getContext(),"Bluetooth device found.", Toast.LENGTH_LONG).show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }


   
    void openBT() throws IOException {
        try {
           BluetoothServerSocket mmServerSocket;
            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

            Toast.makeText(viewGroup.getContext(),"Bluetooth Opened.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
 * after opening a connection to bluetooth printer device,
 * we have to listen and check if a data were sent to be printed.
 */
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 20;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {

                                                Toast.makeText(viewGroup.getContext(),"Datos." + data, Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // this will send text data to be printed by the bluetooth printer
    void sendData() throws IOException {
        try {
            int cont = 1;
            liquidacionesTO = liquidacionBussines.getLiquidacionByIdLiquidacion(viewGroup, idLiquidacion);
            liquidacionesTO.setViajes(liquidacionBussines.getViajesByIdLiquidacion(viewGroup, idLiquidacion.intValue()));
            // the text typed by the user
            String msg ="FECHA: "+ fecha;
            msg += "\n";
            msg +="PIPA: "+liquidacionesTO.getNoPipa().toString();
            msg += "\n";
            msg += "ECONOMICO: " + liquidacionesTO.getEconomico();
            msg += "\n";
            msg += "No. NOMINA CHOFER: " + liquidacionesTO.getNominaChofer();
            msg += "\n";
            msg += "CHOFER: " + liquidacionesTO.getChofer();
            msg += "\n";
            msg += "No. NOMINA AYUDANTE: " + liquidacionesTO.getNominaAyudante();
            msg += "\n";
            msg += "AYUDANTE: " + liquidacionesTO.getAyudante();
            msg += "\n";
            msg += "*************** VIAJES ***************";
            msg += "\n";
            for (ViajesTO viajes: liquidacionesTO.getViajes()) {
                msg += "VIAJE - " + cont;
                msg += "\n";
                msg += "SALIDA: " + viajes.getPorcentajeInicial();
                msg += "\n";
                msg += "LLEGADA: " + viajes.getPorcentajeFinal();
                msg += "\n";
                msg += "TOTALIZADOR INICIAL: " + viajes.getTotalizadorInicial();
                msg += "\n";
                msg += "TOTALIZADOR FINAL: " + viajes.getTotalizadorFinal();
                msg += "\n";
                cont++;
            }
            msg += "**************************************";
            msg += "\n";
            msg += "VARIACION: " + liquidacionesTO.getVariacion();
            msg += "\n";
            msg += "CLAVE: " + pipa.getClave();
            msg += "\n";
            msg += "PORCENTAJE VARIACION: " + liquidacionesTO.getPorcentajeVariacion();
            msg += "\n";
            msg.toString();
            mmOutputStream.write(msg.getBytes());
            limpiarCampos();
            // tell the user data were sent
            Toast.makeText(viewGroup.getContext(),"Imprimiendo Ticket...", Toast.LENGTH_LONG).show();
            closeBT();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            Toast.makeText(viewGroup.getContext(),"Bluetooth Closed", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
