package network;

import java.lang.ProcessBuilder.Redirect;
import java.text.*;

import stocks.*;

import java.util.*;
 
public class NeuralNetwork {
    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    final boolean isTrained = false;
    final DecimalFormat df;
    final Random rand = new Random();
    final ArrayList<Neuron> inputLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
    final ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
    static double inputs[][];
    static double expectedOutputs[];
    static double TestInputs[][];
    public ArrayList<Double> predictValues=new ArrayList<Double>();
    
    final Neuron bias = new Neuron();
    final int[] layers;
    final int randomWeightMultiplier = 1;
    
 
    final double epsilon = 0.00000000001;
 
    final double learningRate = 0.5d;
    final double momentum = 0.7d;
//    final String TrainingSet[]={"1","T","X","Unknown"};
    
    //get it from t_data_input
    /*Copying training data to input array*/
    
    public void createInputData(double training_data[][], int num_inputs, int windowsize)
    {
    	inputs=new double[num_inputs][windowsize];
    	for(int row=0;row<num_inputs;row++)
    	{
    		for(int col=0;col<windowsize;col++)
    		{
    			inputs[row][col]=training_data[row][col];
    		}
    	}
    }
    /*
     * Output training data
     */
    public void createOutputData(double training_output[], int num_input)
    {
    	expectedOutputs=new double[num_input];
    	for(int col=0;col<num_input;col++)
    	{
    		
    			expectedOutputs[col]=training_output[col];
    		
    	}
    }
    
    public void createTestData(double test_data[][],int num_inputs,int windowsize)
    {
    	TestInputs=new double[num_inputs][windowsize];
    	for(int row=0;row<num_inputs;row++)
    	{
    		for(int col=0;col<windowsize;col++)
    		{
    			TestInputs[row][col]=test_data[row][col];
    		}
    	}
    	
    	
    }
    
    public void createTestOutputData(double test_output[], int num_input)
    {
    	expectedOutputs=new double[num_input];
    	for(int col=0;col<num_input;col++)
    	{
    		
    			expectedOutputs[col]=test_output[col];
    		
    	}
    }
    
    public void dispData()
    {
    	System.out.println("training input");
    	for(int row=0;row<inputs.length;row++)
		{
			for(int col=0;col<inputs[1].length;col++)
			{
				System.out.print(inputs[row][col]+" ");
			}
			System.out.println();
		}
		System.out.println("training output");
		for(int i=0;i<expectedOutputs.length;i++)
			System.out.println(expectedOutputs[i]);
		
		System.out.println("test data");
		for(int row=0;row<TestInputs.length;row++)
		{
			for(int col=0;col<TestInputs[1].length;col++)
			{
				System.out.print(TestInputs[row][col]+" ");
			}
			System.out.println();
		}
    
    }
    
    double resultOutputs[]; // dummy init
    double output;
 
    // for weight update all
    final HashMap<String, Double> weightUpdate = new HashMap<String, Double>();
 
  
    public NeuralNetwork(int input, int hidden, int output) 
    {
        this.layers = new int[] { input, hidden, output };
        df = new DecimalFormat("#.0#");
 
        /**
         * Create all neurons and connections Connections are created in the
         * neuron class
         */
        for (int i = 0; i < layers.length; i++) {
            if (i == 0) { // input layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    inputLayer.add(neuron);
                }
            } else if (i == 1) { //  hidden layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.addInConnectionsS(inputLayer);
                    neuron.addBiasConnection(bias);
                    hiddenLayer.add(neuron);
                }
            }
 
            else if (i == 2) { // output layer
                for (int j = 0; j < layers[i]; j++) {
                    Neuron neuron = new Neuron();
                    neuron.addInConnectionsS(hiddenLayer);
                    neuron.addBiasConnection(bias);
                    outputLayer.add(neuron);
                }
            } else {
                System.out.println("!Error NeuralNetwork init");
            }
        }
 
        // initialize random weights
        for (Neuron neuron : hiddenLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
            }
        }
        for (Neuron neuron : outputLayer) {
            ArrayList<Connection> connections = neuron.getAllInConnections();
            for (Connection conn : connections) {
                double newWeight = getRandom();
                conn.setWeight(newWeight);
                
            }
        }
 
        // reset id counters
        Neuron.counter = 0;
        Connection.counter = 0;
 
//        if (isTrained) {
//            trainedWeights();
//            updateAllWeights();
//        }
    }
  
 
    // random
    double getRandom() {
        return randomWeightMultiplier * (rand.nextDouble() * 2 - 1); // [-1;1[
    }
 
    /**
     * 
     * @param inputs
     *            There is equally many neurons in the input layer as there are
     *            in input variables
     */
    public void setInput(double inputs[]) {
        for (int i = 0; i < inputLayer.size(); i++) {
            inputLayer.get(i).setOutput(inputs[i]);
        }
    }
 
    public double getOutput() {
//        double[] outputs = new double[outputLayer.size()];
    	double op=0;
        for (int i = 0; i < outputLayer.size(); i++)
            op = outputLayer.get(i).getOutput();
        return op;
    }
 
    /**
     * Calculate the output of the neural network based on the input The forward
     * operation
     */
    public void activate() {
        for (Neuron n : hiddenLayer)
            n.calculateOutput();
        for (Neuron n : outputLayer)
            n.calculateOutput();
    }
 
    /**
     * all output propagate back
     * 
     * @param expectedOutputs2
     *            first calculate the partial derivative of the error with
     *            respect to each of the weight leading into the output neurons
     *            bias is also updated here
     */
    public void applyBackpropagation(double expectedOutputs2) {
 
        // error check, normalize value ]0;1[
        
            double d = expectedOutputs2;
            //********change the range of values!! ***********
            if (d < -1 || d > 1) {
                if (d < -1)
                    expectedOutputs2 = -1 + epsilon;
                else
                    expectedOutputs2 = 1 - epsilon;
            
        }
 
        int i = 0;
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double ak = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double desiredOutput = expectedOutputs2;
 
                double partialDerivative = -ak * (1 - ak) * ai
                        * (desiredOutput - ak);
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
            i++;
        }
 
        // update weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double aj = n.getOutput();
                double ai = con.leftNeuron.getOutput();
                double sumKoutputs = 0;
                int j = 0;
                for (Neuron out_neu : outputLayer) {
                    double wjk = out_neu.getConnection(n.id).getWeight();
                    double desiredOutput = expectedOutputs2;
                    double ak = out_neu.getOutput();
                    j++;
                    sumKoutputs = sumKoutputs
                            + (-(desiredOutput - ak) * ak * (1 - ak) * wjk);
                }
 
                double partialDerivative = aj * (1 - aj) * ai * sumKoutputs;
                double deltaWeight = -learningRate * partialDerivative;
                double newWeight = con.getWeight() + deltaWeight;
                con.setDeltaWeight(deltaWeight);
                con.setWeight(newWeight + momentum * con.getPrevDeltaWeight());
            }
        }
    }

    public int Testrun(double[][] testInput,int maxSteps, double minError) 
    {
    	
    	resultOutputs=new double[testInput.length];
    	for(int i=0;i<resultOutputs.length;i++)
    		resultOutputs[i]=0;
        int i;
        // Train neural network until minError reached or maxSteps exceeded
        double error = 1;
        for (i = 0; i < maxSteps && error > minError; i++) {
            error = 0;
            for (int p = 0; p < testInput.length; p++) {
                setInput(testInput[p]);
 
                activate();
 
                output = getOutput();
                resultOutputs[p] = output;

            
            }
        }
       return printTestResult(testInput);
   
    }
    int printTestResult(double[][] testInput)
    {
    	System.out.println("***********Printing result***********");
    	System.out.println();
//    	for(int i=0;i<testInput.length;i++)
//    	{
//    		for(int j=0;j<5;j++)
//    		{
//    			System.out.print(testInput[i][j]+ " ");
//    			
//    		}
//    		System.out.println();
//    	}
    	
    	int Success=0;
        int i=0,j=0;

        for (int p = 0; p < testInput.length; p++) {
            System.out.print("\n INPUTS: " );
            for (int x = 0; x < layers[0]; x++) {
            	if(x%8==0)
            		System.out.println();
                System.out.print(testInput[p][x] + " ");
                
            }
 
      
            System.out.print("\n Output: ");
            System.out.print(resultOutputs[j] + " ");

//            for (int x = 0; x < resultOutputs.length; x++) {
//                System.out.print(resultOutputs[p] + " ");
//            }
            
            System.out.print("\n Expected Output :" );
            System.out.print(expectedOutputs[j]+ " ");

//            for (int x = 0; x < expectedOutputs.length; x++) {
//                System.out.print(expectedOutputs[p]+ " ");
//              
//            }
           
            
            //increment the success if the difference between the expected and actual output is not more than 0.02
            System.out.println("Difference : "+ Math.abs(expectedOutputs[j]-resultOutputs[j]));
            if((Math.abs(expectedOutputs[j]-resultOutputs[j]))<=0.02)
            	Success++;
            if(j>1)
            {
            if((resultOutputs[j])<resultOutputs[j-1])
            	System.out.println("Stock Down");
            else if((resultOutputs[j])>resultOutputs[j-1])
            	System.out.println("Stock UP");
            else
            	System.out.println("");
            }
            j++;
            
        }
    //    System.out.println("!@#$%^&*(!@#$%^&*()@#$%^&*()!@#$%^&*("+Success+")(*&^%$#@!)*&^%$#@!)(*&^%$#@!");
     
      if(testInput.length!=1)
      {
//        System.out.println("Output               ");
//        for(int k=0;k<expectedOutputs.length;k++)
//        {
//        	System.out.println(resultOutputs[k]);
//        }
//        
//        System.out.println("Expected Output");
//        for(int k=0;k<expectedOutputs.length;k++)
//        {
//        	System.out.println(expectedOutputs[k]);
//        }
      }
        return Success;
    }
    public void run(int maxSteps, double minError) {
    	resultOutputs=new double[inputs.length];
    	
    	//displaying the resultouputs
    	for(int i=0;i<resultOutputs.length;i++)
    		resultOutputs[i]=0;
        int i;
        // Train neural network until minError reached or maxSteps exceeded
        double error = 1;
//    	System.out.println("Error");
        for (i = 0; i < maxSteps && error > minError; i++) {
        	
            error = 0;
            int j=0;
            //display the input(1 row of inputs array)
            for (int p = 0; p < inputs.length; p++) {
                setInput(inputs[p]);
 
                activate();
 
                output = getOutput();
                resultOutputs[p] = output;
                
//                  System.out.println("Before calculating error");
//                  System.out.println("output = "+output+"           "+"expected output = "+expectedOutputs[j]);
//                  
                    double err = Math.pow(output - expectedOutputs[j], 2);
//                    System.out.println("err = "+err);
                    error += err;
//                    System.out.println(error);
                    j++;
//                }
 
                applyBackpropagation(expectedOutputs[p]);
            }
        }
 
        printResult();
         
        System.out.println("Sum of squared errors = " + error);
        System.out.println("##### EPOCH " + i+"\n");
        if (i == maxSteps) {
            System.out.println("!Error training try again");
        } else {
            printAllWeights();
            printWeightUpdate();
        }
    }
     
    void printResult()
    {
      
    	int i=0,j=0;
    	for (int p = 0; p < inputs.length; p++) {
    		System.out.println("\n\nTraining for input = "+p);
            System.out.print("INPUTS: ");	
            
            for (int x = 0; x < layers[0]; x++) {
            	if(x%8==0)
            		System.out.println();
             
                System.out.print(inputs[p][x] + " ");
            }
 
            System.out.print("\n EXPECTED: ");
            
                System.out.print(expectedOutputs[j] + " ");
            
 
            System.out.print("\n ACTUAL: ");
           
                System.out.print(resultOutputs[j]+ " ");
            
            
            System.out.print("\nFired Neuron:" );
            
                System.out.print(resultOutputs[j]+ " ");
            
            
            System.out.println();
            j++;
        }
        System.out.println();
    }
    
    
    
    /*
     * Predicting the future output values
     * 
     */
    public void init_Predict_Future()
    {
    	//initializing the predicValues arraylist using the Test input data
    	int lastRow=TestInputs.length-1;
    	System.out.println("lastRow = "+lastRow);
    	for(int col=1;col<5;col++)
    	{
//    		System.out.println(col);
    		predictValues.add(TestInputs[lastRow][col]);
    		
    	}
    	
    	//Adding the last output of the test run
    	predictValues.add(resultOutputs[resultOutputs.length-1]);
    	
    	//Copy the values in the arraylist and 
//    	for(int j=0;j<predictValues.size();j++)
//    	System.out.println(predictValues.get(j));
    }
    
    public double[][] createPredictTest()
    {
    	double newTest[][]=new double[1][5];
    	int pointer=predictValues.size()-1;
//    	System.out.println("SIZE = "+pointer);
    	for(int i=4;i>=0;i--)
    	{
    		newTest[0][i]=predictValues.get(pointer);
    		pointer--;
    	}
//    	System.out.println("newTest");
//    	for(int j=0;j<5;j++)
//    		System.out.println(newTest[0][j]);
    	return newTest;
    }
    
    public void updatePredicVal()
    {
    	predictValues.add(resultOutputs[resultOutputs.length-1]);
    }

 
    
    public void printWeightUpdate() {
      //  System.out.println("printWeightUpdate, put this i trainedWeights() and set isTrained to true");
        // weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String w = df.format(con.getWeight());
           //     System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
             //           + con.id + "), " + w + ");");
            }
        }
        // weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                String w = df.format(con.getWeight());
         //       System.out.println("weightUpdate.put(weightKey(" + n.id + ", "
          //              + con.id + "), " + w + ");");
            }
        }
        System.out.println();
    }
 
    public void printAllWeights() {
       //System.out.println("printAllWeights");
        // weights for the hidden layer
        for (Neuron n : hiddenLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
           //     System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        // weights for the output layer
        for (Neuron n : outputLayer) {
            ArrayList<Connection> connections = n.getAllInConnections();
            for (Connection con : connections) {
                double w = con.getWeight();
       //         System.out.println("n=" + n.id + " c=" + con.id + " w=" + w);
            }
        }
        System.out.println();
//    }
}
}