package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import mx.com.desoft.hidrogas.bussines.PersonalBussines;
import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.to.PersonalTO;
import mx.com.desoft.hidrogas.to.PipasTO;
import mx.com.desoft.utils.Utils;

public class ImportarDatos extends Activity{

    private PipasBussines pipasBussines = new PipasBussines();
    private PersonalBussines personalBussines = new PersonalBussines();
    private Utils utils = new Utils();

    public void importarPipas() throws IOException{
        FileInputStream file = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(),"Pipas.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        PipasTO pipasTO = null;
        while (rowIterator.hasNext()){
            row = rowIterator.next();
            if (row.getRowNum()==0) {
               pipasTO = new PipasTO();
            }

            pipasTO.setNoPipa(((Double)Double.parseDouble(row.getCell(0).toString())).intValue());
            pipasTO.setPorcentajeLlenado(0);
            pipasTO.setCapacidad(((Double)Double.parseDouble(row.getCell(1).toString())).intValue());
            pipasTO.setFechaRegistro(utils.getFechaActual());
            pipasTO.setNominaRegistro(LoginActivity.personalTO.getNominaRegistro());
            pipasTO.setIdPipa(pipasBussines.guardar2(pipasTO).intValue());
            pipasBussines.llenar(pipasTO);
        }
        workbook.close();

    }

    public void importarEmpleados() throws IOException{
        FileInputStream file = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(),"Empleados.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        PersonalTO empleados;
        while (rowIterator.hasNext()){
            row = rowIterator.next();
            empleados = new PersonalTO();
            empleados.setNomina(((Double)Double.parseDouble(row.getCell(0).toString())).intValue());
            if (!row.getCell(1).toString().equals("SUPLENTE")) {
                Cursor registros = pipasBussines.buscarByNoPipa(((Double)Double.parseDouble(row.getCell(1).toString())).intValue());
                if (registros.moveToFirst()) {
                    do {
                        empleados.setNoPipa(registros.getInt(0));
                    } while (registros.moveToNext());
                }
            }
            empleados.setApellidoPaterno(row.getCell(2).toString());
            empleados.setApellidoMaterno(row.getCell(3).toString());
            empleados.setNombre(row.getCell(4).toString());
            empleados.setTipoEmpleado(row.getCell(5).toString().equals("CHOFER") ? 1 : 2);
            empleados.setFechaRegistro(utils.getFechaActual());
            empleados.setNominaRegistro(LoginActivity.personalTO.getNominaRegistro());
            personalBussines.guardarEmpleadosExcel(empleados);
        }
        workbook.close();

    }

    public void importarClavesPipas() throws IOException{
        FileInputStream file = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(),"PipasClaves.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        PipasTO pipasTO = new PipasTO();
        while (rowIterator.hasNext()){
            row = rowIterator.next();
            try {
                pipasTO.setNoPipa(((Double) Double.parseDouble(row.getCell(0).toString().trim())).intValue());
                pipasTO.setClavePipa(row.getCell(1).toString().trim());
                if (pipasTO.getNoPipa() > 0 && !pipasTO.getClavePipa().isEmpty()) {
                    pipasBussines.setClavePipa(pipasTO);
                }
            } catch (NumberFormatException nfe) {
                Log.d("ERROR","error pipa o clave no numericos" + nfe);
                continue;
            }
        }
        workbook.close();

    }
}
