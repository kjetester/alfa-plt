package ru.alfabank.platform.reporting;

import org.testng.*;

/**
 * Listeners.
 */
public class CustomListener extends TestListenerAdapter {

  @Override
  public void onTestFailure(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
      ScreenshotWorker.takeScreenshot();
    }
  }
}