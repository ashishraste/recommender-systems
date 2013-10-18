package pa1;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;

import org.grouplens.lenskit.eval.data.CSVDataSource;
import org.grouplens.lenskit.eval.data.CSVDataSourceBuilder;
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

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSortedSet;

import au.com.bytecode.opencsv.CSVWriter;

public class NonPersonalizedRecommender {
	int movies[];
	File csvFile, outputFile;
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
	
	// get a listing of the users who have interacted with one particular item (a single MovieID)
	public LongSet eventsByOneItem(Long mID) {				
		ItemEventDAO ieventdao = csv.getItemEventDAO();
		LongSet mIDSet = ieventdao.getUsersForItem(mID);
		return mIDSet;		
	}
	
	// calculate [(x and y) / x] of UserIDs for given MovieIDs 
	public void nonPersonalRecoCalculation(long[] movieIDs) {
		ItemDAO itemdao ;
		LongSet itemSet ;
		Long[] givenMovieIDs = new Long[movieIDs.length];
        		
		int i=0;
		float numOfXRaters;
		float numOfXYRaters;
		
		int movieIndex = 0, itemIndex = 0; 
		
		for(long temp: movieIDs) { 				// converting the primitive long[] array to Long[]
			givenMovieIDs[i++] = temp;
		}
		
		itemdao = csv.getItemDAO();
		itemSet = itemdao.getItemIds();
		float valuesArray[][]= new float[movieIDs.length][itemSet.size()-1];
		
		for(Long x: givenMovieIDs) {			
			LongSet xUidSet = eventsByOneItem(x);			// get the set of users who have rated mID 'x'
			numOfXRaters = xUidSet.size();
			itemdao = csv.getItemDAO();
			itemSet = itemdao.getItemIds();
			
			if(itemSet.contains(x)) {
				// System.out.println("of course itemSet has " + x);
				itemSet.remove(x);
			}									
			
			for(Long y: itemSet) {
			    // considering {y:y belongs to mIDSet-{x}}						
				LongSet yUidSet = eventsByOneItem(y);	// get the set of users who have rated mID 'y'
				// Calculating the intersection (x AND y)
				LongSortedSet xyDifferenceSet = LongUtils.setDifference(xUidSet, yUidSet);				
					
				// xUidSet will have the users who rated both x AND y, after this operation
				for(Long item: xyDifferenceSet) {
					if(xUidSet.contains(item)) {
						xUidSet.remove(item);
					}
				}					
				numOfXYRaters = xUidSet.size();
				float value = roundTheDecimals(numOfXYRaters/numOfXRaters, 2);
				valuesArray[movieIndex][itemIndex++] = value;										
				xUidSet = eventsByOneItem(x);								
			}
			movieIndex++;
			itemIndex = 0;
		}
		writeToFile(valuesArray);
		return;
	}
	
	public void writeToFile(float[][] valuesArray) {		
		try {
			CSVWriter csvwriter = new CSVWriter(new FileWriter("data/outputfile.csv"), ',');
			for(int movieIndex=0; movieIndex < valuesArray.length; ++movieIndex) {			
				String[] rowValues = new String[valuesArray[movieIndex].length];
				for(int itemIndex=0; itemIndex < valuesArray[movieIndex].length; ++itemIndex) {
					rowValues[itemIndex] = String.format("%s", valuesArray[movieIndex][itemIndex]);
				}
				for(String s:rowValues) {
					System.out.println(s);
				}
				System.out.println("\n\n");
				csvwriter.writeNext(rowValues);
			}
		} 
		catch (IOException e) {			
			e.printStackTrace();
		}			      		                
	}
	
	public float roundTheDecimals(float f, int precision) {
		 BigDecimal bd = new BigDecimal(Float.toString(f));
		 bd = bd.setScale(precision, BigDecimal.ROUND_HALF_UP);
	     return bd.floatValue();
	}	

}
