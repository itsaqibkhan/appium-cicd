package com.qa.pages;

import org.openqa.selenium.WebElement;

import com.qa.MenuPage;

import io.appium.java_client.pagefactory.AndroidFindBy;

public class ProductsDetailsPage extends MenuPage{
	
   @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"container header\"]/android.widget.TextView") private WebElement SLBTitle;
   @AndroidFindBy (xpath = "//android.widget.TextView[@content-desc=\"product price\"]") private WebElement SLBPrice; 
   @AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"gray circle\"]/android.view.ViewGroup") private WebElement SLBColour; 
   @AndroidFindBy (xpath = "//android.widget.TextView[@content-desc=\"product description\"]") private WebElement SLBDescription; 

   
   
    public String getSLBTitle() {
	return getAttribute(SLBTitle, "text");
}
    
    public String getSLBPrice() {
	return getAttribute(SLBPrice, "text");
}
    
    public void SLBColour() {
    	click(SLBColour, "Press SLB colour button");
    }
    
    public String getSLBDesc() {
	return getAttribute(SLBDescription, "text");
}
    public ProductsDetailsPage scrollToSLBDescription() {
     scrollToElement();
     return this;
}
  
}