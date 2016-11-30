package Leave;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class create
 */
public class create extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public create() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		JDBC_Oracle db = new JDBC_Oracle();
		PrintWriter out = response.getWriter();
		String type = request.getParameter("leave");
		String reason = request.getParameter("reason");
		String startdate = request.getParameter("startdate");
		String enddate = startdate;
		String starttime = request.getParameter("starttime");
		String endtime = request.getParameter("endtime");
		String dataStatus = request.getParameter("sendClick");
		HttpSession session = request.getSession();
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:MM");
		String lication_date = SDF.format(new Date());
		int user_id = (int) session.getAttribute("userid");
		String userid = session.getAttribute("userid").toString();
		int status = 1;
		String leave_id = userid + " " + startdate + " " + starttime + ":00";
		String[] ary = starttime.split(":");
		String[] ary2 = endtime.split(":");
		String[] ary3 = startdate.split("-");
		int st = Integer.parseInt(ary[0]);
		int et = Integer.parseInt(ary2[0]);
		int sd = Integer.parseInt(ary3[0]);
		int time = 0;
		int right_time = 0;
		int times = 0;
		if ((st < 12) && (et <= 12)) {
			time = et - st;
			right_time = 0;
			times = time + right_time;
		} else if ((st >= 13) && (et > 13)) {
			time = et - st;
			right_time = 0;
			times = time + right_time;
		} else {
			time = 0;
			right_time = et - st - 1;
			times = time + right_time;
		}
		if (dataStatus.equals("check")) {
			if (db.getStatus(user_id, startdate) == 1 || db.getStatus(user_id, startdate) == 2) {
				out.write("1");
			} else if (user_id == db.getTimes(startdate, user_id, starttime,
					endtime)) {
				out.write("2");
			}  else
				out.write("3");
		}
		else {
			if (db.getStatus(user_id, startdate) == 1 || db.getStatus(user_id, startdate) == 2) {
				out.write("該週尚有待審或已審的工時記錄,故無法新增此日假單！");
			} else if (user_id == db.getTimes(startdate, user_id, starttime,
					endtime)) {
				out.write("您的請假時間重複!!請擇日填寫");
			} else if (times + db.getUsedTime(user_id, type, sd) > db
					.getUpperBound(user_id, type, sd)) {
				out.write("假別時數超過!!無法新增此筆假單");
			}
			else {
				db.newLeaveList(leave_id, user_id, startdate, starttime, enddate,
						endtime, type, reason, status, times, sd, lication_date);
				int used_time = db.getUsedTime(user_id, type, sd);
				db.setUsedTime(used_time, user_id, type, sd);
				out.write("success");
			}
		}
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
