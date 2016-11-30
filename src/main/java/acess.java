

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class acess
 */
public class acess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public acess() {
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
//		HttpSession session = request.getSession(true);
		String task_name = request.getParameter("title");
		int j = 0;
//		int temp = 0;
		int id = Integer.parseInt(request.getParameter("userid"));
		sb.append("<form id='manager_block' class='form1' name='datas_modify_form' action='' method='POST' onsubmit='return false;''>");
		sb.append("<input class='selfData' style='width: 370px; text-align: center; float: left; margin-left: 0px;' class='form-control data' value='工時周次：" + db.getStartDate(task_name) + " - " + db.getEndDate(task_name) + "' readonly='readonly' />");
		sb.append("<input class='selfData' style='width: 180px; text-align: center; float: left; margin-left: 50px;' class='form-control data' value='填寫人姓名：" + db.getUserName(id) + "' readonly='readonly' />");
		sb.append("<input class='selfData' style='width: 200px; text-align: center; float: left; margin-left: 50px;' class='form-control data' value='填寫人ID：" + id + "' readonly='readonly' /><br />");
		sb.append("<table class='table' id='datas_new'><thead id='thead_new'><tr><th style='width: 130px'></th>");
		for (String date : db.queryTaskListDate(task_name, id)) { 
			if (!date.isEmpty()) {
				sb.append("<th id='t"+ j +"' style='width: 65px; font-size: 14px; line-height: 26px;'><input name='t" + j + "' type='text' value='" + date + "' readonly='readonly' /></th>");
			}
			else if (date.isEmpty()) {
				sb.append("<th style='width: 65px;'><input name='t" + j + "' type='text' value='' readonly='readonly' /></th>");
			}
			j++;
		}
		j = 0;
		sb.append("</tr></thead>");
		sb.append("<tbody id='tbody_new'>");
		for(int i = 0; i < db.queryTaskListTask(task_name, id).size(); i++) {
			sb.append("<tr style='color: black' >");
			sb.append("<td id='item" + i + "' class='items' align='center'><input name='item" + i + "' type='text' value='");
			sb.append(db.queryTaskListTask(task_name, id).get(i));
			sb.append("' readonly='readonly' />");
			int hours[][] = db.queryTaskListHour(task_name, id);
			for(j = 0; j < 5; j++) {
				if (hours[i][j] != 10) {
					sb.append("<td><input type='text' id='time" + i + j + "' class='times form-control'  value='");
					sb.append(hours[i][j]);
					sb.append("' readonly='readonly' /></td>");
				}
				else {
					sb.append("<td><input type='text' id='time" + i + j + "' class='times form-control' value='' readonly='readonly' /></td>");	
				}
			}
				sb.append("<td><input type='text' id='time" + i + "5' class='times form-control' name='time" + i + "5' value='' readonly='readonly' /></td>");
				sb.append("<td><input type='text' id='time" + i + "6' class='times form-control' name='time" + i + "6' value='' readonly='readonly' /></td>");
				sb.append("<td><input type='text' id='week" + i + "' class='week times form-control' name='week" + i  + "' readonly='readonly'>");
				sb.append("<td></td></tr>");
			}
			sb.append("</tbody><tfoot><tr><td style='width: 155px;'></td>");
			for (int i = 0; i < 7; i++) {
				sb.append("<td><input type='text' id='day" + i + "' class='days form-control' readonly='readonly' /></td>");
			}		
			sb.append("<td><input type='text' id='weektotal' class='days form-control' name='weektotal' readonly='readonly' /></td>");
			sb.append("<td></td></tr></tfoot></table>");
			sb.append("<input type='hidden' id='status' name='status' value=''  /></form>");
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
