package cn.com.ut.core.common.util.poi;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

import cn.com.ut.core.common.constant.ConstantUtil;

/**
 * POI导出Excel文件工具类
 * 
 * @author wuxiaohua
 * @since 2017年4月18日 下午1:30:55
 */
public class ExcelHelper {

	private Workbook workbook;

	public Workbook getWorkbook() {

		return workbook;
	}

	private ExcelHelper() {
		workbook = new HSSFWorkbook();
	}

	public static ExcelHelper build() {

		return new ExcelHelper();
	}

	public void adjustSheetColumnWidth(Sheet sheet, int columnIndex, int width) {

		if (width > 255)
			width = 255;
		sheet.setColumnWidth(columnIndex, width * 256);
	}

	public Sheet createSheet(String sheetName) {

		return getWorkbook().createSheet(WorkbookUtil.createSafeSheetName(sheetName));
	}

	public short createDataFormat(String format) {

		return getWorkbook().createDataFormat().getFormat(format);
	}

	public CellStyle createCellStyle() {

		return getWorkbook().createCellStyle();
	}

	public ExcelHelper borderCellStyle(CellStyle prevCellStyle, CellStyle borderCellStyle) {

		if (borderCellStyle == null) {
			borderCellStyle = createCellStyle();
			borderCellStyle.setBorderLeft(BorderStyle.THIN);
			borderCellStyle.setBorderRight(BorderStyle.THIN);
			borderCellStyle.setBorderTop(BorderStyle.THIN);
			borderCellStyle.setBorderBottom(BorderStyle.THIN);
		}

		prevCellStyle.setBorderLeft(borderCellStyle.getBorderLeftEnum());
		prevCellStyle.setBorderRight(borderCellStyle.getBorderRightEnum());
		prevCellStyle.setBorderTop(borderCellStyle.getBorderTopEnum());
		prevCellStyle.setBorderBottom(borderCellStyle.getBorderBottomEnum());

		return this;
	}

	public ExcelHelper dataFormatCellStyle(CellStyle prevCellStyle, short dataFormat) {

		prevCellStyle.setDataFormat(dataFormat);
		return this;
	}

	public ExcelHelper fontCellStyle(CellStyle prevCellStyle, Font font) {

		if (font == null) {
			font = getWorkbook().createFont();
			font.setFontHeightInPoints((short) 12);
			font.setFontName("Courier New");
			font.setItalic(false);
			font.setStrikeout(false);
		}
		prevCellStyle.setFont(font);
		return this;
	}

	public ExcelHelper alignmentCellStyle(CellStyle prevCellStyle,
			HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {

		if (horizontalAlignment != null) {
			prevCellStyle.setAlignment(horizontalAlignment);
		}

		if (verticalAlignment != null) {
			prevCellStyle.setVerticalAlignment(verticalAlignment);
		}

		return this;
	}

	public ExcelHelper fillPatternCellStyle(CellStyle prevCellStyle,
			CellStyle fillPatternCellStyle) {

		if (fillPatternCellStyle == null) {
			fillPatternCellStyle = createCellStyle();
			fillPatternCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			fillPatternCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}

		prevCellStyle.setFillForegroundColor(fillPatternCellStyle.getFillForegroundColor());
		prevCellStyle.setFillPattern(fillPatternCellStyle.getFillPatternEnum());

		return this;
	}

	public Cell appendCell(Row row, int cellIndex, CellStyle cellStyle) {

		Cell cell = row.createCell(cellIndex);
		if (cellStyle != null)
			cell.setCellStyle(cellStyle);
		return cell;

	}

	public void exportToFile(OutputStream outputStream) throws IOException {

		getWorkbook().write(outputStream);
		outputStream.close();
		getWorkbook().close();
	}

	public void exportToHttpClient(String fileName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String userAgent = request.getHeader("User-Agent");
		String rtn = "";
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();

			if (userAgent.indexOf("msie") != -1) {
				rtn = "filename=\"" + new String(fileName.getBytes(ConstantUtil.CHARSET_GBK),
						ConstantUtil.CHARSET_ISO) + "\"";
			} else if (userAgent.indexOf("opera") != -1) {
				rtn = "filename*=UTF-8''" + fileName;
			} else if (userAgent.indexOf("safari") != -1) {
				rtn = "filename=\"" + new String(fileName.getBytes(ConstantUtil.CHARSET_UTF8),
						ConstantUtil.CHARSET_ISO) + "\"";
			} else if (userAgent.indexOf("applewebkit") != -1) {
				rtn = "filename=\"" + new String(fileName.getBytes(ConstantUtil.CHARSET_UTF8),
						ConstantUtil.CHARSET_ISO) + "\"";
			} else if (userAgent.indexOf("mozilla") != -1) {
				// rtn = "filename*=UTF-8''" + fileName;
				rtn = "filename=\"" + new String(fileName.getBytes(ConstantUtil.CHARSET_UTF8),
						ConstantUtil.CHARSET_ISO) + "\"";
			} else {
				rtn = "filename=\"" + new String(fileName.getBytes(ConstantUtil.CHARSET_UTF8),
						ConstantUtil.CHARSET_ISO) + "\"";
			}
		} else {
			rtn = "filename=\"" + new String(fileName.getBytes(ConstantUtil.CHARSET_UTF8),
					ConstantUtil.CHARSET_ISO) + "\"";
		}

		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		// response.setContentType("application/octet-stream;charset=UTF-8");
		response.addHeader("Content-Disposition", "attachment;" + rtn);
		
		response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
		
		OutputStream outputStream = response.getOutputStream();
		getWorkbook().write(outputStream);
		outputStream.close();
		getWorkbook().close();
	}

}
