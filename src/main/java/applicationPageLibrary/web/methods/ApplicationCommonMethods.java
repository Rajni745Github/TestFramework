package applicationPageLibrary.web.methods;

import applicationPageLibrary.web.locatorsWeb.*;
import base.CommonMethods;

public class ApplicationCommonMethods extends CommonMethods implements LoginPage {

	public void login() {
		logResults("Login", "info");
		input(username, "username");
		input(password_field, "pass");
		click(login_button);
	}
	
}
