package gestio.moneders.moneders;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Excel {
    private static final Logger LOGGER = Logger.getLogger(Excel.class.getName());

    public static void create(String fileName){
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            workbook.createInformationProperties();
            workbook.getSummaryInformation().setTitle("moneders");
            workbook.getSummaryInformation().setAuthor("moneders");
            Sheet sheet = workbook.createSheet("moneders");
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Hello, Excel!");
            try (FileOutputStream out = new FileOutputStream(fileName)) {
                workbook.write(out);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error writing Excel file: " + fileName, e);
            }
            System.out.println("File created: " + fileName);
        } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating Excel file: " + fileName, e);
        }
    }
    public static void read(String fileName){
        try (FileInputStream fis = new FileInputStream(fileName);
            Workbook workbook = new HSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            System.out.println("Read from file: " + cell.getStringCellValue());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading Excel file: " + fileName, e);
        }
    }
}
