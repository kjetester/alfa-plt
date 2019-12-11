package ru.alfabank.platform.tests;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.concurrent.*;

import static ru.alfabank.platform.helpers.DriverSingleton.getDriver;
import static ru.alfabank.platform.helpers.DriverSingleton.killDriver;

public class BaseTest {

	@BeforeSuite
	public void setUp() {
		getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		getDriver().manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		getDriver().manage().window().setSize(new Dimension(1200, 800));
		getDriver().manage().window().setPosition(new Point(0,0));
	}

	@AfterSuite
	public void tearDown() {
		if (getDriver() != null) {
			killDriver();
		}
	}
}
