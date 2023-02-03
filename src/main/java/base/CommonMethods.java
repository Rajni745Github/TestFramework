package base;

import java.io.File;


import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumServiceBuilder;

/**
 * Contains the common methods which call selenium and appium
 * methods
 * 
 * @author Rajni
 *
 */

public class CommonMethods extends SetupCore {

	static int count = 0;
	public static HashMap<String, String> locatorMapObj = new HashMap<String, String>();
	SoftAssert softAssertsObj = new SoftAssert();

	/**
	 * Launch web application
	 * @param url
	 * @param headless
	 */
	public void launchUrl(String url, Boolean headless) {
		logResults("Opening Chrome with URL:" + url, "info");

		if (driverObj != null)
			previousDriverObj = driverObj;

		ChromeOptions options = new ChromeOptions();
		driverObj = new ChromeDriver(options);
		driverObj.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driverObj.manage().window().maximize();
		driverObj.get(url);
		webdriverWaitObj = new WebDriverWait(driverObj, 20);

	}
	
	/**
	 * openMobileApp
	 * @param OperatingSystem
	 */
	public void openMobileApp(String OperatingSystem) {

		logResults("Opening " + OperatingSystem + " Mobile application", "info");
		String appiumJSPath = propertiesFileObj.getProperty("appiumPath");

		AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder().withAppiumJS(new File(appiumJSPath));

		serviceBuilder.usingAnyFreePort(); 

		serverObj = AppiumDriverLocalService.buildService(serviceBuilder);
		serverObj.start();

		previousDriverObj = (driverObj != null) ? driverObj : null;

		DesiredCapabilities capabilities = new DesiredCapabilities();
		if (OperatingSystem.equalsIgnoreCase("android")) {
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
			driverObj = new AndroidDriver<WebElement>(serverObj.getUrl(), capabilities);
		} else {
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
			driverObj = new IOSDriver<WebElement>(serverObj.getUrl(), capabilities);
		}

		driverObj.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		webdriverWaitObj = new WebDriverWait(driverObj, 20);
	}

	/**
	 * 
	 * @return
	 */
	public String takeScreenShot() {
		File scrFile = ((TakesScreenshot) driverObj).getScreenshotAs(OutputType.FILE);
		String fn = System.getProperty("user.dir") + "/target/screenshot" + count + ".jpg";
		try {
			FileUtils.copyFile(scrFile, new File(fn));
			count++;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fn;
	}


	public void input(By loc, String text) {
		try {
			driverObj.findElement(loc).sendKeys(text);
			logResults("Input text:'" + text + "' on Element:" + (loc), "pass");
		} catch (Exception e) {
			logResults("Input text:'" + text + "' on Element:" + (loc), "fail--" + e.getMessage());
		}
	}

	public void waitForElement(By loc) {
		try {
			webdriverWaitObj.until(ExpectedConditions.presenceOfElementLocated(loc));
			logResults("waitForElement:" + (loc), "pass");
		} catch (Exception e) {
			logResults("waitForElement:" + (loc), "fail--" + e.getMessage());
		}
	}

	public void waitForElementClickable(By loc) {
		try {
			webdriverWaitObj.until(ExpectedConditions.elementToBeClickable(loc));
			logResults("waitForElementClickable:" + (loc), "pass");
		} catch (Exception e) {
			logResults("waitForElementClickable:" + (loc), "fail--" + e.getMessage());
		}
	}


	public boolean isPresent(By loc) {
		try {
			if (driverObj.findElements(loc).size() > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}

	}

	public void click(By loc) {
		try {
			driverObj.findElement(loc).click();
			logResults("Click on " + (loc), "pass");
		} catch (Exception e) {
			logResults("Click on " + (loc), "fail--" + e.getMessage());
		}
	}


	public void verifyElementPresent(By loc) {
		try {
			if (isPresent(loc)) {
				logResults("Element is present" + (loc), "pass");
			} else
				logResults("Element was not present.", "info");

		} catch (Exception e) {
			logResults("verifyElementPresent " + (loc), "fail--" + e.getMessage());
		}
	}

	@AfterTest(alwaysRun = true)
	public void afterTest() {
		try {
			softAssertsObj.assertAll();
		} catch (AssertionError e) {
			softAssertsObj = null; 
			Throwable throwable = new Throwable("Test fail");
			Reporter.getCurrentTestResult().setThrowable(throwable);
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterclass() {
		try {

			if (driverObj != null) {
				driverObj.quit();
			}

			if (previousDriverObj != null) {
				previousDriverObj.quit();
			}

			if (serverObj != null) {
				serverObj.stop();
			}

		} catch (UnhandledAlertException e) {
		}
	}

	public void logResults(String msg, String result) {
		try {
			if (driverObj != null)
				Reporter.log(msg + "--" + takeScreenShot() + "--" + result, true);
			else
				Reporter.log(msg + "--" + "noscreen" + "--" + result, true);

			if (result.contains("fail")) {
				if (Thread.currentThread().getStackTrace()[2].getMethodName().contains("click"))
					Assert.assertTrue(false);
				else
					softAssertsObj.assertTrue(false, "Fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
