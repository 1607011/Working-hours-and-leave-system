
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JDBC.JDBC_Oracle;

/**
 * Servlet implementation class vertifys
 */

public class vertifys extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mailHost;
	private String mailPort;
	private String username;
	private String password;
	private Properties props;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public vertifys() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void init() {
		mailHost = "smtp.gmail.com";
		mailPort = "465";
		username = "stanley820609@gmail.com";
		password = "a9988775";
		props = new Properties();
		props.put("mail.smtp.host", mailHost);
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", mailPort);
		props.setProperty("mail.smtp.socketFactory.port", mailPort);
		props.setProperty("mail.smtp.auth", "true");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		JDBC_Oracle db = new JDBC_Oracle();
		StringBuilder sb = new StringBuilder();
		PrintWriter out = response.getWriter();
		int id = Integer.parseInt(request.getParameter("userid"));
		int status = Integer.parseInt(request.getParameter("vertifyStatus"));
		String cause = request.getParameter("cause");
		if (cause.isEmpty())
			cause = "無";
		String dataStatus = (status == 2) ? "核可" : "退回";
		String task_name = request.getParameter("title");
		String startDate = db.getStartDate(task_name);
		String endDate = db.getEndDate(task_name);
		int days = Integer.parseInt(endDate.substring(8, 10)) - Integer.parseInt(startDate.substring(8, 10)) + 1;
		db.vertifyResult(id, task_name, status, cause);
		out.write(String.format("%s審核結果已送出成功！", task_name));
		out.close();
		// 設定寄發信件的寄送方/接收方/主題/內容
		String from = db.getEmail(401170000);
		String to = db.getEmail(id);
		sb.append(String.format("工時紀錄%s通知", dataStatus));
		String subject = sb.toString();
		sb.append(":\n");
		sb.append(String.format("您送來的%s-%s為期%d日之工時紀錄表已被%s。\n", startDate, endDate, days, dataStatus));
		if (status == 3)
			sb.append(String.format("--- 退回原因如下 ---\n%s", cause));
		else if (status == 2)
			sb.append(String.format("--- 核可原因如下 ---\n%s", cause));
		String text = sb.toString();
		try {
            Message message = getMessage(from, to, subject, text);
            Transport.send(message);
        } catch (Exception e) {
            throw new ServletException(e);
        } 
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

	private Message getMessage(String from, String to, String subject, String text)
			throws MessagingException, AddressException, UnsupportedEncodingException {
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from , "主管"));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject);
		message.setSentDate(new Date());
		message.setText(text);
		return message;
	}

}
