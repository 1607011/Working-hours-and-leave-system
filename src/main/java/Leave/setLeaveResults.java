package Leave;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.SystemOutLogger;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class setLeaveResults
 */
public class setLeaveResults extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public setLeaveResults() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		JDBC_Oracle db = new JDBC_Oracle();
		StringBuilder sb = new StringBuilder();
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String types[] = { "sick", "thing", "business", "die", "pregnant",
				"women", "special", "repair" };
		String typesCh[] = { "病假", "事假", "公假", "喪假", "產假", "生理假", "特休", "補休" };
		int typesHr[] = { 240, 112, 168, 64, 320, 96, 80, 104 };
		int setid = request.getParameter("searchid").isEmpty() ? 0 : Integer
				.parseInt(request.getParameter("searchid"));
		String setname = request.getParameter("searchname");
		if (setid == 0 && setname.isEmpty()) {
			out.write("NULL");
			return;
		} else if (setid != 0 && db.getUserName(setid).isEmpty()) {
			out.write("NULL");
			return;
		}
		if (setid == 0)
			setid = db.getUserId(setname);
		System.out.println(setname + "/" + setid);
		if (db.getSex(setid) == 1) {
			types[4] = "";
			types[5] = "";
		}
		int year = request.getParameter("year") == null ? 2016 : Integer
				.parseInt(request.getParameter("year"));
		sb.append(String.format(
				"<input id='id' name='id' type='hidden' value='%d'>", setid));
		sb.append(String.format(
				"<input id='name' name='name' type='hidden' value='%s'>",
				setname));
		sb.append(String
				.format("<input id='set_year' name='set_year' type='hidden' value='%d'>",
						year));
		sb.append("<table width='340' border='1'><tbody>");
		for (int i = 0; i < types.length; i++) {
			if (!types[i].isEmpty()) {
				if (i % 2 == 0)
					sb.append("<tr>");
				sb.append(String
						.format("<td><div class='form-group'><label class='control-label col-sm-6 col-xs-6' for='%s'>%s:</label>",
								types[i], typesCh[i]));
				sb.append(String
						.format("<div class='col-sm-6 col-xs-6'><input type='number' min='%d' max='%d' id='%s' name='%s' class='form-control types' onkeyup=\"value=value.replace(/[^\\d]/g,'')\" value='%d'>",
								db.getUsedTime(setid, typesCh[i], year),
								typesHr[i], types[i], types[i],
								db.getUpperBound(setid, typesCh[i], year)));
				sb.append("</div></div></td>");
				if (i % 2 == 1)
					sb.append("</tr>");
			}
		}
		sb.append("</tbody></table><br /><div class='form-group'><div class='col-sm-11'><button type='button' class='btn btn-primary css_btn' id='setLeave' style='margin-right: 12px;'><span class='glyphicon glyphicon-cog'></span> <span>設定</span></button><button type='button' class='btn btn-danger css_btn' id='default' style='margin-right: 18px;'><span class='glyphicon glyphicon-refresh'></span> <span>重設</span></button></div></div>");
		out.write(sb.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
