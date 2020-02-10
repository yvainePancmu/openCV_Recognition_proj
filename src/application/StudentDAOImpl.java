package application;

/**
 * StudentDAOImpl class is concrete method implementation of query action
 */
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentDAOImpl implements StudentDAO {

	/**
	 * Query student detailed information by studentID
	 * @param StudentID
	 * @return stu
	 */
	public Student studentDetail(int StudentID) throws Exception {
		String sql = "select * from student where sid=?";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		Student stu = new Student();
		ResultSet rs = null;

		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(sql);
			pstmt.setInt(1, StudentID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// Query the result, and add into student class
				stu.setStudentID(rs.getInt("sid"));
				stu.setStudentName(rs.getString("name"));
				stu.setGender(rs.getString("sgender"));
				stu.setImagePath(rs.getString("path"));
				stu.setStudentProgram(rs.getString("program"));
				stu.setVisitTimes(rs.getInt("times"));
				stu.setImagePath(rs.getString("path"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs.close();
			pstmt.close();
			dbc.close();
		}
		return stu;
	}

	/**
	 * Calculate the number of students
	 */
	public int studentNum() throws Exception {
		String sql = "select * from student";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ResultSet rs = null;
		int rowCount = 0;

		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.last();
			rowCount = rs.getRow();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs.close();
			pstmt.close();
			dbc.close();
		}
		return rowCount;
	}
	/**
	 *  Update student visit times
	 * @param StudentID, times
	 */
	public void addTimes(int StudentID, int times) throws Exception {
		String sql = "UPDATE student SET times=? WHERE sid=?;";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;

		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(sql);
			pstmt.setInt(1, times);
			pstmt.setInt(2, StudentID);
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			dbc.close();
		}
	}
	
	/**
	 * Add new student into database
	 * @param StudentID, name, gender, path, program, times
	 */

	public void addStudent(int studentID, String name, String gender, String path, String program, int times)
			throws Exception {

		String sql = "INSERT INTO student (sid, name, sgender, path, program,times) VALUES(?,?,?,?,?,?);";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ResultSet rs = null;

		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(sql);
			pstmt.setInt(1, studentID);
			pstmt.setString(2, name);
			pstmt.setString(3, gender);
			pstmt.setString(4, path);
			pstmt.setString(5, program);
			pstmt.setInt(6, times);
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			dbc.close();
		}

	}
	
	/**
	 * Query studentID
	 * @return idList
	 */

	public ArrayList<Integer> IDCount() throws Exception {

		String sql = "select * from student;";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ResultSet rs = null;
		ArrayList<Integer> idList = new ArrayList<Integer>();

		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// Query result and add into the object of student class.
				idList.add(rs.getInt("sid"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			dbc.close();
		}
		return idList;
	}
	/**
	 * Query student name
	 * @return nameList
	 */
	public ArrayList<String> NameList() throws Exception {

		String sql = "select * from student;";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ResultSet rs = null;
		ArrayList<String> nameList = new ArrayList<String>();

		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// Query result and add into the object of student class.
				nameList.add(rs.getString("name"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			dbc.close();
		}
		return nameList;

	}

	/**
	 * Test code for studentDAOImpl
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			System.out.println("Input");
			Scanner input = new Scanner(System.in);
			int id = input.nextInt();
			StudentDAOImpl test = new StudentDAOImpl();
			Student stu = test.studentDetail(id);
			System.out.println(stu.getStudentID());
			System.out.println(stu.getStudentName());
			System.out.println(stu.getGender());
			System.out.println(stu.getStudentProgram());
			System.out.println(stu.getVisitTimes());
			System.out.println(stu.getImagePath());
 
			test.addTimes(stu.getStudentID(), stu.getVisitTimes() + 1);

			stu = test.studentDetail(id);
			System.out.println(stu.getStudentID());
			System.out.println(stu.getStudentName());
			System.out.println(stu.getGender());
			System.out.println(stu.getStudentProgram());
			System.out.println(stu.getVisitTimes());
			System.out.println(stu.getImagePath());

			System.out.println("input name");
			input = new Scanner(System.in);
			String name = input.nextLine();

			System.out.println("input gender");
			input = new Scanner(System.in);
			String gender = input.nextLine();

			System.out.println("input program");
			input = new Scanner(System.in);
			String program = input.nextLine();

			input = new Scanner(System.in);
			int StudentID = test.studentNum();

			stu = test.studentDetail(StudentID + 1);
			System.out.println(stu.getStudentID());
			System.out.println(stu.getStudentName());
			System.out.println(stu.getGender());
			System.out.println(stu.getStudentProgram());
			System.out.println(stu.getVisitTimes());
			System.out.println(stu.getImagePath());

			System.out.println(test.IDCount());
			System.out.println(test.NameList());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
