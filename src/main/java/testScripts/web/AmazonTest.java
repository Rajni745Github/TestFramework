package testScripts.web;

import org.testng.annotations.Test;

import applicationPageLibrary.web.locatorsWeb.LoginPage;
import applicationPageLibrary.web.methods.ApplicationCommonMethods;

public class AmazonTest extends ApplicationCommonMethods
		implements LoginPage {

	@Test
	public void createResourceTest() {
		launchUrl("https://onlinebusiness.icbc.com/webdeas-ui/login;type=driver",true);

		waitForElement(login_button);
		login();
		}
}
