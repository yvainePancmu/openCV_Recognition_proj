package application;

import javafx.beans.property.SimpleStringProperty;
/**
 * class for handling the data in property to fill data for charts
 * @author Yunzhou Ning
 *
 */
public class RecordProperty {
	private final SimpleStringProperty recordID;
	private final SimpleStringProperty recordReason;
	private final SimpleStringProperty recordDate;

	public RecordProperty(String recordID, String recordReason, String recordDate) {
		this.recordID = new SimpleStringProperty(recordID);
		this.recordReason = new SimpleStringProperty(recordReason);
		this.recordDate = new SimpleStringProperty(recordDate);
	}

	public String getRecordID() {
		return recordID.get();
	}

	public String getRecordReason() {
		return recordReason.get();
	}

	public String getRecordDate() {
		return recordDate.get();
	}
}