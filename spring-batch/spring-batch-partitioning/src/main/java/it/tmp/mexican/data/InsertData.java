package it.tmp.mexican.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class InsertData {

	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/springbatchtest";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "password";

	public static void main(String[] argv) {

		try {

			insertDataIntoUserTable();

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

	}

	private static void insertDataIntoUserTable() throws Exception {

		Connection dbConnection = null;
		Statement statement = null;

		try {

			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
			
			Random generator = new Random();
			
			createTable(statement);

			for (int i = 1; i <= 100; i++) {

				String insertTableSQL = "INSERT INTO USERS (ID, USER_LOGIN, USER_PASS, AGE) VALUES (':id',':name',':pass',':age')";

				insertTableSQL = insertTableSQL.replaceAll(":id", String.valueOf(i));
				insertTableSQL = insertTableSQL.replaceAll(":name", "user_" + i);
				insertTableSQL = insertTableSQL.replaceAll(":pass", "pass_" + i);

				//insertTableSQL = insertTableSQL.replaceAll(":age", String.valueOf(10 + (int) (Math.random() * 90)));
				insertTableSQL = insertTableSQL.replaceAll(":age", String.valueOf(generator.nextInt(100)));
				
				System.out.println(insertTableSQL);

				statement.addBatch(insertTableSQL);

			}

			statement.executeBatch();

			System.out.println("Record is inserted into USERS table!");

		} catch (Exception e) {

			System.out.println(e.getMessage());

		} finally {

			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}

	private static Connection getDBConnection() {

		Connection dbConnection = null;

		try {

			Class.forName(DB_DRIVER);

		} catch (ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return dbConnection;

	}
	
	private static void createTable(Statement statement){
		
		String createTableSQL = "CREATE TABLE USERS " +
                "(id INTEGER not NULL, " +
                " user_login VARCHAR(255), " + 
                " user_pass VARCHAR(255), " + 
                " age INTEGER, " + 
                " PRIMARY KEY ( id ))";
		
		try {
			statement.execute(createTableSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}