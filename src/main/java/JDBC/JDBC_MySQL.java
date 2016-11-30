package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JDBC_MySQL {
	private String url = "jdbc:mysql://localhost:3306/workTime_db?useUnicode=true&characterEncoding=UTF8";
	private String uid = "root";
	private String pwd = "root";
	private String insertSQL = "INSERT INTO tasklist(id, task_name, status, start, end, task_item, MON, TUE, WED, THU, FRI) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	private Connection connection;
	private PreparedStatement pst = null;
	private Statement statement = null;
	private ResultSet rs = null;

	// 實體化同時註冊驅動
	public JDBC_MySQL() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, uid, pwd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 檢查工時紀錄是否已存在
	public boolean canAdd(String task_name, int id) {
		boolean canAdd = true;
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			String selectSQL = "SELECT status FROM workTime_db.tasklist WHERE task_name='" + task_name + "' AND id="
					+ id + ";";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
				if (rs.getInt(1) == 1 || rs.getInt(1) == 2 || rs.getInt(1) == 3 || rs.getInt(1) == 4)
					canAdd = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return canAdd;
	}

	// 新增工時紀錄方法
	public void newTask(int id, String task_name, int status, String start, String end, String task_item, int MON,
			int TUE, int WED, int THU, int FRI) {
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			pst = connection.prepareStatement(insertSQL);
			pst.setInt(1, id);
			pst.setString(2, task_name);
			pst.setInt(3, status);
			pst.setString(4, start);
			pst.setString(5, end);
			pst.setString(6, task_item);
			pst.setInt(7, MON);
			pst.setInt(8, TUE);
			pst.setInt(9, WED);
			pst.setInt(10, THU);
			pst.setInt(11, FRI);
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Insert datas Exception :" + e.toString());
		}
	}
	
	// 取出待審核資料
	public ArrayList<String> getCheckList(int id) {
		ArrayList<String> tasks = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			String selectSQL = "SELECT distinct task_name FROM workTime_db.tasklist WHERE status=1 AND id=" + id + " ORDER BY task_name ASC;";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
				tasks.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		}
		return tasks;
	}
	
	// 取得員工Name By Id
		public String getUserName(int id) {
			String name = "";
			try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
				// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
				String selectSQL = "SELECT name FROM workTime_db.user WHERE id=" + id + ";";
				statement = connection.createStatement();
				rs = statement.executeQuery(selectSQL);
				while (rs.next()) {
					name = rs.getString(1);
				}
			} catch (SQLException e) {
				System.out.println("Select datas Exception :" + e.toString());
			}
			return name;
		}
	
	// 取得員工id
	public ArrayList<Integer> getUserId() {
		ArrayList<Integer> ids = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			String selectSQL = "SELECT id FROM workTime_db.tasklist WHERE status=1 ORDER BY task_name ASC;";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		}
		return ids;
	}
	
	// 取的所有成員id
	public ArrayList<Integer> getAllUserid() {
		ArrayList<Integer> ids = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			String selectSQL = "SELECT id FROM worktime_db.`user` WHERE job='employee';";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		}
		return ids;
	}

	// 可修改工時資料列表
	public LinkedHashMap<String, Integer> modifyTaskList(int id) {
		LinkedHashMap<String, Integer> tasks = new LinkedHashMap<>();
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			String selectSQL = "SELECT task_name, status FROM workTime_db.tasklist WHERE (status=4 OR status=3) AND id="
					+ id + " ORDER BY task_name ASC;";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
				tasks.put(rs.getString(1), rs.getInt(2));
			}
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		}
		return tasks;
	}

	// 刪除工時紀錄方法
	public void deleteTask(String task_name, int id) {
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			// 0：尚未新增；1：待審核；2：審核通過；3：退回：4：暫存
			String deleteSQL = "DELETE FROM workTime_db.tasklist WHERE task_name='" + task_name + "' AND id=" + id + ";";
			statement = connection.createStatement();
			statement.executeUpdate(deleteSQL);
		} catch (SQLException e) {
			System.out.println("Select datas Exception :" + e.toString());
		}
	}

	// 調出資料庫資料（日期）
	public ArrayList<String> queryTaskListDate(String task_name, int id) {
		String startDate = "";
		String endDate = "";
		ArrayList<String> tasks = new ArrayList<>();
		ArrayList<String> dates = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			String selectSQL = "SELECT start, end, task_item, MON, TUE, WED, THU, FRI FROM workTime_db.tasklist WHERE task_name='"
					+ task_name + "' AND id=" + id + ";";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
					startDate = rs.getString(1);
					endDate = rs.getString(2);
			}
			String sdate[] = startDate.split("\\D");
			String edate[] = endDate.split("\\D");
			String week[] = startDate.split("\\(");
			String weekDay[] = { "(一)", "(二)", "(三)", "(四)", "(五)", "(六)", "(日)" };
			int temp = 0;
			int day = Integer.parseInt(sdate[1]);
			int day2 = Integer.parseInt(edate[1]);
			for (int j = 0; j < 7; j++) {
				if (("(" + week[1]).equals(weekDay[j]))
					temp = j;
			}
			for (int j = 0; j < (day2 - day + 1); j++) 
				dates.add(sdate[0] + "/" + (day + j) + weekDay[temp + j]);
			
			for (int j = (day2 - day + 1); j < 9; j++) 
				dates.add("");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return dates;
	}
	
	// 調出資料庫資料（工時摘要）
	public ArrayList<String> queryTaskListTask(String task_name, int id) {
		ArrayList<String> tasks = new ArrayList<>();
		int taskNum = 0;
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			String selectSQL = "SELECT start, end, task_item, MON, TUE, WED, THU, FRI FROM workTime_db.tasklist WHERE task_name='"
					+ task_name + "' AND id=" + id + ";";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
					tasks.add(rs.getString(3));
					taskNum++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return tasks;
	}
	
	// 調出資料庫資料（工時時數）
		public int[][] queryTaskListHour(String task_name, int id) {
			int i = 0;
			int hours[][] = hours = new int[8][5];
			try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
				String selectSQL = "SELECT start, end, task_item, MON, TUE, WED, THU, FRI FROM workTime_db.tasklist WHERE task_name='"
						+ task_name + "' AND id=" + id + ";";
				statement = connection.createStatement();
				rs = statement.executeQuery(selectSQL);
				int k = 0;
				int taskNum = 0;
				while (rs.next()) {
						i = 0;
						for (int j = 4; j < 9; j++) {
							hours[k][i++] = rs.getInt(j);
						}
						k++;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			return hours;
		}

	// 依id查詢（員工或主管使用）
	public ArrayList<String> taskQuery_employee(int id, boolean input) {
		ArrayList<String> tasks = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			String selectSQL = input ? "SELECT task_name FROM tasklist WHERE status=2 AND id LIKE '%" + id + "%';"
					: "SELECT task_name FROM tasklist WHERE status=2 AND id=" + id + ";";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
				tasks.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Query datas Exception :" + e.toString());
		}
		return tasks;
	}

	// 依姓名查詢（主管使用）
	public ArrayList<String> taskQuery_employee(String name, boolean input) {
		ArrayList<String> tasks = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			String selectSQL = input ? "SELECT task_name FROM tasklist WHERE status=2 AND name LIKE '%" + name + "%';"
					: "SELECT task_name FROM tasklist WHERE status=2 AND name='" + name + "';";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
				tasks.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Query datas Exception :" + e.toString());
		}
		return tasks;
	}

	// 依起訖日搜尋（主管使用）
	public ArrayList<String> taskQuery_manager(int start, int end, boolean input) {
		ArrayList<String> tasks = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(url, uid, pwd)) {
			String selectSQL = "SELECT task_name FROM tasklist WHERE status=2 AND (start BETWEEN '" + start + "%' AND '"
					+ (end + 1) + "%');";
			statement = connection.createStatement();
			rs = statement.executeQuery(selectSQL);
			while (rs.next()) {
				tasks.add(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Query datas Exception :" + e.toString());
		}
		return tasks;
	}
}
