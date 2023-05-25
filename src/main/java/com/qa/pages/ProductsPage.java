package com.qa.pages;

import org.openqa.selenium.WebElement;

import com.qa.BaseTest;
import com.qa.MenuPage;

import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductsPage extends MenuPage{
	
   @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"container header\"]/android.widget.TextView") private WebElement productTitleTxt;

   @AndroidFindBy (xpath = "(//android.widget.TextView[@content-desc=\"store item text\"])[1]") private WebElement SLBTitle;
   @AndroidFindBy (xpath = "(//android.widget.TextView[@content-desc=\"store item price\"])[1]") private WebElement SLBPrice; 

    public String getTitle() {
	return getAttribute(productTitleTxt, "text");
}
    public String getSLBTitle() {
	return getAttribute(SLBTitle, "text");
}
    public String getSLBPrice() {
	return getAttribute(SLBPrice, "text");
}
    public ProductsDetailsPage pressSLBTitle() { 
	  click(SLBTitle, "Press SLB Title"); 
	  return new ProductsDetailsPage(); 
	  }
  
}
 
   
  
