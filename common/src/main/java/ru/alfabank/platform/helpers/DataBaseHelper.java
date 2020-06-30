package ru.alfabank.platform.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DataBaseHelper {

  protected static final Logger LOGGER = LogManager.getLogger(DataBaseHelper.class);

  private static Connection connection;

  /**
   * Get JDBC connection.
   * @return connection
   * @throws SQLException SQLException
   */
  public static Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      try (final var portForwarder = new KubernetesPortForwarder()) {
        portForwarder.ensureForwarded();
        try {
          connection = DriverManager.getConnection(
              "jdbc:mysql://localhost:3306",
              "alfabank_ru_old",
              "alfabank_ru_old");
        } catch (SQLException e) {
          LOGGER.error(e);
          portForwarder.ensureForwarded();
          getConnection();
        }
      }
    }
    return connection;
  }
}
