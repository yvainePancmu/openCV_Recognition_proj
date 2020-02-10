package application;
/**
 * The entity class define the attributes of announcement.
 * @author YanPan
 *
 */
public class Announcement {
	private int announceId;
	private String announceContent;
	/**
	 * The getter method.
	 * @return announceId
	 */
	public int getAnnounceId() {
		return announceId;
	}
	/**
	 * The setter method.
	 * @param announceId
	 */
	public void setAnnounceId(int announceId) {
		this.announceId = announceId;
	}
	/**
	 * The getter method.
	 * @return announceContent
	 */
	public String getAnnounceContent() {
		return announceContent;
	}
	/**
	 * The setter method.
	 * @param announceContent
	 */
	public void setAnnounceContent(String announceContent) {
		this.announceContent = announceContent;
	}

}
