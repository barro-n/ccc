package com.cinco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A factory class for managing database connections; uses Log4j to track
 * connection status and errors
 */
public class ConnectionFactory {

	private static final Logger LOGGER = LogManager.getLogger(ConnectionFactory.class);

	private static final String URL = "jdbc:mysql://nuros.unl.edu:3306/bvaughn5";
	private static final String USER = "bvaughn5";
	private static final String PASS = "maKojaizier2";

	/**
	 * Private constructor, ensures class is only used statically
	 */
	private ConnectionFactory() {
	}

	/**
	 * Creates and returns a connection to the database
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection connection = null;
		try {

			connection = DriverManager.getConnection(URL, USER, PASS);

			LOGGER.info("DB Connection Success :)");

		} catch (SQLException e) {
			LOGGER.error("ERROR: DB connection failed.....", e);
		}

		return connection;
	}
}