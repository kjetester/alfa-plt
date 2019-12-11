package ru.alfabank.platform.helpers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.*;
import java.util.concurrent.*;

import static java.time.Duration.ofSeconds;

public class DriverSingleton {

	private static WebDriver driver;

	public static WebDriver getDriver() {
		if (driver == null) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		return driver;
	}

	public static void waitForElementBecomesVisible(WebElement element, int timeout) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		new WebDriverWait(driver, ofSeconds(timeout)).until(ExpectedConditions.visibilityOf(element));
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	public static void killDriver() {
		driver.close();
		driver.quit();
		driver = null;
	}
}
