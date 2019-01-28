package cn.dreamine.dev.dreamineBank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL {
    private Connection connection;
    private String host, database, username, password;
    private int port;
    private Statement statement;

    public SQL(String host, int port, String database, String username, String password){
    	this.setHost(host);
    	this.setPort(port);
    	this.setDatabase(database);
    	this.setUsername(username);
    	this.setPassword(password);
    	try {
			this.openConnection();
			statement = connection.createStatement();
			createTableIfNotExists();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    public void addNewPlayerIfNotExists(String playername) throws SQLException{
    	String query = "SELECT * FROM dreaminebank WHERE player='" + playername + "'";
    	ResultSet result = statement.executeQuery(query);
    	if(!result.next()){
    		String addPlayer = "INSERT INTO `dreaminebank` VALUES("
	   		 + "  '" + playername + "',"
	   		 + "  0,0,0"
	   		 + ")";
			execute(addPlayer);
    	}
    }

    public int getPlayerBalance(String playername) throws SQLException{
    	int balance = -1;
    	String query = "SELECT * FROM dreaminebank WHERE player='" + playername + "'";
    	ResultSet result = statement.executeQuery(query);
    	if(result.next()){
    		balance = Integer.parseInt(result.getString("money"));
    	}
    	return balance;
    }

    public int getPlayerOfflinein(String playername) throws SQLException{
    	int balance = -1;
    	String query = "SELECT * FROM dreaminebank WHERE player='" + playername + "'";
    	ResultSet result = statement.executeQuery(query);
    	if(result.next()){
    		balance = Integer.parseInt(result.getString("offlinein"));
    	}
    	return balance;
    }

    public int getPlayerOfflineout(String playername) throws SQLException{
    	int balance = -1;
    	String query = "SELECT * FROM dreaminebank WHERE player='" + playername + "'";
    	ResultSet result = statement.executeQuery(query);
    	if(result.next()){
    		balance = Integer.parseInt(result.getString("offlineout"));
    	}
    	return balance;
    }


    public void changeBalance(String playername, int money) throws SQLException{
    	String update = "UPDATE dreaminebank"
    				  + " SET money =" + money
    				  + " WHERE player = '"+ playername +"'";
    	execute(update);
    }

    public void changeOfflinein(String playername, int money) throws SQLException{
    	String update = "UPDATE dreaminebank"
    				  + " SET offlinein =" + money
    				  + " WHERE player = '"+ playername +"'";
    	execute(update);
    }

    public void changeOfflineout(String playername, int money) throws SQLException{
    	String update = "UPDATE dreaminebank"
    				  + " SET offlineout =" + money
    				  + " WHERE player = '"+ playername +"'";
    	execute(update);
    }

    public void createTableIfNotExists(){
		String createTable = "CREATE TABLE IF NOT EXISTS `dreaminebank` ("
				   + "  `player` varchar(64) NOT NULL,"
				   + "  `money` int(10) NOT NULL,"
				   + "  `offlinein` int(10) NOT NULL,"
				   + "  `offlineout` int(10) NOT NULL,"
				   + "  PRIMARY KEY (player)"
				   + ")";
		try {
			execute(createTable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void execute(String sql) throws SQLException{
    	statement.execute(sql);
    }

	public void openConnection() throws SQLException, ClassNotFoundException {
	    if (connection != null && !connection.isClosed()) {
	        return;
	    }

	    synchronized (this) {
	        if (connection != null && !connection.isClosed()) {
	            return;
	        }
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
	    }
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
