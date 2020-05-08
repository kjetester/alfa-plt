package ru.alfabank.platform.pages.acms;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;
import static ru.alfabank.platform.helpers.DriverHelper.waitForElementBecomesClickable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.TestException;
import org.testng.TestNGException;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.businessobjects.enums.CopyMethod;
import ru.alfabank.platform.pages.alfasite.AlfaSitePage;

public class MainPage extends BasePage {

  private static final Logger LOGGER = LogManager.getLogger(MainPage.class);

  @FindBy(css = "[rel='noopener noreferrer']")
  private WebElement pageTitle;
  @FindBy(css = ".rst__node")
  private List<WebElement> widgetsList;
  @FindBy(css = "#root > div div:nth-child(3) > div > button:nth-child(1)")
  private WebElement copyPageButton;
  @FindBy(css = "#root > div div:nth-child(3) > div > button:nth-child(2)")
  private WebElement deleteButton;
  @FindBy(xpath = "//span[text() = 'Сохранить']/..")
  private WebElement saveButton;
  @FindBy(xpath = "//span[text() = 'Опубликовать']/..")
  private WebElement publishButton;
  @FindBy(css = "div:nth-child(1) > button.ant-btn.ant-btn-default:nth-child(1)")
  private WebElement createNewWidgetButton;
  @FindBy(css = "input[placeholder='Введите название виджета']")
  private WebElement newWidgetSearchInput;
  @FindBy(css = ".ant-drawer-body [draggable='true']")
  private List<WebElement> availableWidgetsList;
  @FindBy(css = ".rst__placeholder")
  private WebElement newWidgetDropArea;
  @FindBy(css = ".ant-select-selector")
  private WebElement pagesDropdownList;
  @FindBy(css = "[class *='ant-select-tree-title']")
  private List<WebElement> pageTreeElements;
  @FindBy(css = "[type='checkbox']")
  private WebElement shareCheckbox;
  @FindBy(css = "label:nth-child(1) > span:nth-child(2)")
  private WebElement shareRadio;
  @FindBy(css = "label:nth-child(2) > span:nth-child(2)")
  private WebElement copyRadio;
  @FindBy(css = "label:nth-child(3) > span:nth-child(2)")
  private WebElement asIsRadio;
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
  public MainPage copyWidgetOnPage(String widgetName, String pageName) {
    LOGGER.info(String.format("Копирую виджет '%s' на страницу '%s'", widgetName, pageName));
    hoverAndClick(findWidget(widgetName).findElement(copyButtonSelector));
    waitForElementBecomesClickable(pagesDropdownList).click();
    setValueToMonacoTextArea(pageName, getDriver().switchTo().activeElement());
    waitForElementBecomesClickable(
        getDriver().findElement(By.cssSelector(String.format("[title='%s']", pageName))))
        .click();
    submitDialog();
    closeSuccessBanner();
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
    assertThat(getDriver()
        .findElement(By.xpath(String.format("//span[text() = '%s']/../../div", widgetName)))
        .getCssValue("background"))
        .contains("rgb(239, 49, 36)");
    return this;
  }

  /**
   * Check if notice about draft existence is present.
   * @return this
   */
  public MainPage checkIfNoticeAboutDraftExistenceIsPresent() {
    LOGGER.info("Проверяю, что сообщение о существовании черновика отображается");
    assertThat(getDriver().getPageSource().contains("Имеются неопубликованные изменения"))
        .isTrue();
    return this;
  }

  /**
   * Check if notice about draft existence is not present.
   * @return this
   */
  public MainPage checkIfNoticeAboutDraftExistenceIsNotPresent() {
    LOGGER.info("Проверяю, что сообщение о существовании черновика больше не отображается");
    assertThat(getDriver().getPageSource().contains("Имеются неопубликованные изменения"))
        .isFalse();
    return this;
  }

  /**
   * Save draft.
   * @return this
   */
  public MainPage saveDraft() {
    LOGGER.info("Сохраняю черновик");
    waitForElementBecomesClickable(saveButton).click();
    closeSuccessBanner();
    return this;
  }

  /**
   * Publish draft.
   * @return this
   */
  public MainPage publishDraft() {
    LOGGER.info("Публикую черновик страницы");
    waitForElementBecomesClickable(publishButton).click();
    submitDialog();
    closeSuccessBanner();
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
      submitDialog();
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
      submitDialog();
    } else {
      throw new TestException("Не смог найти подходящий виджет");
    }
    return this;
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
    List<WebElement> otherWidgets = widgetsList.stream().filter(w ->
        !widgetName.equals(w.findElement(widgetsTitleLSelector).getText()))
        .collect(Collectors.toList());
    targetWidgets.forEach(w -> assertThat(
        w.findElement(widgetSelector).getCssValue("background-color")).contains("24, 144, 255"));
    //TODO: fix it!
    LOGGER.info("Проверяю, что лишние виджеты не были отмечены, как соответствующий условю поиска");
    otherWidgets.forEach(w -> assertThat(
        w.findElement(widgetSelector).getCssValue("background-color")).contains(("255, 255, 255")));
    return this;
  }

  /**
   * Check that no one widget is marked.
   */
  public void checkNoWidgetIsMarked() {
    LOGGER.info("Проверяю, что виджет не отмечен, как соответствующий условю поиска");
    widgetsList.forEach(w -> assertThat(
        w.findElement(widgetSelector).getCssValue("background-color"))
        .as("The other widget has been marked")
        .contains(("255, 255, 255")));
  }

  /**
   * Check if page with pagePath is opened.
   * @param pagePath pagePath
   * @return MainPage instance
   */
  public MainPage checkPageOpened(String pagePath) {
    LOGGER.info("Проверяю, отображается ли странца пользователю после создания");
    softly.assertThat(pageTitle.getText().contains(pagePath));
    softly.assertAll();
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

  /**
   * Navigate to AlfaSite.
   * @return AlfaSitePage instance
   */
  public AlfaSitePage navigateToAlfaSite() {
    LOGGER.info("Перехожу на соответствующую страницу на Альфа-сайте");
    waitForElementBecomesClickable(pageTitle).click();
    ArrayList<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
    getDriver().switchTo().window(tabs.get(tabs.size() - 1));
    return PageFactory.initElements(getDriver(), AlfaSitePage.class);
  }

  /**
   * Delete page.
   */
  public void deletePage() {
    LOGGER.info("Удаляю страницу");
    waitForElementBecomesClickable(deleteButton).click();
    submitDialog();
    closeSuccessBanner();
  }

  /**
   * Copy all widgets from a {@code sourcePage} to a {@code targetPage}.
   * @param sourcePage sourcePage
   * @param targetPage targetPage
   */
  public MainPage copyAllWidgets(Page sourcePage, Page targetPage) {
    copyOrShareWidgets(sourcePage.getUri(), targetPage.getUri(), false);
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  public MainPage shareAllWidgets(Page sourcePage, Page targetPage) {
    copyOrShareWidgets(sourcePage.getUri(), targetPage.getUri(), true);
    return PageFactory.initElements(getDriver(), MainPage.class);
  }

  /**
   * Copies or shares all of one page's widgets towards another.
   * @param sourcePageUri source page
   * @param targetPageUri target page
   * @param isSharing the flag if action is sharing
   */
  private void copyOrShareWidgets(String sourcePageUri, String targetPageUri, boolean isSharing) {
    int widgetsCount = widgetsList.size();
    if (widgetsCount > 0) {
      LOGGER.info(String.format(
          "Начинаю %s всех виджетов на страницу '%s'",
          isSharing ? "шаринг" : "копирование", targetPageUri));
      for (int i = widgetsCount; i > 0; i--) {
        hoverAndClick(widgetsList.get(i - 1).findElement(copyButtonSelector));
        waitForElementBecomesClickable(pagesDropdownList).click();
        setValueToMonacoTextArea(targetPageUri, getDriver().switchTo().activeElement());
        pageTreeElements.stream().filter(e ->
            e.getText().equals(targetPageUri)).findFirst().orElseThrow(() ->
            new TestNGException(String.format("Страница '%s' не найдена", targetPageUri))).click();
        if (isSharing) {
          setCheckboxTo(shareCheckbox, true);
        }
        submitDialog();
        closeSuccessBanner();
        if (i != 1) {
          PageFactory.initElements(getDriver(), PagesSliderPage.class).openPage(sourcePageUri);
        }
      }
    } else {
      throw new TestNGException(
          String.format("На странице '%s' нет виджетов для копирования.", sourcePageUri));
    }
  }

  /**
   * Create new widget.
   * @param widgetName widget name
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage createNewWidgetAndContinueWithWidgetSidebarPage(String widgetName) {
    createNewWidget(widgetName);
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Create new widget.
   * @param widgetName widget name
   * @return WidgetSidebarPage
   */
  public WidgetSidebarPage createNewWidget(String widgetName) {
    LOGGER.info("Нажимаю кнопку добавления нового виджета на страницу");
    waitForElementBecomesClickable(createNewWidgetButton).click();
    LOGGER.info("Ввожу наименование виджета для его поиска в справочнике");
    waitForElementBecomesClickable(newWidgetSearchInput).click();
    setValueToMonacoTextArea(widgetName, getDriver().switchTo().activeElement());
    LOGGER.info(String.format(
        "Выбираю в результирующем списке виджет с названием '%s'",
        widgetName));
    WebElement widget = PageFactory.initElements(getDriver(), MainPage.class)
        .availableWidgetsList.stream().filter(w -> w.getText().equals(widgetName))
        .findAny().orElseThrow(() -> new TestNGException(
            String.format("Виджет с названием '%s' не найден в списке", widgetName)))
        .findElement(By.cssSelector("div"));
    var actions = new Actions(getDriver());
    actions.dragAndDrop(widget, newWidgetDropArea).build().perform();
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
  }

  /**
   * Copy page.
   * @param copyMethod copy method
   * @return page creation page
   */
  public NewPageCreationPage copyPage(final CopyMethod copyMethod) {
    waitForElementBecomesClickable(copyPageButton).click();
    switch (copyMethod) {
      case COPY: {
        waitForElementBecomesClickable(copyRadio).click();
        break;
      }
      case SHARE: {
        waitForElementBecomesClickable(shareRadio).click();
        break;
      }
      default: {
        waitForElementBecomesClickable(asIsRadio).click();
        break;
      }
    }
    submitDialog();
    return PageFactory.initElements(getDriver(), NewPageCreationPage.class);
  }
}
