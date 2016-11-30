
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class exportExcel
 */
public class exportExcel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HSSFWorkbook workbook;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public exportExcel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		JDBC_Oracle db = new JDBC_Oracle();
		String id = request.getParameter("search_id");
		String name = request.getParameter("search_name");
		String start = request.getParameter("start");
		String year = request.getParameter("start_year");
		String end = request.getParameter("end");
		ArrayList<String> tasks = db.taskQuery(id, name, year, start, end, true);
		ArrayList<String> ids = db.taskQuery(id, name, year, start, end, false);
		HSSFRow rows[] = new HSSFRow[50];
		HSSFCell cells[] = new HSSFCell[7];
		String columns[] = { "工時週次", "週次起日", "週次訖日", "員工姓名", "員工編號", "工時摘要", "摘要時數" };
		workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("工時紀錄表");
		// 設定樣式
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 11);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.DARK_BLUE.index);
		CellStyle styleRow1 = workbook.createCellStyle();
		CellStyle styleCenter = workbook.createCellStyle();
		styleRow1.setFont(font);
		styleRow1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// styleRow1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// styleRow1.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		// 設定各欄位
		sheet.setColumnWidth(0, 5000);
		rows[0] = sheet.createRow((short) 0);
		for (int i = 0; i < columns.length; i++) {
			cells[i] = rows[0].createCell((short) i);
			cells[i].setCellType(HSSFCell.CELL_TYPE_STRING);
			cells[i].setCellValue(columns[i]);
			cells[i].setCellStyle(styleRow1);
		}
		// 設定內容
		int row = 0;
		int column = 1;
		for (String task : tasks) {
			ArrayList<String> items = db.queryTaskListTask(task, Integer.parseInt(ids.get(row)));
			for (String item : items) {
				sheet.setColumnWidth(column, 5000);
				rows[column] = sheet.createRow((short) column);
				cells[0] = rows[column].createCell((short) 0);
				cells[0].setCellType(HSSFCell.CELL_TYPE_STRING);
				cells[0].setCellValue(task);
				cells[0].setCellStyle(styleCenter);
				cells[1] = rows[column].createCell((short) 1);
				cells[1].setCellType(HSSFCell.CELL_TYPE_STRING);
				cells[1].setCellValue(db.getStartDate(task));
				cells[1].setCellStyle(styleCenter);
				cells[2] = rows[column].createCell((short) 2);
				cells[2].setCellType(HSSFCell.CELL_TYPE_STRING);
				cells[2].setCellValue(db.getEndDate(task));
				cells[2].setCellStyle(styleCenter);
				cells[3] = rows[column].createCell((short) 3);
				cells[3].setCellType(HSSFCell.CELL_TYPE_STRING);
				cells[3].setCellValue(Integer.parseInt(ids.get(row)));
				cells[3].setCellStyle(styleCenter);
				cells[4] = rows[column].createCell((short) 4);
				cells[4].setCellType(HSSFCell.CELL_TYPE_STRING);
				cells[4].setCellValue(db.getUserName(Integer.parseInt(ids.get(row))));
				cells[4].setCellStyle(styleCenter);
				cells[5] = rows[column].createCell((short) 5);
				cells[5].setCellType(HSSFCell.CELL_TYPE_STRING);
				cells[5].setCellValue(item);
				cells[5].setCellStyle(styleCenter);
				cells[6] = rows[column].createCell((short) 6);
				cells[6].setCellType(HSSFCell.CELL_TYPE_STRING);
				cells[6].setCellValue(db.getItemHours(task, Integer.parseInt(ids.get(row)), item));
				cells[6].setCellStyle(styleCenter);
				column++;
			}
			row++;
		}
		sheet.setColumnWidth(column + 1, 5000);
		// 暫存檔路徑
		String tempFilePath = request.getSession().getServletContext().getRealPath("/");
		String fileName = "tempFile.xls";
		File tempFile = new File(tempFilePath, fileName);
		try (FileOutputStream fos =new FileOutputStream(tempFile)) {
			fos.flush();
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		OutputStream os = null;
		SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd"); // 日期格式
		String fileNameDate = SDF.format(new java.util.Date());// 把今天的日期格式化字串
		String excelFileName =  "工時報表_" + fileNameDate + ".xls";
		if (fileName != null && fileName.trim().length() > 0) {
			byte buff[] = new byte[512];
			int nLen = 0;
			try (FileInputStream fis = new FileInputStream(tempFile)) {
				response.reset();
				response.setContentLength((int) tempFile.length());
				response.setContentType("application/octet-stream");
				response.setHeader("name", fileName);
				response.setHeader("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(excelFileName, "UTF-8") + "\""); //
				os = response.getOutputStream();
				os.flush();
				while ((nLen = fis.read(buff)) > 0) {
					os.write(buff, 0, nLen);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 關閉串流及暫存檔刪除
				if (os != null) {
					os.close();
				}
				tempFile.delete();
				tempFile = null;
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
