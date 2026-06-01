
package com.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.userConfig.UserConfig;

import cucumber.api.DataTable;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy.ByAccessibilityId;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;

public class CommonLibrary {

	public static AppiumDriver<MobileElement> driver3 = null;
	public static AndroidDriver<MobileElement> driver2 = null;
	public static IOSDriver<MobileElement> driver1 = null;
	public static WebDriver driver = null;
	public static WebElement element = null;
	public static WebElement element1 = null;
	public static WebElement element2 = null;
	static WebDriverWait browserWithElementWait = null;
	static long t1 = 0;
	static long t2 = 0;
	static long timeTaken = 0;
	String actual = null;
	public static String expecte = null;
	public static String systemUsername = null;
	public static Configuration config = null;
	static String destDir;
	static DateFormat dateFormat;
	static Dimension size;
	public static String driverpath = System.getProperty("user.dir");
	public static String Latesprojectpath = driverpath.replaceAll("\\\\", "\\\\\\\\");
	public static String chromepathLatest = Latesprojectpath + File.separator + "\\Killdriver" + File.separator
			+ "\\Killchrome1.bat";
	public static FileWriter reportFile = null;
	static long pageStartTime = -1;
	static BrowserMobProxy proxy = new BrowserMobProxyServer();

	public CommonLibrary() throws ConfigurationException, IOException {
		ConfigurationFactory factory = new ConfigurationFactory("config/config.xml");
		config = factory.getConfiguration();
	}

	/*
	 * 
	 * Method to Quit application
	 * 
	 */

	public static void closeApplication() throws InterruptedException {

		driver.quit();
		// driver.close();

	}

	public static void initReportFile() throws IOException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy-hh-mm-ss");
			Date date = new Date();

			String reportFileFullPath = UserConfig.reportDir + UserConfig.reportFile + "_" + sdf.format(date) + ".csv";
			File file = new File(reportFileFullPath);
			System.out.print(reportFileFullPath);
			file.getParentFile().mkdirs();
			file.createNewFile();
			System.out.println(reportFileFullPath);
			reportFile = new FileWriter(reportFileFullPath, true);
			reportFile.append("timeStamp,elapsed,label,responseCode,responseMessage,threadName,"
					+ "dataType,success,failureMessage,bytes,grpThreads,allThreads,Latency,IdleTime");
			reportFile.append("\n");

			reportFile.flush();

		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	public static void logPerfMetrics(ReportObject ro1) throws IOException {
		// reportFile=new FileWriter("Report");

		reportFile.append(ro1.toString());
		reportFile.append("\n");
		reportFile.flush();
	}

	public static void takeScreenShot() {
		// Set folder name to store screenshots.
		destDir = "Screenshots";
		// Capture screenshot.
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// Set date format to set It as screenshot file name.
		dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

		// Create folder under project with name "screenshots" provided to
		// destDir.
		new File(destDir).mkdirs();
		// Set file name using current date time.
		String destFile = dateFormat.format(new Date()) + ".png";

		try {
			// Copy paste file at destination folder location
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void uploadFile() {
		WebElement uploadBtn = driver.findElement(By.xpath("//*[@id=\"qrcode\"]"));
		File file = new File("./Screenshots/18-Dec-2024.png");
		System.out.println(file.getAbsolutePath());
		uploadBtn.sendKeys(file.getAbsolutePath());
	}

	public static void dynamicDataValidation_regExpression(String pattern, String objectProperty) {

		element = getElementByProperty(objectProperty, driver);
		String actualText = element.getText();
		try {
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(actualText);
			if (m.find()) {
				System.out.println("The format of value is as expected " + m.group(0));

			} else {

				throw new Exception("NO MATCHING DATA FOUND");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void Assert(boolean Method, String Result) throws Exception {
		boolean e1 = Method;

		if (e1 == true) {
			System.out.println(Result);
		} else {

			CommonLibrary.closeApplication();
			throw new Exception(Result + ": False");

		}
	}

	public static void Soft_Assertion(boolean Method, String Result) {
		SoftAssert s_assert = new SoftAssert();
		s_assert.assertEquals(true, Method, Result);
	}

	public static void Hard_Assertion(boolean Method, String Result) {

		Assert.assertEquals(true, Method, Result);
	}

	/*
	 * 
	 * Method to Launch Application
	 * 
	 */

	public static void AppLaunch() throws Exception {

		systemUsername = System.getProperty("user.name");
		String userProfile = "/Users/" + systemUsername + "/Library/Application Support/Google/Chrome/default";
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/Driver/chromedriver");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-data-directory=" + userProfile);
		options.addArguments("--profile-directory=Default");
		options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
		Object getConfigUrl = config.getProperty("appURL");
		String URL = getConfigUrl.toString();
		driver.get(URL);

		initReportFile();

	}

	public static void ZxingLaunch() throws Exception {

		systemUsername = System.getProperty("user.name");
		String userProfile = "/Users/" + systemUsername + "/Library/Application Support/Google/Chrome/default";
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/Driver/chromedriver");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-data-directory=" + userProfile);
		options.addArguments("--profile-directory=Default");
		options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
//		Object getConfigUrl = config.getProperty("zxingURL");
//		String URL = getConfigUrl.toString();
		driver.get("https://zxing.appspot.com/generator");

		initReportFile();

	}

	public static void QrDecoderLaunch() throws Exception {

		systemUsername = System.getProperty("user.name");
		String userProfile = "/Users/" + systemUsername + "/Library/Application Support/Google/Chrome/default";
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/Driver/chromedriver");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-data-directory=" + userProfile);
		options.addArguments("--profile-directory=Default");
		options.addArguments("--start-maximized");
		driver = new ChromeDriver(options);
//		Object getConfigUrl = config.getProperty("qrDecoderURL");
//		String URL = getConfigUrl.toString();
		driver.get("https://m28dev.github.io/qrdecoder/");

		initReportFile();

	}

	public static boolean isElementPresentVerifyClick(String objectProperty1, String objectproperty, String pageName)
			throws Throwable {
		boolean isVerifiedAndClicked = false;
		browserWithElementWait = new WebDriverWait(driver, 50);

		try {
			element = getElementByProperty(objectProperty1, driver);
			browserWithElementWait.until(ExpectedConditions.elementToBeClickable(element));
			if (element != null) {
				// JavascriptExecutor executor = (JavascriptExecutor)driver;
				// executor.executeScript("arguments[0].click(true);", element);
				proxy.newHar("pageName");
				long startTime = System.currentTimeMillis();
				element.click();
				long endTime = System.currentTimeMillis();
				long timeTaken = (endTime - startTime);
				System.out.println("Total time taken on " + pageName + " :" + timeTaken + "ms");
				Har har = proxy.endHar();
				System.out.println("***********************************");
				String harFileName = System.getProperty("user.dir") + File.separator + "har" + File.separator + pageName
						+ ".har";
				File file = new File(harFileName);
				System.out.println(har.toString());
				har.writeTo(file);
				System.out.println("***********************************");
				// PageLoadEvaluation(pageName, startTime, timeTaken);
				Thread.sleep(1000);
				isVerifiedAndClicked = true;
			} else {
				throw new Exception("Object Couldn't be retrieved and clicked");
			}
		} catch (Exception e) {
			element = null;
			e.printStackTrace();
		}
		return isVerifiedAndClicked;
	}

	public static void closeReportFile() throws IOException {
		reportFile.flush();
		reportFile.close();
	}

	public static boolean isElementPresentVerifyClickAuthentication(String objectProperty, String pageName)
			throws Throwable {

		boolean isVerifiedAndClicked = false;
		browserWithElementWait = new WebDriverWait(driver, 50);

		try {
			element = getElementByProperty(objectProperty, driver);
			browserWithElementWait.until(ExpectedConditions.elementToBeClickable(element));
			if (element != null) {
				// JavascriptExecutor executor = (JavascriptExecutor)driver;
				// executor.executeScript("arguments[0].click(true);", element);
				proxy.newHar("pageName");
				long startTime = System.currentTimeMillis();
				element.click();
				// isElementPresentVerification(com.photon.UIconstants.Horizon_HomeConstants.app_header);
				long endTime = System.currentTimeMillis();
				long timeTaken = (endTime - startTime);
				System.out.println("Total time taken on " + pageName + " :" + timeTaken + "ms");
				Har har = proxy.endHar();
				System.out.println("***********************************");
				String harFileName = System.getProperty("user.dir") + File.separator + "har" + File.separator + pageName
						+ ".har";
				File file = new File(harFileName);
				System.out.println(har.toString());
				har.writeTo(file);
				System.out.println("***********************************");
				// PageLoadEvaluationAuthentication(pageName, startTime,
				// timeTaken);
				Thread.sleep(1000);
				isVerifiedAndClicked = true;
			} else {
				throw new Exception("Object Couldn't be retrieved and clicked");
			}
		} catch (Exception e) {
			element = null;
			e.printStackTrace();
		}
		return isVerifiedAndClicked;
	}

	public static void PageLoadEvaluationAuthentication(String pageName, long TimeStamp, long Elapsed)
			throws Throwable {
		try {

			// element =
			// getElementByProperty(com.photon.UIconstants.Horizon_HomeConstants.Authenticator_logo,
			// driver);

			// MobileElement header =
			// driver.findElementByXPath(com.photon.UIconstants.Horizon_HomeConstants.Authenticator_logo);
			// String currpagename=header.getAttribute("name");
			String currpagename = pageName;
			ReportObject ro = new ReportObject(TimeStamp, Elapsed, currpagename, "Horizon Regression Suite", 0, 0);

			logPerfMetrics(ro);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// driver.context("NATIVE_APP");
			// CommonLibrary.switchNativeview();
		}
	}

	public static void killChromeSession() throws Throwable {
		String os = System.getProperty("os.name");
		if (os != null && os.contains("Mac")) {
			String target = "Killdriver/Killchrome2.sh";
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(target);
			proc.waitFor();
			StringBuffer output = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\t");
				System.out.println("#" + output);
			}
		} else if (os != null && os.contains("Window")) {

			Runtime.getRuntime().exec("cmd /c start " + chromepathLatest);
		} else {
			System.out.println("OS is not supported to kill Chrome session");
		}
	}

	/*
	 * Method for Text validation
	 */

	public static boolean text_Validation(String objectProperty, String expectedText) {
		boolean Textvalidation = false;
		try {
			element = getElementByProperty(objectProperty, driver);
			String actualText = element.getText();
			if (actualText.equalsIgnoreCase(expectedText)) {
				Textvalidation = true;
				System.out.println(" Text expected and actual text are Same:" + actualText);
			} else {
				System.out.println(" Text expected and actual text are not Same:");
				System.out.println(" Text - Actual : " + actualText);
				System.out.println(" Text -Expected : " + expectedText);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return Textvalidation;
	}

	// Added temporarily
	public static boolean text_ValidationUsingContains(String objectProperty, String expectedText) {
		boolean Textvalidation = false;
		try {
			element = getElementByProperty(objectProperty, driver);
			String actualText = element.getText();
			if (actualText.contains(expectedText)) {
				Textvalidation = true;
				System.out.println(" Text expected and actual text are Same:" + actualText);
			} else {
				System.out.println(" Text expected and actual text are not Same:");
				System.out.println(" Text - Actual : " + actualText);
				System.out.println(" Text -Expected : " + expectedText);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return Textvalidation;
	}

	/*
	 * Method for Image Accessibility
	 */

	public static void accessibilityValidation(String objectProperty, String Text, String imgName) {
		try {
			element = getElementByProperty(objectProperty, driver);
			String alt = element.getAttribute("alt");
			if (alt.equalsIgnoreCase(Text)) {
				System.out.println(imgName + " Image accessibility expected and actual name are Same");
			} else {
				System.out.println(imgName + " Image accessibility expected and actual name are not Same");
				System.out.println("Accessibility-Actual : " + alt);
				System.out.println("Accessibility-Expected : " + Text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/*
	 * Method for Link Text Validation
	 */

	public static void linkText_Validation(String objectProperty, String Text) {
		try {
			element = getElementByProperty(objectProperty, driver);
			String linkText = element.getText();

			if (linkText.equalsIgnoreCase(Text)) {
				System.out.println("Link Text expected and actual text are Same");
			} else {
				System.out.println("Link Text expected and actual text are not Same");
				System.out.println("Link Text - Actual : " + linkText);
				System.out.println("Link Text -Expected : " + Text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/*
	 * Method for performance validation
	 */

	public static void performanceValidation(long t1, long t2, String perf_Expected)
			throws Exception, IOException, InterruptedException {
		try {
			timeTaken = (t2 - t1) / 1000;
			long expected = Long.parseLong(perf_Expected);
			if (timeTaken <= expected) {
			} else {
				System.out.println(" Page Load Time is High while Navigating to ::" + driver.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/*
	 * Method for identifying Web-element by ID/CLASS/NAME/XPATH
	 */

	public static WebElement getElementByProperty(String objectProperty, WebDriver webDriver) {
		element = null;

		String propertyType = null;
		WebDriverWait browserWithElementWait = null;
		try {
			// if (browserWithElementWait == null) {
			// browserWithElementWait = new WebDriverWait(webDriver, 80);
			// }
			browserWithElementWait = new WebDriverWait(webDriver, 30);
			propertyType = StringUtils.substringAfter(objectProperty, "~");
			objectProperty = StringUtils.substringBefore(objectProperty, "~");
			if (propertyType.equalsIgnoreCase("ACCESS")) {
				element = browserWithElementWait.until(ExpectedConditions
						.elementToBeClickable(driver.findElement(ByAccessibilityId.AccessibilityId(objectProperty))));
				highlightElement(element, webDriver);
			} else if (propertyType.equalsIgnoreCase("CSS")) {
				element = browserWithElementWait
						.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(objectProperty))));
				highlightElement(element, webDriver);
			} else if (propertyType.equalsIgnoreCase("ID")) {
				element = browserWithElementWait
						.until(ExpectedConditions.visibilityOfElementLocated((By.id(objectProperty))));
				// highlightElement(webElement, browser);
			} else if (propertyType.equalsIgnoreCase("NAME")) {
				element = browserWithElementWait
						.until(ExpectedConditions.visibilityOf(driver.findElement(By.name(objectProperty))));
				highlightElement(element, webDriver);
			} else if (propertyType.equalsIgnoreCase("LINKTEXT")) {
				element = browserWithElementWait
						.until(ExpectedConditions.visibilityOf(driver.findElement(By.linkText(objectProperty))));
				highlightElement(element, webDriver);
			} else if (propertyType.equalsIgnoreCase("CLASS")) {
				element = browserWithElementWait
						.until(ExpectedConditions.visibilityOf(driver.findElement(By.className(objectProperty))));
				highlightElement(element, webDriver);
			} else {
				element = browserWithElementWait
						.until(ExpectedConditions.visibilityOfElementLocated((ByXPath.xpath(objectProperty))));
				// highlightElement(webElement, browser); //to avoid overlapping
				// of elements commented the following line of code
				// -Angeline-03AUG2015
			}
		} catch (Exception e) {

		}

		return element;
	}

	public static void clickWebElement(String objectProperty) {
		String driverPath = System.getProperty("user.dir") + "/drivers" + File.separator + "chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driverPath);

		WebDriver webDriver = new ChromeDriver();

		String propertyType = null;
		WebDriverWait browserWithElementWait = null;
		try {
			if (browserWithElementWait == null) {
				browserWithElementWait = new WebDriverWait(webDriver, 5);
			}
			propertyType = StringUtils.substringAfter(objectProperty, "~");
			objectProperty = StringUtils.substringBefore(objectProperty, "~");
			if (propertyType.equalsIgnoreCase("CSS")) {
				driver.findElement(By.cssSelector(objectProperty)).click();
			} else if (propertyType.equalsIgnoreCase("ID")) {

				driver.findElement(By.id(objectProperty)).click();

			} else if (propertyType.equalsIgnoreCase("NAME")) {
				driver.findElement(By.name(objectProperty)).click();
			} else if (propertyType.equalsIgnoreCase("LINKTEXT")) {
				driver.findElement(By.linkText(objectProperty)).click();
			} else if (propertyType.equalsIgnoreCase("CLASS")) {
				driver.findElement(By.className(objectProperty)).click();
			} else {
				driver.findElement(By.id(objectProperty)).click();
			}
		} catch (Exception e) {

		}

	}

	/*
	 * Common Methods for Element Verification
	 */

	public static boolean isElementPresentVerification(String objectProperty) throws Exception {
		boolean isElementPresent = false;

		try {
			element = getElementByProperty(objectProperty, driver);
			// browserWithElementWait.until(ExpectedConditions.visibilityOf(element));
			if (element != null) {
				isElementPresent = true;
				// t2=System.currentTimeMillis();
			} else {
				// throw new Exception("Object Couldn't be retrieved and
				// verified");
			}
			// Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return isElementPresent;
	}

	public static boolean isElementNotPresentVerification(String objectProperty) throws Exception {
		boolean isElementPresent = true;

		try {
			element = getElementByProperty(objectProperty, driver);
			// browserWithElementWait.until(ExpectedConditions.visibilityOf(element));
			if (element != null) {
				isElementPresent = false;
				// t2=System.currentTimeMillis();
			} else {
				// throw new Exception("Object Couldn't be retrieved and
				// verified");
			}
			// Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return isElementPresent;
	}

	public static boolean isElementSelected(String objectProperty) throws Exception {
		boolean isElementSelected = false;
		try {
			element = getElementByProperty(objectProperty, driver);
			// browserWithElementWait.until(ExpectedConditions.visibilityOf(element));
			if (element.isSelected()) {
				isElementSelected = true;
				// t2=System.currentTimeMillis();
			} else {
				throw new Exception("Given Element not selected");
			}
			// Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return isElementSelected;

	}

	public static boolean isElementDisplayed(String objectProperty) throws Exception {
		boolean isElementDisplayed = false;
		try {
			element = getElementByProperty(objectProperty, driver);
			browserWithElementWait.until(ExpectedConditions.visibilityOf(element));
			if (element.isEnabled()) {
				isElementDisplayed = true;
				t2 = System.currentTimeMillis();
			} else {
				throw new Exception("Given Element is avilable");
			}
			// Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return isElementDisplayed;

	}

	// To check the Element is Enable and disabled

	public static boolean isElementEnabled(String objectProperty, String elementState) throws Exception {
		boolean isElementEnable = false;
		try {
			element = getElementByProperty(objectProperty, driver);
			String att = element.getAttribute("enabled");
			if (elementState.equals("enabled")) {
				if (att.equals("true")) {
					isElementEnable = true;
					System.out.println("Element is :" + elementState);
				} else {
					throw new Exception("Given Element is avilable");
				}
			}

			if (elementState.equals("disabled")) {
				if (att.equals("false")) {
					isElementEnable = true;
					System.out.println("Element is :" + elementState);
					// t2=System.currentTimeMillis();
				} else {
					throw new Exception("Given Element is avilable");
				}
			}
			// Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return isElementEnable;

	}

	/*
	 * Created this method to verify if an element is not clickable and this in turn
	 * verify that the element is also not editable
	 * 
	 */
	public static boolean isElementNotClickable(String objectProperty, String elementState) throws Exception {
		boolean isElementclickable = false;
		try {
			element = getElementByProperty(objectProperty, driver);
			String att = element.getAttribute("clickable");
			if (elementState.equals("clickable")) {
				if (att.equals("false")) {
					isElementclickable = true;
					System.out.println("Element is Not: " + elementState);
				} else {
					throw new Exception("Given Element clickable");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return isElementclickable;

	}

	/*
	 * Method for Entering text in to a field
	 */

	public static boolean clearAndEnterText(String objectProperty, String Text) throws IOException {
		boolean isTextEnteredResult = false;
		// String search_keyword
		// =getXLSTestData(dataMap.get("InputFileName").get(0),
		// dataMap.get("SheetName").get(0), dataMap.get("RowId").get(0),
		// "SearchValue");

		try {
			if ("-".equals(Text)) {
				// ignore this field
				isTextEnteredResult = true;
			} else {
				WebElement textBox = getElementByProperty(objectProperty, driver);
				textBox.clear();
				Thread.sleep(1000);

				/*
				 * Actions action=new Actions(driver);
				 * action.sendKeys(Text).sendKeys(Keys.ENTER).build().perform();
				 */
				textBox.sendKeys(Text);

				isTextEnteredResult = true;

			}
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return isTextEnteredResult;
	}

	public static boolean keywordAndEnterText(String objectProperty, String Text) throws IOException {
		boolean isTextEnteredResult = false;
		// String search_keyword
		// =getXLSTestData(dataMap.get("InputFileName").get(0),
		// dataMap.get("SheetName").get(0), dataMap.get("RowId").get(0),
		// "SearchValue");

		try {
			if ("-".equals(Text)) {
				// ignore this field
				isTextEnteredResult = true;
			} else {
				WebElement textBox = getElementByProperty(objectProperty, driver);
				textBox.clear();
				Thread.sleep(1000);

				/*
				 * Actions action=new Actions(driver);
				 * action.sendKeys(Text).sendKeys(Keys.ENTER).build().perform();
				 */
				textBox.sendKeys(Text);
				textBox.sendKeys(Keys.RETURN);

				isTextEnteredResult = true;

			}
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return isTextEnteredResult;
	}

	/*
	 * Method for Highlight the Elements
	 */
	public static void highlightElement(WebElement element, WebDriver webDriver) {
		for (int i = 0; i < 1; i++) {
			JavascriptExecutor js = (JavascriptExecutor) webDriver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
					"color: black; border: 3px solid black;");
		}
	}

	/*
	 * Method for Click Element if available
	 */

	public static boolean isElementPresentVerifyClick(String objectProperty) throws InterruptedException {
		boolean isVerifiedAndClicked = false;

		browserWithElementWait = new WebDriverWait(driver, 60);
		try {
			element = getElementByProperty(objectProperty, driver);
			browserWithElementWait.until(ExpectedConditions.elementToBeClickable(element));

			if (element != null) {

				// JavascriptExecutor executor = (JavascriptExecutor)driver;
				// executor.executeScript("arguments[0].click(true);", element);
				element.click();
				isVerifiedAndClicked = true;

				// browserWithElementWait = new WebDriverWait(driver,1);
				// Thread.sleep(5000);
			} else {
				throw new Exception("Object Couldn't be retrieved and clicked");
			}
		} catch (Exception e) {
			element = null;
		}
		return isVerifiedAndClicked;
	}

	// switch to frame
	public static boolean switchtoFrame(String frameid) throws InterruptedException {
		boolean switchtoframe = false;

		browserWithElementWait = new WebDriverWait(driver, 60);
		try {

			browserWithElementWait.until(ExpectedConditions.elementToBeClickable(element));

			if (frameid != null) {

				// JavascriptExecutor executor = (JavascriptExecutor)driver;
				// executor.executeScript("arguments[0].click(true);", element);
				driver.switchTo().frame(frameid);
				switchtoframe = true;

				// browserWithElementWait = new WebDriverWait(driver,1);
				// Thread.sleep(5000);
			} else {
				throw new Exception("Frame Couldn't be retrieved");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return switchtoframe;
	}

	// switch to default content
	public static boolean switchtodefault() throws Exception {
		boolean switchtoframe = false;

		try {
			// JavascriptExecutor executor = (JavascriptExecutor)driver;
			// executor.executeScript("arguments[0].click(true);", element);
			driver.switchTo().defaultContent();
			switchtoframe = true;

			// browserWithElementWait = new WebDriverWait(driver,1);
			// Thread.sleep(5000);

		} catch (Exception e) {
			throw new Exception("Frame Couldn't be retrieved");
		}
		return switchtoframe;
	}

	/*
	 * Methods for
	 */

	public static void scrollTo(String objectProperty) {
		/*
		 * ((JavascriptExecutor) driver).executeScript(
		 * "arguments[0].scrollIntoView();", element);
		 */

		element = getElementByProperty(objectProperty, driver);
		Point Location = element.getLocation();
		System.out.println(Location);
		Actions actions = new Actions(driver);
		actions.moveToElement(element, 455, 5555);
		// actions.click();
		actions.perform();

	}

	/*
	 * Methods for
	 */

	public static Map<String, List<String>> getHorizontalData(DataTable dataTable) {
		Map<String, List<String>> dataMap = null;
		try {
			dataMap = new HashMap<String, List<String>>();
			List<String> headingRow = dataTable.raw().get(0);
			int dataTableRowsCount = dataTable.getGherkinRows().size() - 1;
			ArrayList<String> totalRowCount = new ArrayList<String>();
			totalRowCount.add(Integer.toString(dataTableRowsCount));
			dataMap.put("totalRowCount", totalRowCount);
			for (int i = 0; i < headingRow.size(); i++) {
				List<String> dataList = new ArrayList<String>();
				dataMap.put(headingRow.get(i), dataList);
				for (int j = 1; j <= dataTableRowsCount; j++) {
					List<String> dataRow = dataTable.raw().get(j);
					dataList.add(dataRow.get(i));

				}
			}
		} catch (Exception e) {

		}
		return dataMap;
	}

	/*
	 * Methods for
	 */

	public static Map<String, List<String>> getVerticalData(DataTable dataTable) {
		Map<String, List<String>> dataMap = null;
		try {
			int dataTableRowsCount = dataTable.getGherkinRows().size();
			dataMap = new HashMap<String, List<String>>();
			for (int k = 0; k < dataTableRowsCount; k++) {
				List<String> dataRow = dataTable.raw().get(k);
				String key = dataRow.get(0);
				dataRow.remove(0);
				dataMap.put(key, dataRow);
			}
		} catch (Exception e) {

		}
		return dataMap;
	}

	/*
	 * Methods for
	 */

	public static String getXLSTestData(String FileName, String SheetName, String RowId, String column)
			throws IOException {
		config.getString("ItemCode");
		String col1 = null;
		DataFormatter df = new DataFormatter();
		FileInputStream file = new FileInputStream(
				new File(System.getProperty("user.dir") + "/InputData" + File.separator + FileName + ".xls"));
		HSSFWorkbook book = new HSSFWorkbook(file);
		HSSFSheet sheet = book.getSheet(SheetName);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		for (int rowIterator = 1; rowIterator <= rowCount; rowIterator++) {
			String row = sheet.getRow(rowIterator).getCell(0).getStringCellValue();
			if (row.equalsIgnoreCase(RowId)) {
				for (int colIterator = 1; colIterator < sheet.getRow(rowIterator).getLastCellNum(); colIterator++) {
					String col = sheet.getRow(0).getCell(colIterator).getStringCellValue();
					if (col.equalsIgnoreCase(column)) {
						Cell cellvalue = sheet.getRow(rowIterator).getCell(colIterator);
						col1 = df.formatCellValue(cellvalue);
						break;
					}
				}
			}
		}
		return col1;
	}

	public static void BrowserClick(String objectProperty, String pageName) throws Throwable {
		// boolean isVerifiedAndClicked = false;
		// WebDriver driver3 = null;
		// browserWithElementWait = new WebDriverWait(driver3,50);

		try {
			// WebElement element1 =
			// driver.findElement(By.xpath(objectProperty));
			// browserWithElementWait.until(ExpectedConditions.elementToBeClickable(element));
			// if (element != null) {

			WebElement element1 = driver.findElement(By.xpath(objectProperty));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Get the Load Event End

			proxy.newHar(pageName);
			long startTime = System.currentTimeMillis();

			element1.click();

			long loadEventEnd = (Long) js.executeScript("return window.performance.timing.loadEventEnd;");
			// Get the Navigation Event Start
			long navigationStart = (Long) js.executeScript("return window.performance.timing.navigationStart;");
			long pageLoadTime = loadEventEnd - navigationStart;
			System.out.println("Total time taken on Page :" + pageLoadTime + "ms");
			System.out.println(pageName + " --- " + driver.getCurrentUrl());

			long endTime = System.currentTimeMillis();
			long timeTaken = (endTime - startTime);
			System.out.println("Total time taken on " + pageName + " :" + timeTaken + "ms");

			Har har = proxy.endHar();

			long timeStamp;
			long elapsed = pageLoadTime;
			if (navigationStart != pageStartTime) {
				timeStamp = navigationStart;
				elapsed = pageLoadTime;
			} else {
				timeStamp = startTime;
				elapsed = timeTaken;
			}
			pageStartTime = navigationStart;

			System.out.println("@@@@@@" + pageStartTime + "@@@@@@@" + navigationStart + "@@@@@@@@@@" + elapsed
					+ "@@@@@@@" + pageName);
			String harFileName = System.getProperty("user.dir") + File.separator + "har" + File.separator + pageName
					+ ".har";

			File file = new File(harFileName);
			System.out.println(har.toString());
			har.writeTo(file);

			ReportObject ro = new ReportObject(timeStamp, elapsed, pageName, "Horizon Regression Suite", 0, 0);

			System.out.println(
					"@@@@@@" + pageStartTime + "@@@@@@@" + timeStamp + "@@@@@@@@@@" + elapsed + "@@@@@@@" + pageName);

			logPerfMetrics(ro);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			System.out.println("page is loaded");
			// driver.close();

		}

	}

	public static boolean isElementNotSelected(String objectProperty) throws Exception {
		boolean isElementNotSelected = false;
		try {
			element = getElementByProperty(objectProperty, driver);
			// browserWithElementWait.until(ExpectedConditions.visibilityOf(element));
			if (!element.isSelected()) {
				isElementNotSelected = true;
				// t2=System.currentTimeMillis();
			} else {
				throw new Exception("Given Element is selected");
			}
			// Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return isElementNotSelected;

	}

	// Method Wait Time
	public static void longTime() throws Exception {
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}

	public static void mediumTime() throws Exception {
		driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
	}

	public static void shortTime() throws Exception {
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	// Hover to element
	public static void mouseHover(String objectProperty) throws Exception {
		element = getElementByProperty(objectProperty, driver);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();
		Thread.sleep(1000);
	}

	// WaitTimeImlicitly
	public static void waitTimeImplicitly() {
		driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
	}

}