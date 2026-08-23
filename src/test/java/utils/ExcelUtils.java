package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ExcelUtils {

    private ExcelUtils() {
    }

    public static Object[][] readSheet(String resourcePath, String sheetName) {
        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Excel file not found on classpath: " + resourcePath);
            }
            try (Workbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalStateException("Sheet not found: " + sheetName);
                }
                return toDataProviderArray(sheet);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read Excel resource: " + resourcePath, e);
        }
    }

    private static Object[][] toDataProviderArray(Sheet sheet) {
        DataFormatter formatter = new DataFormatter();
        Row header = sheet.getRow(0);
        if (header == null) {
            throw new IllegalStateException("Excel sheet is missing a header row.");
        }

        int columnCount = header.getLastCellNum();
        List<Object[]> rows = new ArrayList<>();

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null || isRowEmpty(row, columnCount, formatter)) {
                continue;
            }
            Object[] values = new Object[columnCount];
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                values[columnIndex] = formatter.formatCellValue(row.getCell(columnIndex)).trim();
            }
            rows.add(values);
        }

        if (rows.isEmpty()) {
            throw new IllegalStateException("Excel sheet has a header but no data rows.");
        }
        return rows.toArray(new Object[0][]);
    }

    private static boolean isRowEmpty(Row row, int columnCount, DataFormatter formatter) {
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            String value = formatter.formatCellValue(row.getCell(columnIndex)).trim();
            if (!value.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
