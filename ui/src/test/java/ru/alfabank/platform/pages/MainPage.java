package ru.alfabank.platform.pages;

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

public class MainPage extends BasePage {

  @FindBy(className = "logo_1TN8l")
  private WebElement logo;
  @FindBy(css = "ul > li:nth-child(2)")
  private WebElement pagesLink;
  @FindBy(xpath = "//li[@class='ant-tree-treenode-switcher-open']//li//a")
  private List<WebElement> rootPageList;
  @FindBy(xpath = "//div[@class = 'rst__rowWrapper null']/div")
  private List<WebElement> widgetsList;
  @FindBy(xpath = "//span[text() = 'Сохранить']/..")
  private WebElement saveButton;
  @FindBy(xpath = "//span[text() = 'Опубликовать']/..")
  private WebElement publishButton;

  /**
   * Opening acms page.
   * @return this
   */
  @Step
  public MainPage openAndAuthorize() {
    String baseUrl = "http://admin:h3VtGrE696@develop.ci.k8s.alfa.link/acms/";
    getDriver().get(baseUrl);
    waitForElementBecomesVisible(logo);
    return this;
  }

  /**
   * Opening pages list.
   * @return this
   */
  @Step
  public MainPage openPagesTree() {
    pagesLink.click();
    waitForElementsBecomeVisible(rootPageList);
    return this;
  }

  /**
   * Opening requested root page.
   * @param pageUri requested page
   */
  @Step
  public void openPage(String pageUri) {
    rootPageList.stream().filter(p -> p.getText().contains(pageUri)).findFirst()
        .orElseThrow(NoSuchElementException::new)
        .click();
    waitForElementsBecomeVisible(widgetsList);
  }

  /**
   * Opening widget sidebar.
   * @param widgetName widget name
   * @return WidgetSidebarPage
   */
  @Step
  public WidgetSidebarPage openWidgetSidebar(String widgetName) {
    getDriver().findElement(By.xpath(String.format("//span[text() = '%s']/../..", widgetName)))
        .click();
    return PageFactory.initElements(getDriver(), WidgetSidebarPage.class);
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
    waitForElementBecomesClickable(publishButton).click();
    waitForElementBecomesVisible(modalWindow);
    waitForElementBecomesClickable(modalWindowSubmitButton).click();
    waitForElementBecomesVisible(draftPublishedNotificationBanner);
    return this;
  }
}
