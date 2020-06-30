package ru.alfabank.platform.widget;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.contentstore.Page;
import ru.alfabank.platform.page.BasePageTest;
import ru.alfabank.platform.pages.alfasite.AlfaSitePage;

/**
 * Class for common methods for the Tests about Widgets.
 */
public class BaseWidgetTest extends BasePageTest {

  private static final Logger LOGGER = LogManager.getLogger(BaseWidgetTest.class);

  /**
   * Getting AlfaSite page source at given path.
   *
   * @param path path
   * @return page source
   */
  protected String getAlfaSitePageSource(String path) {
    PageFactory.initElements(getDriver(), AlfaSitePage.class).open(BaseTest.baseUri + path);
    return getDriver().getPageSource();
  }

  /**
   * Check if HTML source one page is equal to another.
   *
   * @param page page
   */
  protected void compareSourceHtmlAgainst(Page page) {
    assertThat(getAlfaSitePageSource(BaseTest.sourcePage.getUri()))
        .as("Проверяю соответствие десктопных страниц на 'https://alfabank.ru'")
        .isEqualTo(getAlfaSitePageSource(page.getUri()));
  }
}
