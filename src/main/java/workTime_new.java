
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class workTime_new
 */
public class workTime_new extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public workTime_new() {
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
		String startDate = request.getParameter("t0");
		String endDate = "";
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		LinkedList<String> tasks = new LinkedList<>();
		int id = Integer.parseInt(session.getAttribute("userid").toString());
		// 取得工時的起訖
		for (int i = 0; i < 7 ; i++) {
			String dateTitle = request.getParameter("t" + i);
			if (dateTitle == "") {
				System.out.println(i);
				endDate = request.getParameter("t" + (i - 1) );
				break;
			}
			else {
				System.out.println(i);
				endDate = request.getParameter("t6" );
			}
		}
		//	設定工時紀錄名稱
		System.out.println(startDate + "//" + endDate);
		String dates[] = startDate.split("/");
		String date[] = endDate.split("/");
		if (dates[0].length() < 2) {
			dates[0] = "0" + dates[0];
			date[0] = "0" + date[0];
		}
		if (dates[1].length() < 5)
			dates[1] = "0" + dates[1];
		if (date[1].length() < 5)
			date[1] = "0" + date[1];
		startDate= "2016-" + dates[0] + "-" + dates[1];
		endDate = "2016-" + date[0] + "-" + date[1];
		String task_name = db.getTaskTitle(startDate);
		// 設定task_id
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		// 取得任務摘要
		int taskNum = 0;
		int status = Integer.parseInt(request.getParameter("status"));
		int total = (request.getParameter("weektotal").isEmpty()) ? 0 : Integer.parseInt(request.getParameter("weektotal"));
		for (int i = 0; i < 8; i++) {
			String task = request.getParameter("item" + i);
			if (task != null && !task.isEmpty()) {
				tasks.add(task);
				taskNum ++;
			}
			else
				tasks.add("null");
		}
		// 將每日工時花費存放入整數二維陣列中
		int taskHour[][] = new int[8][7];
		int itemHour[] = new int[8];
		for (int i = 0; i < 8; i++) {
			  for (int j = 0; j < 7; j++) {
				  if (tasks.get(i).equals("null"))
						break;
				  itemHour[i] = (request.getParameter("week" + i).isEmpty()) ? 0 : Integer.parseInt(request.getParameter("week" + i));
				  if ((request.getParameter("time" + i + j) == ""))
					  taskHour[i][j] = 10;
				  else
					  if (request.getParameter("time" + i + j) != null)
						  taskHour[i][j] = Integer.parseInt(request.getParameter("time" + i + j));
			  }
		}
		// 利用JDBC執行SQL指令新增工時紀錄
		db.newTask(task_id, id, task_name, startDate, endDate, status, total);
		for (int i = 0; i < 8; i++) {
			if (!tasks.get(i).equals("null"))
				db.newTaskDetail(task_id,  tasks.get(i), taskHour[i][0], taskHour[i][1], taskHour[i][2], taskHour[i][3], taskHour[i][4], taskHour[i][5], taskHour[i][6], itemHour[i]);
		}
//		out.println("<link rel=stylesheet type='text/css' href='/Sample/WebContent/css/alertify.core.css'><link rel=stylesheet type='text/css' href='/Sample/WebContent/css/alertify.default.css'><script src='/Sample/WebContent/js/alertify.js' type='text/javascript'></script>");
		if (status == 1)
			out.write("『" + task_name + "』工時紀錄已提交成功！");
		else if (status == 4)
			out.write("『" + task_name + "』工時紀錄已暫存成功！");
		out.close();
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
