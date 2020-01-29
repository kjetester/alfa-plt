package ru.alfabank.platform.pages.acms;

import org.assertj.core.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.*;
import org.testng.*;
import org.testng.log4testng.*;

import java.util.NoSuchElementException;
import java.util.*;
import java.util.stream.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;
import static ru.alfabank.platform.reporting.BasicLogger.*;

public class MainPage extends BasePage {

  private static final Logger LOGGER = Logger.getLogger(BasePage.class);

  @FindBy(css = "a[href = '/acms/pages']")
  private WebElement pagesLink;
  @FindBy(css = "[style *= 'align-self']")
  private WebElement createNewPageButton;
  @FindBy(css = "li[class = 'ant-tree-treenode-switcher-open'] a")
  private List<WebElement> rootPageList;
  @FindBy(css = "[class ^= screen-title]")
  private WebElement pageTitle;
  @FindBy(css = ".rst__node")
  private List<WebElement> widgetsList;
  @FindBy(xpath = "//span[text() = 'Сохранить']/..")
  private WebElement saveButton;
  @FindBy(xpath = "//span[text() = 'Опубликовать']/..")
  private WebElement publishButton;
  @FindBy(css = "[aria-haspopup = 'listbox']")
  private WebElement pagesDropdownList;
  private By widgetsTitleLSelector = By.cssSelector("span > span");
  private By treeExpandButtonSelector = By.cssSelector("[aria-label = 'Expand']");
  private By treeCollapseButtonSelector = By.cssSelector("[aria-label = 'Collapse']");
  private By sharedMarkerSelector = By.cssSelector("i[class ^= 'anticon anticon-share-alt']");
  private By deleteButtonSelector = By.cssSelector("button[type='button']:not([aria-label])");
  private By copyButtonSelector = By.cssSelector("button [class $= 'copy']");
  private By widgetSelector = By.cssSelector(".rst__rowTitle  > div");

  /**
   * Open acms page.
   * @return this
   */
  public MainPage openAndAuthorize(String baseUrl, String login, String password) {
    info(String.format("Starting the browser and navigating to %s", baseUrl));
    getDriver().get(baseUrl);
    return PageFactory.initElements(getDriver(), AuthPage.class).login(login, password);
  }

  /**
   * Open pages list.
   * @return this
   */
  public MainPage openPagesTree() {
    info("Opening pages link");
    waitForElementBecomesClickable(pagesLink);
    pagesLink.click();
    waitForElementsBecomeVisible(rootPageList);
    return this;
  }

  /**
   * Open requested root page.
   * @param pagePath requested page
   */
  public MainPage selectPage(String pagePath) {
    info(String.format("Opening the '%s' page", pagePath));
    rootPageList.stream().filter(p ->
        p.getText().replace("/", "").equals(pagePath)).findFirst()
        .orElseThrow(NoSuchElementException::new)
        .click();
    waitForElementBecomesVisible(pageTitle);
    waitForElementsBecomeVisible(widgetsList);
    return this;
  }

  /**
   * Open an accordingly named widget's sidebar.
   * @param widgetName widget name
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage openWidgetSidebarToWorkWithWidgetMeta(String widgetName) {
    info(String.format("Opening the %s widget's sidebar", widgetName));
    findWidget(widgetName).findElement(widgetsTitleLSelector).click();
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Open an accordingly named widget's sidebar.
   * @param widgetName widget name
   * @return PropertyAndPropertyValuePage
   */
  public PropertyAndPropertyValuePage openWidgetSidebar(String widgetName) {
    info(String.format("Opening the %s widget's sidebar", widgetName));
    findWidget(widgetName).findElement(widgetsTitleLSelector).click();
    return PageFactory.initElements(getDriver(), PropertyAndPropertyValuePage.class);
  }

  /**
   * Copy an accordingly named widget to a specified page.
   * @param widgetName widget name
   * @param pageName target page name
   * @return target page instance
   */
  public MainPage copyWidgetOnPage(String widgetName, String pageName) throws InterruptedException {
    info(String.format("Copying the '%s' widget to the '%s' page", widgetName, pageName));
    new Actions(getDriver())
        .moveToElement(findWidget(widgetName).findElement(copyButtonSelector))
        .click()
        .build()
        .perform();
    waitForElementBecomesClickable(pagesDropdownList).click();
    setValueToMonacoTextArea(pageName, getDriver().switchTo().activeElement());
    waitForElementBecomesClickable(
        getDriver().findElement(By.cssSelector(String.format("[title='%s']", pageName))))
        .click();
    submit();
    Thread.sleep(2_000L);
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Open widget sidebar.
   * @param widgetName widget name
   */
  private WebElement findWidget(String widgetName) {
    return widgetsList.stream().filter(w ->
        widgetName.equals(w.findElement(widgetsTitleLSelector).getText()))
        .findFirst().orElseThrow(() ->
            new TestNGException(String.format("No Widget named '%s' was found", widgetName)));
  }

  /**
   * Check if 'changed' marker is present.
   * @param widgetName widget name
   * @return this
   */
  public MainPage checkIfWidgetIsMarkedAsChanged(String widgetName) {
    info(String.format("Checking the %s widget has been marked", widgetName));
    Assertions
        .assertThat(getDriver()
            .findElement(By.xpath(String.format("//span[text() = '%s']/../../div", widgetName)))
            .getCssValue("background"))
        .as("Checking if 'changed' marker is present")
        .contains("rgb(239, 49, 36)");
    return this;
  }

  /**
   * Save draft.
   * @return this
   */
  public MainPage saveDraft() {
    LOGGER.info("Saving the draft");
    waitForElementBecomesClickable(saveButton).click();
    waitForElementBecomesClickable(bannerCloseBttn).click();
    return this;
  }

  /**
   * Check if notice about draft existence is present.
   * @return this
   */
  public MainPage checkIfNoticeAboutDraftExistenceIsPresent() {
    LOGGER.info("Checking if a notice about the draft existence is present");
    Assertions
        .assertThat(getDriver().getPageSource().contains("Имеются неопубликованные изменения"))
        .as("A notice about the draft existence isn't present")
        .isTrue();
    return this;
  }

  /**
   * Check if notice about draft existence is not present.
   * @return this
   */
  public MainPage checkIfNoticeAboutDraftExistenceIsNotPresent() {
    LOGGER.info("Checking if a notice about the draft existence is not present");
    Assertions
        .assertThat(getDriver().getPageSource().contains("Имеются неопубликованные изменения"))
        .as("A notice about the draft existence is still shown")
        .isFalse();
    return this;
  }

  /**
   * Publish draft.
   * @return this
   */
  public MainPage publishDraft() {
    LOGGER.info("Publishing draft");
    waitForElementBecomesClickable(publishButton).click();
    submit();
    waitForElementBecomesClickable(bannerCloseBttn).click();
    return this;
  }

  /**
   * Delete a root and non shared Widget.
   * @return this
   */
  public MainPage deleteNonSharedWidgetHasChildren() {
    LOGGER.info("Searching for a non-shared widget with a children");
    WebElement widget = widgetsList.stream().filter(w ->
        (isPresent(w, treeExpandButtonSelector)
            || isPresent(w, treeCollapseButtonSelector))
            && !isPresent(w, sharedMarkerSelector))
        .findFirst().orElse(null);
    if (widget != null) {
      info(String.format("Deleting the '%s' widget",
          widget.findElement(widgetsTitleLSelector).getText()));
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
  
  public MainPage deleteNonSharedWidgetHasNoChildren() {
    LOGGER.info("Searching for a non-shared widget without a children");
    WebElement widget = widgetsList.stream().filter(w ->
        !(isPresent(w, treeExpandButtonSelector)
            || isPresent(w, treeCollapseButtonSelector))
            && !isPresent(w, sharedMarkerSelector))
        .findFirst().orElse(null);
    if (widget != null) {
      info(String.format("Deleting the '%s' widget",
          widget.findElement(widgetsTitleLSelector).getText()));
      widget.findElement(deleteButtonSelector).click();
      submit();
    } else {
      throw new TestException("Could not find suitable widget.");
    }
    return this;
  }

  /**
   * Submit in a modal window.
   */
  public void submit() {
    LOGGER.debug("Clicking the modal window's submit button");
    waitForElementBecomesVisible(modalWindow);
    modalWindowSubmitButton.click();
  }

  /**
   * Checking for a found entity marking.
   * @param widgetName entity name
   */
  public MainPage checkIfWidgetIsMarkedAsFound(String widgetName) {
    info(String.format("Checking if the '%s' widget has been marked", widgetName));
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
    //TODO: fix it!
    otherWidgets.forEach(w -> Assertions
        .assertThat(w.findElement(widgetSelector).getCssValue("background-color"))
        .as("Some other widget has been marked")
        .contains(("255, 255, 255")));
    return this;
  }

  /**
   * Check that no one widget is marked.
   */
  public void checkNoWidgetIsMarked() {
    LOGGER.info("Checking if no one other widget has been marked");
    widgetsList.forEach(w -> Assertions
        .assertThat(w.findElement(widgetSelector).getCssValue("background-color"))
        .as("The other widget has been marked")
        .contains(("255, 255, 255")));
  }

  /**
   * Create a new page.
   * @return new Main Page instance
   */
  public NewPageCreationPage createNewPageFromRoot() {
    createNewPageButton.click();
    return PageFactory.initElements(getDriver(), NewPageCreationPage.class);
  }

  /**
   * Check if page with pagePath is opened.
   * @param pagePath pagePath
   * @return MainPage instance
   */
  public MainPage checkPageOpened(String pagePath) {
    Assertions.assertThat(pageTitle.getText()).contains(pagePath);
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  public MainPage checkIfWidgetIsPresent(String widgetName) {
    findWidget(widgetName);
    return this;
  }
}
