package ru.alfabank.platform.reporting;

import static ru.alfabank.platform.helpers.FileComparator.compare;

import com.epam.reportportal.message.ReportPortalMessage;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Difference listener class.
 */
public class DifferenceListener extends TestListenerAdapter {

  private static final Logger LOGGER = LogManager.getLogger(DifferenceListener.class);

  @Override
  public void onTestFailure(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
      ITestContext context = result.getTestContext();
      if (context.getAttribute("case") != null) {
        String path = context.getAttribute("case").toString().replaceAll("^/$","");
        String expectedPath = "target/results/" + path + "/expected.html";
        String actualPath = "target/results/" + path + "/actual.html";
        String diffPath = "target/results/" + path + "/diff.html";
        File expected = new File(expectedPath);
        File actual = new File(actualPath);
        File diff = new File(diffPath);
        try {
          FileUtils.write(expected, (String) context.getAttribute("expected"), "UTF-8");
          FileUtils.write(actual, (String) context.getAttribute("actual"), "UTF-8");
          FileUtils.write(diff, compare(actual, expected), "UTF-8");
          LOGGER.info(new ReportPortalMessage(actual, "Saved Actual result: " + actualPath));
          LOGGER.info(new ReportPortalMessage(expected, "Saved Expected result: " + expectedPath));
          LOGGER.info(new ReportPortalMessage(diff, "Saved diff: " + diffPath));
        } catch (IOException e) {
          LOGGER.error("Failed to write a file:\n" + e.getMessage());
        }
      } else {
        LOGGER.warn("Листнеру логировать нечего.");
      }
    }
  }
}
