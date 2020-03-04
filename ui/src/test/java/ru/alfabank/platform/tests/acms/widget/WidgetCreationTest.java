package ru.alfabank.platform.tests.acms.widget;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import ru.alfabank.platform.reporting.TestFailureListener;

@Listeners ({TestFailureListener.class})
public class WidgetCreationTest extends BaseWidgetTest {

  private static final Logger LOGGER = LogManager.getLogger(WidgetCreationTest.class);

}
