package application;

/**
 * {@code Student} is a class that defines attributes of student entity.
 *
 */

public class Student {
	private int studentID;
	private String studentName;
	private String gender;
	private String imagePath;
	private int visitTimes;
	private String studentProgram;

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public void setStudentName(String studentname) {
		this.studentName = studentname;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

	public void setImagePath(String path) {
		this.imagePath = path;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setVisitTimes(int times) {
		this.visitTimes = times;
	}

	public int getVisitTimes() {
		return visitTimes;
	}

	public void setStudentProgram(String program) {
		this.studentProgram = program;
	}

	public String getStudentProgram() {
		return studentProgram;
	}

}
