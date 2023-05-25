package com.qa.tests;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.google.gson.JsonObject;
import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsDetailsPage;
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

public class ProductsTests extends BaseTest{
	LoginPage loginPage;
	ProductsPage productsPage;
	SettingsPage settingsPage;
	ProductsDetailsPage productsDetailsPage;
//	TestUtils utils;
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
//		  terminateApp();
//		  activateApp();
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
	
	
	  @Test public void validateProductOnProductsPage() throws InterruptedException
	  {
	  
			SoftAssert sa = new SoftAssert();

            settingsPage = productsPage.pressSettingsBtn();			
			Thread.sleep(3000);
            loginPage = settingsPage.pressLoginButton();

			productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"),
					loginUsers.getJSONObject("validUser").getString("password"));

			String SLBTitle = productsPage.getSLBTitle();
			sa.assertEquals(SLBTitle, getStrings().get("products_page_slb_title"));
			utils.log().info("Expected SLB title is- " + SLBTitle + "\n" + "Actual SLB Title is-"
					+ getStrings().get("products_page_slb_title"));

			String SLBPrice = productsPage.getSLBPrice();
			sa.assertEquals(SLBPrice, getStrings().get("products_page_slb_price"));
			utils.log().info("Expected SLB Price is- " + SLBPrice + "\n" + "Actual SLB Price is-"
					+ getStrings().get("products_page_slb_price"));

			sa.assertAll();
		}
	 
	
	@Test
	public void validateProductOnProductsDetailsPage() throws InterruptedException {
		
		SoftAssert sa = new SoftAssert();
				
		productsDetailsPage = productsPage.pressSLBTitle();
		
		Thread.sleep(3000);

		String SLBTitle = productsDetailsPage.getSLBTitle();
		sa.assertEquals(SLBTitle, getStrings().get("products_details_page_slb_title"));
		utils.log().info("Expected SLB Price at details screen is- " + SLBTitle + "\n" + "Actual SLB Price  at details screen is-"
				+ getStrings().get("products_details_page_slb_title"));
		
	
		String SLBPrice = productsDetailsPage.getSLBPrice();
		sa.assertEquals(SLBPrice, getStrings().get("products_details_page_slb_price"));
		utils.log().info("Expected SLB Price at details screen is- " + SLBPrice + "\n" + "Actual SLB Price at details screen is-"
				+ getStrings().get("products_details_page_slb_price"));
		
		productsDetailsPage.scrollToSLBDescription();
        Thread.sleep(5000);
		
		String SLBDesc = productsDetailsPage.getSLBDesc();
		sa.assertEquals(SLBDesc, getStrings().get("products_details_page_slb_description"));
		utils.log().info("Expected SLB Price at details screen is- " + SLBDesc + "\n" + "Actual SLB Price at details screen is-"
				+ getStrings().get("products_details_page_slb_description"));
		
		sa.assertAll();
	}
}
