

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
 * Servlet implementation class vertify_data
 */
public class vertify_data extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public vertify_data() {
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
//		HttpSession session = request.getSession(true);
		String task_name = request.getParameter("title");
		int j = 0;
		int id = Integer.parseInt(request.getParameter("userid"));
		sb.append("<form id='manager_block' class='form1' name='datas_modify_form' action='' method='POST' onsubmit='return false;''>");
		sb.append("<input type='hidden' name='userid' value='" + id + "' />");
		sb.append("<input type='hidden' name='title' value='" + task_name + "' />");
		sb.append("<input class='selfData' style='width: 370px; text-align: center; float: left; margin-left: 0px;' class='form-control data' value='工時周次：" + db.getStartDate(task_name) + " - " + db.getEndDate(task_name) + "' readonly='readonly' />");
		sb.append("<input class='selfData' style='width: 180px; text-align: center; float: left; margin-left: 50px;' class='form-control data' value='填寫人姓名：" + db.getUserName(id) + "' readonly='readonly' />");
		sb.append("<input class='selfData' style='width: 200px; text-align: center; float: left; margin-left: 50px;' class='form-control data' name='userid' value='填寫人ID：" + id + "' readonly='readonly' /><br />");
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
		sb.append("<table class='table' id='datas_new'><thead id='thead_new'><tr><th style='width: 130px'></th>");
		for (String date : db.queryTaskListDate(task_name, id)) { 
			if (!date.isEmpty()) {
				sb.append("");
				sb.append("<th id='t"+ j +"' style='width: 65px; font-size: 14px; line-height: 26px;'><input name='t" + j + "' type='text' value='" + date + "' readonly='readonly' /></th>");
			}
			else if (date .isEmpty()) {
				sb.append("<th style='width: 65px;'><input name='t" + j + "' type='text' value='' readonly='readonly' /></th>");
			}
			j++;
		}
		j = 0;
		sb.append("</tr></thead>");
		sb.append("<tbody id='tbody_new'>");
		for(int i = 0; i < db.queryTaskListTask(task_name, id).size(); i++) {
			sb.append("<tr style='color: black' >");
			sb.append("<td id='item" + i + "' class='items' align='center'><input name='item" + i + "' type='text' value='");
			sb.append(db.queryTaskListTask(task_name, id).get(i));
			sb.append("' readonly='readonly' />");
			int hours[][] = db.queryTaskListHour(task_name, id);
			for(j = 0; j < 5; j++) {
				if (hours[i][j] != 10) {
					sb.append("<td><input type='text' id='time" + i + j + "' class='times form-control'  value='");
					sb.append(hours[i][j]);
					sb.append("' readonly='readonly' /></td>");
				}
				else {
					sb.append("<td><input type='text' id='time" + i + j + "' class='times form-control' value='' readonly='readonly' /></td>");	
				}
			}
			sb.append("<td><input type='text' id='time" + i + "5' class='times form-control' name='time" + i + "5' value='' readonly='readonly' /></td>");
			sb.append("<td><input type='text' id='time" + i + "6' class='times form-control' name='time" + i + "6' value='' readonly='readonly' /></td>");
			sb.append("<td><input type='text' id='week" + i + "' class='week times form-control' name='week" + i  + "' readonly='readonly'>");
			sb.append("<td></td></tr>");
		}
		sb.append("</tbody><tfoot><tr><td style='width: 155px;'></td>");
		for (int i = 0; i < 7; i++) {
			sb.append("<td><input type='text' min='0' max='8' id='day" + i + "' class='days form-control' readonly='readonly' /></td>");
		}		
		sb.append("<td><input type='text' id='weektotal' class='days form-control' name='weektotal' readonly='readonly' /></td>");
		sb.append("<td></td></tr></tfoot></table>");
		sb.append("<div id='send_block'><textarea id='cause' name='cause' cols='60' rows='6' placeholder='請在此填寫核可/退回原因' autofocus></textarea><br />");
		sb.append("<button id='acess' class='btn btn-primary css_btn' type='button'><span class='glyphicon glyphicon-check'></span> <span id='acessSpan'>核可</span></button> ");
		sb.append("<button id='back' class='btn btn-danger css_btn' type='button'><span class='glyphicon glyphicon glyphicon-remove'></span> <span id='backSpan'>退回</span></button></div>");			
		sb.append("<input type='hidden' id='vertifyStatus' name='vertifyStatus' value='2'  /><input type='hidden' id='vertifyStatus' name='vertifyStatus' value='2'  /></form>");
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
