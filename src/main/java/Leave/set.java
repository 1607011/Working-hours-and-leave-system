package Leave;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class set
 */
public class set extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public set() {
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
		PrintWriter out = response.getWriter();
		String types[] = {"sick", "thing", "business", "die", "pregnant", "women", "special", "repair"};
		String typesCh[] = {"病假", "事假", "公假", "喪假", "產假", "生理假", "特休", "補休"};
		int setid = Integer.parseInt(request.getParameter("id"));
		int year = 2016;
		for (int i = 0; i < typesCh.length; i++) {
			int upperbound = (request.getParameter(types[i]).isEmpty()) ? 0 : Integer.parseInt(request.getParameter(types[i]));
			System.out.println(setid + "/" + year + "/" + upperbound);
			db.setUpperBound(setid, year, typesCh[i], upperbound);
		}
		out.write("請假時數上限設定完成");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
