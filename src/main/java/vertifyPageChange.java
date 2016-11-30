

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class vertifyPageChange
 */
public class vertifyPageChange extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public vertifyPageChange() {
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
		PrintWriter out = response.getWriter();
		ArrayList<String> tasks = db.getAllCheckList();
		StringBuilder sb = new StringBuilder();
		sb.append("<tbody id='tbody' class='tableSortable'>");
		int length = tasks.size();
		int pages = (request.getParameter("now") == null) ? 1 : Integer.parseInt(request.getParameter("now"));
		int startItem = 0;
		int endItem = 5 * pages;
		startItem = 0 + ((pages - 1) * 5) ;
		if (endItem > length)
			endItem = length;
		for (int i = startItem; i < endItem; i++) {
			String task = tasks.get(i);
			String str[] = task.split("\\D");
			int uid = Integer.parseInt(str[0]);
			String task_name = str[1] + "_" + str[2];
			sb.append("<tr id='vertify_datas'><td class='datas' data-toggle='modal' data-target='#myModal'><a class='newTab' style='text-decoration: none;><input id='task' value='");
			sb.append(task_name);
			sb.append("' readonly='readonly' /></a></td>");
			sb.append("<td class='datas' data-toggle='modal' data-target='#myModal'>");
			sb.append(db.getStartDate(task_name));
			sb.append("</td><td class='datas' data-toggle='modal' data-target='#myModal'>");
			sb.append(db.getEndDate(task_name));
			sb.append("</td><td class='datas' data-toggle='modal' data-target='#myModal'><input id='name' value='");
			sb.append(db.getUserName(uid));
			sb.append("' readonly='readonly' /></td>");
			sb.append("<td class='datas' data-toggle='modal' data-target='#myModal'><input id='name' value='");
			sb.append(uid);
			sb.append("' readonly='readonly' /></td>");
			sb.append("<td class='datas' data-toggle='modal' data-target='#myModal'>");
			sb.append(db.getWeekTotal(task_name, uid));
			sb.append("</td></tr>");
		}
		sb.append("</tbody>");
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
