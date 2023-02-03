package testScripts.mobile;

import org.testng.annotations.Test;

import applicationPageLibrary.mobile.locatorsMobile.HomePage;
import applicationPageLibrary.web.locatorsWeb.LoginPage;
import applicationPageLibrary.web.methods.ApplicationCommonMethods;

/**
 * @author Rajni
 */
public class TestLoginPage extends ApplicationCommonMethods implements HomePage,LoginPage{
	
	@Test
	public void loginTech() {
		
		openMobileApp("android");
		waitForElement(loginButton);
		click(loginButton);
		verifyElementPresent(login_button);
		
	}
}
