

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class workTime_modify_data
 */
public class workTime_modify_data extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public workTime_modify_data() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JDBC_Oracle db = new JDBC_Oracle();
		StringBuilder sb = new StringBuilder();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		String task_name = request.getParameter("title");
		int j = 0;
		int id = Integer.parseInt(session.getAttribute("userid").toString());
		sb.append("<form id='manager_block' class='form1 focus modalFrame' name='datas_modify_form' action='/IISI_Project/workTime_modify' method='POST' onsubmit='return false;''>");
		String sql = "SELECT leave_start_date, start_time, end_time, type, reason, times FROM leavelist WHERE status=2 AND user_id=" + id + " AND (leave_start_date BETWEEN '" + db.getStartDate(task_name).substring(0, 10) + "' AND '" + db.getEndDate(task_name).substring(0, 10) +"') ORDER BY leave_start_date";
		ResultSet rs = db.getResultSet(sql);
		try {
			if (rs.next()) {
				sb.append("<div id='leaveBlock'><button id='list' class='btn btn-info css_btn' style='width: 100%; font-weight: 500; font-size: 15px;'><span id='icon' class='glyphicon glyphicon-eye-open pos'></span> <span id='text'>顯示請假資料</span></button><div id='LEAVE' hidden='hidden'>");
				sb.append("<table id='leaveTable' class='table'>");
				sb.append("<thead id='leaveHead'><tr><th class='date'><span class='glyphicon glyphicon-sort-by-attributes'></span>  請假日期</th><th><span class='glyphicon glyphicon-time'></span> 開始時間</th><th><span class='glyphicon glyphicon-time'></span> 結束時間</th><th><span class='glyphicon glyphicon-sort-by-alphabet'></span> 假別</th><th><span class='glyphicon glyphicon-briefcase'></span> 事由</th><th class='allTime'><span class='glyphicon glyphicon-sort-by-order'></span> 請假時數</th></tr></thead>");
			}
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
			sb.append("</table></div></div>");
		}
		sb.append("<table class='table' id='datas_new'><thead id='thead_new'><tr><th style='width: 130px'><img id='add' src='../resourses/img/icon/ad.png' /></th>");
		for (String date : db.queryTaskListDate(task_name, id)) { 
			if (!date.isEmpty()) {
				sb.append("<th id='t"+ j +"' style='width: 65px; font-size: 14px; line-height: 26px;'><input name='t" + j + "' type='text' value='" + date + "' readonly='readonly' /></th>");
			}
			else if (date .isEmpty()) {
				sb.append("<th id='t"+ j +"' style='width: 65px;'><input name='t" + j + "' type='text' value='' readonly='readonly' /></th>");
			}
			j++;
		}
		j = 0;
		sb.append("</tr></thead>");
		sb.append("<tbody id='tbody_new' class='tableSortable'>");
		for(int i = 0; i < db.queryTaskListTask(task_name, id).size(); i++) {
			sb.append("<tr style='color: black' >");
			sb.append("<td id='item" + i + "' class='item' align='center'><input name='item" + i + "' type='text' value='");
			sb.append(db.queryTaskListTask(task_name, id).get(i));
			sb.append("' readonly='readonly' />");
			int hours[][] = db.queryTaskListHour(task_name, id);
			for(j = 0; j < 5; j++) {
				if (hours[i][j] != 10) {
					sb.append("<td><input type='number' min='0' max='8' id='time" + i + j + "' class='times form-control-static' name='time" + i + j + "' value='");
					sb.append(hours[i][j]);
					sb.append("' required /></td>");
				}
				else {
					sb.append("<td><input type='number' id='time" + i + j + "' class='times form-control' name='time" + i + j + "' value='' readonly='readonly' /></td>");	
				}
			}
				sb.append("<td><input type='number' id='time" + i + "5' class='times form-control' name='time" + i + "5' value='' readonly='readonly' /></td>");
				sb.append("<td><input type='number' id='time" + i + "6' class='times form-control' name='time" + i + "6' value='' readonly='readonly' /></td>");
				sb.append("<td><input type='text' id='week" + i + "' class='week times form-control' name='week" + i  + "' readonly='readonly'>");
				sb.append("<td><img src='../resourses/img/icon/trash.png' class='trash'></td></tr>");
			}
			sb.append("</tbody><tfoot><tr><td style='width: 155px;'></td>");
			for (int i = 0; i < 7; i++) {
				sb.append("<td><input type='text' min='0' max='8' id='day" + i + "' class='days form-control' title='一日時數不得超過8小時' readonly='readonly' /></td>");
			}		
			sb.append("<td><input type='text' id='weektotal' class='days form-control' name='weektotal' readonly='readonly' /></td>");
			sb.append("<td></td></tr></tfoot></table>");
			if (db.getStatus(task_name, id) == 3)
				sb.append("<textarea id='cause' name='cause' cols='60' rows='6' disabled>\t\t\t\t\t    -- 退回原因 --\n" + db.getBcakReason(task_name, id) + "</textarea><br />");
			sb.append("<div id='send_block'><button id='save' class='btn btn-success css_btn' type='submit' name='save'><span class='glyphicon glyphicon-floppy-disk pos'></span> <span>暫存</span></button> ");
			sb.append("<button id='send' class='btn btn-primary css_btn' type='button' name='button'><span class='glyphicon glyphicon-ok'></span> <span>送出</span></button> ");
			sb.append("<button id='clear' class='btn btn-danger css_btn' type='button' name='clear'><span class='glyphicon glyphicon glyphicon-remove pos'></span> <span>清空</span></button></div>");
			sb.append("<input type='hidden' id='status' name='status' value='0'  />");
			sb.append("<input  type='hidden' id='deletedNum' name='deletedNum' value='0' /></form>");
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
