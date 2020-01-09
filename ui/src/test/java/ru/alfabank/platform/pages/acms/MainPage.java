package ru.alfabank.platform.pages.acms;

import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesVisible;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementsBecomeVisible;

import io.qameta.allure.Step;
import java.util.List;
import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.TestException;
import org.testng.TestNGException;
import ru.alfabank.platform.pages.AuthPage;

public class MainPage extends BasePage {

  @FindBy(css = "ul > li[class='ant-menu-item']")
  private WebElement pagesLink;
  @FindBy(css = "li[class = 'ant-tree-treenode-switcher-open'] a")
  private List<WebElement> rootPageList;
  @FindBy(css = ".rst__node")
  private List<WebElement> widgetsList;
  @FindBy(xpath = "//span[text() = 'Сохранить']/..")
  private WebElement saveButton;
  @FindBy(xpath = "//span[text() = 'Опубликовать']/..")
  private WebElement publishButton;

  private By widgetsTitleLSelector = By.cssSelector("span > span");
  private By treeExpandButtonSelector = By.cssSelector("[aria-label = 'Expand']");
  private By treeCollapseButtonSelector = By.cssSelector("[aria-label = 'Collapse']");
  private By sharedMarkerSelector = By.cssSelector("i[class ^= 'anticon anticon-share-alt']");
  private By deleteButtonSelector = By.cssSelector("button[type='button']:not([aria-label])");
  private static String widgetTitle = null;

  /**
   * Opening acms page.
   * @return this
   */
  @Step
  public MainPage openAndAuthorize(String login, String password) {
    String baseUrl = "http://develop.ci.k8s.alfa.link/acms/";
    getDriver().get(baseUrl);
    return PageFactory.initElements(getDriver(), AuthPage.class).login(login, password);
  }

  /**
   * Opening pages list.
   * @return this
   */
  @Step
  public MainPage openPagesTree() {
    System.out.println("Opening pages link");
    waitForElementBecomesClickable(pagesLink);
    pagesLink.click();
    waitForElementsBecomeVisible(rootPageList);
    return this;
  }

  /**
   * Opening requested root page.
   * @param pageUri requested page
   */
  @Step
  public MainPage selectPage(String pageUri) {
    System.out.println(String.format("Searching the page '%s'", pageUri));
    rootPageList.stream().filter(p -> p.getText().contains(pageUri)).findFirst()
        .orElseThrow(NoSuchElementException::new)
        .click();
    waitForElementsBecomeVisible(widgetsList);
    return this;
  }

  /**
   * Opening an accordingly named widget's sidebar.
   * @param widgetName widget name
   * @return WidgetSidebarPage
   */
  @Step
  public WidgetSidebarPage openWidgetSidebarToWorkWithWidgetMeta(String widgetName) {
    System.out.println(String.format("Opening The '%s' widget", widgetName));
    WebElement widget = widgetsList.stream().filter(w ->
        widgetName.contains(w.findElement(widgetsTitleLSelector).getText())).findFirst()
        .orElse(null);
    if (widget != null) {
      widget.findElement(widgetsTitleLSelector).click();
    } else {
      throw new TestNGException(String.format("No Widget named '%s' was found", widgetName));
    }
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Opening widget sidebar.
   * @param widgetName widget name
   * @return PropertyAndPropertyValuePage
   */
  @Step
  public PropertyAndPropertyValuePage openWidgetSidebar(String widgetName) {
    getDriver().findElement(By.xpath(String.format("//span[text() = '%s']/../..", widgetName)))
        .click();
    return PageFactory.initElements(getDriver(), PropertyAndPropertyValuePage.class);
  }

  /**
   * Checking if 'changed' marker is present.
   * @param widgetName widget name
   * @return this
   */
  @Step
  public MainPage checkIfWidgetIsMarked(String widgetName) {
    Assertions
        .assertThat(getDriver()
            .findElement(By.xpath(String.format("//span[text() = '%s']/../../div", widgetName)))
            .getCssValue("background"))
        .as("Checking if 'changed' marker is present")
        .contains("rgb(239, 49, 36)");
    return this;
  }

  /**
   * Saving draft.
   * @return this
   */
  @Step
  public MainPage saveDraft() {
    System.out.println("Saving draft");
    waitForElementBecomesClickable(saveButton).click();
    waitForElementBecomesVisible(draftSavedNotificationBanner);
    return this;
  }

  /**
   * Checking if notice about draft existence is present.
   * @return this
   */
  @Step
  public MainPage checkIfNoticeAboutDraftExistenceIsPresent() {
    Assertions
        .assertThat(getDriver().getPageSource().contains("Имеются неопубликованные изменения"))
        .as("Checking if notice about draft existence is present")
        .isTrue();
    return this;
  }

  /**
   * Checking if notice about draft existence is not present.
   * @return this
   */
  @Step
  public MainPage checkIfNoticeAboutDraftExistenceIsNotPresent() {
    Assertions
        .assertThat(getDriver().getPageSource().contains("Имеются неопубликованные изменения"))
        .as("Checking if notice about draft existence is not present")
        .isFalse();
    return this;
  }

  /**
   * Publishing draft.
   * @return this
   */
  @Step
  public MainPage publishDraft() {
    System.out.println("Publishing draft");
    waitForElementBecomesClickable(publishButton).click();
    submit();
    waitForElementBecomesVisible(draftPublishedNotificationBanner);
    return this;
  }

  /**
   * Delete a root and non shared Widget.
   * @return this
   */
  @Step
  public MainPage deleteNonSharedWidgetHasChilds() {
    WebElement widget = widgetsList.stream().filter(w ->
        (isPresent(w, treeExpandButtonSelector)
            || isPresent(w, treeCollapseButtonSelector))
            && !isPresent(w, sharedMarkerSelector))
        .findFirst().orElse(null);
    if (widget != null) {
      widgetTitle = widget.findElement(widgetsTitleLSelector).getText();
      widget.findElement(deleteButtonSelector).click();
      submit();
    } else {
      throw new TestException("Could not find suitable widget.");
    }
    return this;
  }

  /**
   * Delete a child and non shared Widget.
   * @return this
   */
  @Step
  public MainPage deleteNonSharedWidgetHasNoChilds() {
    WebElement widget = widgetsList.stream().filter(w ->
        !(isPresent(w, treeExpandButtonSelector)
            || isPresent(w, treeCollapseButtonSelector))
            && !isPresent(w, sharedMarkerSelector))
        .findFirst().orElse(null);
    if (widget != null) {
      widgetTitle = widget.findElement(widgetsTitleLSelector).getText();
      widget.findElement(deleteButtonSelector).click();
      submit();
    } else {
      throw new TestException("Could not find suitable widget.");
    }
    return this;
  }

  public static String getWidgetTitle() {
    return widgetTitle;
  }

  public void submit() {
    waitForElementBecomesVisible(modalWindow);
    modalWindowSubmitButton.click();
  }
}
