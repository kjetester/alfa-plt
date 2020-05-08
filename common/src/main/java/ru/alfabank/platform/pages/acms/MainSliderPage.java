package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
