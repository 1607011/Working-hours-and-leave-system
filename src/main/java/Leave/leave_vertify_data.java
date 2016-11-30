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
 * Servlet implementation class leave_vertify_data
 */
public class leave_vertify_data extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public leave_vertify_data() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		JDBC_Oracle db = new JDBC_Oracle();
		StringBuilder sb = new StringBuilder();
		PrintWriter out = response.getWriter();
		int user_id = Integer.parseInt(request.getParameter("userid"));
		String leave_date = request.getParameter("title");
		System.out.println(user_id + "/" + leave_date);
		String sql = String.format("SELECT type, start_time, end_time, reason, back_reason FROM leavelist WHERE user_id=%d AND leave_start_date='%s' AND status=1", user_id, leave_date);
		try (ResultSet rs = db.getResultSet(sql)) {
			while (rs.next()) {
				sb.append("<form id='form-create' class='form-horizontal' role='form' name='create_form'action='/IISI_Project/create' method='POST' onSubmit='return true;'><div class='form-group' id='select'><label class='control-label col-sm-2 col-xs-6' for='leave'>請假類別:</label><div class='col-sm-2 col-xs-6'><input id='inputStyle' class='form-control' name='leave' ");
				sb.append("value='" + rs.getString(1) + "' readonly></div></div>");
				sb.append("<div class='form-group'><label class='control-label col-sm-2 col-xs-6' for='name'>員工姓名:</label><div class='col-sm-2 col-xs-6'><input type='text' id='inputStyle' name='username' ");
				sb.append("value='" + db.getUserName(user_id) + "' class='form-control' name='userName' readonly>	</div></div>");
				sb.append("<div class='form-group'><label class='control-label col-sm-2 col-xs-6' for='datepickers'>請假時間:</label><div class='col-sm-2 col-xs-6'><input type='text' id='inputStyle' name='leaveDate' style='width: auto;' class='form-control' readonly name='startdate' value='" + leave_date + "'></div></div>");
				sb.append("<div class='form-group'><label class='control-label col-sm-2 col-xs-6' for='timepickers'>請假時段:</label><div class='col-sm-2 col-xs-6'><input type='text' id='inputStyle' class='form-control' readonly value='" + rs.getString(2) + "' name='starttime'></div>");
				sb.append("<div class='col-sm-2 col-xs-6'><input id='inputStyle' type='text' class='form-control' style='margin-left: 50px;' value='" + rs.getString(3) + "' name='endtime' readonly></div></div>");
				sb.append("<div class='form-group'><label class='control-label col-sm-2 col-xs-6' for='reason'>請假原因:</label><div class='col-sm-4 col-xs-10'><textarea class='form-control' rows='5' id='inputStyle' style='width: 280px;' readonly>" + (rs.getString(4) == null ? "無" : rs.getString(4)) + "</textarea></div></div>");
				sb.append("<div id='send_block'><textarea id='cause' name='cause' cols='60' rows='6' placeholder='請在此填寫核可/退回原因' autofocus></textarea><br />");
				sb.append("<button id='acessLeave' class='btn btn-primary css_btn' type='button'><span class='glyphicon glyphicon-check'></span> <span id='acessSpan'>核可</span></button> ");
				sb.append("<button id='backLeave' class='btn btn-danger css_btn' type='button'><span class='glyphicon glyphicon glyphicon-remove'></span> <span id='backSpan'>退回</span></button><input type='hidden' id='vertifyStatus' name='vertifyStatus' value='2'  /><input type='hidden' id='vertifyStatus' name='vertifyStatus' value='2'  /></div></form>");
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
