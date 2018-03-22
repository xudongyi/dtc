package com.business.dtc.util;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;


/**
* @ClassName: ExportExcel
* @Description: 导出Excel
* @author luys
* @date 2017年2月17日 上午9:08:12
* @tags 
*/ 
@SuppressWarnings("deprecation")
public class ExportExcel {


	public static HSSFCellStyle cellHeadStyle(HSSFWorkbook workbook) {
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		return style;
	}


	public static HSSFCellStyle titleStyle(HSSFWorkbook workbook) {
		// EXCEL 标题样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 17);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		return style;
	}


	public static HSSFCellStyle cellValueStyle(HSSFWorkbook workbook, String formatStr) {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 单元格格式（数据类型）
		HSSFDataFormat format = workbook.createDataFormat();
		style.setDataFormat(format.getFormat(formatStr));
		return style;
	}


	@SuppressWarnings({ "rawtypes" })
	public static void excel(HttpServletRequest request, HttpServletResponse response, String title, String[] headers, String[] column, String[] cellstyles,
			List<Map<String, Object>> dataList, String comments) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString(comments));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		// comment.setAuthor(request.getSession().getAttribute("loginuname").toString());
		// 产生表格标题行
		HSSFRow rowtitle = sheet.createRow(0);
		HSSFCell celltitle = rowtitle.createCell(0);
		celltitle.setCellValue(title);
		celltitle.setCellStyle(ExportExcel.titleStyle(workbook));
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (headers.length - 1)));

		int[] columnWidths = new int[headers.length];// EXCEL列宽
		// 产生表格头
		HSSFRow row = sheet.createRow(1);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(ExportExcel.cellHeadStyle(workbook));
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
			// 根据表格头设置每列宽度,表头字体较大，长度系数X2
			int columnWidth = headers[i].getBytes().length * 1 * 332;
			if (columnWidth > 20 * 2 * 256) {
				columnWidth = 20 * 2 * 256;
			}
			sheet.setColumnWidth(i, columnWidth);
			columnWidths[i] = columnWidth;
			// 这边设置整列的单元格属性,无需对每个单元格进行设置了.
			if(cellstyles==null){
				//默认设置成纯文本
				sheet.setDefaultColumnStyle((short) i, cellValueStyle(workbook,"@"));
			}else{
				sheet.setDefaultColumnStyle((short) i, cellValueStyle(workbook, cellstyles[i]));
			}
		}
		Pattern p = Pattern.compile("^\\-?[0-9]*\\.[0-9]*$");
		for (int i = 0; i < dataList.size(); i++) {
			row = sheet.createRow(i + 2);
			Map maps = (Map) dataList.get(i);
			for (int k = 0; k < column.length; k++) {
				Object value = maps.get(column[k]);
				HSSFCell cell = row.createCell(k);
				// 先将所有的数据类型转为String
				String cellvalue = "";
				if (value instanceof Date || value instanceof Timestamp) {
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = (Date) value;
					cellvalue = sf.format(date);
				} else {
					cellvalue = String.valueOf(value == null ? "" : value);
				}
				Matcher m = p.matcher(cellvalue);
				if(m.matches()){
					cell.setCellValue(cellvalue.equals("")?0:Double.parseDouble(cellvalue));
				}else{
					cell.setCellValue(cellvalue);
				}
				// 根据最长单元格数据设置列宽度，最多不超过40个字符宽度
				int columnWidth = cellvalue.getBytes().length * 256 + 4 * 256;
				if (columnWidth > 40 * 256) {
					columnWidth = 40 * 256;
				}
				if (columnWidth > columnWidths[k]) {
					sheet.setColumnWidth(k, columnWidth);
					columnWidths[k] = columnWidth;
				}
			}

		}
		try {
			// 设置response的Header
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(title.getBytes("gb2312"), "ISO-8859-1") + ".xls");
			OutputStream outputStream = response.getOutputStream();
			workbook.write(outputStream);
			workbook.close();
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
