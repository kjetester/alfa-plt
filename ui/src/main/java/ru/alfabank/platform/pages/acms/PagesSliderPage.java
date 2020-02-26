package ru.alfabank.platform.pages.acms;

import org.apache.log4j.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.testng.*;

import java.util.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class PagesSliderPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(PagesSliderPage.class);

  @FindBy (css = ".ant-tree-title > div")
  private List<WebElement> pageList;

  /**
   * Open requested root page.
   * @param pagePath requested page
   * @return new instance of the MainPage class
   */
  public MainPage selectPage(String pagePath) {
    LOGGER.info(String.format("Открываю страницу '%s'", pagePath));
    getTargetPage(pagePath).click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Search target page with given title.
   * @param pagePath pagePath
   * @return target page
   */
  private WebElement getTargetPage(String pagePath) {
    WebElement targetPage = null;
    for (WebElement page : pageList) {
      if (isPresent(page, By.cssSelector("a"))
          && page.findElement(By.cssSelector("a")).getText().replace("/", "").equals(pagePath)) {
        targetPage = page.findElement(By.cssSelector("a"));
        return targetPage;
      } else if (page.getText().replace("/", "").equals(pagePath.replace("/", ""))) {
        targetPage = page;
        return targetPage;
      }
    }
    if (targetPage != null) {
      return targetPage;
    } else {
      throw new TestNGException(String.format("Страницы '%s' не найдена", pagePath));
    }
  }

  /**
   * Create a new page.
   * @return new Main Page instance
   */
  public NewPageCreationPage createNewPage(String pageName) {
    if (pageName == null) {
      String defaultPage = "https://alfabank.ru";
      LOGGER.debug(String.format("Наименование страницы было определено как '%s'", defaultPage));
      pageName = defaultPage;
    }
    WebElement targetPage = getTargetPage(pageName);
    WebElement createNewPageButton;
    if (isPresent(targetPage, By.cssSelector("button"))) {
      createNewPageButton = targetPage.findElement(By.cssSelector("button"));
    } else {
      createNewPageButton = targetPage.findElement(By.xpath("../button"));
    }
    LOGGER.info(
        String.format("Открываю форму создания новой страницы в корне страницы '%s'", pageName));
    clickWithJavaScriptExecutor(createNewPageButton);
    return PageFactory.initElements(getDriver(), NewPageCreationPage.class);
  }
}
