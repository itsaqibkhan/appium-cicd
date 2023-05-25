package com.qa.tests;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumBy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.InputStream;
import java.lang.reflect.Method;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class LoginTests extends BaseTest{
	LoginPage loginPage;
	ProductsPage productsPage;
	SettingsPage settingsPage;
	TestUtils utils = new TestUtils();


	JSONObject loginUsers;
	
	@BeforeClass
	public void beforeClass() throws Exception {
		InputStream datais = null;
		  try {
			  String dataFileName = "data/loginUsers.json";
			  datais = getClass().getClassLoader().getResourceAsStream(dataFileName);
			  JSONTokener tokener = new JSONTokener(datais);
			  loginUsers = new JSONObject(tokener);
		  } catch(Exception e) {
			  e.printStackTrace();
			  throw e;
		  } finally {
			  if(datais != null) {
				  datais.close();
			  }
		  }
		  terminateApp();
		  activateApp();
	}
	

	@AfterClass
	public void afterClass() {
	}

	@BeforeMethod
	public void beforeMethod(Method m) {
		loginPage = new LoginPage();
		productsPage = new ProductsPage();
		settingsPage = new SettingsPage();
		utils.log().info("\n"+ "******** starting test ********" + m.getName() + "*******" + "\n");
	}

	@AfterMethod
	public void afterMethod() {
	}

	
	@Test
	public void invalidUserName() throws InterruptedException {
		
		settingsPage = productsPage.pressSettingsBtn();			
		Thread.sleep(3000);
        loginPage = settingsPage.pressLoginButton();
		
		loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));
		loginPage.enterPassword(loginUsers.getJSONObject("invalidUser").getString("password"));
		loginPage.pressLoginBtn();
		
		String actualErrTxt = loginPage.getErrTxt();
		String expectedErrTxt = getStrings().get("err_invalid_username_or_password");
		utils.log().info("actual error text - " + actualErrTxt + "\n"+"expected error text- " + expectedErrTxt );
		Assert.assertEquals(actualErrTxt, expectedErrTxt);
	}
	
	
	@Test
    public void sucessfulLogin() throws InterruptedException {

//		driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"open menu\"]/android.widget.ImageView")).click();
//		driver.findElement(AppiumBy.accessibilityId("menu item log in")).click();
		
		loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
		loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
		productsPage = loginPage.pressLoginBtn();
		
		Thread.sleep(3000);

		String actualProductTitle = productsPage.getTitle();
		utils.log().info("Actual Product Title - " + actualProductTitle );
		String expectedProductTitle = getStrings().get("product_title");
		Assert.assertEquals(actualProductTitle, expectedProductTitle);
	}

}
