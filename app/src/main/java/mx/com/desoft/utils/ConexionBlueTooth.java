package mx.com.desoft.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import mx.com.desoft.hidrogas.bussines.LiquidacionBussines;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.PipasTO;
import mx.com.desoft.hidrogas.to.ViajesTO;

public class ConexionBluetooth extends Activity {

    private LiquidacionesTO liquidacionesTO;
    private LiquidacionBussines liquidacionBussines;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    private ViewGroup viewGroup;

    public ConexionBluetooth(ViewGroup viewGroup) {
        liquidacionBussines = new LiquidacionBussines();
        liquidacionesTO = new LiquidacionesTO();
        this.viewGroup = viewGroup;
    }

    public void findBT() {
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
                    if (device.getName().equals("TM-P60II_003133")) {
                        mmDevice = device;
                        break;
                    }
                }
            }
            Toast.makeText(viewGroup.getContext(),"Bluetooth device found.", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(viewGroup.getContext(),"Error: Al Buscar Dispositivo", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public  void openBT() throws IOException {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();
            Toast.makeText(viewGroup.getContext(),"Bluetooth Opened.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(viewGroup.getContext(),"Error; Abriendo Conexion", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void beginListenForData() {
        try {
            final Handler handler = new Handler();
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
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
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
            Toast.makeText(viewGroup.getContext(),"Error: En ListenForData", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void sendData(Long idLiquidacion, PipasTO pipa) throws IOException {
        try {
            Utils utils = new Utils();
            int cont = 1;
            liquidacionesTO = liquidacionBussines.getLiquidacionByIdLiquidacion(idLiquidacion);
            liquidacionesTO.setViajes(liquidacionBussines.getViajesByIdLiquidacion(idLiquidacion.intValue()));

            String msg ="FECHA: "+ utils.convertirFecha(new Date().getTime());
            msg += "\n";
            msg += "FOLIO: " + idLiquidacion;
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
            msg += "CLAVE: " + pipa.getClavePipa();
            msg += "\n";
            msg += "PORCENTAJE VARIACION: " + liquidacionesTO.getPorcentajeVariacion();
            msg += "\n";
            mmOutputStream.write(msg.getBytes());
            Toast.makeText(viewGroup.getContext(),"Imprimiendo Ticket...", Toast.LENGTH_LONG).show();
            closeBT();
        } catch (Exception e) {
            Toast.makeText(viewGroup.getContext(),"Error: Imprimiendo Ticket", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            Toast.makeText(viewGroup.getContext(),"Bluetooth Closed", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(viewGroup.getContext(),"Error: Cerrando Concexion", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
