package stocks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Percent_Change {
	
	protected ArrayList<Double> closes_copy=new ArrayList<Double>();
	protected ArrayList<Double> percent_change_closes=new ArrayList<Double>();
	private double change_val=0.0;
	/*
	 * constructor
	 * */

	public Percent_Change() {
		// TODO Auto-generated constructor stub
	}

	

	public ArrayList<Double> calc_pchange(ArrayList<Double> old_data, ArrayList<Double> new_data) 
	{
//		System.out.println("original data");
//		System.out.println(old_data);
//		System.out.println("Reverse order");
		Collections.reverse(old_data);
		
		/*First element is 0.0 since data older 
		 * than the start date is not available
		 * 
		 */
		Iterator it_old=old_data.iterator();
		Iterator it_new=new_data.iterator();
		it_new.next();
		double new_val, old_val;
		percent_change_closes.add(0.0);

		while(it_new.hasNext())
		{
			new_val=(Double)it_new.next();
			old_val=(Double)it_old.next();
			change_val=(new_val-old_val)/old_val;
			percent_change_closes.add(change_val);
		}
//		System.out.println(old_data);
//		System.out.println("Original");
//		System.out.println(percent_change_closes);
//		System.out.println("After Revesing");
		Collections.reverse(percent_change_closes);
//		System.out.println(percent_change_closes);
		//Create Training data set
		//Here 60 is the number of values that are considered in the training data set
		
		//Passing the input data to the network
		
	return percent_change_closes;
	
	}

}
