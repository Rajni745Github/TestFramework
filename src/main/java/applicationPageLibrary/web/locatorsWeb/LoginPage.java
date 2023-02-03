package applicationPageLibrary.web.locatorsWeb;

import org.openqa.selenium.By;

public interface LoginPage {
	By username=By.id("username");
	By password_field=By.id("password");
	By login_button=By.xpath("//button[contains(text(),'Log in')]");
}
