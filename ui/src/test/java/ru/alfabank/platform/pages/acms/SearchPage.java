package ru.alfabank.platform.pages.acms;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.testng.log4testng.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.reporting.BasicLogger.info;

public class SearchPage extends BasePage {

  private static final Logger LOGGER = Logger.getLogger(SearchPage.class);

  @FindBy(css = ".ant-input-group-wrapper .ant-input-affix-wrapper")
  private WebElement searchInput;
  @FindBy(css = ".ant-input-group-wrapper .ant-input-group-addon")
  private WebElement infoFrame;

  /**
   * Searching text.
   * @param text text
   * @return MainPage
   * @throws InterruptedException InterruptedException
   */
  public MainPage searchFor(String text) throws InterruptedException {
    info(String.format("Setting search for a '%s'", text));
    setValueToMonacoTextArea(text, searchInput);
    Thread.sleep(2_000);
    return PageFactory.initElements(getDriver(), MainPage.class);
  }
}
