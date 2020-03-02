package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.implicitlyWait;

import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.TestNGException;

public class PagesSliderPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(PagesSliderPage.class);

  @FindBy (css = ".ant-tree-title > div")
  private List<WebElement> pageList;

  /**
   * Open requested root page.
   * @param pagePath requested page
   * @return new instance of the MainPage class
   */
  public MainPage openPage(String pagePath) {
    LOGGER.info(String.format("Открываю страницу '%s'", pagePath));
    getTargetPage(pagePath).click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Search target page with given title.
   * @param pageName page name (URI)
   * @return result page
   */
  private WebElement getTargetPage(String pageName) {
    try {
      implicitlyWait(false);
      WebElement resultPage = null;
      for (WebElement page : pageList) {
        pageName = pageName
            .replaceAll("^/", "")
            .replaceAll("/$","");
        if (isPresent(page, By.cssSelector("a"))) {
          WebElement likelyPage = page.findElement(By.cssSelector("a"));
          if (likelyPage.getText().contains(pageName)) {
            resultPage = likelyPage;
            return resultPage;
          }
        }
        if (page.getText().contains(pageName)) {
          resultPage = page;
          return resultPage;
        }
      }
      if (resultPage != null) {
        return resultPage;
      } else {
        throw new TestNGException(String.format("Страница '%s' не найдена", pageName));
      }
    } finally {
      implicitlyWait(true);
    }
  }

  /**
   * Create a new page.
   * @return new Main Page instance
   */
  public NewPageCreationPage createNewPageWithinPage(String parentPageName) {
    if (parentPageName == null) {
      String rootPage = "https://alfabank.ru";
      LOGGER.debug(String.format("Создаю новую страницу в корне страницы '%s'", rootPage));
      parentPageName = rootPage;
    }
    WebElement targetPage = getTargetPage(parentPageName);
    WebElement createNewPageButton;
    if (isPresent(targetPage, By.cssSelector("button"))) {
      createNewPageButton = targetPage.findElement(By.cssSelector("button"));
    } else {
      createNewPageButton = targetPage.findElement(By.xpath("../button"));
    }
    LOGGER.info(String.format(
        "Открываю форму создания новой страницы в корне страницы '%s'", parentPageName));
    clickWithJavaScriptExecutor(createNewPageButton);
    return PageFactory.initElements(getDriver(), NewPageCreationPage.class);
  }
}
