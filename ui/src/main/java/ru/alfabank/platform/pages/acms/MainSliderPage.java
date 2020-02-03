package ru.alfabank.platform.pages.acms;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class MainSliderPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(MainSliderPage.class);

  @FindBy(css = "a[href = '/acms/pages']")
  private WebElement pagesLink;

  /**
   * Open pages list.
   * @return new instance of the PagesSliderPage class
   */
  public PagesSliderPage openPagesTree() {
    LOGGER.info("Открываю список страниц");
    waitForElementBecomesClickable(pagesLink);
    pagesLink.click();
    return PageFactory.initElements(getDriver(), PagesSliderPage.class);
  }
}
