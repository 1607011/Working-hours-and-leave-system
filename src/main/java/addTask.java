

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class addTask
 */
public class addTask extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addTask() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		JDBC_Oracle db = new JDBC_Oracle();
		String startDate = request.getParameter("t0");
		PrintWriter out = response.getWriter();
		StringBuilder sb = new StringBuilder();
		HttpSession session = request.getSession();
		int id = Integer.parseInt(session.getAttribute("userid").toString());
		//	設定工時紀錄名稱
		String dates[] = startDate.split("/");
		if (dates[0].length() < 2) 
			dates[0] = "0" + dates[0];
		if (dates[1].length() < 5)
			dates[1] = "0" + dates[1];
		startDate = "2016-" + dates[0] + "-" + dates[1];
		String task_name = db.getTaskTitle(startDate);
		String endDate = db.getEndDate(task_name);
		if (db.getStatus(task_name, id) != 0)
			out.write(db.getStatus(task_name, id) + task_name);
//		System.out.println(startDate.substring(0, 8));
//		System.out.println(startDate.substring(8, 10));
		System.out.println(endDate.substring(0, 10));
		int startDay = Integer.parseInt(startDate.substring(8, 10));
		int endDay = Integer.parseInt(endDate.substring(8, 10));
		ArrayList<String> taskDates = new ArrayList<>();
		String sql = "SELECT leave_start_date, start_time, end_time, type, reason, times FROM leavelist WHERE status=2 AND user_id=" + id + " AND (leave_start_date BETWEEN '" + startDate.substring(0, 10) + "' AND '" + endDate.substring(0, 10) +"') ORDER BY leave_start_date";
		ResultSet rs = db.getResultSet(sql);
		sb.append("<table id='leaveTable' class='table'>");
		try {
			if (rs.next())
				sb.append("<thead id='leaveHead'><tr><th class='date'><span class='glyphicon glyphicon-sort-by-attributes'></span>  請假日期</th><th><span class='glyphicon glyphicon-time'></span> 開始時間</th><th><span class='glyphicon glyphicon-time'></span> 結束時間</th><th><span class='glyphicon glyphicon-sort-by-alphabet'></span> 假別</th><th><span class='glyphicon glyphicon-briefcase'></span> 事由</th><th class='allTime'><span class='glyphicon glyphicon-sort-by-order'></span> 請假時數</th></tr></thead>");
			rs.beforeFirst();
			int i = 0;
			while(rs.next()) {
				sb.append(String.format("<tr><td id='leave" + i + "' class='datas'>%s</td><td class='datas'>%s</td><td class='datas'>%s</td><td class='datas'>%s</td><td class='datas'>%s</td><td id='leaveTime" + i + "' class='datas'>%d</td></tr>", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6)));
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		sb.append("</table>");
		for (int i = 0; i < (endDay - startDay + 1); i++) {
			if (startDay + i < 10)
				taskDates.add(startDate.substring(0, 8) + "0" + (startDay + i));
			else
				taskDates.add(startDate.substring(0, 8) + (startDay + i));
		}
		
		// 判斷假單是否都完成審核
		boolean canAdd = true;
		ArrayList<String> leaveDates = db.getLeaveDates(id, startDate, endDate);
		for (String leaveDate : leaveDates) {
			for (String taskDate : taskDates) {
				if (taskDate.equals(leaveDate)) {
					out.write(5 + task_name);
					canAdd= false;
					break;
				}
			}
		}
		if (canAdd)
			out.write(sb.toString());
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
