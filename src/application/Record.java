package application;

import java.util.Date;
/**
 * The entity class defines the attribute for record.
 * @author Yan Pan
 *
 */
public class Record {
	private int recordId;
	private String reason;
	private Date recordDate;

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

}
