

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class taskModify
 */
public class taskModify extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public taskModify() {
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
		HttpSession session = request.getSession();
		int i = 0;
		int id = Integer.parseInt(session.getAttribute("userid").toString());
		Set<String> tasks = db.modifyTaskList(id).keySet();
		StringBuilder sb = new StringBuilder();
		sb.append("<form id='manager_block1' name='datas_modify_form' action='' method='POST'>");
		sb.append("<input type='text' id='deleteTask' name='deleteTask' value='' style='visibility: hidden' /><input type='text' id='Go' name='title' value='' style='visibility: hidden' />");
		sb.append("<table id='datas_modify' class='table tableSortable' rules='rows'><thead title='點選欄位可為您資料做排序'>");
		sb.append("<tr id='thead'><th style='width: 80px;'><span class='glyphicon glyphicon-list-alt'></span> 工時紀錄資料</th>");
		sb.append("<th style='width: 70px;' class='date'><span class='glyphicon glyphicon-sort-by-attributes'></span> 週次起日</th>");
		sb.append("<th style='width: 70px;' class='date'><span class='glyphicon glyphicon-sort-by-attributes'></span> 週次訖日</th><th style='width: 70px;' class='status'><span class='glyphicon glyphicon-sort-by-alphabet'></span> 資料狀態 </th><th style='width: 40px;'><span class='glyphicon glyphicon-wrench'></span> 處理</th></tr></thead>");
		sb.append("<tbody id='tbody' class='tableSortable'>");   
		for(String task : tasks) {
			sb.append(String.format("<tr><td class='datas' data-toggle='modal' data-target='#myModal'><a class='newTab' style='text-decoration: none;'><input id='task' name='task%d' value='"+ task +"' readonly='readonly' /></a></td>", i++));
			sb.append("<td id='" + task + "' class='datas' data-toggle='modal' data-target='#myModal'>" + db.getStartDate(task) + "</td>");
			sb.append("<td class='datas' data-toggle='modal' data-target='#myModal'>" + db.getEndDate(task) +"</td>");
			if (db.modifyTaskList(id).get(task) == 4) {
				sb.append("<td class='result save'><img class='save' src='../resourses/img/icon/save.png' />暫存</td>");
			}
			else { 	
				sb.append("<td class='result error'><img class='error' src='../resourses/img/icon/error.png' />退回</td>");
			}
			sb.append("<td><img src='../resourses/img/icon/trash.png' class='trash'></td></tr>");
		}
		sb.append("</tbody><tfoot id='tfoot' hidden='hidden'><tr><td colspan='4'>目前尚無任何資料！</td><td>");
		sb.append("<button id='new' class='btn btn-primary css_btn' onclick='location.replace(" + response.encodeURL("workTime_new.jsp") + ")'>");
		sb.append("<span class='glyphicon glyphicon-plus'>新增</span></button></td></tr></tfoot></table></form>");
		sb.append("<div id='myModal' class='modal fade'  data-backdrop='static' data-keyboard='false'  role='dialog'><div class='modal-dialog'><div class='modal-content'><div class='modal-header'><button type='button' class='close' data-dismiss='modal'>&times;</button><h4 class='modal-title'></h4></div><div class='modal-body'><article class='htmleaf-container'><div id='calendar'></div></article><div class='content'></div></div></div></div></div></div>");
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
