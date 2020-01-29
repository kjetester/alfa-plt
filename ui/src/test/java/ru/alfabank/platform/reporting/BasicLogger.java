package ru.alfabank.platform.reporting;

import com.epam.reportportal.message.*;
import org.apache.logging.log4j.*;

/**
 * Logger.
 */
public class BasicLogger {

  private static Logger LOGGER = LogManager.getLogger(BasicLogger.class);

  /**
   * Log error message.
   * @param msg message
   */
  public static void error(String msg) {
    LOGGER.error(msg);
  }

  /**
   * Log info level.
   * @param msg message
   */
  public static void info(String msg) {
    LOGGER.info(msg);
  }

  /**
   * Log to ReportPortal screenshot info level.
   * @param msg ReporPortal message with screenshot
   */
  public static void info(ReportPortalMessage msg) {
    LOGGER.info(msg);
  }

  /**
   * Log with debug level.
   * @param msg message
   */
  public static void debug(String msg) {
    LOGGER.debug(msg);
  }
}
