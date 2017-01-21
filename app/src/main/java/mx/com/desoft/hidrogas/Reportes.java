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
                "Porcentaje",
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
            dataRow.createCell(1).setCellValue(registros.getPorcentajeLlenado());
            dataRow.createCell(2).setCellValue(convertirFecha(registros.getFechaRegistro()));
            dataRow.createCell(3).setCellValue(registros.getVariacion());
        }

        String str_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        File file ;
        file = new File(str_path, view.getContext().getString(R.string.app_name) + ".xls");

        FileOutputStream file2 = new FileOutputStream(file);
        workbook.write(file2);
        file2.close();
        return str_path;
    }

    public  void reporteExcelPipas(View view, List<PipasTO> lista) throws Exception{
        {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(0, "Hoja excel");

            String[] headers = new String[]{
                    "Producto",
                    "Precio",
                    "Enlace"
            };

            Object[][] data = new Object[][] {
                    new Object[] { "PlayStation 4 (PS4) - Consola 500GB", new BigDecimal("340.95"), "https://www.amazon.es/PlayStation-4-PS4-Consola-500GB/dp/B013U9CW8A/ref=sr_1_1?ie=UTF8&qid=1464521925&sr=8-1&keywords=playstation" },
                    new Object[] { "Raspberry Pi 3 Modelo B (1,2 GHz Quad-core ARM Cortex-A53, 1GB RAM, USB 2.0)", new BigDecimal("41.95"), "https://www.amazon.es/Raspberry-Modelo-GHz-Quad-core-Cortex-A53/dp/B01CD5VC92/ref=sr_1_1?ie=UTF8&qid=1464521956&sr=8-1&keywords=raspberry+pi" },
                    new Object[] { "Gigabyte Brix - Barebón (Intel, Core i5, 2,6 GHz, 6, 35 cm (2.5\"), Serial ATA III, SO-DIMM) Negro ", new BigDecimal("421.36"), "https://www.amazon.es/Gigabyte-Brix-Bareb%C3%B3n-Serial-SO-DIMM/dp/B00HFCTUPM/ref=sr_1_5?ie=UTF8&qid=1464522011&sr=8-5&keywords=brix" }
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

            for (int i = 0; i < data.length; ++i) {
                HSSFRow dataRow = sheet.createRow(i + 1);

                Object[] d = data[i];
                String product = (String) d[0];
                BigDecimal price = (BigDecimal) d[1];
                String link = (String) d[2];

                dataRow.createCell(0).setCellValue(product);
                dataRow.createCell(1).setCellValue(price.doubleValue());
                dataRow.createCell(2).setCellValue(link);
            }

            HSSFRow dataRow = sheet.createRow(1 + data.length);
            HSSFCell total = dataRow.createCell(1);
            total.setCellType(Cell.CELL_TYPE_FORMULA);
            total.setCellStyle(style);
            total.setCellFormula(String.format("SUM(B2:B%d)", 1 + data.length));
            String str_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
            File file ;
            file = new File(str_path, view.getContext().getString(R.string.app_name) + ".xls");

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
