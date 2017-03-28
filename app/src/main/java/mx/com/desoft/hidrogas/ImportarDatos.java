package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.view.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import mx.com.desoft.hidrogas.bussines.PersonalBussines;
import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.to.LiquidacionesTO;
import mx.com.desoft.hidrogas.to.PersonalTO;
import mx.com.desoft.hidrogas.to.PipasTO;

public class ImportarDatos extends Activity{

    private PipasBussines pipasBussines = new PipasBussines();
    private PersonalBussines personalBussines = new PersonalBussines();
    private Calendar calendar = Calendar.getInstance();

    public void importarPipas(View view) throws IOException{
        FileInputStream file = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(),"Pipas.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        PipasTO pipasTO =null;
        while (rowIterator.hasNext()){
            row = rowIterator.next();

            System.out.println("pipa:"+((Double)Double.parseDouble(row.getCell(0).toString())).intValue());
            System.out.println("Capacidad:"+row.getCell(1));
            if (row.getRowNum()==0) {
               pipasTO = new PipasTO();
            }
            pipasTO.setNoPipa(((Double)Double.parseDouble(row.getCell(0).toString())).intValue());
            pipasTO.setPorcentajeLlenado(0);
            pipasTO.setCapacidad(((Double)Double.parseDouble(row.getCell(1).toString())).intValue());
            pipasTO.setFechaRegistro(this.getFechaActual());
            pipasTO.setNominaRegistro(LoginActivity.personalTO.getNominaRegistro());
            pipasTO.setIdPipa(pipasBussines.guardar2(view.getContext(),pipasTO).intValue());
            pipasBussines.llenar(view.getContext(),pipasTO);
        }

        workbook.close();

    }

    public void importarEmpleados(Context context) throws IOException{
        FileInputStream file = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(),"Empleados.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        PersonalTO empleados = null;
        while (rowIterator.hasNext()){
            row = rowIterator.next();
            empleados = new PersonalTO();
            empleados.setNomina(((Double)Double.parseDouble(row.getCell(0).toString())).intValue());
            if (!row.getCell(1).toString().equals("SUPLENTE")) {
                System.out.print("Entre Excel");
                Cursor registros = pipasBussines.buscarByNoPipa(context,((Double)Double.parseDouble(row.getCell(1).toString())).intValue());
                if (registros.moveToFirst()) {
                    do {
                        System.out.print("Entre");
                        empleados.setNoPipa(registros.getInt(0));
                    } while (registros.moveToNext());
                }
            }
            empleados.setApellidoPaterno(row.getCell(2).toString());
            empleados.setApellidoMaterno(row.getCell(3).toString());
            empleados.setNombre(row.getCell(4).toString());
            empleados.setTipoEmpleado(row.getCell(5).toString().equals("CHOFER") ? 1 : 2);
            empleados.setFechaRegistro(this.getFechaActual());
            empleados.setNominaRegistro(LoginActivity.personalTO.getNominaRegistro());
            personalBussines.guardarEmpleadosExcel(context, empleados);

        }
        workbook.close();

    }

    public void importarClavesPipas(View view) throws IOException{
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
                    pipasBussines.setClavePipa(view.getContext(), pipasTO);
                }
            } catch (NumberFormatException nfe) {
                System.out.println("error pipa o clave no numericos" + nfe);
                continue;
            }
        }
        workbook.close();
    }

    private Long getFechaActual(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day);
        Date fecha = null;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fecha = formato.parse(formato.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha.getTime();
    }
}
