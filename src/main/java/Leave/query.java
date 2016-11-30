package Leave;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class query
 */
public class query extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public query() {
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
    	String id = request.getParameter("search_id");
		String name = request.getParameter("search_name");
		String year = request.getParameter("start_year");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		String status = "";
		String types[] = request.getParameterValues("type");
		String leaveTypes[] = request.getParameterValues("leaveType");
		String zero_s = (start.equals("10") || start.equals("11") || start.equals("12")) ? "" : "0";
		String zero_e = (end.equals("10") || end.equals("11") || end.equals("12")) ? "" : "0";
		String ID = (id.isEmpty() || id == null) ? "" : " AND user_id=" + id;
		String NAME = (name.isEmpty() || name == null) ? "" : " AND user_id IN (SELECT id FROM users WHERE name LIKE '%" + name + "%')";
		String MONTH = (start.isEmpty() || start == null) ? "" : " AND (leave_start_date BETWEEN '" + year + "-" + zero_s + start + "-%' AND '" + year + "-" + zero_e + (end + 1) + "-%')";
		StringBuilder selectSQL = new StringBuilder();
		selectSQL.append("SELECT user_id, leave_start_date, lication_date, type, status, times FROM leavelist WHERE status!=0");
		selectSQL.append(ID);
		selectSQL.append(NAME);
		selectSQL.append(MONTH);
		if (types != null){
			if (types.length > 0)
				selectSQL.append(" AND (status=" + types[0]);
			for (int i = 1; i < types.length; i++) {
				selectSQL.append(" OR status=" + types[i]);
				if (i == types.length - 1)
					selectSQL.append(")");
			}
			if (types.length == 1)
				selectSQL.append(")");
		}
		if (leaveTypes != null) { 
			if (leaveTypes.length > 0) {
				selectSQL.append(" AND (type='" + leaveTypes[0]);
				selectSQL.append("'");
			}
			for (int i = 1; i < leaveTypes.length; i++) {
				selectSQL.append(" OR type='" + leaveTypes[i]);
				selectSQL.append("'");
				if (i == leaveTypes.length - 1)
					selectSQL.append(")");
			}
			if (leaveTypes.length == 1)
				selectSQL.append(")");
		}
		String sql = selectSQL.toString();
		System.out.println(sql);
		ResultSet rs = db.getResultSet(sql);
		sb.append("<table id='datas_modify' class='table' rules='rows'>");
		sb.append("<thead title='點選欄位可為您資料做排序'>");
		sb.append("<tr id='thead'>");
		sb.append("<th style='width: 50px;'><span class='glyphicon glyphicon-user'></span>  員工姓名</th>");
		sb.append("<th style='width: 50px;'><span class='glyphicon glyphicon-info-sign'></span>  員工編號</th>");
		sb.append("<th style='width: 60px;' class='date'><span class='glyphicon glyphicon-sort-by-attributes'></span> 假單日期</th>");
		sb.append("<th style='width: 80px;' class='date'><span class='glyphicon glyphicon glyphicon-sort-by-attributes'></span>  申請時間</th>");
		sb.append("<th style='width: 50px;' class='status'><span class='glyphicon glyphicon-sort-by-alphabet'></span> 假別</th>");
		sb.append("<th style='width: 50px;' class='status'><span class='glyphicon glyphicon-sort-by-alphabet'></span> 假單狀態</th>");
		sb.append("<th style='width: 40px;' class='allTime'><span class='glyphicon glyphicon-sort-by-order'></span>  時數</th></tr></thead>");
		sb.append("<tbody id='tbody' class='tableSortable'>");
		int length  = 0;
		try {
			if (!rs.next()) {
				sb.append("<tfoot id='tfoot'><tr><td colspan='7'>查無此項</td>");
				sb.append("</tr></tfoot>");
			}
			rs.last(); 
			length = rs.getRow();
			rs.beforeFirst();	
		} catch (SQLException e1) {
			e1.printStackTrace();
		}	
		int pages = (request.getParameter("now") == null) ? 1 : Integer.parseInt(request.getParameter("now"));
		int startItem = 0;
		int endItem = 5 * pages;
		startItem = 0 + ((pages - 1) * 5) ;
		if (endItem > length)
			endItem = length;
		try {
//			System.out.println(length + "/" +rs.getRow());
			if (startItem != 0)
				rs.absolute(startItem);
			while (rs.next()) {
				switch (rs.getInt(5)) {
				case 1:
					status = "<td class='result' style='color: green;'><img class='save' src='../resourses/img/icon/wait.ico' />待審</td>";
					break;
				case 2:
					status = "<td class='result' style='color: blue;'><img class='approved' src='../resourses/img/icon/approved.png' />核可</td>";
					break;
				case 3:
					status = "<td class='result' style='color: red;'><img class='error' src='../resourses/img/icon/error.png' />退回</td>";
					break;
				}
				sb.append("<tr id='search_leave_results'>");
				sb.append("<td class='datas' data-toggle='modal' data-target='#myModal'><a class='newTab' style='color: rgb(102, 102, 102); text-decoration: none;'>");
				sb.append(db.getUserName(rs.getInt(1)));
				sb.append("</a></td>");
				sb.append("<td class='datas' data-toggle='modal' data-target='#myModal'><input id='user_id' name='user_id' value=");
				sb.append(rs.getInt(1));
				sb.append(" readonly='readonly' /></td><td class='datas' data-toggle='modal' data-target='#myModal'><input id='leave_date' name='leave_date' value=");
				sb.append(rs.getString(2));
				sb.append(" readonly='readonly' /></td><td class='datas' data-toggle='modal' data-target='#myModal'>");
				sb.append(rs.getString(3));
				sb.append("</td><td class='datas' data-toggle='modal' data-target='#myModal'>");
				sb.append(rs.getString(4));
				sb.append("</td>");
				sb.append(status);
				sb.append("<td class='datas' data-toggle='modal' data-target='#myModal'>");
				sb.append(rs.getInt(6));
				sb.append("</td></tr>");
				startItem++;
				if (startItem == endItem)
					break;
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
		sb.append("</tbody>");
		sb.append("</table><input type='hidden' id='userid' name='userid' value='' />");
		sb.append("<input type='hidden' id='Go' name='title' value='' />");
		int nowPage = (request.getParameter("nowPage") == null)? 1: Integer.parseInt(request.getParameter("nowPage"));
		if (request.getParameter("now") != null)
			nowPage = Integer.parseInt(request.getParameter("now"));
		int items = 5 * nowPage;
		sb.append("<table border='0' style='float: left;' id='pageConfig' class='page'>");
		sb.append("<tbody><tr>");
		if (nowPage == 1) { 
			sb.append("<td></td><td></td>");
		}
		else {
			sb.append("<td></td><td id='fast-backward'><span class='glyphicon glyphicon-fast-backward control'></span></td><td id='backward'><span class='glyphicon glyphicon-backward control'></span></td>");			
		}
		sb.append("<td><select id='nowPage' name='nowPage'>");		
		for (int p = 1; p <= Math.ceil((double)length / 5); p++) {
			if(nowPage == p) {
				sb.append("<option selected='selected'>");
				sb.append(p);
				sb.append("</option>");
			}
			else {
				sb.append("<option>");
				sb.append(p);
				sb.append("</option>");
			}
		}
		sb.append("</select></td>");
		if (items < length) {
			sb.append("<td id='forward'><span class='glyphicon glyphicon-forward control'></span></td>");
			sb.append("<td id='fast-forward'><span class='glyphicon glyphicon-fast-forward control'></span></td>");
		}
		else {
			sb.append("<td></td><td></td>");
		}
		sb.append("</tr></tbody></table>");
		if (items < length) {
			sb.append("<div style='position: relative; float: right;' class='page'>[ ");
			sb.append(nowPage);
			sb.append(" - ");
			sb.append(items);
			sb.append(" / ");
			sb.append(length);
			sb.append(" ]</div>");
		} 
		else {
			sb.append("<div style='position: relative; float: right;' class='page'>[ ");
			sb.append(nowPage);
			sb.append(" - ");
			sb.append(length);
			sb.append(" / ");
			sb.append(length);
			sb.append(" ]</div>");
		}
		sb.append("<input id='now' name='now' type='hidden' value='1' />");
		sb.append("<input id='totalPage' name='totalPage' type='hidden' value='1' />");
		if (length != 0)
			sb.append("<br /><br /><button type='button' class='btn btn-success' id='export'><span class='glyphicon glyphicon-export'></span> <span>匯出Excel檔案</span></button>");
		out.write(sb.toString());
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
