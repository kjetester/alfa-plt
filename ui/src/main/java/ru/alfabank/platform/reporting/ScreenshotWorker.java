package ru.alfabank.platform.reporting;

import com.epam.reportportal.message.*;
import org.apache.commons.io.*;
import org.apache.log4j.*;
import org.openqa.selenium.*;

import java.io.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

/**
 * Screenshots working.
 */
public class ScreenshotWorker {

  private static final Logger LOGGER = LogManager.getLogger(ScreenshotWorker.class);

  /**
   * Takes a screenshot.
   */
  public static void takeScreenshot() {
    try {
      String dir = "target/screenshots/";
      File screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
      String screenshotName = dir + System.nanoTime();
      String scrPath = screenshotName + ".jpg";
      File copy = new File(scrPath);
      FileUtils.copyFile(screenshot, copy);
      ReportPortalMessage message = new ReportPortalMessage(new File(scrPath), "Saved screenshot: " + scrPath);
      LOGGER.info(message);
    } catch (IOException e) {
      LOGGER.error("Failed to make screenshot:\n" + e.getMessage());
    }
  }
}