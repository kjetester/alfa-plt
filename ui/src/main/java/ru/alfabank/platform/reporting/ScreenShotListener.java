package ru.alfabank.platform.reporting;

import com.epam.reportportal.message.*;
import org.apache.commons.io.*;
import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.testng.*;

import java.io.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

/**
 * Listeners.
 */
public class ScreenShotListener extends TestListenerAdapter {

  private static final Logger LOGGER = LogManager.getLogger(ScreenShotListener.class);

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
