package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;

import java.sql.Timestamp;
/**
 * The implementation class of recordDAO interface.
 * @author Yan Pan
 *
 */
public class RecordDAOImpl implements RecordDAO {
	public ArrayList<Record> queryRecord(int studId) throws Exception {
		String query = "SELECT * FROM vis_record WHERE sid = ?";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ArrayList<Record> list = new ArrayList<Record>();
		ResultSet rs = null;
		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(query);
			pstmt.setInt(1, studId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Record studentRec = new Record();
				studentRec.setRecordId(rs.getInt("vid"));
				studentRec.setReason(rs.getString("vreason"));
				studentRec.setRecordDate(rs.getDate("vdate"));
				list.add(studentRec);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs.close();
			pstmt.close();
			dbc.close();
		}
		return list;
	}
	/**
	 * The query method to get visit reason and visit times from student ID.
	 * @param studId
	 * @return list
	 */
	public ArrayList<ReportResult> queryVisReason(int studId) throws Exception {
		String query = "SELECT vreason, COUNT(*) as reasonNum FROM db.vis_record WHERE sid = ? GROUP BY vreason";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ArrayList<ReportResult> list = new ArrayList<ReportResult>();
		ResultSet rs = null;
		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(query);
			pstmt.setInt(1, studId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReportResult visReason = new ReportResult();
				visReason.setVisReason(rs.getString("vreason"));
				visReason.setCategoryNum(rs.getInt("reasonNum"));
				list.add(visReason);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs.close();
			pstmt.close();
			dbc.close();
		}
		return list;
	}
	/**
	 * The method to update student record.
	 * @param sid,vreason,vdate
	 */
	public void updateRecord(int sid, String vreason, Timestamp vdate) throws Exception {
		int resultNum = 0;
		String query = "INSERT INTO vis_record(sid,vreason,vdate) VALUES(?,?,?)";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ArrayList<Record> list = new ArrayList<Record>();

		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(query);
			pstmt.setInt(1, sid);
			pstmt.setString(2, vreason);
			pstmt.setTimestamp(3, vdate);
			resultNum = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			pstmt.close();
			dbc.close();
		}
	}
	/**
	 * The method to generate visit reason and visit times by given time period.
	 * @time1,time2
	 */
	public ArrayList<ReportResult> generateCategory(String time1, String time2) throws Exception {
		String query = "SELECT vreason, COUNT(*) as categoryNum FROM db.vis_record WHERE vdate between ? and ? GROUP BY vreason";
		time1 = time1.concat(" 00:00:00");
		time2 = time2.concat(" 59:59:59");
		Timestamp startTime = Timestamp.valueOf(time1);
		Timestamp endTime = Timestamp.valueOf(time2);
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ArrayList<ReportResult> list = new ArrayList<ReportResult>();
		ResultSet rs = null;
		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(query);
			pstmt.setTimestamp(1, startTime);
			pstmt.setTimestamp(2, endTime);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportResult categoryRes = new ReportResult();
				categoryRes.setVisReason(rs.getString("vreason"));
				categoryRes.setCategoryNum(rs.getInt("categoryNum"));
				list.add(categoryRes);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs.close();
			pstmt.close();
			dbc.close();
		}
		return list;

	}
	/**
	 * The method to generate frequency of visit by given time period.
	 * @time1,time2
	 */
	public ArrayList<ReportResult> generateFrequency(String time1, String time2) throws Exception {
		String query = "SELECT sgender,vreason, COUNT(vreason) as categoryCount FROM db.student, db.vis_record\n"
				+ "WHERE vdate BETWEEN ? AND ? " + "AND db.student.sid=db.vis_record.sid\n"
				+ "GROUP BY sgender, vreason ORDER BY sgender";
		time1 = time1.concat(" 00:00:00");
		time2 = time2.concat(" 59:59:59");
		Timestamp startTime = Timestamp.valueOf(time1);
		Timestamp endTime = Timestamp.valueOf(time2);
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ArrayList<ReportResult> result = new ArrayList<ReportResult>();
		ResultSet rs = null;
		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(query);
			pstmt.setTimestamp(1, startTime);
			pstmt.setTimestamp(2, endTime);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReportResult frequencyRes = new ReportResult();
				frequencyRes.setStudGender(rs.getString("sgender"));
				frequencyRes.setVisReason(rs.getString("vreason"));
				frequencyRes.setCategoryNum(rs.getInt("categoryCount"));
				result.add(frequencyRes);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs.close();
			pstmt.close();
			dbc.close();
		}
		return result;
	}

	/**
	 * The entry point of running the application: main method.
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please enter the operation number:");
			String option = scanner.nextLine();
			if ("1".equals(option)) {
				System.out.println("Please enter the student id you want to check");
				int id = scanner.nextInt();
				ArrayList<Record> list = new RecordDAOImpl().queryRecord(id);
				for (int i = 0; i < list.size(); i++) {
					System.out.print(list.get(i).getRecordId() + " ");
					System.out.print(list.get(i).getReason() + " ");
					System.out.println(list.get(i).getRecordDate());
				}
			} else if ("2".equals(option)) {
				System.out.println("Please enter the student id you want to update:");
				int id = new Scanner(System.in).nextInt();
				System.out.println("Please enter the visit reason you want to update:");
				String reason = new Scanner(System.in).nextLine();
				Timestamp visTime = new Timestamp(System.currentTimeMillis());
				new RecordDAOImpl().updateRecord(id, reason, visTime);
				System.out.println("Successfully Added");
			} else if ("3".equals(option)) {
				System.out.println("please enter the start date.");
				String startDate = scanner.nextLine();
				System.out.println("Please enter the end date.");
				String endDate = scanner.nextLine();
				ArrayList<ReportResult> list = new RecordDAOImpl().generateCategory(startDate, endDate);
				for (int i = 0; i < list.size(); i++) {
					System.out.print(list.get(i).getVisReason() + " ");
					System.out.println(list.get(i).getCategoryNum());
				}
			} else if ("4".equals(option)) {
				System.out.println("please enter the start date.");
				String startDate = scanner.nextLine();
				System.out.println("Please enter the end date.");
				String endDate = scanner.nextLine();
				ArrayList<ReportResult> list = new RecordDAOImpl().generateFrequency(startDate, endDate);
				for (int i = 0; i < list.size(); i++) {
					System.out.print(list.get(i).getStudGender() + " ");
					System.out.print(list.get(i).getVisReason() + " ");
					System.out.println(list.get(i).getCategoryNum());
				}
			} else if ("5".equals(option)) {
				System.out.println("Please enter the student id you want to check:");
				int id = new Scanner(System.in).nextInt();
				ArrayList<ReportResult> list = new RecordDAOImpl().queryVisReason(id);
				for (int i = 0; i < list.size(); i++) {
					System.out.print(list.get(i).getVisReason() + " ");
					System.out.println(list.get(i).getCategoryNum());
				}
			}
			scanner.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
