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
 * Servlet implementation class forgetPwd
 */
public class forgetpwd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mailHost;
	private String mailPort;
	private String username;
	private String password;
	private Properties props;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public forgetpwd() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
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
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");  
		JDBC_Oracle db = new JDBC_Oracle();
		PrintWriter out = response.getWriter();
		int id = (request.getParameter("userid").length() == 9) ? Integer.parseInt(request.getParameter("userid")) : 0;
		String email = request.getParameter("email");
		String password = db.getPassword(id, email);
		String url = "localhost/IISI_Project/";
		String from = db.getEmail(401170000);
		String to = db.getEmail(id);
		if (!to.equals(email)) {
			out.write("您所提供的帳號與此信箱不吻合");
			return;
		}
		else
			out.write("已寄送密碼至您的信箱");
		out.close();
		String subject = "找回密碼通知";
		String text = "您在工時統設定的密碼為：" + password +"\n請妥善保管。";
		try {
            Message message = getMessage(from, to, subject, text);
            Transport.send(message);
            
        } catch (Exception e) {
            throw new ServletException(e);
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		message.setFrom(new InternetAddress(from , "時達特工時管理系統"));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject);
		message.setSentDate(new Date());
		message.setText(text);
		return message;
	}
	
}
