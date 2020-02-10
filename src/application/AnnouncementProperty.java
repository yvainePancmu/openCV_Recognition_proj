package application;

import javafx.beans.property.SimpleStringProperty;
/**
 * class for handling the data in property to fill data for charts
 * @author Yunzhou Ning
 *
 */

public class AnnouncementProperty {
	private final SimpleStringProperty announcementNo;
	private final SimpleStringProperty announcementContent;

	public AnnouncementProperty(String announcementNo, String announcementContent) {
		this.announcementNo = new SimpleStringProperty(announcementNo);
		this.announcementContent = new SimpleStringProperty(announcementContent);
	}

	public String getAnnouncementNo() {
		return announcementNo.get();
	}

	public String getAnnouncementContent() {
		return announcementContent.get();
	}

}
