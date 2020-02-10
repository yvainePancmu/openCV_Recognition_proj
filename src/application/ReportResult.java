package application;

import java.util.Date;
/**
 * A medium class for store the record data from database in object
 * defines attributes of report record entity.
 *
 */
public class ReportResult {
	private String studGender;
	private String category;
	private int categoryNum;
	private String visReason;

	public ReportResult() {

	}

	public ReportResult(String visReason, int categoryNum) {
		this.visReason = visReason;
		this.categoryNum = categoryNum;
	}

	public ReportResult(String studGender, String category, int categoryNum) {
		this.studGender = studGender;
		this.category = category;
		this.categoryNum = categoryNum;

	}

	public String getStudGender() {
		return studGender;
	}

	public void setStudGender(String studGender) {
		this.studGender = studGender;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCategoryNum() {
		return categoryNum;
	}

	public void setCategoryNum(int categoryNum) {
		this.categoryNum = categoryNum;
	}

	public String getVisReason() {
		return visReason;
	}

	public void setVisReason(String visReason) {
		this.visReason = visReason;
	}

}
