package ru.alfabank.platform.pages.acms;

import org.apache.log4j.*;
import org.assertj.core.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.testng.*;

import java.util.*;
import java.util.stream.*;

import static ru.alfabank.platform.helpers.DriverHelper.*;

public class MainPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(MainPage.class);

  @FindBy(css = "[class $= 'ant-tree-node-content-wrapper-open'] i")
  private WebElement createNewPageFromRootButton;
  @FindBy(css = "[rel='noopener noreferrer']")
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
   * Open an accordingly named widget's sidebar.
   * @param widgetName widget name
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage openWidgetSidebarToWorkWithWidgetMeta(String widgetName) {
    opnWidgetSidebar(widgetName);
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Open an accordingly named widget's sidebar.
   * @param widgetName widget name
   * @return PropertyAndPropertyValuePage
   */
  public PropertyAndPropertyValuePage openWidgetSidebar(String widgetName) {
    opnWidgetSidebar(widgetName);
    return PageFactory.initElements(getDriver(), PropertyAndPropertyValuePage.class);
  }

  /**
   * Open an accordingly named widget's sidebar.
   * @param widgetName widget name
   */
  private void opnWidgetSidebar(String widgetName) {
    LOGGER.info(String.format("Открываю сайдбар виджета '%s'", widgetName));
    findWidget(widgetName).findElement(widgetsTitleLSelector).click();
  }

  /**
   * Copy an accordingly named widget to a specified page.
   * @param widgetName widget name
   * @param pageName target page name
   * @return target page instance
   */
  public MainPage copyWidgetOnPage(String widgetName, String pageName) throws InterruptedException {
    LOGGER.info(String.format("Копирую виджет '%s' на страницу '%s'", widgetName, pageName));
    clickOnHiddenButton(findWidget(widgetName).findElement(copyButtonSelector));
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
    LOGGER.debug(String.format("Ищу виджет '%s'", widgetName));
    return widgetsList.stream().filter(w ->
        widgetName.equals(w.findElement(widgetsTitleLSelector).getText()))
        .findFirst().orElseThrow(() ->
            new TestNGException(
                String.format("Ни одного виджета с названием '%s' не было найдено", widgetName)));
  }

  /**
   * Check if 'changed' marker is present.
   * @param widgetName widget name
   * @return this
   */
  public MainPage checkIfWidgetIsMarkedAsChanged(String widgetName) {
    LOGGER.info(String.format("Проверяю, что втждет '%s' отмечен, как измененный", widgetName));
    Assertions
        .assertThat(getDriver()
            .findElement(By.xpath(String.format("//span[text() = '%s']/../../div", widgetName)))
            .getCssValue("background"))
        .contains("rgb(239, 49, 36)");
    return this;
  }

  /**
   * Save draft.
   * @return this
   */
  public MainPage saveDraft() {
    LOGGER.info("Сохраняю черновик");
    waitForElementBecomesClickable(saveButton).click();
    waitForElementBecomesClickable(bannerCloseBttn).click();
    return this;
  }

  /**
   * Check if notice about draft existence is present.
   * @return this
   */
  public MainPage checkIfNoticeAboutDraftExistenceIsPresent() {
    LOGGER.info("Проверяю, что сообщение о существовании черновика отображается");
    Assertions
        .assertThat(getDriver().getPageSource().contains("Имеются неопубликованные изменения"))
        .isTrue();
    return this;
  }

  /**
   * Check if notice about draft existence is not present.
   * @return this
   */
  public MainPage checkIfNoticeAboutDraftExistenceIsNotPresent() {
    LOGGER.info("Проверяю, что сообщение о существовании черновика больше не отображается");
    Assertions
        .assertThat(getDriver().getPageSource().contains("Имеются неопубликованные изменения"))
        .isFalse();
    return this;
  }

  /**
   * Publish draft.
   * @return this
   */
  public MainPage publishDraft() {
    LOGGER.info("Публикую черновик страницы");
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
    LOGGER.info("Ищу нешаренный вижджет, имеющий дечение виджеты");
    WebElement widget = widgetsList.stream().filter(w ->
        (isPresent(w, treeExpandButtonSelector)
            || isPresent(w, treeCollapseButtonSelector))
            && !isPresent(w, sharedMarkerSelector))
        .findFirst().orElse(null);
    if (widget != null) {
      LOGGER.info(String.format("Deleting the '%s' widget",
          widget.findElement(widgetsTitleLSelector).getText()));
      widget.findElement(deleteButtonSelector).click();
      submit();
    } else {
      throw new TestException("Не смог найти подходящий виджет");
    }
    return this;
  }

  /**
   * Delete a child and non shared Widget.
   * @return this
   */
  
  public MainPage deleteNonSharedWidgetHasNoChildren() {
    LOGGER.info("Ищу нешаренный вижджет, не имеющий дечених виджеты");
    WebElement widget = widgetsList.stream().filter(w ->
        !(isPresent(w, treeExpandButtonSelector)
            || isPresent(w, treeCollapseButtonSelector))
            && !isPresent(w, sharedMarkerSelector))
        .findFirst().orElse(null);
    if (widget != null) {
      LOGGER.info(String.format("Deleting the '%s' widget",
          widget.findElement(widgetsTitleLSelector).getText()));
      widget.findElement(deleteButtonSelector).click();
      submit();
    } else {
      throw new TestException("Не смог найти подходящий виджет");
    }
    return this;
  }

  /**
   * Submit in a modal window.
   */
  public void submit() {
    LOGGER.debug("Подтверждаю действие");
    waitForElementBecomesVisible(modalWindow);
    modalWindowSubmitButton.click();
  }

  /**
   * Checking for a found entity marking.
   * @param widgetName entity name
   */
  public MainPage checkIfWidgetIsMarkedAsFound(String widgetName) {
    LOGGER.info(
        String.format(
            "Проверяю, что виджет '%s' отмечен, как соответствующий условю поиска",
            widgetName));
    List<WebElement> targetWidgets = widgetsList.stream().filter(w ->
        widgetName.equals(w.findElement(widgetsTitleLSelector).getText()))
        .collect(Collectors.toList());
    boolean b = Object.class instanceof Object;
    List<WebElement> otherWidgets = widgetsList.stream().filter(w ->
        !widgetName.equals(w.findElement(widgetsTitleLSelector).getText()))
        .collect(Collectors.toList());
    targetWidgets.forEach(w -> Assertions
        .assertThat(w.findElement(widgetSelector).getCssValue("background-color"))
        .contains("24, 144, 255"));
    //TODO: fix it!
    LOGGER.info("Проверяю, что лишние виджеты не были отмечены, как соответствующий условю поиска");
    otherWidgets.forEach(w -> Assertions
        .assertThat(w.findElement(widgetSelector).getCssValue("background-color"))
        .contains(("255, 255, 255")));
    return this;
  }

  /**
   * Check that no one widget is marked.
   */
  public void checkNoWidgetIsMarked() {
    LOGGER.info("Проверяю, что виджет не отмечен, как соответствующий условю поиска");
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
    LOGGER.info("Создаю новую страницу в корне");
    clickWithJavaScriptExecutor(createNewPageFromRootButton);
    return PageFactory.initElements(getDriver(), NewPageCreationPage.class);
  }

  /**
   * Check if page with pagePath is opened.
   * @param pagePath pagePath
   * @return MainPage instance
   */
  public MainPage checkPageOpened(String pagePath) {
    LOGGER.info("Проверяю, отображается ли странца пользователю после создания");
    Assertions.assertThat(pageTitle.getText()).contains(pagePath);
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Check if widget is present.
   * @param widgetName widgetName
   * @return this
   */
  public MainPage checkIfWidgetIsPresent(String widgetName) {
    LOGGER.info(String.format("Проверяю, присутствует ли виджет '%s' на странице", widgetName));
    findWidget(widgetName);
    return this;
  }
}
