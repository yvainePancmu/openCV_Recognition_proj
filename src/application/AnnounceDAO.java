package application;

import java.util.ArrayList;
/**
 * The interface of announceDAOImpl to define query operation of announcement.
 * @author Yan Pan
 *
 */
public interface AnnounceDAO {

	public ArrayList<Announcement> queryAnnounce(int value) throws Exception;

}
