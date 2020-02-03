package ru.alfabank.platform.reporting;

import com.epam.reportportal.message.*;
import org.apache.commons.io.*;
import org.apache.log4j.*;
import org.testng.*;

import java.io.*;

import static ru.alfabank.platform.helpers.FileComparator.*;

/**
 * Listeners.
 */
public class DifferenceListener extends TestListenerAdapter {

  private static final Logger LOGGER = LogManager.getLogger(DifferenceListener.class);

  @Override
  public void onTestFailure(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
      ITestContext context = result.getTestContext();
      String url = (String) context.getAttribute("case");
      String expectedPath = "target/results" + url + "expected.txt";
      String actualPath = "target/results" + url + "actual.txt";
      String diffPath = "target/results" + url + "diff.html";
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
    }
  }
}
