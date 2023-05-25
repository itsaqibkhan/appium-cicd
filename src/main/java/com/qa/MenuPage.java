package com.qa;

import org.openqa.selenium.WebElement;

import com.qa.pages.SettingsPage;

import io.appium.java_client.pagefactory.AndroidFindBy;

public class MenuPage extends BaseTest {
	
    @AndroidFindBy (xpath  = "//android.view.ViewGroup[@content-desc=\"open menu\"]/android.widget.ImageView") 
    private WebElement settingBtn;

    public SettingsPage pressSettingsBtn() {
    	click(settingBtn);
    	return new SettingsPage();
    }
	
}
