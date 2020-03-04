package ru.alfabank.platform.reporting;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import com.epam.reportportal.message.ReportPortalMessage;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Listeners.
 */
public class TestFailureListener extends TestListenerAdapter {

  private static final Logger LOGGER = LogManager.getLogger(TestFailureListener.class);

  @Override
  public void onTestFailure(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
      try {
        String p = "target/screenshots/" + System.currentTimeMillis() + ".jpg";
        File f = new File(p);
        FileUtils.copyFile(((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE), f);
        ReportPortalMessage msg = new ReportPortalMessage(f, "Saved screenshot: " + p);
        LOGGER.info(msg);
      } catch (IOException e) {
        LOGGER.error("Failed to make screenshot:\n" + e.getMessage());
      }
    }
  }
}
