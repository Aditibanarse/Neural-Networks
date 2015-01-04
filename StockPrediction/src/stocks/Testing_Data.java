package stocks;

import java.util.ArrayList;

public class Testing_Data {
	
	public double test_data[][];
	public double test_data_op[];
	int start=0;
	int windowsize=5;
	public void CreateTesting_Data(ArrayList<Double> data, int test_data_len)
	{
		start=data.size()-test_data_len;
		int numrows=test_data_len-windowsize+1;
		test_data=new double[numrows][windowsize];
//		System.out.println("Numrows = "+numrows);
		for(int row=0;row<numrows;row++)
		{
//			System.out.println(row+" Display");
			int col=0;
			for(int ptr=start+row;ptr<start+windowsize+row;ptr++)
			{
//				System.out.println("ptr = "+ptr+" data = "+data.get(ptr));
				test_data[row][col]=data.get(ptr);
				col+=1;
				
			}
		}

	}
	public double[] GetTestDataOutput(ArrayList<Double> data,int test_op_len)
	{
		int pointer=0;
		start=data.size()-test_op_len;
		int numrows=test_op_len-windowsize+1;
		test_data_op=new double[numrows];

//		System.out.println("data len= "+numrows+"        "+"data size = "+data.size());
		pointer=start+5;
		for(int col=0;col<numrows-1;col++)
		{
//			System.out.println(col + "             "+pointer);
			
			if(pointer>data.size()-1)
				break;
			test_data_op[col]=data.get(pointer);
			pointer++;
		}
		
//		System.out.println("Test data output");
//		System.out.println("LENGTH = "+test_data_op.length);
//		for(int j=0;j<test_data_op.length;j++)
//			System.out.println(test_data_op[j]);
		return test_data_op;
	}
	public double[][] GetTestData()
	{
		return test_data;
	}
	

}
