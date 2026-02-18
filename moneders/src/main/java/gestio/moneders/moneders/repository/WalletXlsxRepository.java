package gestio.moneders.moneders.repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Repository;

import gestio.moneders.moneders.settings.Settings;
import gestio.moneders.moneders.model.Wallet;

@Repository
public class WalletXlsxRepository implements WalletRepository {

    private static final Logger LOGGER = Logger.getLogger(WalletXlsxRepository.class.getName());

    @Override
    public List<Wallet> findAll() {
        List<Wallet> result = new ArrayList<>();
        String path = Settings.getPath();

        if (path == null || path.isBlank()) {
            LOGGER.warning("Excel path is null/empty");
            return result;
        }

        try (FileInputStream fis = new FileInputStream(path);
            Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter fmt = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String idText = fmt.formatCellValue(row.getCell(0)).trim();
                String respGestioId = fmt.formatCellValue(row.getCell(1)).trim();
                String respId = fmt.formatCellValue(row.getCell(2)).trim();
                String dinersInicials = fmt.formatCellValue(row.getCell(3)).trim();
                String dinersJustificats = fmt.formatCellValue(row.getCell(4)).trim();
                String dinersFinals = fmt.formatCellValue(row.getCell(5)).trim();
                String dataEntrega = fmt.formatCellValue(row.getCell(6)).trim();
                String dataLimit = fmt.formatCellValue(row.getCell(7)).trim();
                String concepte= fmt.formatCellValue(row.getCell(8)).trim();
                String retornat = fmt.formatCellValue(row.getCell(9)).trim();
                boolean retornatBool = "true".equalsIgnoreCase(retornat)
                    || "yes".equalsIgnoreCase(retornat)
                    || "1".equals(retornat);

                if (idText.isEmpty()) continue;

                Wallet w = new Wallet();
                w.setId(Long.valueOf(idText));
                w.setResponsableGestio(respGestioId.isEmpty() ? null : Long.valueOf(respGestioId));
                w.setResponsable(respId.isEmpty() ? null : Long.valueOf(respId));
                w.setDinersInicals(dinersInicials.isEmpty() ? 0.0 : Double.valueOf(dinersInicials));
                w.setDinersJustificats(dinersJustificats.isEmpty() ? 0.0 : Double.valueOf(dinersJustificats));
                w.setDinersFinals(dinersFinals.isEmpty() ? 0.0 : Double.valueOf(dinersFinals));
                w.setDataEntrega(readLocalDate(row.getCell(6), fmt));
                w.setDataLimit(readLocalDate(row.getCell(7), fmt));
                w.setConcepte(concepte);
                w.setRetornat(retornatBool);
                result.add(w);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error reading Excel file: " + path, e);
        }
        return result;
    }

    @Override
    public Optional<Wallet> findById(Long id) {
        return findAll().stream().filter(w -> id.equals(w.getId())).findFirst();
    }

    @Override
    public Wallet updateById(Long id, Wallet entity) {
        String path = Settings.getPath();
        if (path == null || path.isBlank()) {
            LOGGER.warning("Excel path is null/empty");
            return entity;
        }

        try (FileInputStream fis = new FileInputStream(path);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
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
                if (row == null) {
                    row = sheet.createRow(updateRowIndex);
                }

                row.createCell(0).setCellValue(id == null ? "" : String.valueOf(id));
                row.createCell(1).setCellValue(entity.getResponsableGestio() == null ? "" : String.valueOf(entity.getResponsableGestio()));
                row.createCell(2).setCellValue(entity.getResponsable() == null ? "" : String.valueOf(entity.getResponsable()));
                row.createCell(3).setCellValue(entity.getDinersInicals() == null ? 0.0d : entity.getDinersInicals());
                row.createCell(4).setCellValue(entity.getDinersJustificats() == null ? 0.0d : entity.getDinersJustificats());
                row.createCell(5).setCellValue(entity.getDinersFinals() == null ? 0.0d : entity.getDinersFinals());
                row.createCell(6).setCellValue(entity.getDataEntrega() == null ? "" : entity.getDataEntrega().toString());
                row.createCell(7).setCellValue(entity.getDataLimit() == null ? "" : entity.getDataLimit().toString());
                row.createCell(8).setCellValue(entity.getConcepte() == null ? "" : entity.getConcepte());
                row.createCell(9).setCellValue(entity.isRetornat());

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
    public Wallet save(Wallet entity) {
        // TODO append wallet row in xlsx
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        // TODO delete wallet row from xlsx
    }

    private LocalDate readLocalDate(Cell cell, DataFormatter fmt) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        String text = fmt.formatCellValue(cell).trim();
        return parseLocalDate(text);
    }

    private LocalDate parseLocalDate(String text) {
        if (text == null || text.isBlank()) return null;

        try { return LocalDate.parse(text); } catch (DateTimeParseException ignored) {}
        try { return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy")); } catch (DateTimeParseException ignored) {}
        try { return LocalDate.parse(text, DateTimeFormatter.ofPattern("MM/dd/yyyy")); } catch (DateTimeParseException ignored) {}

        return null;
    }
}
