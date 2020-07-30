package ru.alfabank.platform.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DataBaseHelper {

  protected static final Logger LOGGER = LogManager.getLogger(DataBaseHelper.class);

  private static Connection connection;

  /**
   * Get JDBC connection.
   *
   * @return connection
   * @throws SQLException SQLException
   */
  public static Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      try (final var portForwarder = new KubernetesPortForwarder()) {
        portForwarder.ensureForwarded();
        final var url = "jdbc:mysql://localhost:3306";
        final var props = new java.util.Properties();
        props.put("user", System.getProperty("dblogin"));
        props.put("password", System.getProperty("dbpassword"));
        props.put("serverTimezone", "UTC");
        connection = DriverManager.getConnection(url, props);
      }
    }
    return connection;
  }
}
