package com.qa;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.testng.listener.ExtentITestListenerAdapter;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

// @Listeners(ExtentITestListenerAdapter.class)        // To generate basic config spark report
public class BaseTest {
	
	protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal <AppiumDriver>();
	protected static ThreadLocal <Properties> props = new ThreadLocal <Properties>();
	protected static ThreadLocal <HashMap<String, String>> strings = new ThreadLocal <HashMap<String, String>>();
	protected static ThreadLocal <String> dateTime  = new ThreadLocal <String>();
	protected static ThreadLocal <String> deviceName = new ThreadLocal<String>();
	protected static ThreadLocal <String> platformName = new ThreadLocal<String>();
	private static AppiumDriverLocalService server;
	TestUtils utils = new TestUtils();       //TestUtils utils; Replaced command
	
	  public AppiumDriver getDriver() {
		  return driver.get(); 
		  } 
	  public void setDriver(AppiumDriver driver2) {
		   driver.set(driver2); 
		  } 
	  public Properties getProps() {
		  return props.get(); 
		  } 
	  
	  public void setProps(Properties props2) {
		   props.set(props2); 
		  } 
	  
	  public HashMap<String, String> getStrings() {
		  return strings.get();
	  }
	  
	  public void setStrings(HashMap<String, String> strings2) {
		  strings.set(strings2);
	  }
	  public String getDateTime() {
		  return dateTime.get();
	  }
	  public void setDateTime(String dateTime2) {
		  dateTime.set(dateTime2);
	  }
	  public String getDeviceName() {
		  return deviceName.get();
	  }
	  public void setDeviceName(String deviceName2) {
		  deviceName.set(deviceName2);
	  }
	  public String getPlatform() {
		  return platformName.get();
	  }
	  
	  public void setPlatform(String platform2) {
		  platformName.set(platform2);
	  }
	 
	public BaseTest() {
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	@BeforeMethod
	public void beforeMethod() {
		((CanRecordScreen) getDriver()).startRecordingScreen();                 //Start video recording
	}
	
	//stop video capturing and create *.mp4 file
	@AfterMethod
	public synchronized void afterMethod(ITestResult result) throws Exception {              //Synchronizing method by using synchronized keyword
		String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();
		
		if(result.getStatus()== 2) {                                     //Remove this condition if you need all the videos
	
			Map <String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();		
			String dirPath = "videos" + File.separator + params.get("platformName") + "_" + params.get("deviceName") 
			+ File.separator + getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();
			
			File videoDir = new File(dirPath);
			
			synchronized(videoDir){                                      //Synchronizing object by using synchronized keyword
				if(!videoDir.exists()) {
					videoDir.mkdirs();
				}	
			}
			FileOutputStream stream = null;
			try {
				stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
				stream.write(Base64.decodeBase64(media));
				stream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				if(stream != null) {
					stream.close();
				}
			}		
			
		}
		
	}
	
	
	  @BeforeSuite public void beforeSuite(){ 
	  ThreadContext.put("ROUTINGKEY", "ServersLogs");
	  server = getAppiumService();
	  server.start();
	  System.out.println("Appium server started for print");
	  utils.log().info("Appium server started");                     // causing error for the program
	  }
	  
	  @AfterSuite public void afterSuite() {
	  server.stop();
	  utils.log().info("Appium server stop");
	  }
	  
	  public AppiumDriverLocalService getAppiumServerDefault() { 
	  return AppiumDriverLocalService.buildDefaultService();
	  }
	  
	  
	  // To start appium server programatically
	  public AppiumDriverLocalService getAppiumService() {
		  HashMap<String, String> environment = new HashMap<String, String>();
			environment.put("PATH", "/home/user/.npm-global/bin:/usr/local/lib/nodejs/node-v18.13.0-linux-x64/bin:/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:/usr/X11/bin:$PATH:/snap/bin:/usr/lib/jvm/jdk-15.0.2/bin:/home/user/Android/Sdk/tools:/home/user/Android/Sdk/platform-tools:/home/user/Android/Sdk/tools/bin" + System.getenv("PATH"));
			environment.put("ANDROID_HOME", "/home/user/Android/Sdk");
		     return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
		            .usingDriverExecutable(new File("/usr/local/lib/nodejs/node-v18.13.0-linux-x64/bin/node"))
		            .withAppiumJS(new File("/usr/local/lib/nodejs/node-v18.13.0-linux-x64/lib/node_modules/appium/build/lib/main.js"))
		            .usingPort(4723)
		            .withArgument(GeneralServerFlag.SESSION_OVERRIDE)                            //To override the existing session
		            .withEnvironment(environment)
		            .withLogFile(new File("ServersLogs/server.log")) );                           // To output the log in this file                 // Because the system environment variable is not exposed to java
	  }
		
	
	
	
  @Parameters({"platformName", "platformVersion", "deviceName", "systemPort", "chromeDriverPort", "udid", "avd"})
  @BeforeTest
  public void beforeTest(String platformName, String platformVersion, String deviceName, String systemPort, String chromeDriverPort, String udid, String avd) throws Exception {         //Add @optional("androidOnly") to make any parameter optional 
//	  utils = new TestUtils();
	  setPlatform(platformName);
	  setDeviceName(deviceName);
	  setDateTime(utils.dateTime());
		InputStream inputStream = null;
		InputStream stringsis = null;
		AppiumDriver driver;
		
//		utils.log.info("This is a info");
//		utils.log.error("This is a error");
//		utils.log.debug("This is a debug");
//		utils.log.warn("This is a warn");
	
		//Log file path
		String strFile = "logs" + File.separator + platformName + "_" + deviceName;
		File logFile = new File(strFile);
		if (!logFile.exists()) {
			logFile.mkdirs();
		}
		//route logs to separate file for each thread
		ThreadContext.put("ROUTINGKEY", strFile);
		utils.log().info("log path: " + strFile);
		
		Properties props = new Properties();
	  try {
		  props = new Properties();
		  String propFileName = "config.properties";
		  String xmlFileName = "strings/strings.xml";
		  
		  inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		  props.load(inputStream);
		  setProps(props);
		  
		  stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
		  utils = new TestUtils();
		  setStrings(utils.parseStringXML(stringsis));
		  
		  
		  
		DesiredCapabilities caps = new DesiredCapabilities();
		//Device specific capabilities
		caps.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
		caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
		caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
		caps.setCapability(MobileCapabilityType.UDID, udid);
		
		//To launch the avd programaatically
     	caps.setCapability("avd", avd);          // To launch emulator
		caps.setCapability("newCommandTimeout", "180");   // Will wait 180 seconds for new commands before quiting the session Def;60
		
		caps.setCapability("sytemPort", systemPort);          
		caps.setCapability("chromeDriverPort", chromeDriverPort);
		
		
		
		
		caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("androidAutomationName"));
//		caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
//		caps.setCapability("appActivity",props.getProperty("androidAppActivity"));
 
		String appUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
//		String appUrl = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "app" + File.separator + "Android-MyDemoAppRN.1.3.0.build-244.apk";
		utils.log().info(appUrl);
		caps.setCapability(MobileCapabilityType.APP, appUrl);           //Will not work for windows
		URL url = new URL(props.getProperty("appiumUrl")); 		        //Setup appium communication with the server
		
		//Creating a new driver session with capabilities and appium server
	    driver = new AndroidDriver(url, caps);
	    utils.log().info("Session id:" + driver.getSessionId());
	    setDriver(driver); 
	  }
	  catch(Exception e) {
		  e.printStackTrace();
		  throw e;
		  }finally {
			  if(inputStream != null) {
				  inputStream.close();
			  }
			  if(stringsis != null) {
				  stringsis.close();
		  }
	}
	  
  }
  
  public void waitForVisisbility(WebElement ele) {
	  WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TestUtils.WAIT));                      //Explicit wait
	  wait.until(ExpectedConditions.visibilityOf(ele));
  }
  public void click(WebElement ele) {
	  waitForVisisbility(ele);
	  ele.click();
  }
  //Overloaded click method for extent report
  public void click(WebElement ele, String msg) { 
	  waitForVisisbility(ele);
	  utils.log().info(msg);
	  ExtentReport.getTest().log(Status.INFO, msg);
	  ele.click();
  }
  
  public void sendKeys(WebElement ele, String txt) {
	  waitForVisisbility(ele);
	  ele.sendKeys(txt);
  }
  //Overloaded method for extent report
  public void sendKeys(WebElement ele, String txt, String msg) {
	  waitForVisisbility(ele);
	  utils.log().info(msg);
	  ExtentReport.getTest().log(Status.INFO, msg);
	  ele.sendKeys(txt);
  }
  public void clear(WebElement ele, String msg) {
	  waitForVisisbility(ele);
	  utils.log().info(msg);
	  ExtentReport.getTest().log(Status.INFO, msg);
	  ele.clear();
  }
  public String getAttribute(WebElement ele, String attribute) {
	  waitForVisisbility(ele);
	  String msg = "Text fetched- " + ele.getAttribute(attribute);
	  utils.log().info(msg);
	  ExtentReport.getTest().log(Status.INFO, msg);
	  return ele.getAttribute(attribute);
  }
  
  public WebElement scrollToElement() {
	  return getDriver().findElement(AppiumBy.androidUIAutomator(
			  "new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
					  + "new UiSelector().description(\"product description\"));"));
  }
  
	
	  public void activateApp() { ((InteractsWithApps)                                          // To open the app 
	  getDriver()).activateApp(getProps().getProperty("androidAppPackage")); }
	  
	  public void terminateApp() { ((InteractsWithApps)                                         // To close the app 
	  getDriver()).terminateApp(getProps().getProperty("androidAppPackage")); }
	 
  
  @AfterTest
  public void afterTest() {
	  getDriver().quit();
  }

}
