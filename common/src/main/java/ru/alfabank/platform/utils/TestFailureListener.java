package ru.alfabank.platform.utils;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import com.epam.reportportal.message.ReportPortalMessage;
import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntry;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Test fail listener class.
 */
public class TestFailureListener extends TestListenerAdapter {

  private static final Logger LOGGER = LogManager.getLogger(TestFailureListener.class);

  @Override
  public void onTestFailure(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
      try {
        String screenshotPath =
            String.format("target/screenshots/%s.jpg", System.currentTimeMillis());
        File screenshotFile = new File(screenshotPath);
        FileUtils.copyFile(
            ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE),
            screenshotFile);
        LOGGER.error(new ReportPortalMessage(
            screenshotFile,
            String.format("Screenshot is here: %s", screenshotPath)));
        String logPath = String.format(
            "target/logs/%s.log",
            System.currentTimeMillis());
        File consoleLogFile = new File(logPath);
        FileUtils.write(
            consoleLogFile,
            getDriver().manage().logs().get("browser").getAll().stream().map(LogEntry::toString)
                .collect(Collectors.joining("\n")),
            "UTF-8");
        LOGGER.error(new ReportPortalMessage(
            consoleLogFile,
            String.format("Browser's console logs are here: %s", consoleLogFile)));
      } catch (IOException e) {
        LOGGER.error("Failed to make screenshot:\n" + e.getMessage());
      }
    }
  }
}
