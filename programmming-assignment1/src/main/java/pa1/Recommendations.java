package pa1;
import java.io.File;

public class Recommendations {

	public static void main(String[] args) {		
		        File ratingsFile = new File("data/recsys_data_ratings.csv");		        
		        NonPersonalizedRecommender rec = new NonPersonalizedRecommender(ratingsFile);
		        
		        //Invoke the method to get data from CSV file
		        rec.getData();
		        rec.eventExample();
		        
	}

}
