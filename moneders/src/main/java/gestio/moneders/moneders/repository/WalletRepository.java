package gestio.moneders.moneders.repository;

import gestio.moneders.moneders.model.Persona;
import gestio.moneders.moneders.model.Wallet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class WalletRepository {

	private static final String SHEET_NAME = "wallets";
	private static final int HEADER_ROW = 0;

	private final Path excelPath;

	public WalletRepository(@Value("${wallet.excel.file-path:./data/wallets.xlsx}") String excelFilePath) {
		this.excelPath = Paths.get(excelFilePath).toAbsolutePath();
	}

	public Wallet save(Wallet wallet) {
		List<Wallet> wallets = findAll();

		if (wallet.getId() == null) {
			long nextId = wallets.stream()
					.map(Wallet::getId)
					.filter(id -> id != null)
					.max(Comparator.naturalOrder())
					.orElse(0L) + 1;
			wallet.setId(nextId);
			wallets.add(wallet);
		} else {
			boolean updated = false;
			for (int index = 0; index < wallets.size(); index++) {
				if (wallet.getId().equals(wallets.get(index).getId())) {
					wallets.set(index, wallet);
					updated = true;
					break;
				}
			}
			if (!updated) {
				wallets.add(wallet);
			}
		}

		writeAll(wallets);
		return wallet;
	}

	public List<Wallet> findAll() {
		ensureFileExists();
		List<Wallet> wallets = new ArrayList<>();

		try (InputStream inputStream = new FileInputStream(excelPath.toFile());
			 Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = workbook.getSheet(SHEET_NAME);
			if (sheet == null) {
				return wallets;
			}

			int lastRow = sheet.getLastRowNum();
			for (int rowIndex = HEADER_ROW + 1; rowIndex <= lastRow; rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row == null) {
					continue;
				}

				Cell idCell = row.getCell(0);
				if (idCell == null || idCell.getCellType() == CellType.BLANK) {
					continue;
				}

				Wallet wallet = new Wallet();
				wallet.setId((long) idCell.getNumericCellValue());
				wallet.setResponsableGestio(getPersonaCellValue(row, 1));
				wallet.setResponsable(getPersonaCellValue(row, 2));
				wallet.setDinersInicals(getFloatCellValue(row, 3));
				wallet.setDinersJustificats(getFloatCellValue(row, 4));
				wallet.setDinersFinals(getFloatCellValue(row, 5));
				wallet.setDataEntrega(getFloatCellValue(row, 6));
				wallet.setDataLimit(getFloatCellValue(row, 7));
				wallet.setConcepte(getStringCellValue(row, 8));
				wallet.setRetornat(getBooleanCellValue(row, 9));
				wallets.add(wallet);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error reading wallets Excel file", e);
		}

		return wallets;
	}

	public Optional<Wallet> findById(Long id) {
		return findAll().stream().filter(wallet -> id.equals(wallet.getId())).findFirst();
	}

	public boolean existsById(Long id) {
		return findById(id).isPresent();
	}

	public void deleteById(Long id) {
		List<Wallet> wallets = new ArrayList<>(findAll());
		wallets.removeIf(wallet -> id.equals(wallet.getId()));
		writeAll(wallets);
	}

	private void writeAll(List<Wallet> wallets) {
		ensureFileExists();

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet(SHEET_NAME);

			Row headerRow = sheet.createRow(HEADER_ROW);
			headerRow.createCell(0).setCellValue("id");
			headerRow.createCell(1).setCellValue("responsableGestio");
			headerRow.createCell(2).setCellValue("responsable");
			headerRow.createCell(3).setCellValue("dinersInicals");
			headerRow.createCell(4).setCellValue("dinersJustificats");
			headerRow.createCell(5).setCellValue("dinersFinals");
			headerRow.createCell(6).setCellValue("dataEntrega");
			headerRow.createCell(7).setCellValue("dataLimit");
			headerRow.createCell(8).setCellValue("concepte");
			headerRow.createCell(9).setCellValue("retornat");

			int rowIndex = HEADER_ROW + 1;
			for (Wallet wallet : wallets) {
				Row row = sheet.createRow(rowIndex++);
				row.createCell(0).setCellValue(wallet.getId());
				row.createCell(1).setCellValue(getPersonaName(wallet.getResponsableGestio()));
				row.createCell(2).setCellValue(getPersonaName(wallet.getResponsable()));
				row.createCell(3).setCellValue(wallet.getDinersInicals());
				row.createCell(4).setCellValue(wallet.getDinersJustificats());
				row.createCell(5).setCellValue(wallet.getDinersFinals());
				row.createCell(6).setCellValue(wallet.getDataEntrega());
				row.createCell(7).setCellValue(wallet.getDataLimit());
				row.createCell(8).setCellValue(wallet.getConcepte() != null ? wallet.getConcepte() : "");
				row.createCell(9).setCellValue(wallet.isRetornat());
			}

			for (int i = 0; i < 10; i++) {
				sheet.autoSizeColumn(i);
			}

			try (FileOutputStream outputStream = new FileOutputStream(excelPath.toFile())) {
				workbook.write(outputStream);
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error writing wallets Excel file", e);
		}
	}

	private void ensureFileExists() {
		try {
			Path parent = excelPath.getParent();
			if (parent != null && Files.notExists(parent)) {
				Files.createDirectories(parent);
			}

			if (Files.notExists(excelPath)) {
				try (Workbook workbook = new XSSFWorkbook()) {
					Sheet sheet = workbook.createSheet(SHEET_NAME);
					Row headerRow = sheet.createRow(HEADER_ROW);
					headerRow.createCell(0).setCellValue("id");
					headerRow.createCell(1).setCellValue("responsableGestio");
					headerRow.createCell(2).setCellValue("responsable");
					headerRow.createCell(3).setCellValue("dinersInicals");
					headerRow.createCell(4).setCellValue("dinersJustificats");
					headerRow.createCell(5).setCellValue("dinersFinals");
					headerRow.createCell(6).setCellValue("dataEntrega");
					headerRow.createCell(7).setCellValue("dataLimit");
					headerRow.createCell(8).setCellValue("concepte");
					headerRow.createCell(9).setCellValue("retornat");

					try (FileOutputStream outputStream = new FileOutputStream(excelPath.toFile())) {
						workbook.write(outputStream);
					}
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error preparing wallets Excel file", e);
		}
	}

	private String getStringCellValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		return cell == null ? "" : cell.getStringCellValue();
	}

	private float getFloatCellValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			return 0.0f;
		}
		return (float) cell.getNumericCellValue();
	}

	private Persona getPersonaCellValue(Row row, int cellIndex) {
		String value = getStringCellValue(row, cellIndex);
		if (value.isBlank()) {
			return null;
		}
		return Persona.builder().nom(value).build();
	}

	private String getPersonaName(Persona persona) {
		return persona != null && persona.getNom() != null ? persona.getNom() : "";
	}

	private boolean getBooleanCellValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		if (cell == null) {
			return false;
		}

		if (cell.getCellType() == CellType.BOOLEAN) {
			return cell.getBooleanCellValue();
		}

		if (cell.getCellType() == CellType.NUMERIC) {
			return cell.getNumericCellValue() != 0;
		}

		if (cell.getCellType() == CellType.STRING) {
			return Boolean.parseBoolean(cell.getStringCellValue());
		}

		return false;
	}
}
