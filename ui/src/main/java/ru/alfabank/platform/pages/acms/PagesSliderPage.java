package ru.alfabank.platform.pages.acms;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import java.util.NoSuchElementException;
import java.util.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class PagesSliderPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(PagesSliderPage.class);

  @FindBy (css = "li[class = 'ant-tree-treenode-switcher-open'] a")
  private List<WebElement> rootPageList;

  /**
   * Open requested root page.
   * @param pagePath requested page
   * @return new instance of the MainPage class
   */
  public MainPage selectPage(String pagePath) {
    LOGGER.info(String.format("Открываю страницу '%s'", pagePath));
    rootPageList.stream().filter(p ->
        p.getText().replace("/", "").equals(pagePath)).findFirst()
        .orElseThrow(NoSuchElementException::new)
        .click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }
}
