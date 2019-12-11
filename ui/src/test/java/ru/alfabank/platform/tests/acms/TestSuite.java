package ru.alfabank.platform.tests.acms;

import org.openqa.selenium.support.*;
import org.testng.annotations.*;
import ru.alfabank.platform.pages.*;
import ru.alfabank.platform.tests.*;

import static org.assertj.core.api.Assertions.*;
import static ru.alfabank.platform.helpers.DriverSingleton.*;

public class TestSuite extends BaseTest {

	@Test
	public void openingTest() throws InterruptedException {
		MainPage mainPage = PageFactory.initElements(getDriver(), MainPage.class);
		assertThat(mainPage.openAndAuthorize());
	}
}
