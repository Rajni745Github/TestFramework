package base;

import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openqa.selenium.WebDriver;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;

public class SetupCore {

	protected WebDriver driverObj;
	protected WebDriver previousDriverObj;
	protected AppiumDriverLocalService serverObj;
	protected WebDriverWait webdriverWaitObj;
	
	Properties propertiesFileObj;

	@BeforeSuite(alwaysRun = true)
	public void driverSetup() {

		try {
			InputStream input = new FileInputStream("src/main/java/utilities/config.properties");
			propertiesFileObj = new Properties();
			propertiesFileObj.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		WebDriverManager.chromedriver().setup();
	}

	public WebDriver getDriver() {
		return driverObj;
	}
}