package gestio.moneders.moneders.repository;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Repository;
import gestio.moneders.moneders.settings.Settings;

import gestio.moneders.moneders.model.Persona;

@Repository
public class PersonaXlsxRepository implements PersonaRepository {

    private static final Logger LOGGER = Logger.getLogger(PersonaXlsxRepository.class.getName());

    @Override
    public List<Persona> findAll() {
        List<Persona> result = new ArrayList<>();
        String path = Settings.getPath();

        if (path == null || path.isBlank()) {
            LOGGER.warning("Excel path is null/empty");
            return result;
        }

        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(1);
            DataFormatter fmt = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String idText = fmt.formatCellValue(row.getCell(0)).trim();
                String nomText = fmt.formatCellValue(row.getCell(1)).trim();
                String grupText = fmt.formatCellValue(row.getCell(2)).trim();
                String membreGestioText = fmt.formatCellValue(row.getCell(3)).trim();
                boolean membreGestio = "true".equalsIgnoreCase(membreGestioText)
                    || "yes".equalsIgnoreCase(membreGestioText)
                    || "1".equals(membreGestioText);

                if (idText.isEmpty()) continue;

                Persona p = new Persona();
                p.setId(Long.valueOf(idText));
                p.setNom(nomText);
                p.setGrup(grupText);
                p.setMembreGestio(membreGestio);


                result.add(p);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error reading Excel file: " + path, e);
        }

        return result;
    }

    @Override
    public Optional<Persona> findById(Long id) {
        return findAll().stream().filter(p -> id.equals(p.getId())).findFirst();
    }

    @Override
    public Persona updateById(Long id, Persona entity) {
        String path = Settings.getPath();
        if (path == null || path.isBlank()) {
            LOGGER.warning("Excel path is null/empty");
            return entity;
        }

        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(1);
            if (sheet == null) {
                LOGGER.warning("Sheet index 1 not found");
                return entity;
            }

            DataFormatter fmt = new DataFormatter();
            int last = sheet.getLastRowNum();
            int updateRowIndex = -1;

            for (int i = 1; i <= last; i++) { 
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String idText = fmt.formatCellValue(row.getCell(0)).trim();
                if (idText.isEmpty()) continue;

                if (id.equals(Long.valueOf(idText))) {
                    updateRowIndex = i;
                    break;
                }
            }

            if (updateRowIndex >= 0) {
                Row row = sheet.getRow(updateRowIndex);
                row.createCell(1).setCellValue(entity.getNom() == null ? "" : entity.getNom());
                row.createCell(2).setCellValue(entity.getGrup() == null ? "" : entity.getGrup());
                row.createCell(3).setCellValue(entity.isMembreGestio());

                try (FileOutputStream fos = new FileOutputStream(path)) {
                    workbook.write(fos);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting persona from Excel file: " + path, e);
        }
        return entity;
    }

    @Override
    public Persona save(Persona entity) {
        String path = Settings.getPath();
        if (path == null || path.isBlank()) {
            LOGGER.warning("Excel path is null/empty");
            return entity;
        }

        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(1);
            if (sheet == null) {
                LOGGER.warning("Sheet index 1 not found");
                return entity;
            }

            if (entity.getId() == null) {
                entity.setId(nextId(sheet));
            }

            int rowIndex = sheet.getLastRowNum() + 1;
            if (rowIndex == 0) rowIndex = 1;
            Row row = sheet.createRow(rowIndex);

            row.createCell(0).setCellValue(entity.getId());
            row.createCell(1).setCellValue(entity.getNom() == null ? "" : entity.getNom());
            row.createCell(2).setCellValue(entity.getGrup() == null ? "" : entity.getGrup());
            row.createCell(3).setCellValue(entity.isMembreGestio());

            try (FileOutputStream fos = new FileOutputStream(path)) {
                workbook.write(fos);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saving persona in Excel file: " + path, e);
        }

        return entity;
    }

    @Override
    public void deleteById(Long id) {
        String path = Settings.getPath();
        if (path == null || path.isBlank()) {
            LOGGER.warning("Excel path is null/empty");
            return;
        }

        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(1);
            if (sheet == null) {
                LOGGER.warning("Sheet index 1 not found");
                return;
            }

            DataFormatter fmt = new DataFormatter();
            int last = sheet.getLastRowNum();
            int deleteRowIndex = -1;

            for (int i = 1; i <= last; i++) { // start at 1: row 0 is header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String idText = fmt.formatCellValue(row.getCell(0)).trim();
                if (idText.isEmpty()) continue;

                if (id.equals(Long.valueOf(idText))) {
                    deleteRowIndex = i;
                    break;
                }
            }

            if (deleteRowIndex >= 0) {
                Row row = sheet.getRow(deleteRowIndex);
                if (row != null) sheet.removeRow(row);

                if (deleteRowIndex < last) {
                    sheet.shiftRows(deleteRowIndex + 1, last, -1);
                }

                try (FileOutputStream fos = new FileOutputStream(path)) {
                    workbook.write(fos);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting persona from Excel file: " + path, e);
        }
    }

    private long nextId(Sheet sheet) {
        DataFormatter fmt = new DataFormatter();
        long max = 0L;

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String idText = fmt.formatCellValue(row.getCell(0)).trim();
            if (idText.isEmpty()) continue;

            try {
                long id = Long.parseLong(idText);
                if (id > max) max = id;
            } catch (NumberFormatException ignored) {
            }
        }
        return max + 1;
    }
}
