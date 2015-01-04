package stocks;

import java.util.ArrayList;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.sql.*;

public class YahooData {
	public static final int DATE=0;
	public static final int OPEN=1;
	public static final int HIGH=2;
	public static final int LOW=3;
	public static final int CLOSE=4;
	public static final int VOLUME=5;
	public static final int ADJCLOSE=6;
	
	private ArrayList<GregorianCalendar> dates;
	private ArrayList<Double> opens;
	private ArrayList<Double> highs;
	private ArrayList<Double> lows;
	protected ArrayList<Double> closes;
	private ArrayList<Integer> volumes;
	private ArrayList<Double> adjcloses;
	private ArrayList<Double> closes_copy;

	
	public YahooData(String symbol, GregorianCalendar start, GregorianCalendar end)
	{
		dates = new ArrayList<GregorianCalendar>();
		opens = new ArrayList<Double>();
		highs = new ArrayList<Double>();
		lows = new ArrayList<Double>();
		closes = new ArrayList<Double>();
		volumes = new ArrayList<Integer>();
		adjcloses = new ArrayList<Double>();
		closes_copy=new ArrayList<Double>();
		/******
		 * Getting the data from finance.yahoo.com
		 * Constructing the url for getting the data in real time
		 * *******/
		//http://real-chart.finance.yahoo.com/table.csv?s=%5EIXIC&a=00&b=3&c=1950&d=10&e=3&f=2014&g=d&ignore=.csv
		//http://real-chart.finance.yahoo.com/table.csv?s=%5EGSPC&a=00&b=3&c=1950&d=10&e=3&f=2014&g=d&ignore=.csv
		//http://real-chart.finance.yahoo.com/table.csv?s=AAPL&a=01&b=18&c=2012&d=01&e=18&f=2014&g=d&ignore=.csv
		
		//per month data
		//http://real-chart.finance.yahoo.com/table.csv?s=IBM&a=00&b=2&c=2009&d=10&e=5&f=2014&g=m&ignore=.csv
		String url="http://real-chart.finance.yahoo.com/table.csv?s=" +symbol+ 
				"&a=0"+start.get(Calendar.MONTH)+
				"&b="+start.get(Calendar.DAY_OF_MONTH)+
				"&c="+start.get(Calendar.YEAR)+
				"&d="+end.get(Calendar.MONTH)
				+ "&e="+end.get(Calendar.DAY_OF_MONTH)
				+ "&f=2014"+end.get(Calendar.YEAR)
				+ "&g=d&ignore=.csv";
		
		//http://real-chart.finance.yahoo.com/table.csv?s=IBM&a=06&b=18&c=2014&d=10&e=12&f=2014&g=d&ignore=.csv

		String url_month="http://real-chart.finance.yahoo.com/table.csv?s=" +symbol+ 
				"&a=0"+start.get(Calendar.MONTH)+
				"&b="+start.get(Calendar.DAY_OF_MONTH)+
				"&c="+start.get(Calendar.YEAR)+
				"&d=0"+end.get(Calendar.MONTH)
				+ "&e="+end.get(Calendar.DAY_OF_MONTH)
				+ "&f=2014"+end.get(Calendar.YEAR)
				+ "&g=m&ignore=.csv";
		
		
		System.out.println(url);
		try
		{
			URL getdata=new URL(url);
			URLConnection data=getdata.openConnection();
			Scanner input=new Scanner(data.getInputStream());
			
			//Database Connection
//			Class.forName("com.mysql.jdbc.Driver");
//			Connection myCon= DriverManager.getConnection("jdbc:mysql://localhost:3306/Stocks","root","");
//			System.out.println("Connection has been made");
//			Statement myStmt=myCon.createStatement();
//			System.out.println("Connection has been made");
//			//Create table query
//			String query="CREATE TABLE "+symbol+" (Date DATE NOT NULL, Open DOUBLE NOT NULL)";
//			
//			myStmt.executeUpdate(query);
////			ResultSet result=myStmt.execute("Create Table IBM(Date DATE)");
			
			if(input.hasNext())
			{
				input.nextLine(); //Skip the header Line
				
			}
			
			//Start reading the data from the website
			int cnt=0;
			while(input.hasNext())
			{
				cnt++;
				String line=input.nextLine();
//				System.out.println(line);
				
				StringTokenizer tokens=new StringTokenizer(line,",");
				while(tokens.hasMoreElements())
				{
					String date=tokens.nextToken();
					
//					dates.addAll((Collection<? extends GregorianCalendar>) tokens.nextElement());
					opens.add(Double.parseDouble(tokens.nextToken()));
					highs.add(Double.parseDouble(tokens.nextToken()));
					lows.add(Double.parseDouble(tokens.nextToken()));
					closes.add(Double.parseDouble(tokens.nextToken()));
					volumes.add(Integer.parseInt(tokens.nextToken()));
					adjcloses.add(Double.parseDouble(tokens.nextToken()));
					
					
				}
				
			}
//			System.out.println(cnt+" " +opens+" "+highs+" "+lows+" "+closes+" "+volumes+" "+adjcloses);
			System.out.println("Total data records : " +closes.size());
//			System.out.println(closes);
		}
		
		catch(Exception e)
		{
			System.err.println();
			e.printStackTrace();
			
		}
		

	}
	protected ArrayList<Double> getOpens()
	{
		return opens;
}
	protected ArrayList<Double> getHighs()
	{
		return highs;
	}
	protected ArrayList<Double> getLow()
	{
		return lows;
	}
	
	protected ArrayList<Double> getCloses()
	{
		return closes;
	}
	
	

}
