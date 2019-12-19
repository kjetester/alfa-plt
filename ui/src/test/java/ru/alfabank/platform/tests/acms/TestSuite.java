package ru.alfabank.platform.tests.acms;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.alfabank.platform.helpers.DriverSingleton.getDriver;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import ru.alfabank.platform.pages.MainPage;
import ru.alfabank.platform.tests.BaseTest;

public class TestSuite extends BaseTest {

  @Test
  public void openingTest() throws InterruptedException {
    MainPage mainPage = PageFactory.initElements(getDriver(), MainPage.class);
    assertThat(mainPage.openAndAuthorize());
  }
}
