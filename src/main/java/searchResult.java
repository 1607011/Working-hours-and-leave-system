

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class searchResult
 */
public class searchResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public searchResult() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		JDBC_Oracle db = new JDBC_Oracle();
		StringBuilder sb = new StringBuilder();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
    	String id = request.getParameter("search_id");
		String name = request.getParameter("search_name");
		String year = request.getParameter("start_year");
		String start = request.getParameter("start");
		String end = request.getParameter("end");
		sb.append("<table id='datas_modify' class='table' rules='rows'>");
		sb.append("<thead title='點選欄位可為您資料做排序'>");
		sb.append("<tr id='thead'>");
		sb.append("<th style='width: 60px;'><span class='glyphicon glyphicon-list-alt'></span> 工時紀錄資料</th>");
		sb.append("<th style='width: 50px;' class='date'><span class='glyphicon glyphicon-sort-by-attributes'></span>  週次起日</th>");
		sb.append("<th style='width: 50px;' class='date'><span class='glyphicon glyphicon-sort-by-attributes'></span> 週次訖日</th>");
		sb.append("<th style='width: 40px;'><span class='glyphicon glyphicon-user'></span>  員工姓名</th>");
		sb.append("<th style='width: 50px;'><span class='glyphicon glyphicon-info-sign'></span>  員工編號</th>");
		sb.append("<th style='width: 40px;' class='allTime'><span class='glyphicon glyphicon-sort-by-order'></span>  週總時數</th></tr></thead>");
		sb.append("<tbody id='tbody' class='tableSortable'>");
		ArrayList<String> results = db.taskQuery(id, name, year, start, end, true);
		ArrayList<String> uids = db.taskQuery(id, name, year, start, end, false);
		int length = results.size();
		int pages = (request.getParameter("now") == null) ? 1 : Integer.parseInt(request.getParameter("now"));
		int startItem = 0;
		int endItem = 5 * pages;
		startItem = 0 + ((pages - 1) * 5) ;
		if (endItem > results.size())
			endItem = results.size();
		for (int i = startItem; i < endItem; i++) {
			String result = results.get(i);
			int uid = Integer.parseInt(uids.get(i));
			sb.append("<tr id='search_results'>");
			sb.append("<td class='datas' data-toggle='modal' data-target='#myModal'><a class='newTab' style='text-decoration: none;'><input id='task' value=");
			sb.append(result);
			sb.append(" readonly='readonly' /></a></td>");
			sb.append("<td class='datas' data-toggle='modal' data-target='#myModal'>");
			sb.append(db.getStartDate(result));
			sb.append("</td><td class='datas' data-toggle='modal' data-target='#myModal'>");
			sb.append(db.getEndDate(result));
			sb.append("</td><td class='datas' data-toggle='modal' data-target='#myModal'>");
			sb.append("<input id='name' value='");
			sb.append(db.getUserName(uid));
			sb.append("' readonly='readonly' /></td><td class='datas' data-toggle='modal' data-target='#myModal'><input id='id' value='");
			sb.append(uid);
			sb.append("' readonly='readonly' /></td><td class='datas' data-toggle='modal' data-target='#myModal'>");
			sb.append(db.getWeekTotal(result, uid));
			sb.append("</td></tr>");
		}
		sb.append("</tbody>");
		if (results.isEmpty()) {
			sb.append("<tfoot id='tfoot'><tr><td colspan='6'>查無此項</td>");
			sb.append("</tr></tfoot>");
		}	
		sb.append("</table><input type='hidden' id='userid' name='userid' value='' />");
		sb.append("<input type='hidden' id='Go' name='title' value='' />");
		int nowPage = (request.getParameter("nowPage") == null)? 1: Integer.parseInt(request.getParameter("nowPage"));
		if (request.getParameter("now") != null)
			nowPage = Integer.parseInt(request.getParameter("now"));
		int items = 5 * nowPage;
		sb.append("<table border='0' style='float: left;' id='pageConfig' class='page'>");
		sb.append("<tbody><tr>");
		if (nowPage == 1) { 
			sb.append("<td></td><td></td>");
		}
		else {
			sb.append("<td></td><td id='fast-backward'><span class='glyphicon glyphicon-fast-backward control'></span></td><td id='backward'><span class='glyphicon glyphicon-backward control'></span></td>");			
		}
		sb.append("<td><select id='nowPage' name='nowPage'>");		
		for (int p = 1; p <= Math.ceil((double)length / 5); p++) {
			if(nowPage == p) {
				sb.append("<option selected='selected'>");
				sb.append(p);
				sb.append("</option>");
			}
			else {
				sb.append("<option>");
				sb.append(p);
				sb.append("</option>");
			}
		}
		sb.append("</select></td>");
		if (items < length) {
			sb.append("<td id='forward'><span class='glyphicon glyphicon-forward control'></span></td>");
			sb.append("<td id='fast-forward'><span class='glyphicon glyphicon-fast-forward control'></span></td>");
		}
		else {
			sb.append("<td></td><td></td>");
		}
		sb.append("</tr></tbody></table>");
		if (items < length) {
			sb.append("<div style='position: relative; float: right;' class='page'>[ ");
			sb.append(nowPage);
			sb.append(" - ");
			sb.append(items);
			sb.append(" / ");
			sb.append(length);
			sb.append(" ]</div>");
		} 
		else {
			sb.append("<div style='position: relative; float: right;' class='page'>[ ");
			sb.append(nowPage);
			sb.append(" - ");
			sb.append(length);
			sb.append(" / ");
			sb.append(length);
			sb.append(" ]</div>");
		}
		sb.append("<input id='now' name='now' type='hidden' value='1' />");
		sb.append("<input id='totalPage' name='totalPage' type='hidden' value='1' />");
		if (length != 0)
			sb.append("<br /><br /><button type='button' class='btn btn-success' id='export'><span class='glyphicon glyphicon-export'></span> <span>匯出Excel檔案</span></button>");
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
