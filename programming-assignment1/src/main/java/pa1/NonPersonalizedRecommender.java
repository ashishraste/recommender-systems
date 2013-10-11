package pa1;

import java.io.File;
import org.grouplens.lenskit.eval.data.CSVDataSource;
import org.grouplens.lenskit.eval.data.CSVDataSourceBuilder;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import org.grouplens.lenskit.collections.LongUtils;
import org.grouplens.lenskit.cursors.Cursor;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.ItemEventDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Event;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.data.pref.PreferenceDomain;
import org.grouplens.lenskit.data.pref.PreferenceDomainBuilder;

public class NonPersonalizedRecommender {
	int movies[];
	File csvFile;
	CSVDataSource csv;
	
	public NonPersonalizedRecommender(File csvFile) {
		movies = new int[5];
		this.csvFile = csvFile;
	}

	public void getData() {
		// Building the preference domain, integral ratings in range 1-5
		PreferenceDomainBuilder pdb = new PreferenceDomainBuilder(1, 5);
		PreferenceDomain pd = pdb.setPrecision(1.0).build();
		
		// getting the data from a csv file
		CSVDataSourceBuilder cdb = new CSVDataSourceBuilder(csvFile).setName("nprecommender");
		cdb.setDomain(pd);
		csv = cdb.build();
	}
	
	// list all the events taking place in the dataset
	public void eventsListing() {
		EventDAO eventdao = csv.getEventDAO();
		// a cursor over all events in the dataset
		Cursor<Event> eventCursor = eventdao.streamEvents();
		System.out.println("Events list:\n");
		for(Event aEvent : eventCursor) {
			System.out.println(aEvent.getItemId() +  ", " + aEvent.getUserId());
		}	        
	 }


	// get a listing of the items that each user has interacted with
	public void eventsByUsers() {
		UserDAO userdao = csv.getUserDAO();
		LongSet userSet = userdao.getUserIds();
		UserEventDAO ueventdao = csv.getUserEventDAO();

		for(Long userID: userSet) {
			System.out.println("\nUserID: " + userID + " ");
			UserHistory<Event> ueventHistory = ueventdao.getEventsForUser(userID);
			LongSet userEventSet = ueventHistory.itemSet();
			System.out.println(userEventSet.toString());
		}		
	}
	
	// get a listing of all the users who have interacted with an item
	public void eventsByItems() {
		ItemDAO itemdao = csv.getItemDAO();
		LongSet itemSet = itemdao.getItemIds();
		ItemEventDAO ieventdao = csv.getItemEventDAO();				
		
		for(Long itemID: itemSet) {
			System.out.println("\nItemID: " + itemID + " ");			
			LongSet userItemSet = ieventdao.getUsersForItem(itemID);
			System.out.println(userItemSet.toString());
		}
	}

}
