package Leave;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class searchleave
 */
public class searchleave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public searchleave() {
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
		int year = Integer.parseInt(request.getParameter("year"));
		String searchname = request.getParameter("searchname");
		int search_id =db.getUserId(searchname);
		search_id =	Integer.parseInt(request.getParameter("searchid"));
		String typesCh[] = {"病假", "事假", "公假", "喪假", "產假", "生理假", "特休", "補休"};
		for (int i = 0; i < typesCh.length; i++) {
		db.getUpperBound(search_id, typesCh[i], year);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
