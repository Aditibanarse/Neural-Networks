package stocks;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import network.*;
public class Predictor {
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * Declarations
		 */
		ArrayList<Double> closes_data;
		ArrayList<Double> opens_data;
		ArrayList<Double> pChangeCloses;
		ArrayList<Double> pChangeOpens;
		double training_input[][];
		double training_output[];
		double test_input[][];
		double test_output[];

		GregorianCalendar start=new GregorianCalendar(2014, 6, 11);
		GregorianCalendar end=new GregorianCalendar(2014, 10, 13);
		YahooData downloader=new YahooData("CSCO",start,end);
		Percent_Change pChange=new Percent_Change();
		
		int num_train_inputs=60;
//		System.out.println("Getting the closing data");
		closes_data=downloader.getCloses();
		
		/******Calculating the percent changes in stocks per month********/
//		System.out.println("Calculating percent change");
//		
//		System.out.println(closes_data);
//		System.out.println("Length of data = "+closes_data.size());
		pChangeCloses=pChange.calc_pchange(closes_data, closes_data);
//		System.out.println("Executed");
		
		//Get training data here
//		System.out.println("Getting the training data");
		Training_Data tdata=new Training_Data();
		tdata.CreateTraining_data(pChangeCloses, num_train_inputs);
		training_input=tdata.GetTrainingData_input();
		training_output=tdata.GetTrainingData_output();
//		System.out.println("Training input data length = "+training_input.length);
//		System.out.println("Training Input");
//		System.out.println("Training output data length = "+training_output.length);

		//Get testing data here
//		System.out.println("Getting the test data");
		Testing_Data test_data=new Testing_Data();
		test_data.CreateTesting_Data(pChangeCloses, pChangeCloses.size()-num_train_inputs);
		test_input=test_data.GetTestData();
//		System.out.println("Training data length = "+training_input.length);

		test_output=test_data.GetTestDataOutput(pChangeCloses,pChangeCloses.size()-num_train_inputs);
//		System.out.println("Test input data length = "+test_input.length);
//		System.out.println("Test output data length = "+test_output.length);


		
		//Get code form network main here
		
		NeuralNetwork nn=new NeuralNetwork(5, 5, 1);
		nn.createInputData(training_input, training_input.length, 5);
		nn.createOutputData(training_output, training_output.length);
		nn.createTestData(test_input, test_input.length, 5);
		
//		nn.dispData();

//		nn.dispData();
    	/*
    	 * sysout all the arrays
    	 */
    	
         int maxRuns = 10;
        double minErrorCondition = 0.001;
        
        System.out.println("Testing Without training");
        System.out.println();
        System.out.println("Success rate:" +((double)nn.Testrun(training_input,maxRuns, minErrorCondition)/(double)num_train_inputs)*100+"%");
        
           System.out.println("Training Set with sample input!!");
           long StartTime=System.nanoTime();

        nn.run(maxRuns, minErrorCondition);
        System.out.println();
        System.out.println("Success rate of Training Data:" +((double)nn.Testrun(training_input,maxRuns, minErrorCondition)/(double)num_train_inputs)*100+"%");
        System.out.println();
//      System.out.println("Success rate:" +(double)((double)nn.Testrun(Testinputs0,maxRuns, minErrorCondition,new String[]{"1","T","X"})/(double)3)*100+"%");
      long EndTime=System.nanoTime();
      System.out.println("Runtime per test="+(EndTime-StartTime)/training_input.length+"NanoSeconds");
      System.out.println();  
      System.out.println("=============================Testing==================================");
        //Creating expected test output
        nn.createTestOutputData(test_output, test_output.length);
        System.out.println("Success rate:" +((double)nn.Testrun(test_input,maxRuns, minErrorCondition)/(double)test_input.length)*100+"%");
		nn.init_Predict_Future();

		
		System.out.println();
		System.out.println("=================PREDICTION STARTS!!!!!!==============");
		double[][] TestInput1=nn.createPredictTest();
        System.out.println("********Predicted Value 1*********");
        System.out.println("Success rate:" +((double)nn.Testrun(TestInput1,maxRuns, minErrorCondition)/(double)1)*100+"%");
        System.out.println("********Predicted Value 2*********");
        nn.updatePredicVal();
		TestInput1=nn.createPredictTest();
        System.out.println("Success rate:" +((double)nn.Testrun(TestInput1,maxRuns, minErrorCondition)/(double)1)*100+"%");
        System.out.println("********Predicted Value 3*********");
        nn.updatePredicVal();
		TestInput1=nn.createPredictTest();
        System.out.println("Success rate:" +((double)nn.Testrun(TestInput1,maxRuns, minErrorCondition)/(double)1)*100+"%");
        
        /*
         * Lamstar network
         */
        
        System.out.println();
        System.out.println();
        System.out.println();

        StartTime=System.nanoTime();

        
        EndTime=System.nanoTime();
        System.out.println("Total runtime = "+(EndTime-StartTime)/training_input.length+" Nanoseconds");





	}
}

