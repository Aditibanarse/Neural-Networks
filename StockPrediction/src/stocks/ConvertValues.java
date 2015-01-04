package stocks;

public class ConvertValues {

	public double convertPredictedVal(double p_output, double prev_val)
	{
		double predictedVal=0.0;
		/*
		 * 
		 *(New val-oldval)/oldval=percent change
		 */
		predictedVal=p_output*prev_val+prev_val;
		
		return predictedVal;
		
	}
	
}
