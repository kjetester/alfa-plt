package ru.alfabank.platform.pages.acms;

import io.qameta.allure.*;
import org.assertj.core.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.testng.*;

import java.util.NoSuchElementException;
import java.util.*;
import java.util.stream.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class MainPage extends BasePage {

  @FindBy(css = "a[href = '/acms/pages']")
  private WebElement pagesLink;
  @FindBy(css = "[style *= 'align-self']")
  private WebElement addPageButton;
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
  private By widgetSelector = By.cssSelector(".rst__rowTitle  > div");

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
    rootPageList.stream().filter(p -> p.getText().equals(pageUri)).findFirst()
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
    openWidget(widgetName);
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Opening an accordingly named widget's sidebar.
   * @param widgetName widget name
   * @return PropertyAndPropertyValuePage
   */
  @Step
  public PropertyAndPropertyValuePage openWidgetSidebar(String widgetName) {
    openWidget(widgetName);
    return PageFactory.initElements(getDriver(), PropertyAndPropertyValuePage.class);
  }

  /**
   * Opening widget sidebar.
   * @param widgetName widget name
   */
  private void openWidget(String widgetName) {
    System.out.println(String.format("Opening the '%s' widget's sidebar", widgetName));
    WebElement widget = widgetsList.stream().filter(w ->
        widgetName.equals(w.findElement(widgetsTitleLSelector).getText()))
        .findFirst().orElseThrow(() ->
            new TestNGException(String.format("No Widget named '%s' was found", widgetName)));
    widget.findElement(widgetsTitleLSelector).click();
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
    waitForElementBecomesClickable(bannerCloseBttn).click();
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
    waitForElementBecomesClickable(bannerCloseBttn).click();
    return this;
  }

  /**
   * Delete a root and non shared Widget.
   * @return this
   */
  @Step
  public MainPage deleteNonSharedWidgetHasChildren() {
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
  public MainPage deleteNonSharedWidgetHasNoChildren() {
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

  public void submit() {
    waitForElementBecomesVisible(modalWindow);
    modalWindowSubmitButton.click();
  }

  /**
   * Checking for a found entity marking.
   * @param widgetName entity name
   */
  public MainPage checkWidgetIsMarked(String widgetName) {
    System.out.println("Checking if the widget has been marked");
    List<WebElement> targetWidgets = widgetsList.stream().filter(w ->
        widgetName.equals(w.findElement(widgetsTitleLSelector).getText()))
        .collect(Collectors.toList());
    boolean b = Object.class instanceof Object;
    List<WebElement> otherWidgets = widgetsList.stream().filter(w ->
        !widgetName.equals(w.findElement(widgetsTitleLSelector).getText()))
        .collect(Collectors.toList());
    targetWidgets.forEach(w -> Assertions
        .assertThat(w.findElement(widgetSelector).getCssValue("background-color"))
        .as("The target widget hasn't been marked")
        .contains("24, 144, 255"));
    otherWidgets.forEach(w -> Assertions
        .assertThat(w.findElement(widgetSelector).getCssValue("background-color"))
        .as("The other widget has been marked")
        .contains(("255, 255, 255")));
    return this;
  }

  /**
   * Check that no one widget is marked.
   */
  public void checkNoWidgetIsMarked() {
    System.out.println("Checking if no widget has been marked");
    widgetsList.forEach(w -> Assertions
        .assertThat(w.findElement(widgetSelector).getCssValue("background-color"))
        .as("The other widget has been marked")
        .contains(("255, 255, 255")));
  }
}
