package ru.alfabank.platform.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class RetryListener implements IAnnotationTransformer {

  /**
   * Listener method.
   *
   * @param annotation      annotation
   * @param testClass       testClass
   * @param testConstructor testConstructor
   * @param testMethod      testMethod
   */
  public void transform(final ITestAnnotation annotation,
                        final Class testClass,
                        final Constructor testConstructor,
                        final Method testMethod) {
    final var retry = annotation.getRetryAnalyzerClass();
    if (retry == null) {
      annotation.setRetryAnalyzer(Retry.class);
    }
  }
}
