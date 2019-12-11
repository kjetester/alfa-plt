package ru.alfabank.platform.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import static ru.alfabank.platform.helpers.DriverSingleton.*;

public class MainPage {

	@FindBy(className = "logo_1TN8l")
	private WebElement logo;

	public boolean openAndAuthorize() throws InterruptedException {
		String baseUrl = "http://admin:h3VtGrE696@develop.ci.k8s.alfa.link/acms/pages/";
		getDriver().get(baseUrl);
		waitForElementBecomesVisible(logo, 55);
		return logo.isDisplayed();
	}
}
