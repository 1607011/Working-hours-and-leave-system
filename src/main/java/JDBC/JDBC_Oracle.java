package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JDBC_Oracle {
	// private String url = "jdbc:oracle:thin:@localhost:1521:work";
	// private String uid = "root";
	// private String pwd = "root";
	private Connection connection = null;
	private PreparedStatement pst = null;
	private Statement statement = null;
	private ResultSet rs = null;
	private DataSource ds = null;
	private String selectSQL = "";
	private String updateSQL = "";
	private String insertTasklist = "";
	private String insertDetail = "";
	private String deleteTask = "";
	private String deleteTaskDetail = "";
	private String insertLeaveList = "";

	// 實體化同時註冊驅動
	public JDBC_Oracle() {
		try {
			// Class.forName("oracle.jdbc.driver.OracleDriver");
			InitialContext context = new InitialContext();
			Context envContext = (Context) context.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/DataSource");
			connection = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 獲取結果集方法
	public ResultSet getResultSet(String sql) {
		try {
			connection = ds.getConnection();
			pst = connection
					.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
			rs = pst.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	// 關閉連線
	public void close() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
		} catch (SQLException e) {
			System.out.println("Close Exception :" + e.toString());
		}
	}

	// 檢驗登入是否成功
	public boolean login(int id, String password) {
		boolean login = false;
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT password FROM users WHERE id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals(password))
					login = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return login;
	}

	// 檢驗登入是否成功，方法多載
	public boolean login(String account, String password) {
		boolean login = false;
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT password FROM users WHERE account=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, account);
			rs = pst.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals(password))
					login = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return login;
	}

	public String getPassword(int id) {
		String password = "";
		try (Connection connection = ds.getConnection()) {
			String updateSQL = "SELECT password FROM users WHERE id=?";
			pst = connection.prepareStatement(updateSQL);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				password = rs.getString("password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return password;
	}

	public String getPassword(int id, String email) {
		String password = "";
		try (Connection connection = ds.getConnection()) {
			String updateSQL = "SELECT password FROM users WHERE id=? AND email=?";
			pst = connection.prepareStatement(updateSQL);
			pst.setInt(1, id);
			pst.setString(2, email);
			rs = pst.executeQuery();
			while (rs.next()) {
				password = rs.getString("password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return password;
	}

	// 密碼修改
	public boolean modifyPassword(int id, String new_password) {
		boolean modify = false;
		try (Connection connection = ds.getConnection()) {
			updateSQL = "Update users SET password=? WHERE id=?";
			pst = connection.prepareStatement(updateSQL);
			pst.setString(1, new_password);
			pst.setInt(2, id);
			pst.executeUpdate();
		} catch (SQLException e) {
			return false;
		} finally {
			close();
		}
		return modify;
	}

	// 密碼修改，方法多載
	public boolean modifyPassword(String account, String new_password) {
		boolean modify = false;
		try (Connection connection = ds.getConnection()) {
			updateSQL = "Update users SET password=? WHERE account=?";
			pst = connection.prepareStatement(updateSQL);
			pst.setString(1, new_password);
			pst.setString(2, account);
			pst.executeUpdate();
		} catch (SQLException e) {
			return false;
		} finally {
			close();
		}
		return modify;
	}

	// 檢查工時紀錄是否已存在
	public boolean canAdd(String task_name, int id) {
		boolean canAdd = true;
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT status FROM tasklist WHERE task_id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) != 0)
					canAdd = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return canAdd;
	}
	
	// 取得員工性別
	public int getSex(int id) {
		int sex = 0;
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT sex FROM users WHERE id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				sex = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return sex;
	}
	
	// 取出工時資料狀態
	public int getStatus(String task_name, int id) {
		int status = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT status FROM tasklist WHERE task_id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				status = (rs.getInt(1) != 0) ? rs.getInt(1) : 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return status;
	}

	// 取得工時紀錄名稱
	public String getTaskTitle(String start_date) {
		String task_title = "";
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT week_name FROM week WHERE start_date=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, start_date);
			rs = pst.executeQuery();
			while (rs.next()) {
				task_title = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return task_title;
	}

	// 取得工時紀錄起日
	public String getStartDate(String task_name) {
		String startDate = "";
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT start_date FROM week WHERE week_name=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_name);
			rs = pst.executeQuery();
			while (rs.next()) {
				startDate = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return startDate;
	}

	// 取得工時紀錄訖日
	public String getEndDate(String task_name) {
		String endDate = "";
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT end_date FROM week WHERE week_name=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_name);
			rs = pst.executeQuery();
			while (rs.next()) {
				endDate = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return endDate;
	}

	// 新增工時紀錄方法
	public void newTask(String task_id, int user_id, String task_name,
			String start, String end, int status, int total) {
		try (Connection connection = ds.getConnection()) {
			insertTasklist = "INSERT INTO tasklist(task_id, user_id, task_name, start_date, end_date, status, total) VALUES(?, ?, ?, ?, ?, ?, ?)";
			pst = connection.prepareStatement(insertTasklist);
			pst.setString(1, task_id);
			pst.setInt(2, user_id);
			pst.setString(3, task_name);
			pst.setString(4, start);
			pst.setString(5, end);
			pst.setInt(6, status);
			pst.setInt(7, total);
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Insert datas Exception :" + e.toString());
		} finally {
			close();
		}
	}

	// 新增工時紀錄（明細）方法
	public void newTaskDetail(String task_id, String task_item, int MON,
			int TUE, int WED, int THU, int FRI, int SAT, int SUN, int ITEM_TOTAL) {
		try (Connection connection = ds.getConnection()) {
			insertDetail = "INSERT INTO detail(task_id, task_item, MON, TUE, WED, THU, FRI, SAT, SUN, item_total) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pst = connection.prepareStatement(insertDetail);
			pst.setString(1, task_id);
			pst.setString(2, task_item);
			pst.setInt(3, MON);
			pst.setInt(4, TUE);
			pst.setInt(5, WED);
			pst.setInt(6, THU);
			pst.setInt(7, FRI);
			pst.setInt(8, SAT);
			pst.setInt(9, SUN);
			pst.setInt(10, ITEM_TOTAL);
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Insert datas Exception :" + e.toString());
		} finally {
			close();
		}
	}

	// 取出待審核資料
	public ArrayList<String> getCheckList(int id) {
		ArrayList<String> tasks = new ArrayList<>();
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT task_name FROM tasklist WHERE status=1 AND user_id=? ORDER BY task_name ASC";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				tasks.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return tasks;
	}

	// 取出待審核資料
	public ArrayList<String> getAllCheckList() {
		ArrayList<String> tasks = new ArrayList<>();
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT task_id FROM tasklist WHERE status=1 ORDER BY task_id ASC";
			pst = connection.prepareStatement(selectSQL);
			rs = pst.executeQuery();
			while (rs.next()) {
				tasks.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return tasks;
	}

	// 送出審核結果
	public void vertifyResult(int id, String task_name, int status,
			String back_reason) {
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			updateSQL = "UPDATE tasklist SET status=?, back_reason=? WHERE task_id=?";
			pst = connection.prepareStatement(updateSQL);
			pst.setInt(1, status);
			pst.setString(2, back_reason);
			pst.setString(3, task_id);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// 取得員工信箱
	public String getEmail(int id) {
		String email = "";
		try (Connection conn = ds.getConnection()) {
			selectSQL = "SELECT email FROM users WHERE id=?";
			pst = conn.prepareStatement(selectSQL);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				email = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return email;
	}

	// 取出週總時數
	public int getWeekTotal(String task_name, int id) {
		int weekTotal = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT total FROM tasklist WHERE task_id=? ORDER BY task_id ASC";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				weekTotal = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return weekTotal;
	}

	// 取得已審核的資料狀態
	public int getStatus(String task_name) {
		int status = 0;
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT DISTINCT status FROM tasklist WHERE (status=2 OR status=3) AND task_name=? ORDER BY task_id ASC";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_name);
			rs = pst.executeQuery();
			while (rs.next()) {
				status = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return status;
	}

	// 取得已審核的資料員工id
	public int getCheckedID(String task_name) {
		int status = 0;
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT DISTINCT id FROM tasklist WHERE (status=2 OR status=3) AND task_name=? ORDER BY task_id ASC";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_name);
			rs = pst.executeQuery();
			while (rs.next()) {
				status = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return status;
	}

	// 取得員工Name By Id
	public String getUserName(int id) {
		String name = "";
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT name FROM users WHERE id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				name = rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return name;
	}

	// 取得員工id by工時紀錄
	public int getUserIdByTask(String task_name) {
		int id = 0;
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT user_id FROM tasklist WHERE task_name=? ORDER BY task_id ASC";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_name);
			rs = pst.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return id;
	}

	// 取得員工id by工時紀錄及工時狀態
	public int getUserIdByTask(String task_name, int status) {
		int id = 0;
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT user_id FROM tasklist WHERE task_name=? AND status=? ORDER BY task_id ASC";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_name);
			pst.setInt(2, status);
			rs = pst.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return id;
	}

	// 取得工時紀錄退回原因
	public String getBcakReason(String task_name, int id) {
		String backReason = "";
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT back_reason FROM tasklist WHERE task_id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				backReason = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return backReason;
	}

	// 取得員工id
	public int getUserId(String name) {
		int id = 0;
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT id FROM users WHERE name=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, name);
			rs = pst.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return id;
	}

	// 取的所有成員id
	public ArrayList<Integer> getAllUserid() {
		ArrayList<Integer> ids = new ArrayList<>();
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT id FROM users WHERE job='employee'";
			pst = connection.prepareStatement(selectSQL);
			rs = pst.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return ids;
	}

	// 可修改工時資料列表
	public LinkedHashMap<String, Integer> modifyTaskList(int id) {
		LinkedHashMap<String, Integer> tasks = new LinkedHashMap<>();
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT task_name, status FROM tasklist WHERE (status=4 OR status=3) AND user_id=? ORDER BY task_id ASC";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				tasks.put(rs.getString(1), rs.getInt(2));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return tasks;
	}

	// 刪除工時紀錄方法
	public void deleteTask(String task_name, int id) {
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			StringBuilder sb = new StringBuilder();
			sb.append(id);
			sb.append("_");
			sb.append(task_name);
			String task_id = sb.toString();
			deleteTaskDetail = "DELETE FROM Detail WHERE task_id='" + task_id
					+ "'";
			deleteTask = "DELETE FROM tasklist WHERE task_id='" + task_id + "'";
			statement = connection.createStatement();
			statement.executeUpdate(deleteTaskDetail);
			statement = connection.createStatement();
			statement.executeUpdate(deleteTask);
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
	}

	// 調出資料庫資料（日期）
	public ArrayList<String> queryTaskListDate(String task_name, int id) {
		String startDate = "";
		String endDate = "";
		ArrayList<String> dates = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT start_date, end_date FROM tasklist WHERE task_id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				startDate = rs.getString(1);
				endDate = rs.getString(2);
			}
			String sdate[] = startDate.split("\\D");
			String edate[] = endDate.split("\\D");
			String week[] = startDate.split("\\(");
			String weekDay[] = { "(一)", "(二)", "(三)", "(四)", "(五)", "(六)",
					"(日)" };
			int temp = 0;
			int month = Integer.parseInt(sdate[1]);
			int day = Integer.parseInt(sdate[2]);
			int day2 = Integer.parseInt(edate[2]);
			for (int j = 0; j < 7; j++) {
				if (("(" + week[1]).equals(weekDay[j]))
					temp = j;
			}
			for (int j = 0; j < (day2 - day + 1); j++)
				dates.add(month + "/" + (day + j) + weekDay[temp + j]);

			for (int j = (day2 - day + 1); j < 9; j++)
				dates.add("");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return dates;
	}

	// 調出資料庫資料（工時摘要）
	public ArrayList<String> queryTaskListTask(String task_name, int id) {
		ArrayList<String> tasks = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT task_item FROM detail WHERE task_id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				tasks.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return tasks;
	}

	// 調出摘要時數
	public int getItemHours(String task_name, int id, String task_item) {
		int itemHours = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT item_total FROM detail WHERE task_id=? AND task_item=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_id);
			pst.setString(2, task_item);
			rs = pst.executeQuery();
			while (rs.next()) {
				itemHours = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return itemHours;
	}

	// 調出資料庫資料（工時時數）
	public int[][] queryTaskListHour(String task_name, int id) {
		int i = 0;
		int hours[][] = hours = new int[8][5];
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("_");
		sb.append(task_name);
		String task_id = sb.toString();
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT MON, TUE, WED, THU, FRI FROM detail WHERE task_id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, task_id);
			rs = pst.executeQuery();
			int k = 0;
			// int taskNum = 0;
			while (rs.next()) {
				i = 0;
				for (int j = 1; j < 6; j++) {
					hours[k][i++] = rs.getInt(j);
				}
				k++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return hours;
	}

	// 工時查詢（主管使用）
	public ArrayList<String> taskQuery(String id, String name, String year, String start, String end, boolean toogle) {
		ArrayList<String> tasks = new ArrayList<>();
		ArrayList<String> uids = new ArrayList<>();
		String zero_s = (start.equals("10") || start.equals("11") || start.equals("12")) ? "" : "0";
		String zero_e = (end.equals("10") || end.equals("11") || end.equals("12")) ? "" : "0";
		String ID = (id.isEmpty() || id == null) ? "" : " AND user_id=" + id;
		String NAME = (name.isEmpty() || name == null) ? "" : " AND user_id IN (SELECT id FROM users WHERE name LIKE '%" + name + "%')";
		String MONTH = (start.isEmpty() || start == null) ? "" : " AND (start_date BETWEEN '" + year + "-" + zero_s + start + "-%' AND '" + year + "-" + zero_e + (end + 1) + "-%')";
		try (Connection connection = ds.getConnection()) {
			StringBuilder selectSQL = new StringBuilder("");
			selectSQL.append("SELECT task_name, user_id FROM tasklist WHERE status=2");
			selectSQL.append(ID);
			selectSQL.append(NAME);
			selectSQL.append(MONTH);
			selectSQL.append(" ORDER BY task_id ASC");
			pst = connection.prepareStatement(selectSQL.toString());
			rs = pst.executeQuery();
			while (rs.next()) {
				tasks.add(rs.getString(1));
				uids.add(rs.getInt(2) + "");
			}
		} catch (SQLException e) {
			System.out.println("Query datas Exception :" + e.toString());
		} finally {
			close();
		}
		if (toogle)
			return tasks;
		else
			return uids;
	}
		
	// 請假系統JDBC方法

	// 取出請假時數
	public int getUpperBound(int search_id, String typesCh, int year) {
		int upperbound = 0;
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT UPPERBOUND FROM set_upperbound WHERE user_id=? AND type=? AND year=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, search_id);
			pst.setString(2, typesCh);
			pst.setInt(3, year);
			rs = pst.executeQuery();
			while (rs.next()) {
				upperbound = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return upperbound;
	}

	// 設定請假時數
	public void setUpperBound(int setid, int year, String type, int upperbound) {
		try (Connection connection = ds.getConnection()) {
			updateSQL = "UPDATE set_upperbound SET upperbound=? WHERE user_id=? AND type=? AND year=?";
			pst = connection.prepareStatement(updateSQL);
			pst.setInt(1, upperbound);
			pst.setInt(2, setid);
			pst.setString(3, type);
			pst.setInt(4, year);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		} 
	}

	// 新增假單
	public void newLeaveList(String leave_id, int user_id, String startdate,
			String starttime, String enddate, String endtime, String type,
			String reason, int status, int times, int sd, String lication_date) {
		try (Connection connection = ds.getConnection()) {
			insertLeaveList = "INSERT INTO leavelist(leave_id, user_id, leave_start_date, start_time, leave_end_date, end_time, type, reason, status,times,year, lication_date) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pst = connection.prepareStatement(insertLeaveList);
			pst.setString(1, leave_id);
			pst.setInt(2, user_id);
			pst.setString(3, startdate);
			pst.setString(4, starttime);
			pst.setString(5, enddate);
			pst.setString(6, endtime);
			pst.setString(7, type);
			pst.setString(8, reason);
			pst.setInt(9, status);
			pst.setInt(10, times);
			pst.setInt(11, sd);
			pst.setString(12, lication_date);
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Insert datas Exception :" + e.toString());
		} finally {
			close();
		}
	}

	// 取出過去已請的時數(取出times作累加)
	public int getUsedTime(int user_id, String type, int year) {
		int used_time = 0;
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT sum(times) FROM leavelist where user_id=? AND type=? AND status!=3 AND year=? group by user_id,type";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, user_id);
			pst.setString(2, type);
			pst.setInt(3, year);
			rs = pst.executeQuery();
			while (rs.next()) {
				used_time = rs.getInt(1);
				System.out.println(used_time);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return used_time;
	}

	// 設定已花費的時數
	public void setUsedTime(int used_time, int user_id, String type, int sd) {
		try (Connection connection = ds.getConnection()) {
			updateSQL = "UPDATE set_upperbound SET used_time=? WHERE user_id=? AND type=? AND year=?";
			pst = connection.prepareStatement(updateSQL);
			pst.setInt(1, used_time);
			pst.setInt(2, user_id);
			pst.setString(3, type);
			pst.setInt(4, sd);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// 判定是否有重複時段
	public int getTimes(String startdate, int user_id, String starttime,
			String endtime) {
		int id = 0;
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT user_id FROM leavelist where leave_start_date=? AND user_id=? AND(? BETWEEN start_time AND end_time OR ? BETWEEN start_time AND end_time OR (?<start_time AND ?>end_time))";
			pst = connection.prepareStatement(selectSQL);
			pst.setString(1, startdate);
			pst.setInt(2, user_id);
			pst.setString(3, starttime);
			pst.setString(4, endtime);
			pst.setString(5, starttime);
			pst.setString(6, endtime);
			rs = pst.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
//				System.out.println(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return id;
	}
	
	//判斷是否有工時記錄
	public int getStatus(int user_id, String startdate) {
		int status=0;
		try (Connection connection = ds.getConnection()) {
			selectSQL = "SELECT status FROM tasklist where user_id=? AND (? between start_date AND end_date)";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, user_id);
			pst.setString(2, startdate);
			rs = pst.executeQuery();
			while (rs.next()) {
				status = rs.getInt(1);
				System.out.println(status);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		} 
		return status;
	}
	
	// 取出待審核資料
	public ArrayList<String> getAllLeaveCheckList() {
		ArrayList<String> leaves = new ArrayList<>();
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT leave_id FROM leavelist WHERE status=1 ORDER BY leave_id ASC";
			pst = connection.prepareStatement(selectSQL);
			rs = pst.executeQuery();
			while (rs.next()) {
				leaves.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return leaves;
	}
	
	// 取出待審核資料
	public ArrayList<String> getLeaveDates(int id, String startDate, String endDate) {
		ArrayList<String> dates = new ArrayList<>();
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT leave_start_date FROM leavelist WHERE status!=2 AND user_id=? AND (leave_start_date BETWEEN ? AND ?)";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, id);
			pst.setString(2, startDate);
			pst.setString(3, endDate);
			rs = pst.executeQuery();
			while (rs.next()) {
				dates.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return dates;
	}
	
	// 取出待審核資料
	public ArrayList<String> getModifyList(int user_id) {
		ArrayList<String> modifys = new ArrayList<>();
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT leave_id FROM leavelist WHERE status=3 AND user_id=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, user_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				modifys.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
		return modifys;
	}
	
	// 刪除工時紀錄方法
	public void deleteLeave(String leave_start_date, int user_id) {
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			String delete = "DELETE FROM leavelist WHERE leave_start_date=? AND user_id=?";
			pst = connection.prepareStatement(delete);
			pst.setString(1, leave_start_date);
			pst.setInt(2, user_id);
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		} finally {
			close();
		}
	}
	
	// 送出假單審核結果
	public void leaveVertifyResult(int id, String leave_start_date, int status,
			String back_reason) {
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			updateSQL = "UPDATE leavelist SET status=?, back_reason=? WHERE user_id=? AND leave_start_date=?";
			pst = connection.prepareStatement(updateSQL);
			pst.setInt(1, status);
			pst.setString(2, back_reason);
			pst.setInt(3, id);
			pst.setString(4, leave_start_date);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	// 送出假單審核結果
	public int getLeaveStatus(int id, String leave_start_date) {
		int status = 0;
		try (Connection connection = ds.getConnection()) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			selectSQL = "SELECT status FROM leavelist WHERE user_id=? AND leave_start_date=?";
			pst = connection.prepareStatement(selectSQL);
			pst.setInt(1, id);
			pst.setString(2, leave_start_date);
			rs = pst.executeQuery();
			if (rs.next())
				status = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return status;
	}
}
