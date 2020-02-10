package application;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * The class for implementing AnnounceDAO interface.
 * @author Yan Pan
 *
 */
public class AnnounceDAOImpl implements AnnounceDAO {
	// executeQuery
	public ArrayList<Announcement> queryAnnounce(int studId) throws Exception {
		String query = "SELECT db.annouce_info.aid,db.annouce_info.acontent\n"
				+ "FROM db.annouce_info, db.announce_match\n"
				+ "WHERE db.annouce_info.aid = db.announce_match.aaid AND db.announce_match.ssid = ?";
		PreparedStatement pstmt = null;
		DBConnect dbc = null;
		ArrayList<Announcement> list = new ArrayList<Announcement>();
		ResultSet rs = null;

		try {
			dbc = new DBConnect();
			pstmt = dbc.getConnection().prepareStatement(query);
			pstmt.setInt(1, studId);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				// 
				Announcement studAnnounce = new Announcement();
				studAnnounce.setAnnounceId(rs.getInt("aid"));
				studAnnounce.setAnnounceContent(rs.getString("acontent"));
				list.add(studAnnounce);
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
	 * The entry point of running the application:main method.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Please enter the student id you want search:");
		try {
			Scanner input = new Scanner(System.in);
			int id = input.nextInt();
			ArrayList<Announcement> list = new AnnounceDAOImpl().queryAnnounce(id);
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getAnnounceId());
				System.out.println(list.get(i).getAnnounceContent());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
