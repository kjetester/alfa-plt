package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {

  private static final int MAX_RETRY_COUNT = 3;

  private int retryCount = 0;

  /**
   * Retry.
   * @param testResult testResult
   * @return boolean
   */
  public boolean retry(ITestResult testResult) {
    try {
      final var throwableAsString = testResult.getThrowable().toString();
      if (throwableAsString.contains("504")
          || throwableAsString.contains("502")) {
        if (retryCount < MAX_RETRY_COUNT) {
          retryCount++;
          return true;
        }
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }
}
