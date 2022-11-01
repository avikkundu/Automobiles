package  automobile;

import java.io.*;
import java.sql.*;

public class TRy {
	
	static Connection con;
	
	public static void createConnection()
	{
		String url = "jdbc:mysql://localhost:3306/automobiles"; // table details
        String username = "root"; // MySQL credentials
        String password = "12345";
      
         
        try {
		    Class.forName( "com.mysql.cj.jdbc.Driver");
		    con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established....");
    
            } catch (ClassNotFoundException | SQLException e) {
		    // TODO Auto-generated catch block
		       e.printStackTrace();
	        } // Driver name
	}
	public static void closeConnection()
	{
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insert_into_cust(String name,String address,String gender,String phone,int annual_income,int cust_id)
	{
		String query = "INSERT INTO customer values(\""+name+"\",\""+address+"\",\""+phone+"\",\""+gender+"\",\""+annual_income+"\",\""+cust_id+"\");";
		
		try {
			Statement st=con.createStatement();
			st.executeUpdate(query);
			st.close();
			System.out.println("Successfully added value into customer!!");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void insert_into_dealer(Date edate,Date sdate,int dealer_id,int price,int vin,int cust_id)
	{
		String query = "INSERT INTO dealer values(\""+edate+"\",\""+sdate+"\",\""+dealer_id+"\",\""+price+"\",\""+vin+"\",\""+cust_id+"\");";	
		try {
			Statement st=con.createStatement();
			st.executeUpdate(query);
			st.close();
			System.out.println("Successfully added value into dealer!!");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insert_into_vehicle(int vin,String model,String color,String brand)
	{
		String query = "INSERT INTO vehicle values(\""+vin+"\",\""+model+"\",\""+color+"\",\""+brand+"\");";
		try {
			Statement st=con.createStatement();
			st.executeUpdate(query);
			st.close();
			System.out.println("Successfully added value into vehicle!!");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String q)
	{
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			
			//stmt.close();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public void setScreenForClient()
	{
		System.out.println("Query list :: ");
		System.out.println("1.Show sales trends for various brands");
		System.out.println("2.Find VIN of each vehicle,customer who bought it having defective parts in between a given dates");
		System.out.println("3.Find the top 2 brands by dollar-amount sold in the past year");
		System.out.println("4.Find the top 2 brands by unit sales in the past year");
		System.out.println("5.In which month do convertibles sell best?");
		System.out.println("6.Find those dealers who keep a vehicle in the inventory for the longest aerage time");
		System.out.println("7.To exit");
		
	}
	public void query1(String date)
	{
		//sum of sell price for a given date ;
		String q="SELECT sum(d.price),v.brand,d.sell_date FROM dealer d,vehicle v WHERE d.vin=v.vin AND d.sell_date<\""+date+"\"  GROUP BY brand;";
		ResultSet rs=new TRy().query(q);
		try {
			while(rs.next())
			{
				//System.out.println("ok");
				System.out.println(rs.getInt(1)+","+rs.getString(2)+","+rs.getDate(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void query2()
	{
		//most number of days kept in inventory
		String q2 = "SELECT d.dealer_id FROM dealer d WHERE DATEDIFF(d.sell_date,d.entry_date)=(SELECT max(DATEDIFF(d.sell_date,d.entry_date)) FROM dealer d);";
		ResultSet rs=new TRy().query(q2);
		try {
			while(rs.next())
			{
				System.out.println(rs.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void query3(String year)
	{
		//find top 2 brands according to max sale in the past year
		String q3="SELECT v.brand,sum(d.price) FROM vehicle v ,dealer d where  v.vin=d.vin and year(d.sell_date)<"+year+" group by v.brand order by sum(d.price) desc limit 2;";
		ResultSet rs=new TRy().query(q3);
		try {
			while(rs.next())
			{
				System.out.println(rs.getString(1)+","+rs.getInt(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void query4(String year)
	{
		//find top 2 brands according to max unit sale in the past year
		String q4="SELECT v.brand,sum(d.price)/count(d.price) FROM vehicle v ,dealer d where  v.vin=d.vin and year(d.sell_date)<"+year+" group by v.brand order by sum(d.price)/count(d.price) desc limit 2;";
		ResultSet rs=new TRy().query(q4);
		try {
			while(rs.next())
			{
				System.out.println(rs.getString(1)+","+rs.getInt(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void query5(String brand)
	{
		//in which month do convertibles sell best;
		String q5="select monthname((d.sell_date)) from vehicle v,dealer d where d.vin=v.vin and v.brand=\""+brand+"\";";
		ResultSet rs=new TRy().query(q5);
		try {
			while(rs.next())
			{
				System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void query6(String manufacturer,String sdate,String edate)
	{
		//finding vin,name for defective supply from manufacturer.
	    String q6="select v.vin,c.name from vehicle v,dealer d,customer c where v.vin=d.vin and d.manufacturer=\""+manufacturer+"\" and d.entry_date>\""+sdate +"\" and d.sell_date<\""+edate+"\" ;";
	    ResultSet rs=new TRy().query(q6);
	    try {
			while(rs.next())
			{
				System.out.println(rs.getInt(1)+","+rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		createConnection();
		TRy ob=new TRy();
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String user;
		System.out.println("\t\t\t\t\t  Automobiles sales Database");
		System.out.println("Enter Role: (admin,client)");
		user = br.readLine();
		
		if(user.equals("admin"))
		{
			int ch;
			System.out.println("To enter data into customer table press 1."
					           + "\n to enter data into dealer table press 2."
					           + "\n to enter data into vehicle table press 3");
			ch=Integer.parseInt(br.readLine());
			
			switch(ch) {
			
			case 1:
				   String name,address,gender,phone;
				   int annual_income,cust_id;
				   
				   System.out.println("Enter name");
				   name=br.readLine();
				   System.out.println("Enter address");
				   address=br.readLine();
				   System.out.println("Enter gender");
				   gender = br.readLine();
				   
				   System.out.println("Enter phone no.,annual income, and cust_id");
				   phone=br.readLine();
				   annual_income = Integer.parseInt(br.readLine());
				   cust_id = Integer.parseInt(br.readLine());
				   
				  new TRy().insert_into_cust(name,address,gender,phone,annual_income,cust_id);
				   
				   break;
			case 2:
				   Date edate = null,sdate = null;
				   int dealer_id,price,vin;
				   
				   System.out.println("Entry date,sale date");
				   try {
					edate=Date.valueOf(br.readLine());
					sdate=Date.valueOf(br.readLine());
					   
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				   
				   dealer_id=Integer.parseInt(br.readLine());
				   price=Integer.parseInt(br.readLine());
				   vin=Integer.parseInt(br.readLine());
				   cust_id=Integer.parseInt(br.readLine());
				   
				   new TRy().insert_into_dealer(edate,sdate,dealer_id,price,vin,cust_id);
				   
				   break;
			case 3:
				   
				   String model,color,brand;
				   
				   System.out.println("Enter VIN,model,color,brand");
				   vin=Integer.parseInt(br.readLine());
				   model=br.readLine();
				   color=br.readLine();
				   brand=br.readLine(); 
				   
				   new TRy().insert_into_vehicle(vin,model,color,brand);
				   
				   break;
			
			default:
				
			}
			
			
		}
		else if(user.equals("client"))
		{
			
			
			new TRy().setScreenForClient();
			int ch=-1;
			while(ch!=7)
			{
				System.out.println("Enter your choice");
				ch=Integer.parseInt(br.readLine());
				switch(ch)
				{
				   case 1:
					      System.out.print("Enter today's date in yyyy-MM-dd");
					      String s=br.readLine();
					      ob.query1(s);
					      break;
				   case 2:
					      ob.query2();
					      break;
				   case 3:
					      System.out.println("Enter year");
					      String year=br.readLine();
					      ob.query3(year);
					      break;
				   case 4:
					      System.out.println("Enter year");
					      String y=br.readLine();
					      ob.query4(y);
					      break;
				   case 5:
					      System.out.println("Enter brand");
					      String b=br.readLine();
					      ob.query5(b);
					      break;    
				   case 6:
					      System.out.println("Enter manufacturer name");
				          String manufac=br.readLine();
				          System.out.println("Enter start date");
				          String dd=br.readLine();
				          System.out.println("Enter end date");
				          String dd2=br.readLine();
				          ob.query6(manufac,dd,dd2);
				      
				}
			}
			
		    
		}
		else
		{
			System.out.println("Invalid input! Please try again !! ");
		}
	
		System.out.println("Exit successful");
		
	}
	

}
