package uk.bris.esserver.repository.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author hh13577
 * This is a database connection class
 */
public class DBConnection {

	/**
	 * Connection object
	 */
	private Connection con = null;
	private final static Logger LOGGER = Logger.getLogger(DBConnection.class.getName());


	public DBConnection(){

	}
	/**
	 * @throws NamingException
	 * @throws SQLException
	 */
	public DBConnection(boolean autocommit){
		try{
			LOGGER.info("Getting DB Connection");
			//initialise context
			Context i = new InitialContext();
			//lookup for JNDI
			Context e = (Context) i.lookup("java:/comp/env");
			//create datasource
			DataSource d = (DataSource) e.lookup("jdbc/eventsdb");
			//derive the connection
			con = d.getConnection();

			//set auto commit to false to handle the JTA transactions manually, else true
			con.setAutoCommit(autocommit);
		}catch(NamingException ne){
			ne.printStackTrace();
		}catch(SQLException se){
			se.printStackTrace();
		}
	}

	/**
	 * @return Connection object
	 */
	public Connection getDBConnection(){
		return con;	
	}
}
