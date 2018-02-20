/*Joshua Phillip, 207961907, EECS 3421, Assignment 2*/

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;


public class CustDB {
	private Connection conDB;
	private String url, custName, custCity, bookClub;
	private String inp, err, custCat, custBook; 
	private int custID, year, intCat, intBook, size, quantity;
	private boolean valid;
	private Scanner in;
	private ArrayList<String> catList, langList, titleList;
	private ArrayList<Integer> yearList, weightList;
	private double bookPrice;
	private Timestamp time; 
	
	public static void main(String[] args){	
		System.out.print("Welcome, You may use \"exit\" to quit the program and save at any time. ");
		System.out.print("Alternatively, you can use \"abort\" to exit without saving. ");
		System.out.print("Note, in the prompt to run the program again, \"abort\" will exit but has already commited. "); 
		System.out.print("For any (y/n) choice, any input beginning with a y or Y will be accepted. ");
		System.out.println("Alternatively, any other input will be treated as n.");
		CustDB cd = new CustDB();
	}
	
	/**
	* Constucts the CustDB object which connects to the db and calls run.
	*/
	public CustDB(){
		dbConnect();
		run();
	}
	
	/**
	* The run method acts as a sort of a main method that does not behave as a static one, this was
	* to avoid putting too much into the constructor. It also allows the run method to be called
	* from within the program.
	*/
	public void run(){
		/*
		* Here the code asks for user ID and returns the info of that ID, if it is the correct user
		* the program can continue, otherwise the user can say no and select a different user. Should
		* the requested ID not be in the db, the program will prompt for a new input.
		*/
		valid = false;
		do{
			System.out.print("Please enter a customer ID: ");
			custID = intInput("ID");			
			if(customerCheck()){
				System.out.println("Customer #"+ custID+", Name: "+custName+", City: "+custCity);
				System.out.print("Is this correct? ");
				valid = charInput();
			}else{
				System.out.println(custID+" is not in the database.");
			}
		}while(!valid);
		
		/*
		* Here the program asks to update the customer name. The db only takes a 20 character max for
		* name so the program will reject any names longer than that, it will also reject a blank name.
		* After update, the program asks if the user is satisfied with the name change, if so it
		* progresses, otherwise, the user is asked again if they want to update the name.
		*/
		do{
			System.out.print("Update Customer Name(y/n)? ");
			valid = charInput();
			if(valid){
				do{
					System.out.print("Enter Customer Name: ");
					custName = charInput(20);
				}while(custName.length() > 20);		
				System.out.println("You have entered Name: "+custName);
				System.out.print("Is this correct(y/n)? ");
				valid = charInput();
				if(valid){
					try{
						updateInfo("name");
					}catch(Exception e){
						e.printStackTrace();
						exit("term");
					}
					System.out.println("Name updated");
				}
			}else{
				valid = true;
			}
		}while(!valid);
		
		/*
		* This block of code is identical to the name one but for city. The only other difference is
		* the max length for city is 15.
		*/
		do{
			System.out.print("Update Customer City(y/n)? ");
			valid = charInput();
			if(valid){	
				do{
					System.out.print("Enter Customer City: ");
					custCity = charInput(15);
				}while(custCity.length() > 15);
				System.out.println("You have entered City: "+ custCity);
				System.out.print("Is this correct(y/n)? ");
				valid = charInput();
				if(valid){
					try{
						updateInfo("city");
					}catch(Exception e){
						e.printStackTrace();
						exit("term");
					}
					System.out.println("City updated");
				}
			}else{
				valid = true;
			}
		}while(!valid);

		/*
		* Here the program lists all the categories and a value that is offset by 1.
		* Inside the loop, it checks for the user category and fixes the offset to the db.
		* If the user choice is valid, it confirms the users choice and then proceeds.
		*/
		size = listCat();
		for(int i = 0; i < size; i++){
			System.out.println((i+1)+": "+catList.get(i));
		}
		valid = false;
		do{
			System.out.print("Choose a category by number: ");
			intCat = intInput("category");
			if(intCat <= size && intCat > 0){
				custCat = catList.get(intCat-1);
				System.out.print("You have selected "+ custCat+", is that correct(y/n)? ");
				valid = charInput();
			}else{
				System.out.println(intCat + " does not correspond to a category");
			}
		}while(!valid);
		
		/*
		* Here the program lists all the books with the same offset as before. The basic
		* logic is the same as category list.
		*/
		size = listBook();
		for(int i = 0; i < size; i++){
			System.out.print((i+1)+": Title: " + titleList.get(i));
			System.out.print(", Year: " + yearList.get(i));
			System.out.print(", Language: "+ langList.get(i));
			System.out.println(", Weight: "+ weightList.get(i));
		}
		valid = false;
		do{
			System.out.print("Enter the number of a book: ");
			intBook = intInput("book");
			if(intBook <= size && intBook > 0){
				custBook = titleList.get(intBook-1);
				year = yearList.get(intBook-1);
				System.out.print("You have selected "+ custBook+", "+year +", is that correct(y/n)? ");
				valid = charInput();
			}else{
				System.out.println(intBook + " does not correspond to a book");
			}
		}while(!valid);
		
		/*
		* Here the program lists the cheapest price of the book and what club it is available from. It
		* asks the user for a requested quantity and returns the price of purchasing that many books. 
		* The db only allows for a smallint which is the equivalent of a short so the system will reject
		* any quantity larger than that. The program than asks if the user would like to purchase at this
		* point the program asks for confirmation and enters the exit routine.
		*/
		valid = false;
		if(getPrice() > 0){
			System.out.println(custBook +" "+ year+ " has price: $"+ bookPrice+ " from: " + bookClub);
			do{
				System.out.print("Enter a desired quantity: ");
				quantity = intInput("quantity");
				if(quantity > 0 && quantity <= Short.MAX_VALUE){
					System.out.print("You have selected "+ quantity +", is that correct(y/n)? ");
					valid = charInput();
				}else{
					System.out.println(quantity + " is not a valid quantity");
				}
			}while(!valid);
			
			System.out.println("Total is: $"+ String.format("%.2f",quantity*bookPrice));
			System.out.print("Would you like to make a purchase (y/n)? ");
			valid = charInput();
			if(valid){
				insertPurchase();
				System.out.println("Purchase inserted");
			}
		}else{
			System.out.println("Book is not available at any of your clubs");
		}
		
		System.out.print("Would you like to save all changes (y/n)? ");
		if (charInput())
			exit("exit");
		else
			exit("abort");
	}
	
	/**
	* A helper method that checks if the input is yes or not, it also serves as a means
	* for the user to exit the program. 
	* 
	* @return true if input is yes, false otherwise.
	*/
	public boolean charInput(){
		try{
			inp = in.nextLine();
			if(Character.toLowerCase(inp.charAt(0)) == 'y'){
				return true;
			}else if(inp.equals("exit") || inp.equals("abort")){
				exit(inp);
			}
		}catch (StringIndexOutOfBoundsException e){
			return false;
		}catch (NoSuchElementException e){
			exit("term");
		}
		return false;
	}
	
	/**
	* A helper method, that gets the name or city that is to be passed to the update. It 
	* also enforces the character limit for those updates.
	* 
	* @param max
	*			the max characters that can be entered for the input
	* @return the user input to be changed in the db
	*/
	public String charInput(int max){
		try{
			inp = in.nextLine();
			if(inp.equals("exit") || inp.equals("abort")){
				exit(inp);
			}else if(inp.length() > max){
				System.out.println(inp + " is longer than "+max+" characters");
			}
		}catch(NoSuchElementException e){
			exit("term");
		}
		return inp;
	}
	
	/**
	* A helper method that gets a number input to be passed back to calling thread.
	*
	* @param type
	*			the type of input so that it can print it out in the error.
	* @return the int given by the user.
	*/
	public int intInput(String type){
		while(true){
			try{
				return Integer.parseInt(err = in.nextLine());
			}catch (NumberFormatException e){
				if(err.equals("exit") || err.equals("abort")){
					exit(err);
				}
				System.out.print(err + " is not a valid "+type+ " ,please enter a number: ");
			}catch (NoSuchElementException e){
				exit("term");;
			}
		}
	}
	
	/**
	* A method that handles all the connection aspects of the db as well as open 
	* the scanner for user input.
	*/
	public void dbConnect(){
		try{
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(0);
        }
		url = "jdbc:db2:c3421a";
		
		try{
			conDB = DriverManager.getConnection(url);		
        } catch (SQLException e) {
			System.out.print("\nSQL: database connection error.\n");
			System.out.println(e.toString());
			System.exit(0);
        }
		
		try {
            conDB.setAutoCommit(false);
        } catch(SQLException e) {
            System.out.print("\nFailed trying to turn autocommit off.\n");
            e.printStackTrace();
            System.exit(0);
        }
		in = new Scanner(System.in);
	}
	
	/**
	* A method than handles all the closing aspects of the db as well as close the scanner.
	*/
	public void dbDisconnect(){
		in.close();
		try {
			conDB.close();
        } catch(SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	* A helper method that closes any PreparedStatements in a given query.
	*
	* @param id
	*			the id of the query.
	* @param queryST
	*			the PreparedStatement being closed.
	*/
	public void close(int id, PreparedStatement querySt){
		try {
		    querySt.close();
		} catch(SQLException e) {
		    System.out.print("SQL#"+id+" failed closing the handle.\n");
		    System.out.println(e.toString());
		    exit("term");
		}    
	}
    
	/**
	* A helper method that closes any ResultSet in a given query.
	*
	* @param id
	*			the id of the query.
	* @param queryST
	*			the ResultSet being closed.
	*/
	public void close(int id, ResultSet answers){
		try {
		    answers.close();
		} catch(SQLException e) {
		    System.out.print("SQL#"+id+" failed closing cursor.\n");
		    System.out.println(e.toString());
		    exit("term");
		}
	}
	
	/**
	* A helper method that opens any PreparedStatements in a given query.
	*
	* @param id
	*			the id of the query.
	* @param queryText
	*			the text to be prepared.
	* @return the PreparedStatement from the calling thread. 
	*/
	public PreparedStatement open(int id, String queryText){		
		PreparedStatement querySt = null;
		
		try {
            querySt = conDB.prepareStatement(queryText);
			querySt.setQueryTimeout(5);
        } catch(SQLException e) {
            System.out.println("SQL#"+id+" failed in prepare");
            System.out.println(e.toString());
            exit("term");
        }
		
		return querySt;
	}
	/**
	* A helper method that handles all means of closing the db, exit being normal exit
	* and commit, abort being normal exit and rollback and term being failed exit and 
	* rollback/disconnect. In both the exit and abort cases, the user is then prompted
	* if they want to restart the program, otherwise the program closes the db connection
	* and exits.
	*
	* @param exit
	*			the type of exit that is being called.
	*/
	public void exit(String exit){
		if (exit.equals("exit")){
			try {
				conDB.commit();
			} catch(SQLException e) {
				System.out.print("\nFailed trying to commit.\n");
				e.printStackTrace();
				System.exit(0);
			}    
		}else if(exit.equals("abort")){
			try{
				conDB.rollback();
			}catch(SQLException e){
				System.out.print("\nFailed trying to rollback.\n");
				e.printStackTrace();
				System.exit(0);
			}
		}else if (exit.equals("term")){
			try{
				conDB.rollback();
				dbDisconnect();
				System.exit(0);
			}catch(SQLException e){
				System.out.print("\nFailed trying to terminate.\n");
				e.printStackTrace();
				System.exit(0);
			}
		}
		System.out.print("Would you like to restart the program (y/n)? ");
		inp = in.nextLine();
		try{
			if(Character.toLowerCase(inp.charAt(0)) == 'y'){
				run();
			}else{
				dbDisconnect();
			}
		}catch (StringIndexOutOfBoundsException e){
			dbDisconnect();
		}catch (NoSuchElementException e){
			System.exit(0);
		}
		System.exit(0);
	}

	/**
	* A method that queries the db and retrieves the customer name and city
	* based on the cid. 
	*
	* @return true if the customer id matches a cid in the db, false otherwise
	*/
	public boolean customerCheck() {
        String queryText = "";
        PreparedStatement querySt = null;
        ResultSet answers  = null;
        boolean inDB = false;
		int id = 1;

        queryText =
            "SELECT name, city "
          + "FROM yrb_customer "
          + "WHERE cid = ?     ";
		
		querySt = open(id, queryText);

        try {
            querySt.setInt(1, custID);
            answers = querySt.executeQuery();
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in execute");
            System.out.println(e.toString());
            exit("term");
        }

        try {
            if (answers.next()) {
                inDB = true;
                custName = answers.getString("name");
				custCity = answers.getString("city");
            } else {
                inDB = false;
                custName = null;
				custCity = null;
            }
        } catch(SQLException e) {
            System.out.println("SQL#1 failed in cursor.");
            System.out.println(e.toString());
            exit("term");
        }

		close(id, answers);
		close(id, querySt);

        return inDB;
    }

	/**
	* A method that updates the user city or name in the db based on cid.
	*
	* @param type
	*			determines whether the update is for city or name.
	*/
	public void updateInfo(String type) {
		String queryText = "";
        PreparedStatement querySt = null;
		int id = 2;
		
		if(type == "city"){
			queryText =
				"UPDATE yrb_customer "
			  + "SET city = ? "
			  + "WHERE cid = ?";
		}else if(type == "name"){
			queryText =
				"UPDATE yrb_customer "
			  + "SET name = ? "
			  + "WHERE cid = ?";
			}
		  
		querySt = open(id, queryText);
		
        try {
			if(type == "city"){
				querySt.setString(1, custCity);
			}else if (type == "name"){
				querySt.setString(1, custName);
			}
            querySt.setInt(2, custID);
            querySt.executeUpdate();

        } catch(SQLException e) {
            System.out.println("SQL#2 failed to update");
            System.out.println(e.toString());
            exit("term");
        }

		close(id, querySt);
    }
	
	/**
	* A method that queries the db and returns all categories.
	*
	* @return the number of categories in the db.
	*/
	public int listCat(){
		String queryText = "";
        PreparedStatement querySt = null;
        ResultSet answers  = null;
		int id = 3;

		catList = new ArrayList<String>();
		
		queryText =
            "SELECT * "
          + "FROM yrb_category ";
		  
		querySt = open(id, queryText);
			  
		try {
            answers = querySt.executeQuery();
        } catch(SQLException e) {
            System.out.println("SQL#3b failed in execute");
            System.out.println(e.toString());
            exit("term");
        }

        try {
            while (answers.next()) {
				catList.add(answers.getString("cat"));
            }
        } catch(SQLException e) {
            System.out.println("SQL#3 failed in cursor.");
            System.out.println(e.toString());
            exit("term");
        }

		close(id, answers);
		close(id, querySt);
		
		return catList.size();
    }
	
	/**
	* A method that queries the db and returns all books, years, languages and weights.
	*
	* @return the number of books in the category.
	*/
	public int listBook(){
		String queryText = "";
        PreparedStatement querySt = null;
        ResultSet answers  = null;
		int id = 4;

		titleList = new ArrayList<String>();
		yearList = new ArrayList<Integer>();
		langList = new ArrayList<String>();
		weightList = new ArrayList<Integer>();	
			
		queryText =
			"SELECT title, year, language, weight "
		  + "FROM yrb_book "
		  + "WHERE cat = ? ";
		  
		querySt = open(id, queryText);		
		  
		try {
			querySt.setString(1, custCat);
			answers = querySt.executeQuery();
		} catch(SQLException e) {
			System.out.println("SQL#4b failed in execute");
			System.out.println(e.toString());
			exit("term");
		}
		
		try {
			for (int i = 0; answers.next(); i++) {
				titleList.add(answers.getString("title"));
				yearList.add(answers.getInt("year"));
				langList.add(answers.getString("language"));
				weightList.add(answers.getInt("weight"));
			}
		} catch(SQLException e) {
			System.out.println("SQL#4 failed in cursor.");
			System.out.println(e.toString());
			exit("term");
		}

		close(id, answers);
		close(id, querySt);
		
		return yearList.size();
    }
	
	/**
	* A method that queries the db and returns the cheapest price for the book in any of
	* the user's clubs.
	*
	* @return the cheapest price of the book avaulable to the user.
	*/
	public double getPrice() {
		String queryText = "";
        PreparedStatement querySt = null;
        ResultSet answers  = null;
		int id = 5;

        queryText =
            "SELECT O.club, O.price "
          + "FROM yrb_offer O, yrb_member M "
          + "WHERE O.title = ? AND O.year = ? AND M.club = O.club AND M.CID = ? "
		  + "AND O.price <= ALL( "
		  + "	SELECT O2.price "
		  + "	FROM yrb_offer O2, yrb_member M2 "
		  + "	WHERE O2.title = ? and O2.year = ? AND M2.club = O2.club AND M2.CID = ? "
		  + ") ";

		querySt = open(id, queryText);

        try {
            querySt.setString(1, custBook);
			querySt.setInt(2, year);
			querySt.setInt(3, custID);
			querySt.setString(4, custBook);
			querySt.setInt(5, year);
			querySt.setInt(6, custID);
            answers = querySt.executeQuery();
        } catch(SQLException e) {
            System.out.println("SQL#5 failed in execute");
            System.out.println(e.toString());
            exit("term");
        }

        try {
            if (answers.next()) {
                bookPrice = answers.getDouble("price");
				bookClub = answers.getString("club");
            } else {;
				bookClub = null;
            }
        } catch(SQLException e) {
            System.out.println("SQL#5 failed in cursor.");
            System.out.println(e.toString());
            exit("term");
        }

		close(id, answers);
		close(id, querySt);
		
		return bookPrice;
    }
	
	/**
	* A method that inserts a user purchase into the db.
	*/
	public void insertPurchase() {
		String queryText = "";
        PreparedStatement querySt = null;
		int id = 6;
				
		time = new Timestamp(System.currentTimeMillis());

        queryText =
            "INSERT INTO yrb_purchase "
          + "VALUES (?, ? ,?, ?, ?, ?) ";

		querySt = open(id, queryText);

        try {
            querySt.setInt(1, custID);
			querySt.setString(2, bookClub);
			querySt.setString(3, custBook);
			querySt.setInt(4, year);
			querySt.setTimestamp(5, time);
			querySt.setInt(6, quantity);		
            querySt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("SQL#6 failed to insert");
            System.out.println(e.toString());
            exit("term");
        }
		
		close(id, querySt);
    }
}