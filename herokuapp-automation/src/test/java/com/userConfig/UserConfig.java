package com.userConfig;

import java.io.File;

public class UserConfig {

	public static String PlatformVersion = "";

	public static String Device = "";

	public static String AppPackage = "";
	public static String AppActivity = "";

	public static String AppPackageolive = "";
	public static String AppActivityolive = "";

	public static String AppWaitActivity = "com.wag.horizon.android.auth.view.SAMLServiceProviderActivity";
	public static String chromelocation = System.getProperty("user.dir") + File.separator + "Driver" + File.separator
			+ "chromedriver";
	public static String reportDir = System.getProperty("user.dir") + File.separator + "CSVFile" + File.separator;
	public static String reportFile = "AppiumReport";
	public static String AdbLocation = "/Users/abdussalam_s/MyWork/android-sdk-macosx/platform-tools/";

}
