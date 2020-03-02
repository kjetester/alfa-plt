package ru.alfabank.platform.tests.acms.widget;

import org.apache.log4j.*;
import ru.alfabank.platform.buisenessobjects.*;
import ru.alfabank.platform.tests.acms.page.*;

import java.util.*;

public class BaseWidgetTest extends BasePageTest {

  private static final Logger LOGGER = LogManager.getLogger(BaseWidgetTest.class);

  protected String sourcePage = "/about/";
  protected Map<String, Widget> createdWidgets = new HashMap<>();
}
