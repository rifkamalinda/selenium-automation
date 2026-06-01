package com.runner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;

import com.cucumber.listener.ExtentCucumberFormatter;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(features = { "Feature" }, glue = { "stepDefinition" }, tags = { "BL" }, plugin = {
		"com.cucumber.listener.ExtentCucumberFormatter:output" })

public class ExtentReportRunner extends AbstractTestNGCucumberTests {
	@BeforeClass
	public static void setup() {
// Initiates the extent report and generates the output in the output/Run_<unique timestamp>/report.html file by default.
		ExtentCucumberFormatter.initiateExtentCucumberFormatter();
// Loads the extent config xml to customize on the report.
		ExtentCucumberFormatter.loadConfig(new File("src/test/java/extent-config.xml"));
		ExtentCucumberFormatter.addSystemInfo("Browser", "Chrome");
		Map<String, String> systemInfo = new HashMap<String, String>();

		systemInfo.put("Cucumber version", "v1.2.4");
		systemInfo.put("Extent Cucumber Reporter version", "v1.1.1");
		ExtentCucumberFormatter.addSystemInfo(systemInfo);
	}
}