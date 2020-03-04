package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(SearchPage.class);

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
    LOGGER.info(String.format("Ввожу в поле поиска '%s'", text));
    setValueToMonacoTextArea(text, searchInput);
    Thread.sleep(2_000);
    return PageFactory.initElements(getDriver(), MainPage.class);
  }
}
