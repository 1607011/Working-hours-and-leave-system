package Leave;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class leaveVertifys
 */
public class leaveVertifys extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public leaveVertifys() {
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
		int id = db.getUserId(request.getParameter("username"));
		int status = Integer.parseInt(request.getParameter("vertifyStatus"));
		String cause = request.getParameter("cause");
		if (cause.isEmpty())
			cause = "無";
//		String dataStatus = (status == 2) ? "核可" : "退回";
		String leave_start_date = request.getParameter("leaveDate");
		db.leaveVertifyResult(id, leave_start_date, status, cause);
		System.out.println(request.getParameter("username") + "/" + id + "/" + leave_start_date + "/" + cause);
		out.write(String.format("%s審核結果已送出成功！", leave_start_date));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
