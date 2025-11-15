/**
 * Copyright (C) 2025, Claus Nielsen, clausn999@gmail.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package dk.clanie.saxo;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import dk.clanie.saxo.dto.SaxoAccountStatementRow;
import dk.clanie.saxo.dto.SaxoAssetType;
import dk.clanie.saxo.dto.SaxoBookedTradeAmountsRow;
import dk.clanie.saxo.dto.SaxoBoughtOrSold;
import dk.clanie.saxo.dto.SaxoCurrencyCode;
import dk.clanie.saxo.dto.SaxoOpenClose;
import dk.clanie.saxo.dto.SaxoTradesExecutedRow;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
class SaxoXlsxUtils {

	private static final DateTimeFormatter DD_MM_YYY_FORMATTER =  DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private static final List<String> MONTH_NAMES = List.of("jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "okt", "nov", "dec");


	List<SaxoAccountStatementRow> parseAccountStatement(byte[] data) {
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
				Workbook workbook = new XSSFWorkbook(inputStream)) {

			// Check workbook has expected sheets
			if (workbook.getNumberOfSheets() != 2) throw new RuntimeException("Unexpected number of sheets: %d (expected 2).".formatted(workbook.getNumberOfSheets()));
			Sheet sheet = workbook.getSheetAt(0);
			if (!"Kontooversigt".equals(sheet.getSheetName())) throw new RuntimeException("1st sheet is: '%s' (expected 'Kontooversigt').".formatted(sheet.getSheetName()));
			sheet = workbook.getSheetAt(1);
			if (!sheet.getSheetName().startsWith("Kontoudtog ")) throw new RuntimeException("2nd sheet is: '%s' (expected 'Kontoudtog <accountId>').".formatted(sheet.getSheetName()));

			// Check sheet has expected columns
			Row row = sheet.getRow(0);
			checkHeading(row, 0, "Konto ID");
			checkHeading(row, 1, "Posteringsdato");
			checkHeading(row, 2, "Valørdato");
			checkHeading(row, 3, "Begivenhed");
			checkHeading(row, 4, "Ændring");
			checkHeading(row, 5, "Kontant balance");
			checkHeading(row, 6, "Kommentar");

			// Iterate through the rows and columns
			List<SaxoAccountStatementRow> accountStatementRows = new ArrayList<>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				int columnNumber = 0;
				String accountId = readCell(row, columnNumber++, String.class);
				if (accountId == null) continue; // Skip empty row
				accountStatementRows.add(SaxoAccountStatementRow.builder()
						.accountId(accountId)
						.postingDate(readCell(row, columnNumber++, LocalDate.class))
						.valueDate(readCell(row, columnNumber++, LocalDate.class))
						.arrangement(readCell(row, columnNumber++, String.class))
						.change(readCell(row, columnNumber++, Double.class))
						.cashBalance(readCell(row, columnNumber++, Double.class))
						.comment(readCell(row, columnNumber++, String.class))
						.build());
			}
			return accountStatementRows;
		} catch (Exception e) {
			printXlsx(data);
			throw new RuntimeException(e);
		}
	}


	List<SaxoTradesExecutedRow> parseTradesExecuted(byte[] data) {
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
				Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = getTradesExecutedWorkbookSheet(workbook, "Handler med yderligere oplysnin");

			// Check sheet has expected columns
			Row row = sheet.getRow(0);
			checkHeading(row, 0, "Handels-ID");
			checkHeading(row, 1, "Konto ID");
			checkHeading(row, 2, "Instrument");
			checkHeading(row, 3, "Handelstidspunkt");
			checkHeading(row, 4, "K/S");
			checkHeading(row, 5, "Åben/luk");
			checkHeading(row, 6, "Beløb");
			checkHeading(row, 7, "Pris");
			checkHeading(row, 8, "Handelsværdi");
			checkHeading(row, 9, "Spread-omkostninger");
			checkHeading(row, 10, "Bogført beløb");
			checkHeading(row, 11, "Symbol");
			checkHeading(row, 12, "Børs");
			checkHeading(row, 13, "Lokal børs");
			checkHeading(row, 14, "Valørdato");
			checkHeading(row, 15, "Ordre-ID");
			checkHeading(row, 16, "Aktivtype");
			checkHeading(row, 17, "Kontovalutadecimaler");
			checkHeading(row, 18, "Kontovaluta");
			checkHeading(row, 19, "Bogført beløb i kundevaluta");
			checkHeading(row, 20, "Bogført beløb i USD");
			checkHeading(row, 21, "Kundevaluta");
			checkHeading(row, 22, "Justeret handelsdato");
			checkHeading(row, 23, "Udløbsdato");
			checkHeading(row, 24, "Startmargin");
			checkHeading(row, 25, "Rentemargin");
			checkHeading(row, 26, "Instrumentvalutadecimaler");
			checkHeading(row, 27, "Navn på instrumentsektor");
			checkHeading(row, 28, "Id for instrumentsektortype");
			checkHeading(row, 29, "Optionseventtype");
			checkHeading(row, 30, "Navn på rodinstrumentsektor");
			checkHeading(row, 31, "Type-id for rodinstrumentsektor");
			checkHeading(row, 32, "Spread-omkostning i kundevaluta");
			checkHeading(row, 33, "Spread-omkostning i USD");
			checkHeading(row, 34, "Retning");
			checkHeading(row, 35, "Strike");
			checkHeading(row, 36, "Barrier/Stop Loss");
			checkHeading(row, 37, "Financing Level");
			checkHeading(row, 38, "Issuer");
			checkHeading(row, 39, "Gennemførselstidspunkt for handel");
			checkHeading(row, 40, "UIC (Unified Instrument Code)");
			checkHeading(row, 41, "Beskrivelse af underliggende instrument");
			checkHeading(row, 42, "Symbol for underliggende instrument");

			// Iterate through the rows and columns
			List<SaxoTradesExecutedRow> tradesExecutedRows = new ArrayList<>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				int columnNumber = 0;
				Long tradeId = readCell(row, columnNumber++, Long.class);
				if (tradeId == null) continue; // Skip empty row
				tradesExecutedRows.add(SaxoTradesExecutedRow.builder()
						.tradeId(tradeId)
						.accountId(readCell(row, columnNumber++, String.class))
						.instrument(readCell(row, columnNumber++, String.class))
						.tradeDate(readCell(row, columnNumber++, LocalDate.class))
						.boughtOrSold(readCell(row, columnNumber++, SaxoBoughtOrSold.class))
						.openClose(readCell(row, columnNumber++, SaxoOpenClose.class))
						.quantity(readCell(row, columnNumber++, Long.class))
						.price(readCell(row, columnNumber++, Double.class))
						.tradeValue(readCell(row, columnNumber++, Double.class))
						.spreadCosts(readCell(row, columnNumber++, Double.class))
						.bookedAmount(readCell(row, columnNumber++, Double.class))
						.symbol(readCell(row, columnNumber++, String.class))
						.exchange(readCell(row, columnNumber++, String.class))
						.localExchange(readCell(row, columnNumber++, String.class))
						.valueDate(readCell(row, columnNumber++, LocalDate.class))
						.orderId(readCell(row, columnNumber++, Long.class))
						.assetType(readCell(row, columnNumber++, SaxoAssetType.class))
						.accountCurrencyDecimals(readCell(row, columnNumber++, Integer.class))
						.accountCurrency(readCell(row, columnNumber++, SaxoCurrencyCode.class))
						.bookedAmountInCustomerCurrency(readCell(row, columnNumber++, Double.class))
						.bookedAmountInUsd(readCell(row, columnNumber++, Double.class))
						.customerCurrency(readCell(row, columnNumber++, SaxoCurrencyCode.class))
						.adjustedTradeDate(readCell(row, columnNumber++, LocalDate.class))
						.expiryDate(readCell(row, columnNumber++, LocalDate.class))
						.initialMargin(readCell(row, columnNumber++, Double.class))
						.interestMargin(readCell(row, columnNumber++, Double.class))
						.instrumentCurrencyDecimals(readCell(row, columnNumber++, Integer.class))
						.instrumentSectorName(readCell(row, columnNumber++, String.class))
						.instrumentSectorTypeId(readCell(row, columnNumber++, String.class))
						.optionEventType(readCell(row, columnNumber++, String.class))
						.underlyingInstrumentSectorName(readCell(row, columnNumber++, String.class))
						.underlyingInstrumentSectorTypeId(readCell(row, columnNumber++, String.class))
						.spreadCostInCustomerCurrency(readCell(row, columnNumber++, Double.class))
						.spreadCostInUsd(readCell(row, columnNumber++, Double.class))
						.direction(readCell(row, columnNumber++, String.class))
						.strike(readCell(row, columnNumber++, String.class))
						.barrierStopLoss(readCell(row, columnNumber++, String.class))
						.financingLevel(readCell(row, columnNumber++, String.class))
						.issuer(readCell(row, columnNumber++, String.class))
						.tradeExecutionDate(readCell(row, columnNumber++, LocalDate.class))
						.uic(readCell(row, columnNumber++, Long.class))
						.underlyingInstrumentDescription(readCell(row, columnNumber++, String.class))
						.underlyingInstrumentSymbol(readCell(row, columnNumber++, String.class))
						.build());
			}
			return tradesExecutedRows;
		} catch (Exception e) {
			printXlsx(data);
			throw new RuntimeException(e);
		}
	}


	public List<SaxoBookedTradeAmountsRow> parseBookedTradeAmounts(byte[] data) {
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
				Workbook workbook = new XSSFWorkbook(inputStream)) {

			Sheet sheet = getTradesExecutedWorkbookSheet(workbook, "Bogført handelsbeløb");

			// Check sheet has expected columns
			Row row = sheet.getRow(0);
			checkHeading(row, 0, "Id for relateret handel");
			checkHeading(row, 1, "Id for relateret position");
			checkHeading(row, 2, "Dato");
			checkHeading(row, 3, "Konto ID");
			checkHeading(row, 4, "Valørdato");
			checkHeading(row, 5, "Bogførings-ID");
			checkHeading(row, 6, "Instrumentbeskrivelse");
			checkHeading(row, 7, "Aktivtype");
			checkHeading(row, 8, "Bogføringsbeløbstype");
			checkHeading(row, 9, "beløb");
			checkHeading(row, 10, "Beløb i kontovaluta");
			checkHeading(row, 11, "Beløb i kundevaluta");

			// Iterate through the rows and columns
			List<SaxoBookedTradeAmountsRow> bookedTradeAmountsRows = new ArrayList<>();
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				int columnNumber = 0;
				Long relatedTradeId = readCell(row, columnNumber++, Long.class);
				if (relatedTradeId == null) continue; // Skip empty row
				bookedTradeAmountsRows.add(SaxoBookedTradeAmountsRow.builder()
						.relatedTradeId(relatedTradeId)
						.relatedPositionId(readCell(row, columnNumber++, Long.class))
						.date(readCell(row, columnNumber++, LocalDate.class))
						.accountId(readCell(row, columnNumber++, String.class))
						.valueDate(readCell(row, columnNumber++, LocalDate.class))
						.postingId(readCell(row, columnNumber++, Long.class))
						.instrumentDescription(readCell(row, columnNumber++, String.class))
						.assetType(readCell(row, columnNumber++, SaxoAssetType.class))
						.postingAmountType(readCell(row, columnNumber++, String.class))
						.quantityOrAmount(readCell(row, columnNumber++, Long.class))
						.amountInAccountCurrency(readCell(row, columnNumber++, Double.class))
						.amountInCustomerCurrency(readCell(row, columnNumber++, Double.class))
						.build());
			}
			return bookedTradeAmountsRows;
		} catch (Exception e) {
			printXlsx(data);
			throw new RuntimeException(e);
		}
	}


	private Sheet getTradesExecutedWorkbookSheet(Workbook workbook, String sheetName) {
		if (workbook.getNumberOfSheets() != 3) throw new RuntimeException("Unexpected number of sheets: %d (expected 2).".formatted(workbook.getNumberOfSheets()));
		Sheet sheet = workbook.getSheetAt(0);
		if (!"Handler".equals(sheet.getSheetName())) throw new RuntimeException("1st sheet is: '%s' (expected 'Handler').".formatted(sheet.getSheetName()));
		sheet = workbook.getSheetAt(1);
		if (!"Handler med yderligere oplysnin".equals(sheet.getSheetName())) throw new RuntimeException("2nd sheet is: '%s' (expected 'Handler med yderligere oplysnin').".formatted(sheet.getSheetName()));
		sheet = workbook.getSheetAt(2);
		if (!"Bogført handelsbeløb".equals(sheet.getSheetName())) throw new RuntimeException("3rd sheet is: '%s' (expected 'Bogført handelsbeløb').".formatted(sheet.getSheetName()));
		return workbook.getSheet(sheetName);
	}


	private <T> T readCell(Row row, int cellIndex, Class<T> clazz) {
		Cell cell = row.getCell(cellIndex);
		if (cell == null) return null;
		CellType cellType = cell.getCellType();
		if (clazz == Double.class) {
			if (cellType == NUMERIC) return clazz.cast(cell.getNumericCellValue());
		} else if (clazz == Integer.class) {
			if (cellType == NUMERIC) return clazz.cast((int)cell.getNumericCellValue());
		} else if (clazz == LocalDate.class) {
			if (cellType == STRING) return clazz.cast(localDateOfString(cell));
			else if (cellType == NUMERIC) return clazz.cast(localDateOfNumeric(cell));
		} else if (clazz == Long.class) {
			if (cellType == NUMERIC) return clazz.cast((long)cell.getNumericCellValue());
		} else if (clazz == String.class) {
			if (cellType == STRING) return clazz.cast(cell.getStringCellValue());
		} else if (clazz == SaxoCurrencyCode.class) {
			if (cellType == STRING) return clazz.cast(SaxoCurrencyCode.valueOf(cell.getStringCellValue()));
		} else if (clazz == SaxoAssetType.class) {
			if (cellType == STRING) return clazz.cast(SaxoAssetType.valueOf(cell.getStringCellValue()));
		} else if (clazz == SaxoBoughtOrSold.class) {
			if (cellType == STRING) return clazz.cast(SaxoBoughtOrSold.parse(cell.getStringCellValue()));
		} else if (clazz == SaxoOpenClose.class) {
			if (cellType == STRING) return clazz.cast(SaxoOpenClose.parse(cell.getStringCellValue()));
		}
		throw new RuntimeException("Failed parsing cell at row %d, column %d into a %s. Cell type is %s.".formatted(
				row.getRowNum(), cell.getColumnIndex(), clazz.getSimpleName(), cell.getCellType()));
	}


	private void printXlsx(byte[] data) {
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
				Workbook workbook = new XSSFWorkbook(inputStream)) {

			// Get the first sheet (by index)
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				System.out.println("\n\n\nSheet: " + sheet.getSheetName());

				// Iterate through the rows and columns
				for (Row row : sheet) {
					for (Cell cell : row) {
						// Depending on the cell type, retrieve the value
						switch (cell.getCellType()) {
						case STRING:
							System.out.print(cell.getStringCellValue() + "\t");
							break;
						case NUMERIC:
							// Check if the cell is a date
							if (DateUtil.isCellDateFormatted(cell)) {
								System.out.print(cell.getDateCellValue() + "\t");
							} else {
								System.out.print(cell.getNumericCellValue() + "\t");
							}
							break;
						case BOOLEAN:
							System.out.print(cell.getBooleanCellValue() + "\t");
							break;
						case FORMULA:
							System.out.print(cell.getCellFormula() + "\t");
							break;
						default:
							System.out.print(" \t");
							break;
						}
					}
					System.out.println(); // Move to the next line after each row
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	private LocalDate localDateOfString(Cell cell) {
		String dateString = cell.getStringCellValue();
		String month = dateString.substring(3, 6);
		dateString = dateString.replace(month, String.format("%02d", MONTH_NAMES.indexOf(month) + 1));
		LocalDate localDate = LocalDate.parse(dateString.replace("okt", "oct"), DD_MM_YYY_FORMATTER);
		return localDate;
	}


	private LocalDate localDateOfNumeric(Cell cell) {
		long excelSerialDate = (long) cell.getNumericCellValue();
        // Excel's epoch starts from 1900-01-01
        LocalDate excelEpoch = LocalDate.of(1900, 1, 1);
        // Convert serial number to LocalDate (subtract 2 for Excel's leap year bug)
        LocalDate localDate = excelEpoch.plusDays(excelSerialDate - 2);
		return localDate;
	}


	private void checkHeading(Row row, int columnNumber, String expectedHeading) {
		String heading = row.getCell(columnNumber).getStringCellValue();
		if (!expectedHeading.equalsIgnoreCase(heading)) {
			throw new RuntimeException("Unexpected heading '%s' for column %d. Expected '%s'.".formatted(heading, columnNumber, expectedHeading));
		}
	}


}
