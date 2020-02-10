package application;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
/**
 * The interface class to define several operations.
 * @author Yan Pan
 *
 */
public interface RecordDAO {
	public ArrayList<Record> queryRecord(int studId) throws Exception;

	public void updateRecord(int sid, String vreason, Timestamp vdate) throws Exception;

	public ArrayList<ReportResult> queryVisReason(int studId) throws Exception;

	public ArrayList<ReportResult> generateCategory(String time1, String time2) throws Exception;

	public ArrayList<ReportResult> generateFrequency(String time1, String time2) throws Exception;
}
