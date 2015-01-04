package stocks;

import java.util.ArrayList;

public class Training_Data {
	
	
	protected double t_data_input[][];
	protected double t_data_output[];
	int windowsize=5;
	
		
	public void CreateTraining_data(ArrayList<Double> training_data, int train_data_len)
	{
		try{
//			final int numrows=training_data.size()-windowsize+1;
			/*
			 * To specify the number of values to be considered in the training data
			 * Here, I am considering a data of 6 years in total
			 * Training data = first 5 years = 60 months
			 * Test data= last 1 year*/	
			final int num_train_data=train_data_len;
			final int numrows=num_train_data-windowsize+1;
			t_data_input=new double[numrows][windowsize];
			t_data_output=new double[numrows];
			
			/***Preparing Training input data***/
				for(int i=0;i<numrows;i++)
				{
//					for(int k=0;k<windowsize;k++)
//					{
					int k=0;
						for(int j=i;j<i+windowsize;j++)
						{
							
//							System.out.println("Training data "+j +" "+training_data.get(j));
						t_data_input[i][k]=training_data.get(j);
						k+=1;
						}
//					}

				}
				
//				System.out.println("Training data size "+t_data_input.length);
				
			/***Preparing Training output data ***/
				int pointer=0;
				for(int col=0;col<numrows;col++)
				{
					pointer=col+5;
					
					if(pointer>training_data.size()-1)
						break;
					t_data_output[col]=training_data.get(pointer);
				}
//				System.out.println("done");
//				disp_data();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}

	public double[][]GetTrainingData_input()
	{
		return t_data_input;
	}
	
	public double[]GetTrainingData_output()
	{
		return t_data_output;
	}
	//to display data in the t_data array
	public void disp_data()
	{
		System.out.println("numrows = "+t_data_input.length);
		System.out.println("numcols = "+t_data_input[1].length);

		for(int row=0;row<t_data_input.length;row++)
		{
			for(int col=0;col<t_data_input[1].length;col++)
			{
				System.out.print(t_data_input[row][col]+" ");
			}
			System.out.println();
		}
		System.out.println("t_data_output");
		for(int i=0;i<t_data_output.length;i++)
			System.out.println(t_data_output[i]);
		System.out.println(t_data_output.length);
	}
	
	
	
}

	
	//send the training data to the network
	


