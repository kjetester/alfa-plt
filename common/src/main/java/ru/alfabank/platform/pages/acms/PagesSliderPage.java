package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

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
   * @param pageUri requested page
   * @return new instance of the MainPage class
   */
  public MainPage openPage(String pageUri) {
    LOGGER.info(String.format("Открываю страницу '%s'", pageUri));
    getTargetPage(pageUri).click();
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Search target page with given title.
   * @param pageUri page name (URI)
   * @return result page
   */
  private WebElement getTargetPage(final String pageUri) {
    return pageList.stream().filter(p ->
        p.getText().equals(pageUri.replaceAll("^/", "")))
        .findFirst().orElseThrow(() ->
            new TestNGException(String.format("Страница '%s' не найдена", pageUri)));
  }

  /**
   * Create a new page.
   * @param parentPageName parent page name
   * @return new Main Page instance
   */
  public NewPageCreationPage createNewPageWithinPage(String parentPageName) {
    By createNewPageButtonSelector;
    WebElement createNewPageButton;
    if (parentPageName == null) {
      LOGGER.debug("Создаю новую страницу в корне");
      createNewPageButtonSelector = By.cssSelector("[data-test-id = 'pg_crt_btn_/']");
    } else {
      LOGGER.debug(String.format("Создаю новую страницу в корне '%s'", parentPageName));
      createNewPageButtonSelector =
          By.cssSelector(String.format("[data-test-id ^= 'pg_crt_btn_/%s']", parentPageName));
    }
    createNewPageButton = getDriver().findElement(createNewPageButtonSelector);
    LOGGER.info(parentPageName == null
        ? "Открываю форму создания новой страницы в корне"
        : String.format(
        "Открываю форму создания новой страницы в корне страницы '%s'",
        parentPageName));
    clickWithJavaScriptExecutor(createNewPageButton);
    return PageFactory.initElements(getDriver(), NewPageCreationPage.class);
  }
}
