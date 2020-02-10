package application;

/**
 * StudentDAO class is an interface of different query actions.
 */
import java.util.ArrayList;


public interface StudentDAO {
	public Student studentDetail(int StudentID) throws Exception;

	public int studentNum() throws Exception;

	public ArrayList<Integer> IDCount() throws Exception;

	public ArrayList<String> NameList() throws Exception;

	public void addTimes(int StudentID, int times) throws Exception;

	public void addStudent(int StudentID, String name, String gender, String path, String program, int times)
			throws Exception;

}
