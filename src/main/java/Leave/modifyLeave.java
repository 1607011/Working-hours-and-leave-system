package Leave;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class modifyLeave
 */
public class modifyLeave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public modifyLeave() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		JDBC_Oracle db = new JDBC_Oracle();
		StringBuilder sb = new StringBuilder();
		PrintWriter out = response.getWriter();
		int user_id = Integer.parseInt(request.getParameter("userid"));
		String leave_date = request.getParameter("title");
		System.out.println(user_id + "/" + leave_date);
		String sql = String.format("SELECT type, start_time, end_time, reason, back_reason FROM leavelist WHERE user_id=%d AND leave_start_date='%s' AND status=3", user_id, leave_date);
		try (ResultSet rs = db.getResultSet(sql)) {
			while (rs.next()) {
				sb.append("<form id='form-create' class='form-horizontal' role='form' name='create_form'action='/IISI_Project/create' method='POST' onSubmit='return true;'><div class='form-group' id='select'><label class='control-label col-sm-2 col-xs-6' for='leave'>請假類別:</label><div class='col-sm-2 col-xs-6'><input id='leave' class='form-control' name='leave' ");
				sb.append("value='" + rs.getString(1) + "'  ></div></div>");
				sb.append("<div class='form-group'><label class='control-label col-sm-2 col-xs-6' for='name'>員工姓名:</label><div class='col-sm-2 col-xs-6'><input type='text' name='username' ");
				sb.append("value='" + db.getUserName(user_id) + "' class='form-control' name='userName'  >	</div></div>");
				sb.append("<div class='form-group'><label class='control-label col-sm-2 col-xs-6' for='datepickers'>請假時間:</label><div class='col-sm-2 col-xs-6'><input type='text' id='datepickers' name='leaveDate' style='width: auto;' class='form-control'   name='startdate' value='" + leave_date + "'></div></div>");
				sb.append("<div class='form-group'><label class='control-label col-sm-2 col-xs-6' for='timepickers'>請假時段:</label><div class='col-sm-2 col-xs-6'><input type='text' id='timepickers' class='form-control'   value='" + rs.getString(2) + "' name='starttime'></div>");
				sb.append("<div class='col-sm-2 col-xs-6'><input id='timepickers2' type='text' class='form-control' value='" + rs.getString(3) + "' name='endtime'  ></div></div>");
				sb.append("<div class='form-group'><label class='control-label col-sm-2 col-xs-6' for='reason'>請假原因:</label><div class='col-sm-4 col-xs-6'><textarea class='form-control' rows='5' id='reason' name='reason'  >" + (rs.getString(4) == null ? "無" : rs.getString(4)) + "</textarea></div></div>");
				sb.append("<textarea id='cause' name='cause' cols='50' rows='6' disabled>" + rs.getString(5) + "</textarea><br />");
				sb.append("<br><div class='form-group'><div class='col-sm-10'><button type='submit' class='btn btn-primary css_btn' id='send'><span class='glyphicon glyphicon-ok'></span> <span>送出</span></button></div></div></form>");
			}
			out.write(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
