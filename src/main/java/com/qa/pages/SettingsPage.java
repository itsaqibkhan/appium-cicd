package com.qa.pages;

import org.openqa.selenium.WebElement;

import com.qa.BaseTest;

import io.appium.java_client.pagefactory.AndroidFindBy;

public class SettingsPage extends BaseTest{
	
	 @AndroidFindBy (xpath  = "//android.view.ViewGroup[@content-desc=\"menu item log in\"]") 
	 private WebElement loginBtn;

	    public LoginPage pressLoginButton() {
	    	click(loginBtn, "Press settings button");
	    	return new LoginPage();
	    } 
}
