package pa1;
import java.io.File;

public class Recommendations {

	public static void main(String[] args) {		
		        File ratingsFile = new File("data/recsys_data_ratings.csv");		        
		        NonPersonalizedRecommender npr = new NonPersonalizedRecommender(ratingsFile);
		        
		        //Invoke the method to get data from CSV file
		        npr.getData();
		        
		        // list all the events in the dataset
		        // npr.eventsListing();
		        
		        // get a listing of all the users who have interacted with an item		    	
		        //npr.eventsByItems();
		        
		        // get a listing of the items that each user has interacted with
		        // npr.eventsByUsers();
		        
		        long listOfMovieIDs[] = {629, 9331, 180};
		        npr.nonPersonalRecoCalculation(listOfMovieIDs);		       
	}
}
