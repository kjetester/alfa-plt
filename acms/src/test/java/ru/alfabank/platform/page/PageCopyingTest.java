package ru.alfabank.platform.page;

import static ru.alfabank.platform.businessobjects.enums.CopyMethod.COPY;
import static ru.alfabank.platform.businessobjects.enums.CopyMethod.CURRENT;
import static ru.alfabank.platform.businessobjects.enums.CopyMethod.SHARE;
import static ru.alfabank.platform.helpers.DriverHelper.getDriver;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import ru.alfabank.platform.BaseTest;
import ru.alfabank.platform.businessobjects.Page;
import ru.alfabank.platform.pages.acms.MainPage;
import ru.alfabank.platform.utils.TestFailureListener;

@Listeners({TestFailureListener.class})
public class PageCopyingTest extends BasePageTest {

  private Page copiedPage;

  @Test (
      description = "Тест копирования страницы:\n"
          + "\t1. Метод - CURRENT\n",
      groups = {"page", "copyingPage"})
  public void pageCopyingByCurrentTest() throws InterruptedException {
    // STEPS //
    copiedPage = PageFactory.initElements(getDriver(), MainPage.class)
        .openAndAuthorize(BaseTest.baseUri, BaseTest.USER)
        .openPagesTree()
        .openPage(sourcePage.getUri())
        .copyPage(CURRENT)
        .checkAutoFilledCreationForm(sourcePage)
        .fillAndSubmitCreationForm(basePage);
    createdPages.put(copiedPage.getUri(), copiedPage);
    TimeUnit.SECONDS.sleep(5);
    // CHECKS //
    comparePagesInPageController(copiedPage);
    comparePagesMetaInfoContentPageController(copiedPage, CURRENT);
    comparePagesInContentPageController(copiedPage, CURRENT);
  }

  @Test (
      description = "Тест копирования страницы:\n"
          + "\t1. Метод - COPY\n",
      groups = {"page", "copyingPage"})
  public void pageCopyingByCopyTest() throws InterruptedException {
    // STEPS //
    copiedPage =
        PageFactory.initElements(getDriver(), MainPage.class)
            .openAndAuthorize(BaseTest.baseUri, BaseTest.USER)
            .openPagesTree()
            .openPage(sourcePage.getUri())
            .copyPage(COPY)
            .checkAutoFilledCreationForm(sourcePage)
            .fillAndSubmitCreationForm(basePage);
    createdPages.put(copiedPage.getUri(), copiedPage);
    TimeUnit.SECONDS.sleep(5);
    // CHECKS //
    comparePagesInPageController(copiedPage);
    comparePagesMetaInfoContentPageController(copiedPage, COPY);
    comparePagesInContentPageController(copiedPage, COPY);
  }

  @Test (
      description = "Тест копирования страницы:\n"
          + "\t1. Метод - SHARE\n",
      groups = {"page", "copyingPage"})
  public void pageCopyingByShareTest() throws InterruptedException {
    // STEPS //
    copiedPage =
        PageFactory.initElements(getDriver(), MainPage.class)
            .openAndAuthorize(BaseTest.baseUri, BaseTest.USER)
            .openPagesTree()
            .openPage(sourcePage.getUri())
            .copyPage(SHARE)
            .checkAutoFilledCreationForm(sourcePage)
            .fillAndSubmitCreationForm(basePage);
    createdPages.put(copiedPage.getUri(), copiedPage);
    TimeUnit.SECONDS.sleep(5);
    // CHECKS //
    comparePagesInPageController(copiedPage);
    comparePagesMetaInfoContentPageController(copiedPage, SHARE);
    comparePagesInContentPageController(copiedPage, SHARE);
  }
}
