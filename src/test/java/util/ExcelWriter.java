package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelWriter {

//	public static void main(String[] args) {
//		ExcelWriter excelWriter2 = new ExcelWriter();
//		excelWriter2.writeExcelFile("user_skills_id","I am Great");
//	}

	public boolean writeExcelFile(String excelFilePath, String sheetName, int rowNumber, String columnName,
			Object cellValue) {
		// String excelFilePath = "C:/@Ruchira/@TestFiles/InputData_UserSkills.xlsx";
		boolean status;
		try {
			String path = "src/test/resources/" + excelFilePath;
			File fileObj = new File(path);
			String dirPath = fileObj.getAbsolutePath();

			FileInputStream inputStream = new FileInputStream(new File(dirPath));
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheet(sheetName);
			Row row = sheet.getRow(rowNumber);
			int columnIndex = getColumnNames(sheet, columnName);

			Cell cell = row.getCell(columnIndex);
			if (cell == null) {
				cell = row.createCell(columnIndex, CellType.STRING);
			}
			
			if(cellValue instanceof Integer) {
				cell.setCellValue((int) cellValue);
			}else if(cellValue instanceof String) {
				cell.setCellValue((String) cellValue);
			}
			
			inputStream.close();

			FileOutputStream outputStream = new FileOutputStream(dirPath);
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
			status = Boolean.TRUE;
		} catch (Exception ex) {
			ex.printStackTrace();
			status = Boolean.FALSE;
		}
		return status;
	}

	public int getColumnNames(Sheet sheet, String colName) {
		try {
			Iterator<Row> rows = sheet.iterator();
			Row firstrow = rows.next();
			Iterator<Cell> ce = firstrow.cellIterator();
			while (ce.hasNext()) {
				Cell value = ce.next();

				if (value.getCellType() == CellType.STRING && colName.equalsIgnoreCase(value.getStringCellValue())) {
					return value.getColumnIndex();
				}

			}
			return 0;
		} catch (Exception e) {
			System.out.println(e.getMessage() + e);
			return 0;
		}

	}

}
