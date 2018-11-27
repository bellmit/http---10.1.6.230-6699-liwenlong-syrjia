package cn.syrjia.util;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 
 * @author liwenlong
 * 
 *         2016-8-9
 */
public class ReadExcel {

	public static void readExcel(MultipartFile file) {
		
		CommonsMultipartFile cf = (CommonsMultipartFile) file;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File f = fi.getStoreLocation();
		Workbook workbook=null;
		try {
			FileInputStream is = new FileInputStream(f);// 文件流

			workbook = WorkbookFactory.create(is); // 这种方式 Excel
															// 2003/2007/2010
															// 都是可以处理的

			int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量

			for (int i = 0; i < sheetCount; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				if (sheet == null) {
					continue;
				}
				int firstRowIndex = sheet.getFirstRowNum();
				int lastRowIndex = sheet.getLastRowNum();

				// 读取首行 即,表头
				Row firstRow = sheet.getRow(firstRowIndex);
				for (int j = firstRow.getFirstCellNum(); j <= firstRow
						.getLastCellNum(); j++) {
					Cell cell = firstRow.getCell(j);
					String cellValue = getCellValue(cell, true);
					System.out.print(" " + cellValue + "\t");
				}

				// 读取数据行
				for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);// 当前行
					int firstColumnIndex = currentRow.getFirstCellNum(); // 首列
					int lastColumnIndex = currentRow.getLastCellNum();// 最后一列
					for (int columnIndex = firstColumnIndex; columnIndex <= lastColumnIndex; columnIndex++) {
						Cell currentCell = currentRow.getCell(columnIndex);// 当前单元格
						String currentCellValue = getCellValue(
								currentCell, true);// 当前单元格的值
						System.out.print(currentCellValue + "\t");
					}
					System.out.println("");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取单元格的值
	 * 
	 * @param cell
	 *            单元格对象
	 * @param treatAsStr
	 *            为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
	 * @return
	 */
	public static String getCellValue(Cell cell, boolean treatAsStr) {
		if (cell == null) {
			return "";
		}

		if (treatAsStr) {
			// 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
			// 加上下面这句，临时把它当做文本来读取
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}

		if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		} else {
			return String.valueOf(cell.getStringCellValue());
		}
	}
}
