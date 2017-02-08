package mx.com.desoft.hidrogas;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import mx.com.desoft.hidrogas.bussines.PipasBussines;
import mx.com.desoft.hidrogas.to.PipasTO;

public class ImportarDatos extends Activity{

    private PipasBussines pipasBussines = new PipasBussines();
    private Calendar calendar = Calendar.getInstance();

    public void guardardatos(View view) throws IOException{
        FileInputStream file = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString(),"Pipas.xls"));
        // Crear el objeto que tendra el libro de Excel
        HSSFWorkbook workbook = new HSSFWorkbook(file);

	/*

	 * Obtenemos la primera pestaña a la que se quiera procesar indicando el indice.

	 * Una vez obtenida la hoja excel con las filas que se quieren leer obtenemos el iterator

	 * que nos permite recorrer cada una de las filas que contiene.

	 */

        HSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Row row;
        PipasTO pipasTO =null;
        // Recorremos todas las filas para mostrar el contenido de cada celda
int contador =0;
        while (rowIterator.hasNext()){
            row = rowIterator.next();

            // Obtenemos el iterator que permite recorres todas las celdas de una fila
            Iterator<Cell> cellIterator = row.cellIterator();
            Cell celda;
            contador =0;

            System.out.println("pipa:"+((Double)Double.parseDouble(row.getCell(0).toString())).intValue());
            System.out.println("Nopipa:"+row.getCell(1));
            if (row.getRowNum()==0) {
               pipasTO = new PipasTO();
            }
            pipasTO.setNoPipa(((Double)Double.parseDouble(row.getCell(0).toString())).intValue());
            pipasTO.setPorcentajeLlenado(((Double)Double.parseDouble(row.getCell(1).toString())).intValue());
            pipasTO.setFechaRegistro(this.getFechaActual());
            pipasTO.setNominaRegistro("1001");
            pipasTO.setIdPipa(pipasBussines.guardar2(view.getContext(),pipasTO).intValue());
            pipasBussines.llenar(view.getContext(),pipasTO);

            while (cellIterator.hasNext()){
                celda = cellIterator.next();
                //System.out.println(row.getRowNum());
                //System.out.println(celda.getNumericCellValue());
               // System.out.println("pipa:"+celda.getRow().getCell(0));
                //System.out.println("Nopipa:"+celda.getRow().getCell(1));
                if (contador ==1){
                    pipasTO = new PipasTO();
                System.out.println("*****");
                    contador =0;
                }
               contador++;
                // Dependiendo del formato de la celda el valor se debe mostrar como String, Fecha, boolean, entero...
                /*switch(celda.getCellType()) {

                    case Cell.CELL_TYPE_NUMERIC:
                        if( HSSFDateUtil.isCellDateFormatted(celda) ){
                            System.out.println(celda.getDateCellValue());
                        }else{
                            System.out.println(celda.getNumericCellValue());
                        }
                        System.out.println(celda.getNumericCellValue());

                        break;

                    case Cell.CELL_TYPE_STRING:
                        System.out.println(celda.getStringCellValue());
                        break;

                    case Cell.CELL_TYPE_BOOLEAN:
                        System.out.println(celda.getBooleanCellValue());
                        break;

                }*/

            }

        }



        // cerramos el libro excel

        workbook.close();

    }

    private Long getFechaActual(){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day-1);
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
