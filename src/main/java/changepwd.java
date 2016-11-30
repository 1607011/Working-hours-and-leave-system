

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class changepwd
 */
public class changepwd extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public changepwd() {
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
		String oldpwd = request.getParameter("old_password");
		String newpwd = request.getParameter("new_password");
		String confirmpwd = request.getParameter("confirm_password");
		HttpSession session = request.getSession();
		int formid = (int)session.getAttribute("userid");
		String pwd = db.getPassword(formid);
//		int code = 0;
		if (!pwd.equals(oldpwd)) 
			out.println("<script type='text/javascript'>alert('舊密碼不正確，請重新輸入!');history.go(-1); </script>");
		else {
	       ServletContext sc = getServletContext();
	       RequestDispatcher rd = null;
	       out.println("<script type='text/javascript'>alert('成功修改密碼！');window.location.assign('/IISI_Project/login.html');</script>");
	       db.modifyPassword(formid, newpwd);	
//	    rd = sc.getRequestDispatcher("/login.html");
//	   rd.forward(request, response);	
//			out.write(4);
			//code = 4;
		}
	//	out.write(code);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
