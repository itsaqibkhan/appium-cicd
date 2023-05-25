package com.qa.pages;

import org.openqa.selenium.WebElement;

import com.qa.BaseTest;

import io.appium.java_client.pagefactory.AndroidFindBy;

public class LoginPage extends BaseTest{
	
	@AndroidFindBy(accessibility = "Username input field")
	private WebElement usernameTxtFld;

	@AndroidFindBy(accessibility = "Password input field")
	private WebElement passwordTxtFld;

	@AndroidFindBy(accessibility = "Login button")
	private WebElement loginBtn;

	@AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"generic-error-message\"]/android.widget.TextView")
	private WebElement errTxt;
		/*
		 * @AndroidFindBy (accessibility = "test-Username") private WebElement
		 * usernameTxtFld;
		 * 
		 * @AndroidFindBy (accessibility = "test-Password") private WebElement
		 * passwordTxtFld;
		 * 
		 * @AndroidFindBy (accessibility = "test-LOGIN") private WebElement loginBtn;
		 * 
		 * @AndroidFindBy (xpath =
		 * "//android.view.ViewGroup[@content-desc=\"generic-error-message\"]/android.widget.TextView")
		 * private WebElement errTxt;
		 */
	
	
public LoginPage enterUserName(String username) {
	clear(usernameTxtFld, "Clearing username field");
	sendKeys(usernameTxtFld, username, "Login with username- " + username);
	return this;
}
public LoginPage enterPassword(String password) {
	clear(passwordTxtFld, "Clearing password field");
	sendKeys(passwordTxtFld, password, "Login with password- " + password);
	return this;
}
public ProductsPage pressLoginBtn() {
	click(loginBtn, "Press login button");
	return new ProductsPage();
}
public String getErrTxt() {
	return getAttribute(errTxt, "text");
}

public ProductsPage login(String username, String password) {
	enterUserName(username);
	enterPassword(password);
	return pressLoginBtn();
}

}




