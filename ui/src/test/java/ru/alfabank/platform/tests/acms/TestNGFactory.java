package ru.alfabank.platform.tests.acms;

import org.testng.annotations.*;

public class TestNGFactory {

  /**
   * Factory.
   * @return tests
   */
  @Factory(enabled = false)
  public Object[] getCriticalPathTestClasses() {
    Object[] tests = new Object[2];
    tests[0] = new EndToEndTest();
    tests[1] = new SearchTest();
    return tests;
  }
}