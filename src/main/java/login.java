

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class login
 */
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
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
		int id = 0;
//		String account = "";
		boolean log = false;
		String password = request.getParameter("password");
		if (request.getParameter("userid").length() == 9) {
			id = Integer.parseInt(request.getParameter("userid"));
			log = db.login(id, password);
		}
//		else if (request.getParameter("userid").length() == 7) {
//			account = request.getParameter("userid");
//			log = db.login(account, password);
//		}
//		String index = response.encodeURL("jsp/index.jsp");
		if (log) {
			HttpSession session = request.getSession(true);
			session.setAttribute("userid", id);
			int formid = (int) session.getAttribute("userid");
			String name = db.getUserName(formid);
			session.setAttribute("name", name);
//			response.sendRedirect(index);
			out.write("您已成功登入系統！");
		}
		else {
//			out.println("<script type='text/javascript'>alert('帳號或密碼有誤！請重新確認輸入');window.location.replace('login.html');</script>");
			out.write("帳號或密碼有誤！請重新確認輸入");
		}
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
