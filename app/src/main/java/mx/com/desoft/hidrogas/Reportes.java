package mx.com.desoft.hidrogas;

import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import mx.com.desoft.hidrogas.to.LlenadoTO;
import mx.com.desoft.hidrogas.to.PipasTO;

/**
 * Created by carlosdavid.castro on 05/01/2017.
 */

public class Reportes  {

    public String excel(View view, List<LlenadoTO> lista)throws Exception{
        LlenadoTO[] miarray = new LlenadoTO[lista.size()];
        miarray = lista.toArray(miarray);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Hoja excel");

        String[] headers = new String[]{
                "No. Pipa",
                "Fecha Registro",
                "Variación"
        };

        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(header);
        }

        for (int i = 0; i < miarray.length; ++i) {
            HSSFRow dataRow = sheet.createRow(i + 1);
            LlenadoTO registros = miarray[i];
            dataRow.createCell(0).setCellValue(registros.getNoPipa());
            dataRow.createCell(1).setCellValue(convertirFecha(registros.getFechaRegistro()));
            dataRow.createCell(2).setCellValue(registros.getVariacion());
        }

        String str_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        File file ;
        file = new File(str_path, view.getContext().getString(R.string.app_name)  + "_Reporte_Variacion.xls");

        FileOutputStream file2 = new FileOutputStream(file);
        workbook.write(file2);
        file2.close();
        return str_path;
    }

    public  void reporteExcelPipas(View view, List<PipasTO> lista) throws Exception{
        {
            PipasTO[] miarray = new PipasTO[lista.size()];
            miarray = lista.toArray(miarray);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0, "Hoja excel");

            String[] headers = new String[]{
                    "No. Pipa",
                    "Porcentaje de Llenado"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            headerStyle.setFont(font);

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);

            HSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; ++i) {
                String header = headers[i];
                HSSFCell cell = headerRow.createCell(i);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(header);
            }

            for (int i = 0; i < miarray.length; ++i) {
                HSSFRow dataRow = sheet.createRow(i + 1);
                PipasTO registros = miarray[i];
                dataRow.createCell(0).setCellValue(registros.getNoPipa());
                dataRow.createCell(1).setCellValue(registros.getPorcentajeLlenado()+"%");
            }

            String str_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
            File file ;
            file = new File(str_path, view.getContext().getString(R.string.app_name)+"_Reporte_Llenado" + ".xls");

            FileOutputStream file2 = new FileOutputStream(file);
            workbook.write(file2);
            file2.close();
        }
    }
    private String convertirFecha(Long fechaLong){
        Date date=new Date(fechaLong);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(date);
    }
}
